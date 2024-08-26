import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ParkingLotManager {
    private static final int TOTAL_2WHEELER_SPOTS = 20;
    private static final int TOTAL_4WHEELER_SPOTS = 20;

    public void vehicleEntry(String name, String vehicleId, String vehicleType) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (!isSpotAvailable(vehicleType)) {
                throw new SQLException("No available spots for " + vehicleType);
            }

            String sql = "INSERT INTO parking_history (name, vehicle_id, vehicle_type, entry_time, exit_time, status) VALUES (?, ?, ?, ?, NULL, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, vehicleId);
            statement.setString(3, vehicleType);
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            statement.setString(5, "Entered");
            statement.executeUpdate();

            updateSpotAvailability(vehicleType, false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void vehicleExit(String vehicleId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Check if the vehicle ID exists and is currently in the "Entered" state
            String checkSql = "SELECT vehicle_type FROM parking_history WHERE vehicle_id = ? AND status = 'Entered'";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, vehicleId);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                String vehicleType = resultSet.getString("vehicle_type");

                // Update the exit time and status
                String updateSql = "UPDATE parking_history SET exit_time = ?, status = ? WHERE vehicle_id = ? AND status = 'Entered'";
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                updateStatement.setString(2, "Exited");
                updateStatement.setString(3, vehicleId);
                int rowsUpdated = updateStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    // Update spot availability
                    updateSpotAvailability(vehicleType, true);
                } else {
                    throw new SQLException("Failed to update exit status. Vehicle might already have exited.");
                }
            } else {
                throw new SQLException("Vehicle ID not found or not in 'Entered' status.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private boolean isSpotAvailable(String vehicleType) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM parking_history WHERE vehicle_type = ? AND status = 'Entered'";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, vehicleType);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int parkedCount = resultSet.getInt("count");

            if (vehicleType.equals("2-Wheeler")) {
                return parkedCount < TOTAL_2WHEELER_SPOTS;
            } else if (vehicleType.equals("4-Wheeler")) {
                return parkedCount < TOTAL_4WHEELER_SPOTS;
            }
            return false;
        }
    }

    private void updateSpotAvailability(String vehicleType, boolean isEntering) throws SQLException {
        String sql = "UPDATE spot_availability SET available_spots = available_spots " + (isEntering ? "+ 1" : "- 1") + " WHERE vehicle_type = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, vehicleType);
            statement.executeUpdate();
        }
    }


}
