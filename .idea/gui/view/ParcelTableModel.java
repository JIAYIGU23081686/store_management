package depot.gui.view;

import depot.model.Parcel;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ParcelTableModel extends AbstractTableModel {
    private List<Parcel> parcels;
    private final String[] columnNames = {
        "ID", "Days", "Weight", 
        "Length", "Width", "Height"
    };
    
    public ParcelTableModel() {
        this.parcels = new ArrayList<>();
    }
    
    public void setParcels(List<Parcel> parcels) {
        this.parcels = new ArrayList<>(parcels);
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return parcels.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Parcel parcel = parcels.get(rowIndex);
        switch (columnIndex) {
            case 0: return parcel.getId();
            case 1: return parcel.getDaysInDepot();
            case 2: return parcel.getWeight();
            case 3: return parcel.getLength();
            case 4: return parcel.getWidth();
            case 5: return parcel.getHeight();
            default: return null;
        }
    }
} 