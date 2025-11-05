import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PayrollConfig {
    
    // Static inner class to hold the configuration data
    public static class Rates {
        public double taxRate;
        public double pfRate;
        public double overtimeRate;
    }

    // Method to load current rates from the database
    public static Rates loadRates() throws SQLException {
        String sql = "SELECT tax_rate, pf_rate, overtime_rate FROM payroll_config WHERE id = 1";
        Rates rates = new Rates();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                rates.taxRate = rs.getDouble("tax_rate");
                rates.pfRate = rs.getDouble("pf_rate");
                rates.overtimeRate = rs.getDouble("overtime_rate");
                return rates;
            }
        }
        // Fallback to defaults if DB is empty, though the INSERT statement should prevent this
        rates.taxRate = 0.15;
        rates.pfRate = 0.05;
        rates.overtimeRate = 1.5;
        return rates;
    }

    // Method to update rates in the database
    public static void updateRates(double taxRate, double pfRate, double overtimeRate) throws SQLException {
        // Assuming we always update the record with ID 1
        String sql = "UPDATE payroll_config SET tax_rate = ?, pf_rate = ?, overtime_rate = ? WHERE id = 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, taxRate);
            pstmt.setDouble(2, pfRate);
            pstmt.setDouble(3, overtimeRate);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                // If the record doesn't exist, insert it (shouldn't happen if setup is correct)
                String insertSql = "INSERT INTO payroll_config (id, tax_rate, pf_rate, overtime_rate) VALUES (1, ?, ?, ?)";
                 try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                    insertPstmt.setDouble(1, taxRate);
                    insertPstmt.setDouble(2, pfRate);
                    insertPstmt.setDouble(3, overtimeRate);
                    insertPstmt.executeUpdate();
                 }
            }
        }
    }
}