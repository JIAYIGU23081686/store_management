package depot.model;

public class Parcel {
    private String id;
    private int daysInDepot;
    private double weight;
    private int length;
    private int width; 
    private int height;
    private boolean collected;

    public Parcel(String id, int daysInDepot, double weight, int length, int width, int height) {
        this.id = id;
        this.daysInDepot = daysInDepot;
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
        this.collected = false;
    }

    // Getters and setters
    public String getId() { return id; }
    public int getDaysInDepot() { return daysInDepot; }
    public double getWeight() { return weight; }
    public int getLength() { return length; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isCollected() {
        return collected;
    }
    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    @Override
    public String toString() {
        return String.format("Parcel[ID=%s, Days=%d, Weight=%.2f, Size=%dx%dx%d]",
            id, daysInDepot, weight, length, width, height);
    }
} 