# Employee-Payroll-System

A console-based **Employee Payroll System developed in Java** to manage employee records, calculate salaries, generate payslips, and maintain payroll data using file handling.

## Features

* Add Full-Time Employees, Managers, and Interns
* View all employees
* Generate individual payslips
* Update employee details
* Delete employee records
* Search employees by department
* Generate department-wise payroll summary
* Calculate total company payroll
* Sort employees by net salary
* Store and load employee data from a file
* Custom exception handling for duplicate and missing employees

## Employee Types

### Full-Time Employee

Calculates salary using:

* HRA: 20% of basic salary
* DA: 10% of basic salary
* Tax: 10% of gross salary
* PF: 1800

### Manager

Extends the Full-Time Employee class and additionally includes:

* Bonus
* Team Size

### Intern

Uses a monthly stipend as the gross salary and has no deductions.

## Technologies Used

* Java
* Object-Oriented Programming
* Inheritance & Polymorphism
* Collections
* File Handling
* Exception Handling

## Data Storage

Employee records are stored in:

```text
data/employees.txt
```

The application automatically loads existing records when started and saves changes after adding, updating, or deleting employees.

## How to Run

Compile and run the `Main.java` file:

```bash
javac Main.java
java Main
```

The application provides a menu-driven interface for managing payroll operations.

## Project Structure

```text
Employee-Payroll-System/
│
├── Main.java
├── data/
│   └── employees.txt
└── README.md
```

## Author

**Naksh Khandelwal**
