package depot.gui.view;

import depot.gui.model.DepotModel;
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
        
        // 设置窗口属性
        setTitle("Depot Management System - System Status");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // 关闭窗口而不是退出程序
        setSize(1000, 800);
        setLocationRelativeTo(null);
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 创建左侧面板（客户队列）
        customerPanel = new CustomerQueuePanel();
        mainPanel.add(customerPanel, BorderLayout.WEST);
        
        // 创建中央面板（包裹列表）
        parcelPanel = new ParcelPanel();
        mainPanel.add(parcelPanel, BorderLayout.CENTER);
        
        // 创建底部面板（统计信息）
        statisticsPanel = new StatisticsPanel();
        mainPanel.add(statisticsPanel, BorderLayout.SOUTH);
        
        // 创建工具栏
        JToolBar toolBar = new JToolBar();
        JButton addParcelButton = new JButton("添加包裹 (Add Parcel)");
        addParcelButton.addActionListener(e -> showAddParcelDialog());
        JButton addCustomerButton = new JButton("添加客户 (Add Customer)");
        addCustomerButton.addActionListener(e -> showAddCustomerDialog());
        JButton removeCustomerButton = new JButton("删除客户 (Remove Customer)");
        removeCustomerButton.addActionListener(e -> showRemoveCustomerDialog());
        toolBar.add(addParcelButton);
        toolBar.add(addCustomerButton);
        toolBar.add(removeCustomerButton);
        mainPanel.add(toolBar, BorderLayout.NORTH);
        
        add(mainPanel);
        
        // 更新显示
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
        String name = JOptionPane.showInputDialog(this,
            "请输入要删除的客户姓名 (Enter customer name to remove):",
            "删除客户 (Remove Customer)",
            JOptionPane.QUESTION_MESSAGE);
            
        if (name != null && !name.trim().isEmpty()) {
            if (model.removeCustomer(name.trim())) {
                JOptionPane.showMessageDialog(this,
                    "客户已成功删除 (Customer successfully removed)",
                    "成功 (Success)",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "未找到该客户 (Customer not found)",
                    "错误 (Error)",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {
        updateDisplay();
    }
} 