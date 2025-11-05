import java.sql.*;
import java.time.LocalDate; // Import for LocalDate
import java.util.ArrayList;
import java.util.List;

public class Employee {
    // ... (rest of the class fields and existing getters/setters are unchanged)
    private String id;
    private String name;
    private String position;
    private double basicSalary;
    
    public Employee(String id, String name, String position, double basicSalary) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.basicSalary = basicSalary;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPosition() { return position; }
    public double getBasicSalary() { return basicSalary; }
    
    public void setName(String name) { this.name = name; }
    public void setPosition(String position) { this.position = position; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }

    // Database operations
    public static void addEmployee(Employee emp) throws SQLException {
        String sql = "INSERT INTO employees (id, name, position, basic_salary) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, emp.getId());
            pstmt.setString(2, emp.getName());
            pstmt.setString(3, emp.getPosition());
            pstmt.setDouble(4, emp.getBasicSalary());
            pstmt.executeUpdate();
        }
    }
    
    public static Employee getEmployee(String id) throws SQLException {
        String sql = "SELECT * FROM employees WHERE id = ?";
        Employee emp = null;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                emp = new Employee(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("position"),
                    rs.getDouble("basic_salary")
                );
            }
        }
        return emp;
    }
    
    // *** MODIFIED METHOD ***
    public static void updateEmployee(Employee newEmp) throws SQLException {
        Employee oldEmp = getEmployee(newEmp.getId());
        
        // 1. Log to history if basic salary is changing
        if (oldEmp != null && oldEmp.getBasicSalary() != newEmp.getBasicSalary()) {
            logSalaryChange(newEmp.getId(), oldEmp.getBasicSalary(), newEmp.getBasicSalary());
        }
        
        // 2. Perform the update
        String sql = "UPDATE employees SET name = ?, position = ?, basic_salary = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEmp.getName());
            pstmt.setString(2, newEmp.getPosition());
            pstmt.setDouble(3, newEmp.getBasicSalary());
            pstmt.setString(4, newEmp.getId());
            pstmt.executeUpdate();
        }
    }

    // New helper method to log salary changes
    private static void logSalaryChange(String empId, double oldSalary, double newSalary) throws SQLException {
        String sql = "INSERT INTO salary_history (emp_id, change_date, old_basic_salary, new_basic_salary) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, empId);
            pstmt.setDate(2, Date.valueOf(LocalDate.now()));
            pstmt.setDouble(3, oldSalary);
            pstmt.setDouble(4, newSalary);
            pstmt.executeUpdate();
        }
    }

    public static void deleteEmployee(String id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }
    
    public static List<Employee> getAllEmployees() throws SQLException {
        String sql = "SELECT * FROM employees";
        List<Employee> employees = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Employee emp = new Employee(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("position"),
                    rs.getDouble("basic_salary")
                );
                employees.add(emp);
            }
        }
        return employees;
    }
}