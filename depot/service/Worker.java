package depot.service;

import depot.model.Customer;
import depot.model.Parcel;
import depot.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public class Worker {
    private ParcelMap parcelMap;
    private QueueOfCustomers customerQueue;
    private Log log;
    private double dailyTotalFee;  // 添加每日收费总额
    
    public Worker() {
        this.parcelMap = new ParcelMap();
        this.customerQueue = new QueueOfCustomers();
        this.log = Log.getInstance();
        this.log.setWorker(this);
        this.dailyTotalFee = 0.0;
    }
    
    public double calculateFee(Parcel parcel) {
        double baseFee = 10.0; // 基础费用
        double volumeFee = parcel.getLength() * parcel.getWidth() * parcel.getHeight() * 0.1;
        double daysFee = parcel.getDaysInDepot() * 5.0;
        double discount = parcel.getId().startsWith("C") ? 0.9 : 1.0;
        
        return (baseFee + volumeFee + daysFee) * discount;
    }
    
    public void processNextCustomer() {
        if (!customerQueue.hasCustomers()) {
            log.info("当前没有客户在排队 (No customers in queue)");
            return;
        }
        
        Customer customer = customerQueue.getNextCustomer();
        System.out.printf("正在处理客户: %s%n", customer.getName());
        
        String parcelId = customer.getParcelId();
        System.out.printf("查找包裹ID: %s%n", parcelId);
        
        if (!parcelMap.hasParcel(parcelId)) {
            log.error("找不到包裹: " + parcelId);
            return;
        }
        
        Parcel parcel = parcelMap.getParcel(parcelId);
        double fee = calculateFee(parcel);
        dailyTotalFee += fee;  // 累加费用
        
        log.info(String.format("客户 %s 领取包裹 %s, 费用 %.2f", 
            customer.getName(), parcelId, fee));
        log.recordTransaction(parcelId, fee);
            
        parcelMap.markParcelAsCollected(parcelId);
        System.out.println("包裹已成功领取！");
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
        // ID格式: C或X后跟2-3个数字
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
            System.out.println("队列为空 (Queue is empty)");
            return;
        }
        
        List<Customer> customers = customerQueue.getCustomers();
        
        for (Customer customer : customers) {
            System.out.printf("序号: %d, 姓名: %s, 包裹ID: %s%n",
                customer.getQueueNumber(),
                customer.getName(),
                customer.getParcelId());
        }
    }
    
    public void displayParcels() {
        List<Parcel> parcels = getUncollectedParcels();
        if (parcels.isEmpty()) {
            System.out.println("仓库为空 (Depot is empty)");
            return;
        }
        
        for (Parcel parcel : parcels) {
            System.out.printf("ID: %s, 天数: %d, 尺寸: %dx%dx%d, 重量: %.1f%n",
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
        
        System.out.println("总包裹数 (Total parcels): " + totalParcels);
        System.out.println("已领取包裹数 (Collected parcels): " + collectedParcels);
        System.out.println("超过7天未取包裹数 (Parcels over 7 days): " + overdueParcels);
        System.out.printf("今日收费总额 (Today's total fee): %.2f%n", dailyTotalFee);
    }
    
    public int getNextQueueNumber() {
        return customerQueue.size() + 1;
    }
    
    public void findAndDisplayParcel(String id) {
        // 先在未收集的包裹中查找
        Parcel parcel = parcelMap.getParcel(id);
        if (parcel != null) {
            System.out.println("\n包裹状态：等待领取 (Status: Waiting for collection)");
            displayParcelDetails(parcel);
            return;
        }
        
        // 在已收集的包裹中查找
        for (Parcel p : parcelMap.getCollectedParcels()) {
            if (p.getId().equals(id)) {
                System.out.println("\n包裹状态：已领取 (Status: Collected)");
                displayParcelDetails(p);
                return;
            }
        }
        
        // 未找到包裹
        System.out.println("未找到包裹 (Parcel not found): " + id);
    }
    
    private void displayParcelDetails(Parcel parcel) {
        System.out.println("包裹详情 (Parcel details):");
        System.out.printf("ID: %s%n", parcel.getId());
        System.out.printf("在仓库天数 (Days in depot): %d%n", parcel.getDaysInDepot());
        System.out.printf("重量 (Weight): %.1f%n", parcel.getWeight());
        System.out.printf("尺寸 (Dimensions): %dx%dx%d%n", 
            parcel.getLength(), parcel.getWidth(), parcel.getHeight());
        System.out.printf("预计费用 (Estimated fee): %.2f%n", calculateFee(parcel));
    }
    
    public double getDailyTotalFee() {
        return dailyTotalFee;
    }
} 