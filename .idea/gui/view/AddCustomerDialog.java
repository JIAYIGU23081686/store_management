package depot.gui.view;

import depot.model.Customer;
import depot.gui.model.DepotModel;
import javax.swing.*;
import java.awt.*;

public class AddCustomerDialog extends JDialog {
    private DepotModel model;
    private JTextField nameField;
    private JTextField parcelIdField;
    private JLabel messageLabel;
    
    public AddCustomerDialog(JFrame parent, DepotModel model) {
        super(parent, "Add New Customer", true);
        this.model = model;
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create input panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        
        // Name input
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        
        // Parcel ID input
        inputPanel.add(new JLabel("Parcel ID:"));
        parcelIdField = new JTextField();
        inputPanel.add(parcelIdField);
        
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
        buttonPanel.add(messageLabel);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add button event
        addButton.addActionListener(e -> addCustomer());
        cancelButton.addActionListener(e -> dispose());
        
        // Set dialog properties
        add(mainPanel);
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void addCustomer() {
        try {
            String name = nameField.getText().trim();
            String parcelId = parcelIdField.getText().trim().toUpperCase();
            
            if (name.isEmpty()) {
                messageLabel.setText("Please enter name");
                return;
            }
            
            if (!parcelId.matches("^[CX]\\d{2,3}$")) {
                messageLabel.setText("Invalid parcel ID format");
                return;
            }
            
            // Generate queue number and create new customer
            int queueNumber = model.getWorker().getNextQueueNumber();
            Customer newCustomer = new Customer(queueNumber, name, parcelId);
            
            // Add customer
            model.addCustomer(newCustomer);
            
            // Close dialog
            dispose();
            
        } catch (Exception e) {
            messageLabel.setText(e.getMessage());
        }
    }
} 