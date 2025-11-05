import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReportGenerator {

    public static void generateMonthlyPayrollRegister(int month, int year) throws SQLException {
        // 1. Get all employees
        List<Employee> employees = Employee.getAllEmployees();
        
        String reportFileName = String.format("PayrollRegister_%d_%d.csv", year, month);
        
        System.out.println("\nGenerating Monthly Payroll Register for " + month + "/" + year);
        
        try (FileWriter writer = new FileWriter(reportFileName)) {
            // Write CSV Header
            writer.append("ID,Name,Basic Salary,Overtime Hours,Overtime Pay,Gross Salary,Tax,PF,Net Salary\n");
            
            double totalNetPayroll = 0;

            for (Employee emp : employees) {
                // Get month's overtime hours
                double overtimeHours = AttendanceTracker.getOvertimeHours(emp.getId(), month, year);
                
                // Calculate payroll components
                double grossSalary = PayrollCalculator.calculateGrossSalary(emp.getBasicSalary(), overtimeHours);
                double tax = PayrollCalculator.calculateTax(grossSalary);
                double pf = PayrollCalculator.calculatePF(emp.getBasicSalary());
                double netSalary = PayrollCalculator.calculateNetSalary(grossSalary, emp.getBasicSalary());
                double overtimePay = grossSalary - emp.getBasicSalary();
                
                // Write employee details to CSV
                String line = String.format("%s,%s,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f\n",
                    emp.getId(),
                    emp.getName(),
                    emp.getBasicSalary(),
                    overtimeHours,
                    overtimePay,
                    grossSalary,
                    tax,
                    pf,
                    netSalary);
                
                writer.append(line);
                totalNetPayroll += netSalary;
            }
            
            // Write a footer with total payroll
            writer.append(String.format(",,,,,,TOTAL NET PAYROLL,,%.2f\n", totalNetPayroll));

            System.out.println("✅ Report successfully generated and saved to: " + reportFileName);
        } catch (IOException e) {
            System.err.println("Error writing report file: " + e.getMessage());
        }
    }
}