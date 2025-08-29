import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PayslipGenerator {
    public static void generatePayslip(Employee emp, double grossSalary, double tax, double netSalary) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        String monthYear = dateFormat.format(new Date());
        
        String payslip = String.format(
            "========== PAYSLIP ==========\n" +
            "Employee ID: %s\n" +
            "Name: %s\n" +
            "Position: %s\n" +
            "Month: %s\n\n" +
            "Basic Salary: %.2f\n" +
            "Overtime Pay: %.2f\n" +
            "Gross Salary: %.2f\n" +
            "Tax Deduction: %.2f\n" +
            "Net Salary: %.2f\n" +
            "=============================",
            emp.getId(), emp.getName(), emp.getPosition(), monthYear,
            emp.getBasicSalary(), grossSalary - emp.getBasicSalary(),
            grossSalary, tax, netSalary
        );
        
        System.out.println(payslip);
        saveToFile(emp.getId() + "_" + monthYear.replace(" ", "_") + ".txt", payslip);
    }
    
    private static void saveToFile(String filename, String content) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
            System.out.println("Payslip saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving payslip: " + e.getMessage());
        }
    }
}