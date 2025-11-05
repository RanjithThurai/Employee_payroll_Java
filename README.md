# 👨‍💻 Employee Payroll and Attendance System (CLI)

This is a comprehensive Command Line Interface (CLI) application developed in Java for managing employee records, tracking attendance, calculating dynamic payroll, and generating detailed reports. The system is backed by a **MySQL database** for persistent storage.

---

## ✨ Key Features

### 1. Employee Management 👥

Manage core employee data and track salary changes.

* **CRUD Operations:** Easily **Add, View, Update, and Delete** employee records (ID, Name, Position, Basic Salary).
* **Salary History Logging:** The system automatically **logs and tracks** changes to an employee's basic salary in a `salary_history` table whenever an employee record is updated.
* **View Salary History:** Users can view a complete chronological record of salary changes for any employee.
* **View All Employees:** List all current employees with their primary details.

### 2. Attendance Tracking 📅

Track employee presence, record overtime hours, and generate attendance reports.

* **Daily Attendance:** **Mark daily attendance** for all employees, allowing the user to quickly toggle who is present for a given date.
* **Overtime Recording:** Record specific **overtime hours** for any employee on a given day.
* **Attendance Report Generation:** Generate a comprehensive **Attendance Report** between specified start and end dates, detailing Present Days, Absent Days, and Total Overtime Hours for each employee.

### 3. Dynamic Payroll Calculation & Payslips 💸

Automate payroll generation with rates sourced dynamically from the database.

* **Dynamic Rate Configuration (NEW):** Tax Rate, PF Rate, and Overtime Multiplier are **stored in the database** and can be updated via the configuration menu.
* **Gross Salary Calculation:** Calculates Gross Salary based on **Basic Salary** plus **Overtime Pay** (using the dynamic Overtime Multiplier).
* **Statutory Deductions:** Calculates and applies deductions using dynamic rates:
    * **Tax Deduction:** Calculated based on the configurable rate of Gross Salary.
    * **PF Deduction (Provident Fund):** Calculated based on the configurable rate of Basic Salary.
* **Net Salary Calculation:** Determines the final net salary after all dynamic deductions.
* **Payslip Generation:** Generates a detailed monthly payslip for a specific employee, showing the full breakdown of earnings, deductions, and Net Salary.
* **Payslip Storage:** Generated payslips are saved as **text files** (`.txt`) in the root directory.

### 4. Reporting & Configuration Module 📊⚙️

Generate official reports and manage global payroll settings.

* **Change Payroll Rates (NEW):** Allows the administrator to **view and update** the core payroll rates (Tax, PF, Overtime Multiplier) which are then used system-wide.
* **Monthly Payroll Register:** Generates a detailed report of all employee payroll calculations for a specified month and year.
* **CSV Format:** The Payroll Register is outputted as a **CSV (Comma Separated Values) file**.
* **Report File Storage:** All generated CSV files are automatically saved within a dedicated **`payroll_reports` folder** for organization. The report includes a footer with the total net payroll amount.

---

## 🛠️ Technology Stack

| Component | Technology | Description |
| :--- | :--- | :--- |
| **Language** | Java | Core application logic. |
| **Database** | MySQL | Data persistence for employees, attendance, salary history, and global configuration rates. |
| **Connectivity** | JDBC (MySQL Connector) | Used for database interaction and transaction management. |

# 🧾 Employee Payroll System (Java)

![Java](https://img.shields.io/badge/Language-Java-red)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue)
![CLI](https://img.shields.io/badge/Application-Offline%20CLI-success)

A **Java-based Employee Payroll System** designed to manage employee records, calculate salaries, track attendance, and generate payslips efficiently.  
It is an **offline Command-Line (CLI) application** connected to a **MySQL** database for persistent storage.

---

## 🚀 Features
- ➕ Add, update, and manage employee details  
- 💰 Automatically calculate monthly payroll  
- 🧾 Generate and display payslips  
- 🗄️ Connects to a **MySQL database** for data storage  
- 🔒 Ensures data integrity with foreign key relationships  

---

## 🧩 Prerequisites
Before running this project, ensure you have:
- ☕ **Java JDK 8** or higher  
- 🐬 **MySQL Server** installed and running  
- 📦 **MySQL Connector/J** (`mysql-connector-j-9.4.0.jar`) — included in the `lib/` folder  

---

## 🏗️ Database Setup

### Step 1: Create Database
Open MySQL and create a database named **`payroll_db`**:

```sql

CREATE DATABASE payroll_db;
USE payroll_db;
```
```
Step 2: Create Tables

Run the following SQL commands to create the necessary tables:

CREATE TABLE employees (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    position VARCHAR(100),
    basic_salary DECIMAL(10,2)
);

CREATE TABLE attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id VARCHAR(20),
    date DATE NOT NULL,
    is_present BOOLEAN DEFAULT FALSE,
    overtime_hours DECIMAL(5,2) DEFAULT 0.00,
    FOREIGN KEY (emp_id) REFERENCES employees(id),
    UNIQUE KEY unique_attendance (emp_id, date)
);
```
### ⚙️ Database Configuration
Open the file **`DBConnection.java`** and update your MySQL credentials:

```java
String url = "jdbc:mysql://localhost:3306/payroll_db";
String username = "root";      // your MySQL username
String password = "password";  // your MySQL password
```
▶️ How to Compile and Run
```
Step 1: Open Terminal

Navigate to the src/ folder inside your project directory.

Step 2: Compile Java Files

javac -cp ".;..\lib\mysql-connector-j-9.4.0.jar" *.java

Step 3: Run the Application

java -cp ".;..\lib\mysql-connector-j-9.4.0.jar" PayrollSystem

```
