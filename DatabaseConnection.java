import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection("jdbc:mysql:///parking_lot","root","Krish@19");
    }

}
