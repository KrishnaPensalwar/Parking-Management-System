import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ParkingLotGUI extends JFrame {
    private ParkingLotManager manager;
    private HistoryTracker tracker;

    public ParkingLotGUI() {
        manager = new ParkingLotManager();
        tracker = new HistoryTracker();

        setTitle("Parking Lot Management");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.DARK_GRAY);
        setLocation(350,70);


        int imageWidth = 200;
        int imageHeight = 200;


        int frameWidth = getWidth();
        int frameHeight = getHeight();
        int xCenter = (frameWidth - imageWidth * 3) / 4;
        int yCenter = (frameHeight - imageHeight) / 2;


        ImageIcon entryIcon = new ImageIcon(ClassLoader.getSystemResource("icons/entry.jpeg"));
        Image entryImage = entryIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT);
        ImageIcon scaledEntryIcon = new ImageIcon(entryImage);

        JLabel entryLabel = new JLabel(scaledEntryIcon);
        entryLabel.setBounds(xCenter, yCenter, imageWidth, imageHeight);
        add(entryLabel);

        entryLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new VehicleEntryForm().setVisible(true);
            }
        });


        ImageIcon exitIcon = new ImageIcon(ClassLoader.getSystemResource("icons/exit.jpeg"));
        Image exitImage = exitIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT);
        ImageIcon scaledExitIcon = new ImageIcon(exitImage);

        JLabel exitLabel = new JLabel(scaledExitIcon);
        exitLabel.setBounds(xCenter + imageWidth + (xCenter / 2), yCenter, imageWidth, imageHeight);
        add(exitLabel);

        exitLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new BillGeneratorGUI().setVisible(true);
            }
        });


        ImageIcon historyIcon = new ImageIcon(ClassLoader.getSystemResource("icons/history.jpeg"));
        Image historyImage = historyIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT);
        ImageIcon scaledHistoryIcon = new ImageIcon(historyImage);

        JLabel historyLabel = new JLabel(scaledHistoryIcon);
        historyLabel.setBounds(xCenter + 2 * imageWidth + xCenter , yCenter, imageWidth, imageHeight);
        add(historyLabel);

        historyLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tracker.displayHistory();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ParkingLotGUI().setVisible(true);
        });
    }
}
