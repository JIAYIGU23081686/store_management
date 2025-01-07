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
        super(parent, "添加新包裹 (Add New Parcel)", true);
        this.model = model;
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 创建输入面板
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        // ID输入
        inputPanel.add(new JLabel("ID (C/X + 2-3位数字):"));
        idField = new JTextField();
        inputPanel.add(idField);
        
        // 天数输入
        inputPanel.add(new JLabel("在仓库天数 (Days):"));
        daysField = new JTextField();
        inputPanel.add(daysField);
        
        // 重量输入
        inputPanel.add(new JLabel("重量 (Weight):"));
        weightField = new JTextField();
        inputPanel.add(weightField);
        
        // 长度输入
        inputPanel.add(new JLabel("长度 (Length):"));
        lengthField = new JTextField();
        inputPanel.add(lengthField);
        
        // 宽度输入
        inputPanel.add(new JLabel("宽度 (Width):"));
        widthField = new JTextField();
        inputPanel.add(widthField);
        
        // 高度输入
        inputPanel.add(new JLabel("高度 (Height):"));
        heightField = new JTextField();
        inputPanel.add(heightField);
        
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
        addButton.addActionListener(e -> addParcel());
        cancelButton.addActionListener(e -> dispose());
        
        // 设置对话框属性
        add(mainPanel);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void addParcel() {
        try {
            // 获取并验证输入
            String id = idField.getText().trim();
            int days = Integer.parseInt(daysField.getText().trim());
            double weight = Double.parseDouble(weightField.getText().trim());
            int length = Integer.parseInt(lengthField.getText().trim());
            int width = Integer.parseInt(widthField.getText().trim());
            int height = Integer.parseInt(heightField.getText().trim());
            
            // 创建新包裹
            Parcel newParcel = new Parcel(id, days, weight, length, width, height);
            
            // 添加包裹
            model.addParcel(newParcel);
            
            // 关闭对话框
            dispose();
            
        } catch (NumberFormatException e) {
            messageLabel.setText("请输入有效的数字 (Please enter valid numbers)");
        } catch (Exception e) {
            messageLabel.setText(e.getMessage());
        }
    }
} 