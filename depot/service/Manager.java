package depot.service;

import depot.model.Customer;
import depot.model.Parcel;
import depot.util.Log;
import depot.gui.model.DepotModel;
import depot.gui.view.MainFrame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.SwingUtilities;

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
        
        // 从CSV文件加载数据
        loadData();
        
        // 处理客户队列
        processCustomers();
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
    
    private void processCustomers() {
        while (true) {
            System.out.println("\n=== Depot Management System ===");
            System.out.println("1. 处理下一位客户 (Process next customer)");
            System.out.println("2. 添加新包裹 (Add new parcel)");
            System.out.println("3. 添加新客户 (Add new customer)");
            System.out.println("4. 删除客户 (Remove customer)");
            System.out.println("5. 查找包裹 (Find parcel)");
            System.out.println("6. 显示系统状态 (Show system status)");
            System.out.println("7. 生成今日报告 (Generate daily report)");
            System.out.println("8. 退出系统 (Exit system)");
            System.out.println("请选择操作 (Please select): ");
            
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    System.out.println("\n--- 处理客户 (Processing Customer) ---");
                    worker.processNextCustomer();
                    break;
                case 2:
                    System.out.println("\n--- 添加新包裹 (Adding New Parcel) ---");
                    addNewParcel();
                    break;
                case 3:
                    System.out.println("\n--- 添加新客户 (Adding New Customer) ---");
                    addNewCustomer();
                    break;
                case 4:
                    System.out.println("\n--- 删除客户 (Remove Customer) ---");
                    removeCustomer();
                    break;
                case 5:
                    System.out.println("\n--- 查找包裹 (Find Parcel) ---");
                    findParcel();
                    break;
                case 6:
                    System.out.println("\n--- 系统状态 (System Status) ---");
                    showSystemStatus();
                    break;
                case 7:
                    System.out.println("\n--- 生成报告 (Generating Report) ---");
                    log.writeDailySummary();
                    System.out.println("报告已生成到 " + Log.LOG_FILE);
                    break;
                case 8:
                    System.out.println("\n--- 系统关闭 (System Shutdown) ---");
                    log.writeDailySummary();
                    log.info("System shutting down...");
                    System.out.println("报告已生成到 " + Log.LOG_FILE);
                    return;
                default:
                    System.out.println("无效选择，请重试 (Invalid choice, please try again)");
            }
        }
    }
    
    private void addNewParcel() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("请输入包裹信息 (Enter parcel information):");
            System.out.print("ID (格式: C/X + 2-3位数字): ");
            String id = scanner.nextLine().trim();
            
            System.out.print("在仓库的天数 (Days in depot): ");
            int days = Integer.parseInt(scanner.nextLine());
            
            System.out.print("重量 (Weight): ");
            double weight = Double.parseDouble(scanner.nextLine());
            
            System.out.print("长度 (Length): ");
            int length = Integer.parseInt(scanner.nextLine());
            
            System.out.print("宽度 (Width): ");
            int width = Integer.parseInt(scanner.nextLine());
            
            System.out.print("高度 (Height): ");
            int height = Integer.parseInt(scanner.nextLine());
            
            Parcel newParcel = new Parcel(id, days, weight, length, width, height);
            worker.addParcel(newParcel);
            
        } catch (NumberFormatException e) {
            log.error("Invalid number format: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error adding new parcel: " + e.getMessage());
        }
    }
    
    private void addNewCustomer() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("请输入客户信息 (Enter customer information):");
            System.out.print("姓名 (Name): ");
            String name = scanner.nextLine().trim();
            
            System.out.print("包裹ID (Parcel ID): ");
            String parcelId = scanner.nextLine().trim();
            
            // 生成队列号
            int queueNumber = worker.getNextQueueNumber();
            
            Customer newCustomer = new Customer(queueNumber, name, parcelId);
            worker.addCustomer(newCustomer);
            
        } catch (Exception e) {
            log.error("Error adding new customer: " + e.getMessage());
        }
    }
    
    private void showSystemStatus() {
        // 使用GUI显示包裹列表
        SwingUtilities.invokeLater(() -> {
            DepotModel model = new DepotModel(worker);
            MainFrame frame = new MainFrame(model);
            frame.setVisible(true);
        });
    }
    
    private void findParcel() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入包裹ID (Enter parcel ID): ");
        String id = scanner.nextLine().trim();
        
        worker.findAndDisplayParcel(id);
    }
    
    private void removeCustomer() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入要删除的客户姓名 (Enter customer name to remove): ");
        String name = scanner.nextLine().trim();
        
        if (worker.removeCustomer(name)) {
            System.out.println("客户已成功删除 (Customer successfully removed)");
        } else {
            System.out.println("未找到该客户 (Customer not found)");
        }
    }
    
    public static void main(String[] args) {
        new Manager().start();
    }
} 