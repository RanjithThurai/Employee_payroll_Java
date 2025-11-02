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

▶️ How to Compile and Run
```
Step 1: Open Terminal
Navigate to the src/ folder inside your project directory.
Step 2: Compile Java Files
javac -cp ".;..\lib\mysql-connector-j-9.4.0.jar" *.java
Step 3: Run the Application
java -cp ".;..\lib\mysql-connector-j-9.4.0.jar" PayrollSystem
```
