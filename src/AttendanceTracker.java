import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceTracker {
    public static void markAttendance(String empId, boolean isPresent) throws SQLException {
        String sql = "INSERT INTO attendance (emp_id, date, is_present) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, empId);
            pstmt.setDate(2, Date.valueOf(LocalDate.now()));
            pstmt.setBoolean(3, isPresent);
            pstmt.executeUpdate();
        }
    }
    
    public static void addOvertime(String empId, double hours) throws SQLException {
        String sql = "INSERT INTO attendance (emp_id, date, overtime_hours) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, empId);
            pstmt.setDate(2, Date.valueOf(LocalDate.now()));
            pstmt.setDouble(3, hours);
            pstmt.executeUpdate();
        }
    }
    
    public static int getDaysPresent(String empId, int month, int year) throws SQLException {
        String sql = "SELECT COUNT(*) FROM attendance WHERE emp_id = ? AND is_present = TRUE " +
                     "AND MONTH(date) = ? AND YEAR(date) = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, empId);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    // Add to AttendanceTracker.java
public static void markAllEmployeesAttendance(LocalDate date, Map<String, Boolean> attendanceMap) throws SQLException {
    String sql = "INSERT INTO attendance (emp_id, date, is_present) VALUES (?, ?, ?) " +
                 "ON DUPLICATE KEY UPDATE is_present = ?";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        for (Map.Entry<String, Boolean> entry : attendanceMap.entrySet()) {
            pstmt.setString(1, entry.getKey());
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setBoolean(3, entry.getValue());
            pstmt.setBoolean(4, entry.getValue());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
    }
}

public static Map<String, Boolean> getDailyAttendance(LocalDate date) throws SQLException {
    Map<String, Boolean> attendanceMap = new HashMap<>();
    String sql = "SELECT e.id, COALESCE(a.is_present, false) as is_present " +
                 "FROM employees e LEFT JOIN attendance a " +
                 "ON e.id = a.emp_id AND a.date = ?";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setDate(1, Date.valueOf(date));
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            attendanceMap.put(rs.getString("id"), rs.getBoolean("is_present"));
        }
    }
    return attendanceMap;
}
// Add to AttendanceTracker.java
public static void generateAttendanceReport(LocalDate startDate, LocalDate endDate) throws SQLException {
    // Get all employees
    List<Employee> employees = Employee.getAllEmployees();
    
    // Print report header
    System.out.println("\nATTENDANCE REPORT (" + startDate + " to " + endDate + ")");
    System.out.println("====================================================================");
    System.out.printf("%-10s %-20s %-10s %-10s %-10s%n", 
        "Emp ID", "Name", "Present", "Absent", "Overtime");
    System.out.println("--------------------------------------------------------------------");
    
    // Calculate attendance for each employee
    for (Employee emp : employees) {
        // Get present days
        String presentSql = "SELECT COUNT(*) FROM attendance " +
                          "WHERE emp_id = ? AND is_present = TRUE " +
                          "AND date BETWEEN ? AND ?";
        
        // Get overtime hours
        String overtimeSql = "SELECT SUM(overtime_hours) FROM attendance " +
                           "WHERE emp_id = ? AND date BETWEEN ? AND ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement presentStmt = conn.prepareStatement(presentSql);
             PreparedStatement overtimeStmt = conn.prepareStatement(overtimeSql)) {
            
            // Set parameters for both queries
            presentStmt.setString(1, emp.getId());
            presentStmt.setDate(2, Date.valueOf(startDate));
            presentStmt.setDate(3, Date.valueOf(endDate));
            
            overtimeStmt.setString(1, emp.getId());
            overtimeStmt.setDate(2, Date.valueOf(startDate));
            overtimeStmt.setDate(3, Date.valueOf(endDate));
            
            // Execute queries
            ResultSet presentRs = presentStmt.executeQuery();
            ResultSet overtimeRs = overtimeStmt.executeQuery();
            
            // Get results
            int presentDays = presentRs.next() ? presentRs.getInt(1) : 0;
            double overtimeHours = overtimeRs.next() ? overtimeRs.getDouble(1) : 0.0;
            int totalDays = (int) startDate.datesUntil(endDate.plusDays(1)).count();
            int absentDays = totalDays - presentDays;
            
            // Print employee row
            System.out.printf("%-10s %-20s %-10d %-10d %-10.2f%n",
                emp.getId(),
                emp.getName(),
                presentDays,
                absentDays,
                overtimeHours);
        }
    }
    System.out.println("====================================================================");
}
    
    public static double getOvertimeHours(String empId, int month, int year) throws SQLException {
        String sql = "SELECT SUM(overtime_hours) FROM attendance WHERE emp_id = ? " +
                     "AND MONTH(date) = ? AND YEAR(date) = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, empId);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }
}