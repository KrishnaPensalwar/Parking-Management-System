import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class HistoryTracker {

    public void displayHistory() {
        // Create JFrame for displaying history
        JFrame frame = new JFrame("Parking History");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);

        // Table model to hold data
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("Vehicle ID");
        tableModel.addColumn("Vehicle Type");
        tableModel.addColumn("Entry Time");
        tableModel.addColumn("Exit Time");
        tableModel.addColumn("Status");

        // JTable to display the data
        JTable table = new JTable(tableModel);

        // Fetch data from database and populate the table model
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM parking_history";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String vehicleId = resultSet.getString("vehicle_id");
                String vehicleType = resultSet.getString("vehicle_type");
                Timestamp entryTime = resultSet.getTimestamp("entry_time");
                Timestamp exitTime = resultSet.getTimestamp("exit_time");
                String status = resultSet.getString("status");

                // Add data to the table model
                tableModel.addRow(new Object[]{name, vehicleId, vehicleType, entryTime, exitTime, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add JTable to JScrollPane for smooth scrolling
        JScrollPane scrollPane = new JScrollPane(table);

        // Add scrollPane to the frame
        frame.add(scrollPane);
        frame.setVisible(true);
    }
}
