package depot.gui.view;

import depot.gui.model.DepotModel;
import depot.util.Log;
import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class MainFrame extends JFrame implements Observer {
    private DepotModel model;
    private ParcelPanel parcelPanel;
    private CustomerQueuePanel customerPanel;
    private StatisticsPanel statisticsPanel;
    
    public MainFrame(DepotModel model) {
        this.model = model;
        model.addObserver(this);
        
        // Set window properties
        setTitle("Depot Management System - System Status");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create left panel (customer queue)
        customerPanel = new CustomerQueuePanel();
        mainPanel.add(customerPanel, BorderLayout.WEST);
        
        // Create center panel (parcel list)
        parcelPanel = new ParcelPanel();
        mainPanel.add(parcelPanel, BorderLayout.CENTER);
        
        // Create the bottom panel (Statistics)
        statisticsPanel = new StatisticsPanel();
        mainPanel.add(statisticsPanel, BorderLayout.SOUTH);
        
        // Create toolbar
        JToolBar toolBar = new JToolBar();
        // Handle customer button
        JButton processCustomerButton = new JButton("Process Next Customer");
        processCustomerButton.addActionListener(e -> processNextCustomer());
        toolBar.add(processCustomerButton);
        toolBar.addSeparator();
        
        // Package management button
        JButton addParcelButton = new JButton("Add Parcel");
        addParcelButton.addActionListener(e -> showAddParcelDialog());
        JButton findParcelButton = new JButton("Find Parcel");
        findParcelButton.addActionListener(e -> showFindParcelDialog());
        toolBar.add(addParcelButton);
        toolBar.add(findParcelButton);
        toolBar.addSeparator();
        
        // Customer management button
        JButton addCustomerButton = new JButton("Add Customer");
        addCustomerButton.addActionListener(e -> showAddCustomerDialog());
        JButton removeCustomerButton = new JButton("Remove Customer");
        removeCustomerButton.addActionListener(e -> showRemoveCustomerDialog());
        toolBar.add(addCustomerButton);
        toolBar.add(removeCustomerButton);
        toolBar.addSeparator();
        
        // System function button
        JButton generateReportButton = new JButton("Generate Report");
        generateReportButton.addActionListener(e -> generateReport());
        toolBar.add(generateReportButton);
        
        mainPanel.add(toolBar, BorderLayout.NORTH);
        
        add(mainPanel);
        
        // Update display
        updateDisplay();
    }
    
    private void updateDisplay() {
        customerPanel.updateCustomers(model.getCustomers());
        parcelPanel.updateParcels(model.getParcels());
        statisticsPanel.updateStatistics(model.getWorker());
    }
    
    private void showAddParcelDialog() {
        AddParcelDialog dialog = new AddParcelDialog(this, model);
        dialog.setVisible(true);
    }
    
    private void showAddCustomerDialog() {
        AddCustomerDialog dialog = new AddCustomerDialog(this, model);
        dialog.setVisible(true);
    }
    
    public void showRemoveCustomerDialog() {
        UIManager.put("OptionPane.okButtonText", "OK");
        UIManager.put("OptionPane.cancelButtonText", "Cancel");
        UIManager.put("OptionPane.yesButtonText", "Yes");
        UIManager.put("OptionPane.noButtonText", "No");
        
        String name = JOptionPane.showInputDialog(this,
            "Enter customer name to remove:",
            "Remove Customer",
            JOptionPane.QUESTION_MESSAGE);
            
        if (name != null && !name.trim().isEmpty()) {
            if (model.removeCustomer(name.trim())) {
                JOptionPane.showMessageDialog(this,
                    "Customer successfully removed",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Customer not found",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void processNextCustomer() {
        if (!model.hasCustomers()) {
            JOptionPane.showMessageDialog(this,
                "No customers in queue",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        model.processNextCustomer();
    }
    
    private void showFindParcelDialog() {
        String id = JOptionPane.showInputDialog(this,
            "Enter parcel ID:",
            "Find Parcel",
            JOptionPane.QUESTION_MESSAGE);
            
        if (id != null && !id.trim().isEmpty()) {
            model.findParcel(id.trim());
        }
    }
    
    private void generateReport() {
        model.generateReport();
        JOptionPane.showMessageDialog(this,
            "Report generated to " + Log.LOG_FILE,
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void update(Observable o, Object arg) {
        updateDisplay();
    }
} 