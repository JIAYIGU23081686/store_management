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
        
        // Create table model
        tableModel = new ParcelTableModel();
        parcelTable = new JTable(tableModel);
        
        // Set table properties
        parcelTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        parcelTable.setAutoCreateRowSorter(true);
        
        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(parcelTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Add title
        JLabel titleLabel = new JLabel("Parcel List", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);
    }
    
    public void updateParcels(List<Parcel> parcels) {
        tableModel.setParcels(parcels);
    }
} 