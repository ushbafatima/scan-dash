package SalesManagement;

import DatabaseConfig.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.TreeMap;
import java.util.Map;

public class SalesDataAccess {
    public static Map<LocalDate, Double> getDailySales() {
        Map<LocalDate, Double> salesData = new TreeMap<>(); // TreeMap maintains date order

        String query = "SELECT date, SUM(\"totalAmount\") as daily_total " +
                "FROM \"salesPerCard\" " +
                "GROUP BY date " +
                "ORDER BY date";

        try (Connection conn = DBConnection.connectToDB();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                LocalDate date = rs.getDate("date").toLocalDate();
                double total = rs.getDouble("daily_total");
                salesData.put(date, total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salesData;
    }
}