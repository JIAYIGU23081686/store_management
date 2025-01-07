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
        
        add(mainPanel);
        
        // 更新显示
        updateDisplay();
    }
    
    private void updateDisplay() {
        customerPanel.updateCustomers(model.getCustomers());
        parcelPanel.updateParcels(model.getParcels());
        statisticsPanel.updateStatistics(model.getWorker());
    }
    
    @Override
    public void update(Observable o, Object arg) {
        updateDisplay();
    }
} 