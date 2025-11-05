public class PayrollCalculator {
    // Remove the fixed static final constants: TAX_RATE, PF_RATE, OVERTIME_RATE
    
    // Helper method to get current rates
    private static PayrollConfig.Rates getCurrentRates() {
        try {
            return PayrollConfig.loadRates();
        } catch (Exception e) {
            // Log error and return safe defaults in case of a DB connection failure
            System.err.println("Error loading payroll rates: " + e.getMessage());
            PayrollConfig.Rates defaultRates = new PayrollConfig.Rates();
            defaultRates.taxRate = 0.15;
            defaultRates.pfRate = 0.05;
            defaultRates.overtimeRate = 1.5;
            return defaultRates;
        }
    }

    public static double calculateGrossSalary(double basicSalary, double overtimeHours) {
        PayrollConfig.Rates rates = getCurrentRates();
        double hourlyRate = basicSalary / 160; // Assuming 160 working hours/month
        // Use dynamic rate
        return basicSalary + (overtimeHours * hourlyRate * rates.overtimeRate);
    }
    
    public static double calculateTax(double grossSalary) {
        PayrollConfig.Rates rates = getCurrentRates();
        // Use dynamic rate
        return grossSalary * rates.taxRate;
    }
    
    public static double calculatePF(double basicSalary) {
        PayrollConfig.Rates rates = getCurrentRates();
        // Use dynamic rate
        return basicSalary * rates.pfRate;
    }
    
    // Updated Net Salary calculation remains the same, but internal calls use dynamic rates
    public static double calculateNetSalary(double grossSalary, double basicSalary) {
        double totalDeduction = calculateTax(grossSalary) + calculatePF(basicSalary);
        return grossSalary - totalDeduction;
    }
    
    // Helper to get total deductions for reporting
    public static double calculateTotalDeductions(double grossSalary, double basicSalary) {
        return calculateTax(grossSalary) + calculatePF(basicSalary);
    }
}