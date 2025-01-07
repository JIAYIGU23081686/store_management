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
        
        // 创建表格模型
        tableModel = new CustomerTableModel();
        customerTable = new JTable(tableModel);
        
        // 设置表格属性
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.setAutoCreateRowSorter(true);
        
        // 添加滚动面板
        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // 添加标题
        JLabel titleLabel = new JLabel("客户队列 (Customer Queue)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);
    }
    
    public void updateCustomers(List<Customer> customers) {
        tableModel.setCustomers(customers);
    }
} 