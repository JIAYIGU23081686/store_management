package depot.service;

import depot.model.Customer;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class QueueOfCustomers {
    private List<Customer> customers;
    private static final Comparator<Customer> SURNAME_COMPARATOR = (c1, c2) -> {
        // Get surname (last word)
        String[] name1Parts = c1.getName().split(" ");
        String[] name2Parts = c2.getName().split(" ");
        String surname1 = name1Parts[name1Parts.length - 1].toLowerCase();
        String surname2 = name2Parts[name2Parts.length - 1].toLowerCase();
        
        int surnameCompare = surname1.compareTo(surname2);
        if (surnameCompare == 0) {
            // If surnames are same, sort by first name
            String firstName1 = name1Parts[0].toLowerCase();
            String firstName2 = name2Parts[0].toLowerCase();
            return firstName1.compareTo(firstName2);
        }
        return surnameCompare;
    };
    
    public QueueOfCustomers() {
        customers = new ArrayList<>();
    }
    
    public void addCustomer(Customer customer) {
        customers.add(customer);
        customers.sort(SURNAME_COMPARATOR);  // Sort immediately after adding
    }
    
    public Customer getNextCustomer() {
        if (customers.isEmpty()) {
            return null;
        }
        return customers.remove(0);  // Remove and return first customer
    }
    
    public boolean hasCustomers() {
        return !customers.isEmpty();
    }
    
    public int size() {
        return customers.size();
    }
    
    public List<Customer> getCustomers() {
        return new ArrayList<>(customers);  // Return a copy of the list
    }
    
    public boolean removeCustomer(String name) {
        return customers.removeIf(customer -> 
            customer.getName().equalsIgnoreCase(name));
    }
} 