package depot.util;

import depot.model.Parcel;
import depot.service.Worker;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Log {
    private static Log instance;
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String LOG_FILE = "depot_log.txt";
    private List<String> dailyTransactions;
    private double dailyTotalFee;
    private Worker worker;

    private Log() {
        dailyTransactions = new ArrayList<>();
        dailyTotalFee = 0.0;
        
        // 创建或清空日志文件
        try (PrintWriter writer = new PrintWriter(LOG_FILE)) {
            writer.println("Depot Management System Log");
            writer.println("==========================");
        } catch (FileNotFoundException e) {
            System.err.println("Could not create log file: " + e.getMessage());
        }
    }

    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    public void recordTransaction(String parcelId, double fee) {
        dailyTransactions.add(String.format("Parcel %s processed, fee: %.2f", parcelId, fee));
        dailyTotalFee += fee;
    }

    public void writeDailySummary() {
        if (worker == null) {
            error("Worker not set in Log class");
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println("\nDaily Summary " + LocalDateTime.now().format(formatter));
            writer.println("----------------------------------------");
            
            // 1. 交易记录
            writer.println("1. Transactions:");
            for (String transaction : dailyTransactions) {
                writer.println("- " + transaction);
            }
            
            // 2. 财务汇总
            writer.println("\n2. Financial Summary:");
            writer.println(String.format("Total fees collected: %.2f", dailyTotalFee));
            
            // 3. 包裹统计
            writer.println("\n3. Parcel Statistics:");
            long overdueParcels = worker.getOverdueParcels(7);
            writer.println("Total parcels in depot: " + worker.getUncollectedParcels().size());
            writer.println("Total collected parcels: " + worker.getCollectedParcels().size());
            writer.println("Parcels in depot over 7 days: " + overdueParcels);
            writer.printf("Today's total fee: %.2f%n", worker.getDailyTotalFee());
            
            // 4. 未领取包裹列表
            writer.println("\n4. Waiting Parcels (等待领取):");
            for (Parcel parcel : worker.getUncollectedParcels()) {
                writer.println(String.format("- %s (Days: %d, Size: %dx%dx%d)", 
                    parcel.getId(), 
                    parcel.getDaysInDepot(),
                    parcel.getLength(),
                    parcel.getWidth(),
                    parcel.getHeight()));
            }
            
            writer.println("\n5. Collected Parcels (已领取):");
            for (Parcel parcel : worker.getParcelMap().getCollectedParcels()) {
                writer.println(String.format("- %s (Collected)", parcel.getId()));
            }
            
            writer.println("----------------------------------------\n");
            dailyTransactions.clear();
            dailyTotalFee = 0.0;
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    private void log(String level, String message) {
        String logEntry = String.format("[%s] %s: %s",
            LocalDateTime.now().format(formatter), level, message);

        // 输出到控制台
        System.out.println(logEntry);

        // 写入文件
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println(logEntry);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}