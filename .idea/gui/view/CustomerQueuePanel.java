package depot.gui.view;

import depot.model.Customer;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomerQueuePanel extends JPanel {
    private JTable customerTable;
    private CustomerTableModel tableModel;
    
    public CustomerQueuePanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 0));
        
        // Create table model
        tableModel = new CustomerTableModel();
        customerTable = new JTable(tableModel);
        
        // Set table properties
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.setAutoCreateRowSorter(true);
        
        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Add title
        JLabel titleLabel = new JLabel("Customer Queue", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);
    }
    
    public void updateCustomers(List<Customer> customers) {
        tableModel.setCustomers(customers);
    }
} 