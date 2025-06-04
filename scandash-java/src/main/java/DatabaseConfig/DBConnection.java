package DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database credentials
    private static final String URL = "jdbc:postgresql://aws-0-ap-south-1.pooler.supabase.com:5432/postgres";
    private static final String USER = "postgres.hcsvpkykasdfmmflldby";
    private static final String PASSWORD = "_ScanDash7226";

    /**
     * Establishes and returns a connection to the PostgreSQL database.
     *
     * @return Connection object if the connection is successful, otherwise null.
     */
    public static Connection connectToDB() {
        try {
            // Create and return the connection
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database successfully!");
            return conn;
        } catch (SQLException e) {
            // Handle SQL exceptions
            System.err.println("Connection to the database failed!");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Test the database connection.
     */
    public static void main(String[] args) {
        // Test the connection
        Connection testConnection = connectToDB();
        if (testConnection != null) {
            try {
                testConnection.close();
                System.out.println("Connection closed successfully!");
            } catch (SQLException e) {
                System.err.println("Failed to close the connection!");
                e.printStackTrace();
            }
        }
    }
}
