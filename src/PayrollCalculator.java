public class PayrollCalculator {
    private static final double TAX_RATE = 0.15; // 15% tax
    private static final double OVERTIME_RATE = 1.5; // 1.5x hourly rate
    
    public static double calculateGrossSalary(double basicSalary, double overtimeHours) {
        double hourlyRate = basicSalary / 160; // Assuming 160 working hours/month
        return basicSalary + (overtimeHours * hourlyRate * OVERTIME_RATE);
    }
    
    public static double calculateTax(double grossSalary) {
        return grossSalary * TAX_RATE;
    }
    
    public static double calculateNetSalary(double grossSalary) {
        return grossSalary - calculateTax(grossSalary);
    }
}