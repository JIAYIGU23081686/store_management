package depot.service;
import depot.model.Parcel;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class ParcelMap {
    private Map<String, Parcel> parcels = new HashMap<>();
    private Map<String, Parcel> collectedParcels = new HashMap<>();
    
    public void addParcel(Parcel parcel) {
        parcels.put(parcel.getId(), parcel);
    }
    
    public Parcel getParcel(String id) {
        return parcels.get(id);
    }
    
    public void markParcelAsCollected(String id) {
        Parcel parcel = parcels.remove(id);
        if (parcel != null) {
            parcel.setCollected(true);
            collectedParcels.put(id, parcel);
        }
    }
    
    public boolean hasParcel(String id) {
        return parcels.containsKey(id);
    }
    
    public Collection<Parcel> getParcels() {
        return parcels.values();
    }
    
    public Collection<Parcel> getCollectedParcels() {
        return collectedParcels.values();
    }
} 