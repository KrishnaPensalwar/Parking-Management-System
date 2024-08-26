import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VehicleEntryForm extends JFrame {

    private JTextField driverNameField;
    private JTextField vehicleNumberField;
    private JComboBox<String> vehicleTypeComboBox;
    private JLabel messageLabel;
    private ParkingLotManager manager;

    public VehicleEntryForm() {
        manager = new ParkingLotManager(); // Assuming this class is already defined

        // Set up the JFrame
        setTitle("Vehicle Entry Form");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocation(520,200);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Driver Name label and text field
        JLabel driverNameLabel = new JLabel("Driver Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(driverNameLabel, gbc);

        driverNameField = new JTextField(20);
        gbc.gridx = 1;
        add(driverNameField, gbc);

        // Vehicle Number label and text field
        JLabel vehicleNumberLabel = new JLabel("Vehicle Number:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(vehicleNumberLabel, gbc);

        vehicleNumberField = new JTextField(20);
        gbc.gridx = 1;
        add(vehicleNumberField, gbc);

        // Vehicle Type label and dropdown
        JLabel vehicleTypeLabel = new JLabel("Vehicle Type:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(vehicleTypeLabel, gbc);

        String[] vehicleTypes = {"Select" ,"2-Wheeler", "4-Wheeler"};
        vehicleTypeComboBox = new JComboBox<>(vehicleTypes);
        gbc.gridx = 1;
        add(vehicleTypeComboBox, gbc);

        // Submit button
        JButton submitButton = new JButton("Submit");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(submitButton, gbc);

        // Message label
        messageLabel = new JLabel("");
        gbc.gridy = 4;
        add(messageLabel, gbc);

        // Button action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String driverName = driverNameField.getText();
                String vehicleNumber = vehicleNumberField.getText();
                String vehicleType = (String) vehicleTypeComboBox.getSelectedItem();

                if (driverName.isEmpty() || vehicleNumber.isEmpty() || vehicleType == null || vehicleType == "Select") {
                    messageLabel.setText("Please fill out all fields.");
                    messageLabel.setForeground(Color.RED);
                } else {
                    // Perform vehicle entry operation
                    manager.vehicleEntry(driverName, vehicleNumber, vehicleType);
                    messageLabel.setText("Vehicle Entry Successful.");
                    messageLabel.setForeground(Color.GREEN);
                    setVisible(false);
                }

            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VehicleEntryForm().setVisible(true));
    }
}
