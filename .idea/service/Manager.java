package depot.service;

import depot.model.Customer;
import depot.model.Parcel;
import depot.util.Log;
import depot.gui.model.DepotModel;
import depot.gui.view.MainFrame;
import depot.gui.view.AddParcelDialog;
import depot.gui.view.AddCustomerDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Manager {
    private Worker worker;
    private Log log;
    private static final String CUSTS_FILE = "Custs.csv";
    private static final String PARCELS_FILE = "Parcels.csv";
    
    public Manager() {
        this.worker = new Worker();
        this.log = Log.getInstance();
    }
    
    public void start() {
        log.info("Depot management system starting...");
        
        // Load data from CSV files
        loadData();
        
        // Start GUI interface
        SwingUtilities.invokeLater(() -> {
            DepotModel model = new DepotModel(worker);
            MainFrame frame = new MainFrame(model);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit program when window is closed
            frame.setVisible(true);
        });
    }
    
    private void loadData() {
        loadParcels();
        loadCustomers();
    }
    
    private void loadParcels() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PARCELS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    Parcel parcel = new Parcel(
                        data[0],                    // id
                        Integer.parseInt(data[1]),  // daysInDepot
                        Double.parseDouble(data[2]),// weight
                        Integer.parseInt(data[3]),  // length
                        Integer.parseInt(data[4]),  // width
                        Integer.parseInt(data[5])   // height
                    );
                    worker.addParcel(parcel);
                }
            }
            log.info("Parcels loaded from " + PARCELS_FILE);
        } catch (IOException e) {
            log.error("Error loading parcels: " + e.getMessage());
        }
    }
    
    private void loadCustomers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTS_FILE))) {
            String line;
            int queueNumber = 1;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    Customer customer = new Customer(
                        queueNumber++,
                        data[0],                    // name
                        data[1].trim()              // parcelId
                    );
                    worker.addCustomer(customer);
                }
            }
            log.info("Customers loaded from " + CUSTS_FILE);
        } catch (IOException e) {
            log.error("Error loading customers: " + e.getMessage());
        }
    }
    
    private void addNewParcel() {
        // Display the Add Package dialog using the GUI
        SwingUtilities.invokeLater(() -> {
            DepotModel model = new DepotModel(worker);
            AddParcelDialog dialog = new AddParcelDialog(null, model);
            dialog.setVisible(true);
        });
    }
    
    private void addNewCustomer() {
        SwingUtilities.invokeLater(() -> {
            DepotModel model = new DepotModel(worker);
            AddCustomerDialog dialog = new AddCustomerDialog(null, model);
            dialog.setVisible(true);
        });
    }
    
    private void showSystemStatus() {
        // Display the package list using the GUI
        SwingUtilities.invokeLater(() -> {
            DepotModel model = new DepotModel(worker);
            MainFrame frame = new MainFrame(model);
            frame.setVisible(true);
        });
    }
    
    private void findParcel() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter parcel ID: ");
        String id = scanner.nextLine().trim();
        
        worker.findAndDisplayParcel(id);
    }
    
    private void removeCustomer() {
        SwingUtilities.invokeLater(() -> {
            DepotModel model = new DepotModel(worker);
            MainFrame frame = new MainFrame(model);
            frame.showRemoveCustomerDialog();
        });
    }
    
    public static void main(String[] args) {
        new Manager().start();
    }
} 