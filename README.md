# Employee Payroll System (Java)

![Java](https://img.shields.io/badge/Language-Java-red)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue)

A Java-based Employee Payroll System to manage employee records, calculate salaries, and generate payslips efficiently.

---

## Features
- Add, update, and manage employee details
- Calculate monthly payroll automatically
- Generate payslips for employees
- Connects to MySQL database for persistent storage

---

## Prerequisites
- Java JDK 8 or higher
- MySQL database
- MySQL Connector/J (`mysql-connector-j-9.4.0.jar` included in `lib/`)

# Create the database Structure:

    CREATE TABLE employees (
        id VARCHAR(20) PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        position VARCHAR(100),
        basic_salary DECIMAL(10,2)
    );


    Create the attendance table:

    CREATE TABLE attendance (
        id INT AUTO_INCREMENT PRIMARY KEY,
        emp_id VARCHAR(20),
        date DATE NOT NULL,
        is_present BOOLEAN DEFAULT FALSE,
        overtime_hours DECIMAL(5,2) DEFAULT 0.00,
        FOREIGN KEY (emp_id) REFERENCES employees(id),
        UNIQUE KEY unique_attendance (emp_id, date)
    );


    Update database connection in DBConnection.java:

    String url = "jdbc:mysql://localhost:3306/payroll_db";
    String username = "root";      // your MySQL username
    String password = "password";  // your MySQL password


---

## How to Compile and Run
1. Open terminal/PowerShell in the `src/` folder.
2. Compile all Java files:
   ```powershell
   javac -cp ".;..\lib\mysql-connector-j-9.4.0.jar" *.java
   java -cp ".;..\lib\mysql-connector-j-9.4.0.jar" PayrollSystem