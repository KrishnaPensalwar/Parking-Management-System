import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class BillGeneratorGUI extends JFrame {
    private JTextField vehicleIdField;
    private ParkingLotManager manager;
    private JLabel billLabel;
    private static final int RATE_PER_HOUR = 50;

    public BillGeneratorGUI() {
        manager = new ParkingLotManager(); // Initialize the ParkingLotManager

        setTitle("Bill Generator");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Vehicle ID label and text field
        JLabel vehicleIdLabel = new JLabel("Vehicle ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(vehicleIdLabel, gbc);

        vehicleIdField = new JTextField(20);
        gbc.gridx = 1;
        add(vehicleIdField, gbc);

        // Calculate Bill button
        JButton calculateButton = new JButton("Generate Bill");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(calculateButton, gbc);

        // Bill amount label
        billLabel = new JLabel("");
        gbc.gridy = 2;
        add(billLabel, gbc);

        // Button action
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String vehicleId = vehicleIdField.getText();
                if (vehicleId.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a vehicle ID.");
                    return;
                }
                try {
                    manager.vehicleExit(vehicleId);
                    double amount = calculateBill(vehicleId);
                    billLabel.setText("Amount to Pay: ₹" + amount);

                    // Create a Timer to hide the frame after 5 seconds
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(() -> setVisible(false));
                        }
                    }, 5000); // 5000 milliseconds = 5 seconds

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error calculating bill.");
                }
            }
        });
    }

    private double calculateBill(String vehicleId) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql:///parking_lot", "root", "Krish@19");
        PreparedStatement pst = connection.prepareStatement(
                "SELECT entry_time, exit_time FROM parking_history WHERE vehicle_id = ?");
        pst.setString(1, vehicleId);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            Timestamp entryTime = rs.getTimestamp("entry_time");
            Timestamp exitTime = rs.getTimestamp("exit_time");

            if (entryTime == null || exitTime == null) {
                throw new SQLException("Entry or exit time is missing.");
            }

            long durationMillis = exitTime.getTime() - entryTime.getTime();
            long durationHours = (durationMillis / (1000 * 60 * 60)) + 1; // Round up to next full hour

            return durationHours * RATE_PER_HOUR;
        } else {
            throw new SQLException("Vehicle ID not found.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BillGeneratorGUI().setVisible(true));
    }
}
