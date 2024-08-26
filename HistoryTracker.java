import java.sql.*;

public class HistoryTracker {
    public void displayHistory() {
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

                System.out.printf("Name : %s ,Vehicle ID: %s, Vehicle Type: %s, Entry Time: %s, Exit Time: %s, Status: %s%n",
                        name ,vehicleId, vehicleType, entryTime, exitTime, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
