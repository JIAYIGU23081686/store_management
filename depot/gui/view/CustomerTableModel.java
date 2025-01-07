package depot.gui.view;

import depot.model.Customer;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class CustomerTableModel extends AbstractTableModel {
    private List<Customer> customers;
    private final String[] columnNames = {
        "序号 (No.)", "姓名 (Name)", "包裹ID (Parcel ID)"
    };
    
    public CustomerTableModel() {
        this.customers = new ArrayList<>();
    }
    
    public void setCustomers(List<Customer> customers) {
        this.customers = new ArrayList<>(customers);
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return customers.size();
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
        Customer customer = customers.get(rowIndex);
        switch (columnIndex) {
            case 0: return customer.getQueueNumber();
            case 1: return customer.getName();
            case 2: return customer.getParcelId();
            default: return null;
        }
    }
} 