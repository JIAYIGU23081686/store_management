package depot.gui.view;

import depot.model.Parcel;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ParcelPanel extends JPanel {
    private JTable parcelTable;
    private ParcelTableModel tableModel;
    
    public ParcelPanel() {
        setLayout(new BorderLayout());
        
        // 创建表格模型
        tableModel = new ParcelTableModel();
        parcelTable = new JTable(tableModel);
        
        // 设置表格属性
        parcelTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        parcelTable.setAutoCreateRowSorter(true);
        
        // 添加滚动面板
        JScrollPane scrollPane = new JScrollPane(parcelTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // 添加标题
        JLabel titleLabel = new JLabel("包裹列表 (Parcel List)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);
    }
    
    public void updateParcels(List<Parcel> parcels) {
        tableModel.setParcels(parcels);
    }
} 