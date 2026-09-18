Employee-Payroll-System
The Employee Payroll System is a Java Program designed to manage basic employee salary information. It takes employee details as input and calculates the salary based on the data entered. The system runs through the command line, so it can be used without any graphical interface.

Features
It Adds Full-Time Employees, Managers, and Interns
View all employees
Generate individual employees payslips
Update the  employee details
Delete the  employee records
Search the employees by department
Generates the  department-wise payroll summary
Calculate the  total company payroll
Sort employees by net salary
Store and load the  employee data from a file
Custom exception handling for duplicate and missing employees
Employee Types
Full-Time Employee
Calculates salary using:

HRA: 20% of basic salary
DA: 10% of basic salary
Tax: 10% of gross salary
PF: 1800
Manager
Extends the Full-Time Employee class and additionally includes:

The Bonus
Team Size
Intern
Uses a monthly stipend as the gross salary and has no deductions.

Technologies Used
Java
Object-Oriented Programming
Inheritance & Polymorphism
Collections
File Handling
Exception Handling
Data Storage
Employee records are stored in:

data/employees.txt

The application automatically loads existing records when started and saves changes after adding, updating, or deleting employees.

How to Run
Compile and run the Main.java file:

javac Main.java java Main

The application provides a menu-driven interface for managing payroll operations.

Project Structure
Employee-Payroll-System/ │ Main.java ── data │  employees.txt ── README.md
