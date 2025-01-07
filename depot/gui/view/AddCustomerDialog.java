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
        super(parent, "添加新客户 (Add New Customer)", true);
        this.model = model;
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 创建输入面板
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        
        // 姓名输入
        inputPanel.add(new JLabel("姓名 (Name):"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        
        // 包裹ID输入
        inputPanel.add(new JLabel("包裹ID (Parcel ID):"));
        parcelIdField = new JTextField();
        inputPanel.add(parcelIdField);
        
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("添加 (Add)");
        JButton cancelButton = new JButton("取消 (Cancel)");
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        // 消息标签
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        buttonPanel.add(messageLabel);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // 添加按钮事件
        addButton.addActionListener(e -> addCustomer());
        cancelButton.addActionListener(e -> dispose());
        
        // 设置对话框属性
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
                messageLabel.setText("请输入姓名 (Please enter name)");
                return;
            }
            
            if (!parcelId.matches("^[CX]\\d{2,3}$")) {
                messageLabel.setText("包裹ID格式无效 (Invalid parcel ID format)");
                return;
            }
            
            // 生成队列号并创建新客户
            int queueNumber = model.getWorker().getNextQueueNumber();
            Customer newCustomer = new Customer(queueNumber, name, parcelId);
            
            // 添加客户
            model.addCustomer(newCustomer);
            
            // 关闭对话框
            dispose();
            
        } catch (Exception e) {
            messageLabel.setText(e.getMessage());
        }
    }
} 