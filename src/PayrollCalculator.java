public class PayrollCalculator {
    private static final double TAX_RATE = 0.15; // 15% tax
    private static final double PF_RATE = 0.05;  // 5% Provident Fund deduction (New)
    private static final double OVERTIME_RATE = 1.5; // 1.5x hourly rate
    
    public static double calculateGrossSalary(double basicSalary, double overtimeHours) {
        double hourlyRate = basicSalary / 160; // Assuming 160 working hours/month
        return basicSalary + (overtimeHours * hourlyRate * OVERTIME_RATE);
    }
    
    public static double calculateTax(double grossSalary) {
        return grossSalary * TAX_RATE;
    }
    
    // New method for PF calculation
    public static double calculatePF(double basicSalary) {
        return basicSalary * PF_RATE;
    }
    
    // Updated Net Salary calculation to include PF
    public static double calculateNetSalary(double grossSalary, double basicSalary) {
        double totalDeduction = calculateTax(grossSalary) + calculatePF(basicSalary);
        return grossSalary - totalDeduction;
    }
    
    // Helper to get total deductions for reporting
    public static double calculateTotalDeductions(double grossSalary, double basicSalary) {
        return calculateTax(grossSalary) + calculatePF(basicSalary);
    }
}