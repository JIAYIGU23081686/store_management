package depot.service;

import depot.model.Customer;
import depot.model.Parcel;
import depot.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Worker {
    private ParcelMap parcelMap;
    private QueueOfCustomers customerQueue;
    private Log log;
    private double dailyTotalFee;  // Total fee collected for the day
    
    public Worker() {
        this.parcelMap = new ParcelMap();
        this.customerQueue = new QueueOfCustomers();
        this.log = Log.getInstance();
        this.log.setWorker(this);
        this.dailyTotalFee = 0.0;
    }
    
    public double calculateFee(Parcel parcel) {
        double baseFee = 10.0; // Base fee
        double volumeFee = parcel.getLength() * parcel.getWidth() * parcel.getHeight() * 0.1;
        double daysFee = parcel.getDaysInDepot() * 5.0;
        double discount = parcel.getId().startsWith("C") ? 0.9 : 1.0;
        
        return (baseFee + volumeFee + daysFee) * discount;
    }
    
    public void processNextCustomer() {
        if (!customerQueue.hasCustomers()) {
            log.info("No customers in queue");
            return;
        }
        
        Customer customer = customerQueue.getNextCustomer();
        String parcelId = customer.getParcelId();
        
        if (!parcelMap.hasParcel(parcelId)) {
            log.error("Parcel not found: " + parcelId);
            return;
        }
        
        Parcel parcel = parcelMap.getParcel(parcelId);
        double fee = calculateFee(parcel);
        dailyTotalFee += fee;  // Add fee to daily total
        
        log.info(String.format("Customer %s collected parcel %s, fee %.2f", 
            customer.getName(), parcelId, fee));
        log.recordTransaction(parcelId, fee);  // Record the transaction
            
        parcelMap.markParcelAsCollected(parcelId);
        System.out.println("Parcel successfully collected!");
    }
    
    public void addCustomer(Customer customer) {
        if (!parcelMap.hasParcel(customer.getParcelId())) {
            log.error("Cannot add customer: Parcel " + customer.getParcelId() + " not found");
            return;
        }
        customerQueue.addCustomer(customer);
        log.info("Added customer to queue: " + customer);
    }
    
    public boolean removeCustomer(String name) {
        boolean removed = customerQueue.removeCustomer(name);
        if (removed) {
            log.info("Customer removed from queue: " + name);
        } else {
            log.error("Customer not found in queue: " + name);
        }
        return removed;
    }
    
    public void addParcel(Parcel parcel) {
        if (!isValidParcelId(parcel.getId())) {
            log.error("Invalid parcel ID format: " + parcel.getId());
            return;
        }
        parcelMap.addParcel(parcel);
        log.info("Added parcel to depot: " + parcel);
    }
    
    private boolean isValidParcelId(String id) {
        // ID format: C or X followed by 2-3 digits
        return id.matches("^[CX]\\d{2,3}$");
    }
    
    public long getOverdueParcels(int days) {
        return parcelMap.getParcels().stream()
            .filter(p -> p.getDaysInDepot() > days)
            .count();
    }
    
    public List<Parcel> getUncollectedParcels() {
        return new ArrayList<>(parcelMap.getParcels());
    }
    
    public Collection<Parcel> getCollectedParcels() {
        return parcelMap.getCollectedParcels();
    }
    
    public ParcelMap getParcelMap() {
        return parcelMap;
    }
    
    public void displayCustomerQueue() {
        if (!customerQueue.hasCustomers()) {
            System.out.println("Queue is empty");
            return;
        }
        
        List<Customer> customers = customerQueue.getCustomers();
        
        for (Customer customer : customers) {
            System.out.printf("No.: %d, Name: %s, Parcel ID: %s%n",
                customer.getQueueNumber(),
                customer.getName(),
                customer.getParcelId());
        }
    }
    
    public void displayParcels() {
        List<Parcel> parcels = getUncollectedParcels();
        if (parcels.isEmpty()) {
            System.out.println("Depot is empty");
            return;
        }
        
        for (Parcel parcel : parcels) {
            System.out.printf("ID: %s, Days: %d, Size: %dx%dx%d, Weight: %.1f%n",
                parcel.getId(),
                parcel.getDaysInDepot(),
                parcel.getLength(),
                parcel.getWidth(),
                parcel.getHeight(),
                parcel.getWeight());
        }
    }
    
    public void displayStatistics() {
        int totalParcels = getUncollectedParcels().size();
        long overdueParcels = getOverdueParcels(7);
        int collectedParcels = parcelMap.getCollectedParcels().size();
        
        System.out.println("Total parcels: " + totalParcels);
        System.out.println("Collected parcels: " + collectedParcels);
        System.out.println("Parcels over 7 days: " + overdueParcels);
        System.out.printf("Today's total fee: %.2f%n", dailyTotalFee);
    }
    
    public int getNextQueueNumber() {
        return customerQueue.size() + 1;
    }
    
    public void findAndDisplayParcel(String id) {
        UIManager.put("OptionPane.okButtonText", "OK");
        UIManager.put("OptionPane.cancelButtonText", "Cancel");
        
        // First check uncollected parcels
        Parcel parcel = parcelMap.getParcel(id);
        StringBuilder message = new StringBuilder();
        if (parcel != null) {
            message.append("Status: Waiting for collection\n\n");
            message.append(getParcelDetails(parcel));
            JOptionPane.showMessageDialog(null, message.toString(),
                "Parcel Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Then check collected parcels
        for (Parcel p : parcelMap.getCollectedParcels()) {
            if (p.getId().equals(id)) {
                message.append("Status: Collected\n\n");
                message.append(getParcelDetails(p));
                JOptionPane.showMessageDialog(null, message.toString(),
                    "Parcel Information",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        
        // Parcel not found
        JOptionPane.showMessageDialog(null,
            "Parcel not found: " + id,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    
    private String getParcelDetails(Parcel parcel) {
        StringBuilder details = new StringBuilder();
        details.append(String.format("Parcel details:\n"));
        details.append(String.format("ID: %s\n", parcel.getId()));
        details.append(String.format("Days in depot: %d\n", parcel.getDaysInDepot()));
        details.append(String.format("Weight: %.1f\n", parcel.getWeight()));
        details.append(String.format("Dimensions: %dx%dx%d\n",
            parcel.getLength(), parcel.getWidth(), parcel.getHeight()));
        details.append(String.format("Estimated fee: %.2f", calculateFee(parcel)));
        return details.toString();
    }
    
    public double getDailyTotalFee() {
        return dailyTotalFee;
    }
    
    public QueueOfCustomers getCustomerQueue() {
        return customerQueue;
    }
} 