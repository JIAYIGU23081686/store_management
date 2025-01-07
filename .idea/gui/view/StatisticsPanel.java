package depot.gui.view;

import depot.service.Worker;
import javax.swing.*;
import java.awt.*;

public class StatisticsPanel extends JPanel {
    private JLabel totalParcelsLabel;
    private JLabel collectedParcelsLabel;
    private JLabel overdueParcelsLabel;
    private JLabel dailyFeeLabel;
    
    public StatisticsPanel() {
        setLayout(new GridLayout(2, 2, 10, 5));
        setBorder(BorderFactory.createTitledBorder("Statistics"));
        
        totalParcelsLabel = new JLabel();
        collectedParcelsLabel = new JLabel();
        overdueParcelsLabel = new JLabel();
        dailyFeeLabel = new JLabel();
        
        add(totalParcelsLabel);
        add(collectedParcelsLabel);
        add(overdueParcelsLabel);
        add(dailyFeeLabel);
    }
    
    public void updateStatistics(Worker worker) {
        totalParcelsLabel.setText("Total parcels: " + 
            worker.getUncollectedParcels().size());
        collectedParcelsLabel.setText("Collected parcels: " + 
            worker.getCollectedParcels().size());
        overdueParcelsLabel.setText("Overdue parcels: " + 
            worker.getOverdueParcels(7));
        dailyFeeLabel.setText(String.format("Today's fee: %.2f", 
            worker.getDailyTotalFee()));
    }
} 