package depot.model;

public class Customer {
    private int queueNumber;
    private String name;
    private String parcelId;

    public Customer(int queueNumber, String name, String parcelId) {
        this.queueNumber = queueNumber;
        this.name = name;
        this.parcelId = parcelId;
    }

    // Getters
    public int getQueueNumber() { return queueNumber; }
    public String getName() { return name; }
    public String getParcelId() { return parcelId; }

    @Override
    public String toString() {
        return String.format("Customer[#%d %s, Parcel=%s]", 
            queueNumber, name, parcelId);
    }
} 