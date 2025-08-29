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

---

## How to Compile and Run
1. Open terminal/PowerShell in the `src/` folder.
2. Compile all Java files:
   ```powershell
   javac -cp ".;..\lib\mysql-connector-j-9.4.0.jar" *.java
   java -cp ".;..\lib\mysql-connector-j-9.4.0.jar" PayrollSystem


