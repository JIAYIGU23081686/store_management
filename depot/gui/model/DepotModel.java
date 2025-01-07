package depot.gui.model;

import depot.model.Parcel;
import depot.model.Customer;
import depot.service.Worker;
import java.util.List;
import java.util.ArrayList;
import java.util.Observable;

public class DepotModel extends Observable {
    private Worker worker;
    private List<Parcel> parcels;
    private List<Customer> customers;
    
    public DepotModel(Worker worker) {
        this.worker = worker;
        this.parcels = new ArrayList<>();
        this.customers = new ArrayList<>();
        refreshData();
    }
    
    public void refreshData() {
        parcels = worker.getUncollectedParcels();
        customers = worker.getCustomerQueue().getCustomers();
        setChanged();
        notifyObservers();
    }
    
    public List<Parcel> getParcels() {
        return new ArrayList<>(parcels);
    }
    
    public List<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }
    
    public Worker getWorker() {
        return worker;
    }
    
    public void addParcel(Parcel parcel) {
        worker.addParcel(parcel);
        refreshData();
    }
} 