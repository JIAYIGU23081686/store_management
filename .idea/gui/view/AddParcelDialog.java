package depot.gui.view;

import depot.model.Parcel;
import depot.gui.model.DepotModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddParcelDialog extends JDialog {
    private DepotModel model;
    private JTextField idField;
    private JTextField daysField;
    private JTextField weightField;
    private JTextField lengthField;
    private JTextField widthField;
    private JTextField heightField;
    private JLabel messageLabel;
    
    public AddParcelDialog(JFrame parent, DepotModel model) {
        super(parent, "Add New Parcel", true);
        this.model = model;
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create input panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        // ID input
        inputPanel.add(new JLabel("ID (C/X + 2-3 digits):"));
        idField = new JTextField();
        inputPanel.add(idField);
        
        // Days input
        inputPanel.add(new JLabel("Days in depot:"));
        daysField = new JTextField();
        inputPanel.add(daysField);
        
        // Weight input
        inputPanel.add(new JLabel("Weight:"));
        weightField = new JTextField();
        inputPanel.add(weightField);
        
        // Length input
        inputPanel.add(new JLabel("Length:"));
        lengthField = new JTextField();
        inputPanel.add(lengthField);
        
        // Width input
        inputPanel.add(new JLabel("Width:"));
        widthField = new JTextField();
        inputPanel.add(widthField);
        
        // Height input
        inputPanel.add(new JLabel("Height:"));
        heightField = new JTextField();
        inputPanel.add(heightField);
        
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        // Message label
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        
        try {
            messageLabel.setText("Please enter valid numbers");
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter valid numbers");
        }
        
        buttonPanel.add(messageLabel);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add button event
        addButton.addActionListener(e -> addParcel());
        cancelButton.addActionListener(e -> dispose());
        
        // Set dialog properties
        add(mainPanel);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void addParcel() {
        try {
            // Get and validate input
            String id = idField.getText().trim();
            int days = Integer.parseInt(daysField.getText().trim());
            double weight = Double.parseDouble(weightField.getText().trim());
            int length = Integer.parseInt(lengthField.getText().trim());
            int width = Integer.parseInt(widthField.getText().trim());
            int height = Integer.parseInt(heightField.getText().trim());
            
            // Create new parcel
            Parcel newParcel = new Parcel(id, days, weight, length, width, height);
            
            // Add parcel
            model.addParcel(newParcel);
            
            // Close dialog
            dispose();
            
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter valid numbers");
        } catch (Exception e) {
            messageLabel.setText(e.getMessage());
        }
    }
} 