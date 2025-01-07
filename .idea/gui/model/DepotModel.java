package depot.gui.model;

import depot.model.Parcel;
import depot.model.Customer;
import depot.service.Worker;
import depot.util.Log;

import java.util.List;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Model class for the depot management system.
 * Handles data and business logic for the GUI components.
 */
public class DepotModel extends Observable {
    private Worker worker;
    private List<Parcel> parcels;
    private List<Customer> customers;
    
    /**
     * Constructs a new DepotModel with the given worker.
     * Initializes parcel and customer lists.
     */
    public DepotModel(Worker worker) {
        this.worker = worker;
        this.parcels = new ArrayList<>();
        this.customers = new ArrayList<>();
        refreshData();
    }
    
    /**
     * Updates the model's data from the worker and notifies observers.
     */
    public void refreshData() {
        parcels = worker.getUncollectedParcels();
        customers = worker.getCustomerQueue().getCustomers();
        setChanged();
        notifyObservers();
    }
    
    /**
     * Returns a copy of the parcel list to prevent external modification.
     */
    public List<Parcel> getParcels() {
        return new ArrayList<>(parcels);
    }
    
    /**
     * Returns a copy of the customer list to prevent external modification.
     */
    public List<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }
    
    /**
     * Returns the worker instance.
     */
    public Worker getWorker() {
        return worker;
    }
    
    /**
     * Adds a new parcel to the system and refreshes the data.
     */
    public void addParcel(Parcel parcel) {
        worker.addParcel(parcel);
        refreshData();
    }
    
    /**
     * Adds a new customer to the system and refreshes the data.
     */
    public void addCustomer(Customer customer) {
        worker.addCustomer(customer);
        refreshData();
    }
    
    /**
     * Removes a customer by name and refreshes the data.
     * Returns true if customer was successfully removed.
     */
    public boolean removeCustomer(String name) {
        boolean result = worker.removeCustomer(name);
        refreshData();
        return result;
    }
    
    /**
     * Checks if there are any customers in the queue.
     */
    public boolean hasCustomers() {
        return worker.getCustomerQueue().hasCustomers();
    }
    
    /**
     * Processes the next customer in the queue and refreshes the data.
     */
    public void processNextCustomer() {
        worker.processNextCustomer();
        refreshData();
    }
    
    /**
     * Searches for and displays parcel information by ID.
     */
    public void findParcel(String id) {
        worker.findAndDisplayParcel(id);
    }
    
    /**
     * Generates and writes the daily summary report.
     */
    public void generateReport() {
        Log.getInstance().writeDailySummary();
    }
} 