import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Employee {
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
    
    public static void updateEmployee(Employee emp) throws SQLException {
        String sql = "UPDATE employees SET name = ?, position = ?, basic_salary = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, emp.getName());
            pstmt.setString(2, emp.getPosition());
            pstmt.setDouble(3, emp.getBasicSalary());
            pstmt.setString(4, emp.getId());
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