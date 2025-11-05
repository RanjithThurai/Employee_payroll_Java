![MySQL](https://img.shields.io/badge/Database-MySQL-blue)
![CLI](https://img.shields.io/badge/Application-Offline%20CLI-success)

A **Java-based Employee Payroll System** designed to manage employee records, calculate salaries using **dynamic rates**, track attendance, and generate detailed reports and payslips efficiently.
It is an **offline Command-Line (CLI) application** connected to a **MySQL** database for persistent storage.

---

## 🚀 Key Features

### 1. Employee Management & Salary History 👥
* **CRUD Operations:** Easily **Add, View, Update, and Delete** employee records.
* **Salary History Tracking:** The system automatically logs and tracks changes to an employee's basic salary in the database.
* **View History:** Users can view a complete chronological record of salary changes for any employee.

### 2. Dynamic Payroll & Deductions 💸
* **Configurable Rates:** Tax Rate, PF Rate, and Overtime Multiplier are **stored in the database** and can be updated via the application's configuration menu.
* **Dynamic Calculation:** Gross and Net Salaries are calculated using the currently configured rates, providing flexibility for statutory changes.
* **Payslip Generation:** Generates a detailed monthly payslip showing the full breakdown of earnings and dynamic deductions.
* **Payslip Storage:** Generated payslips are saved as **text files** (`.txt`) in the root directory.

### 3. Comprehensive Attendance 📅
* **Daily Marking:** **Mark daily attendance** for all employees (Present/Absent).
* **Overtime:** Record specific **overtime hours** for individual employees.
* **Attendance Reports:** Generate a comprehensive report detailing Present Days, Absent Days, and Total Overtime Hours between custom dates.

### 4. Reporting & Configuration Module 📊⚙️
* **Change Payroll Rates (NEW):** Allows the administrator to **view and update** the core payroll rates (Tax, PF, Overtime Multiplier).
* **Monthly Payroll Register:** Generates a detailed report of all employee payroll calculations for a specified month/year.
* **Organized Storage:** All generated CSV reports are automatically saved within a dedicated **`payroll_reports` folder**.

---

## 🧩 Prerequisites
Before running this project, ensure you have:
* ☕ **Java JDK 8** or higher
* 🐬 **MySQL Server** installed and running
* 📦 **MySQL Connector/J** (`mysql-connector-j-9.4.0.jar`) — typically included in the `lib/` folder

---

## 🏗️ Database Setup

### Step 1: Create Database
Open MySQL and create a database named **`payroll_db`**:

```sql
CREATE DATABASE payroll_db;
USE payroll_db;
Step 2: Create Tables and Configuration
Run the following SQL commands to create the necessary tables and initialize the dynamic configuration table:

SQL

-- Employee Master Table
CREATE TABLE employees (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    position VARCHAR(100),
    basic_salary DECIMAL(10,2)
);

-- Attendance Tracking Table
CREATE TABLE attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id VARCHAR(20),
    date DATE NOT NULL,
    is_present BOOLEAN DEFAULT FALSE,
    overtime_hours DECIMAL(5,2) DEFAULT 0.00,
    FOREIGN KEY (emp_id) REFERENCES employees(id),
    UNIQUE KEY unique_attendance (emp_id, date)
);

-- Salary History Log (for tracking changes)
CREATE TABLE salary_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id VARCHAR(20) NOT NULL,
    change_date DATE NOT NULL,
    old_basic_salary DECIMAL(10, 2),
    new_basic_salary DECIMAL(10, 2),
    FOREIGN KEY (emp_id) REFERENCES employees(id)
);

-- Global Payroll Configuration (for dynamic rates)
CREATE TABLE payroll_config (
    id INT PRIMARY KEY,
    tax_rate DECIMAL(5, 4) NOT NULL,    
    pf_rate DECIMAL(5, 4) NOT NULL,     
    overtime_rate DECIMAL(5, 2) NOT NULL 
);

-- Initialize default rates
INSERT INTO payroll_config (id, tax_rate, pf_rate, overtime_rate) VALUES (1, 0.15, 0.05, 1.50);
⚙️ Database Configuration
Open the file DBConnection.java and update your MySQL credentials:

Java

String URL = "jdbc:mysql://localhost:3306/payroll_db";
String USER = "root";      // your MySQL username
String PASSWORD = "";  // your MySQL password
▶️ How to Compile and Run
Step 1: Open Terminal
Navigate to the src/ folder inside your project directory.

Step 2: Compile Java Files
Use the provided MySQL Connector in the classpath:

Bash

javac -cp ".;..\lib\mysql-connector-j-9.4.0.jar" *.java
Step 3: Run the Application
Start the Payroll System:

Bash

java -cp ".;..\lib\mysql-connector-j-9.4.0.jar" PayrollSystem
