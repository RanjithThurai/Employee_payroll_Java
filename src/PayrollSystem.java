import java.sql.Connection; // NEW
import java.sql.PreparedStatement; // NEW
import java.sql.ResultSet; // NEW
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PayrollSystem {
    // ... rest of the class
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("Employee Payroll System with MySQL Database");
        
        try {
            // Test database connection
            DBConnection.getConnection();
            System.out.println("Connected to database successfully!");
            
            while(true) {
                System.out.println("\nMain Menu:");
                System.out.println("1. Employee Management");
                System.out.println("2. Attendance Tracking");
                System.out.println("3. Generate Payslip");
                System.out.println("4. Reports & Configuration"); // MODIFIED ITEM
                System.out.println("5. Exit"); // CHOICE NUMBER CHANGED
                System.out.print("Enter your choice: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch(choice) {
                    case 1:
                        employeeManagementMenu();
                        break;
                    case 2:
                        attendanceTrackingMenu();
                        break;
                    case 3:
                        generatePayslipMenu();
                        break;
                    case 4: // NEW CASE
                        reportsMenu();
                        break;
                    case 5: // CHOICE NUMBER CHANGED
                        System.out.println("Exiting system...");
                        DBConnection.closeConnection();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
    
    private static void employeeManagementMenu() throws SQLException {
        while(true) {
            System.out.println("\nEmployee Management:");
            System.out.println("1. Add Employee");
            System.out.println("2. View Employee");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. View All Employees");
            System.out.println("6. View Salary History"); // NEW ITEM
            System.out.println("7. Back to Main Menu"); // CHOICE NUMBER CHANGED
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch(choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    viewEmployee();
                    break;
                case 3:
                    updateEmployee();
                    break;
                case 4:
                    deleteEmployee();
                    break;
                case 5:
                    viewAllEmployees();
                    break;
                case 6: // NEW CASE
                    viewSalaryHistoryMenu();
                    break;
                case 7: // CHOICE NUMBER CHANGED
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    // *** NEW METHOD for Salary History ***
    private static void viewSalaryHistoryMenu() throws SQLException {
        System.out.print("Enter Employee ID to view history: ");
        String id = scanner.nextLine();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM salary_history WHERE emp_id = ? ORDER BY change_date DESC")) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (!rs.isBeforeFirst()) {
                System.out.println("No salary history found for Employee ID: " + id);
                return;
            }
            
            System.out.println("\nSalary History for Employee ID: " + id);
            System.out.println("---------------------------------------------------------");
            System.out.printf("%-15s %-15s %-15s%n", "Change Date", "Old Salary", "New Salary");
            System.out.println("---------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-15s %-15.2f %-15.2f%n", 
                    rs.getDate("change_date").toLocalDate(), 
                    rs.getDouble("old_basic_salary"),
                    rs.getDouble("new_basic_salary"));
            }
            System.out.println("---------------------------------------------------------");
            
        } catch (SQLException e) {
            System.err.println("Error viewing salary history: " + e.getMessage());
        }
    }
    // *** End NEW METHOD ***
    
    // ... (rest of addEmployee, viewEmployee, updateEmployee, deleteEmployee, viewAllEmployees are unchanged)
    
    private static void addEmployee() throws SQLException {
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Position: ");
        String position = scanner.nextLine();
        System.out.print("Enter Basic Salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        
        Employee emp = new Employee(id, name, position, salary);
        Employee.addEmployee(emp);
        System.out.println("Employee added successfully!");
    }
    
    private static void viewEmployee() throws SQLException {
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine();
        Employee emp = Employee.getEmployee(id);
        
        if(emp != null) {
            System.out.println("\nEmployee Details:");
            System.out.println("ID: " + emp.getId());
            System.out.println("Name: " + emp.getName());
            System.out.println("Position: " + emp.getPosition());
            System.out.println("Basic Salary: " + emp.getBasicSalary());
        } else {
            System.out.println("Employee not found!");
        }
    }
    
    private static void updateEmployee() throws SQLException {
        System.out.print("Enter Employee ID to update: ");
        String id = scanner.nextLine();
        Employee emp = Employee.getEmployee(id);
        
        if(emp != null) {
            System.out.print("Enter new Name (current: " + emp.getName() + "): ");
            String name = scanner.nextLine();
            System.out.print("Enter new Position (current: " + emp.getPosition() + "): ");
            String position = scanner.nextLine();
            System.out.print("Enter new Basic Salary (current: " + emp.getBasicSalary() + "): ");
            double salary = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            
            emp.setName(name);
            emp.setPosition(position);
            emp.setBasicSalary(salary);
            // Employee.updateEmployee method now handles logging salary history!
            Employee.updateEmployee(emp); 
            System.out.println("Employee updated successfully! Salary change logged.");
        } else {
            System.out.println("Employee not found!");
        }
    }
    
    private static void deleteEmployee() throws SQLException {
        System.out.print("Enter Employee ID to delete: ");
        String id = scanner.nextLine();
        Employee.deleteEmployee(id);
        System.out.println("Employee deleted successfully!");
    }
    
    private static void viewAllEmployees() throws SQLException {
        List<Employee> employees = Employee.getAllEmployees();
        
        System.out.println("\nEmployee List:");
        System.out.println("ID\tName\tPosition\tBasic Salary");
        for(Employee emp : employees) {
            System.out.printf("%s\t%s\t%s\t%.2f%n", 
                emp.getId(), emp.getName(), emp.getPosition(), emp.getBasicSalary());
        }
    }
    
    private static void attendanceTrackingMenu() throws SQLException {
    while(true) {
        System.out.println("\nAttendance Tracking:");
        System.out.println("1. Add Overtime");
        System.out.println("2. Mark Daily Attendance for All");
        System.out.println("3. Generate Attendance Report");
        System.out.println("4. Back to Main Menu");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        switch(choice) {
            case 1:
                addOvertime();
                break;
            case 2:
                markDailyAttendanceForAll();
                break;
            case 3:
                generateAttendanceReport();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}

private static void generateAttendanceReport() throws SQLException {
    System.out.println("\nGENERATE ATTENDANCE REPORT");
    System.out.print("Enter start date (YYYY-MM-DD): ");
    LocalDate startDate = LocalDate.parse(scanner.nextLine());
    System.out.print("Enter end date (YYYY-MM-DD): ");
    LocalDate endDate = LocalDate.parse(scanner.nextLine());
    
    if (startDate.isAfter(endDate)) {
        System.out.println("Error: Start date must be before end date!");
        return;
    }
    
    AttendanceTracker.generateAttendanceReport(startDate, endDate);
}
// This method should be outside attendanceTrackingMenu()
private static void markDailyAttendanceForAll() throws SQLException {
    System.out.println("\nDAILY ATTENDANCE FOR ALL EMPLOYEES");
    LocalDate date = LocalDate.now();
    System.out.println("Date: " + date);
    
    // Get all employees
    List<Employee> employees = Employee.getAllEmployees();
    Map<String, Boolean> attendanceMap = new HashMap<>();
    
    // Default all as absent first
    for (Employee emp : employees) {
        attendanceMap.put(emp.getId(), false);
    }
    
    // Mark present employees
    System.out.println("Mark present employees (Enter ID, 'done' to finish):");
    while (true) {
        System.out.print("Employee ID (or 'done'): ");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("done")) break;
        
        if (attendanceMap.containsKey(input)) {
            attendanceMap.put(input, true);
            System.out.println("Marked present: " + input);
        } else {
            System.out.println("Invalid ID! Try again.");
        }
    }
    
    // Save to database
    AttendanceTracker.markAllEmployeesAttendance(date, attendanceMap);
    System.out.println("Attendance saved for " + date);
}
    
   
    
    private static void addOvertime() throws SQLException {
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine();
        
        if(Employee.getEmployee(id) != null) {
            System.out.print("Enter overtime hours: ");
            double hours = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            AttendanceTracker.addOvertime(id, hours);
            System.out.println("Overtime recorded!");
        } else {
            System.out.println("Employee not found!");
        }
    }
    
    // *** MODIFIED METHOD for Detailed Payslip Breakdown ***
    private static void generatePayslipMenu() throws SQLException {
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine();
        Employee emp = Employee.getEmployee(id);
        
        if(emp != null) {
            LocalDate currentDate = LocalDate.now();
            double overtimeHours = AttendanceTracker.getOvertimeHours(
                id, currentDate.getMonthValue(), currentDate.getYear());
            
            double grossSalary = PayrollCalculator.calculateGrossSalary(
                emp.getBasicSalary(), overtimeHours);
            double tax = PayrollCalculator.calculateTax(grossSalary);
            double pf = PayrollCalculator.calculatePF(emp.getBasicSalary()); // NEW
            double netSalary = PayrollCalculator.calculateNetSalary(grossSalary, emp.getBasicSalary()); // MODIFIED
            
            // MODIFIED call to PayslipGenerator
            PayslipGenerator.generatePayslip(emp, grossSalary, tax, pf, netSalary); 
        } else {
            System.out.println("Employee not found!");
        }
    }
    // *** End MODIFIED METHOD ***
    
    // *** NEW METHOD for Reports ***
    private static void reportsMenu() throws SQLException {
        while(true) {
            System.out.println("\nReports & Configuration Menu:"); // MODIFIED LABEL
            System.out.println("1. Monthly Payroll Register (CSV)");
            System.out.println("2. Change Payroll Rates"); // NEW ITEM
            System.out.println("3. Back to Main Menu"); // CHOICE NUMBER CHANGED
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch(choice) {
                case 1:
                    generatePayrollRegisterMenu();
                    break;
                case 2: // NEW CASE
                    changePayrollRatesMenu();
                    break;
                case 3: // CHOICE NUMBER CHANGED
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    // *** End NEW METHOD ***
    
    // *** NEW METHOD for changing Taxing Attributes ***
    private static void changePayrollRatesMenu() throws SQLException {
        PayrollConfig.Rates currentRates = PayrollConfig.loadRates();
        
        System.out.println("\n--- Change Payroll Rates ---");
        System.out.println("Current Tax Rate: " + (currentRates.taxRate * 100) + "%");
        System.out.println("Current PF Rate: " + (currentRates.pfRate * 100) + "%");
        System.out.println("Current Overtime Multiplier: " + currentRates.overtimeRate + "x");
        
        System.out.print("Enter NEW Tax Rate (e.g., 0.15 for 15%): ");
        double newTaxRate = scanner.nextDouble();
        System.out.print("Enter NEW PF Rate (e.g., 0.05 for 5%): ");
        double newPfRate = scanner.nextDouble();
        System.out.print("Enter NEW Overtime Multiplier (e.g., 1.5): ");
        double newOvertimeRate = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        
        if (newTaxRate < 0 || newPfRate < 0 || newOvertimeRate < 0) {
            System.out.println("Rates cannot be negative. Update cancelled.");
            return;
        }

        try {
            PayrollConfig.updateRates(newTaxRate, newPfRate, newOvertimeRate);
            System.out.println("✅ Payroll rates updated successfully!");
        } catch (SQLException e) {
            System.err.println("Error updating payroll rates: " + e.getMessage());
        }
    }
    // *** End NEW METHOD ***

    // *** NEW METHOD for Payroll Register Input ***
    private static void generatePayrollRegisterMenu() throws SQLException {
        System.out.print("Enter month (1-12): ");
        int month = scanner.nextInt();
        System.out.print("Enter year (YYYY): ");
        int year = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        if (month < 1 || month > 12) {
            System.out.println("Invalid month. Please try again.");
            return;
        }
        
        // This relies on the new ReportGenerator.java class
        ReportGenerator.generateMonthlyPayrollRegister(month, year);
    }
    // *** End NEW METHOD ***
}