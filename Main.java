import java.io.*;
import java.util.*;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final PayrollManager PAYROLL = new PayrollManager("data/employees.txt");

    public static void main(String[] args) {

        System.out.println("==========//////////////////==========");
        System.out.println("        EMPLOYEE PAYROLL SYSTEM       ");
        System.out.println("==========//////////////////==========");

        boolean running = true;

        while (running) {
            showMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addEmployee();
                    break;

                case 2:
                    viewEmployees();
                    break;

                case 3:
                    showPayslip();
                    break;

                case 4:
                    updateEmployee();
                    break;

                case 5:
                    deleteEmployee();
                    break;

                case 6:
                    searchDepartment();
                    break;

                case 7:
                    departmentSummary();
                    break;

                case 8:
                    sortBySalary();
                    break;

                case 9:
                    System.out.println("\nThank you for using the Employee Payroll System.");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        SCANNER.close();
    }

    static void showMenu() {
        System.out.println("\n---------- MENU ----------");
        System.out.println("1. Add Employee");
        System.out.println("2. View All Employees");
        System.out.println("3. Generate Payslip");
        System.out.println("4. Update Employee");
        System.out.println("5. Delete Employee");
        System.out.println("6. Search by Department");
        System.out.println("7. Department Payroll Summary");
        System.out.println("8. Sort Employees by Net Salary");
        System.out.println("9. Exit");
        System.out.println("----------------------------");
    }

    static void addEmployee() {

        System.out.println("\n--- Add Employee ---");
        System.out.println("1. Full-Time Employee");
        System.out.println("2. Manager");
        System.out.println("3. Intern");

        int type = readInt("Enter employee type: ");

        if (type < 1 || type > 3) {
            System.out.println("Invalid employee type.");
            return;
        }

        int id = readPositiveInt("Enter Employee ID: ");
        String name = readText("Enter Name: ");
        String department = readText("Enter Department: ");

        try {
            Employee employee;

            if (type == 1) {
                double salary = readPositiveDouble("Enter Basic Salary: ");
                employee = new FullTimeEmployee(id, name, department, salary);
            }
            else if (type == 2) {
                double salary = readPositiveDouble("Enter Basic Salary: ");
                double bonus = readPositiveDouble("Enter Bonus: ");
                int teamSize = readNonNegativeInt("Enter Team Size: ");

                employee = new Manager(
                        id, name, department, salary, bonus, teamSize
                );
            }
            else {
                double stipend = readPositiveDouble("Enter Monthly Stipend: ");
                employee = new Intern(id, name, department, stipend);
            }

            PAYROLL.addEmployee(employee);
            System.out.println("Employee added successfully.");

        } catch (DuplicateEmployeeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewEmployees() {

        List<Employee> employees = PAYROLL.getAllEmployees();

        System.out.println("\n--- Employee List ---");

        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        printHeader();

        for (Employee employee : employees) {
            System.out.println(employee.toRow());
        }
    }

    static void showPayslip() {

        System.out.println("\n--- Generate Payslip ---");

        int id = readPositiveInt("Enter Employee ID: ");

        try {
            Employee employee = PAYROLL.findById(id);
            System.out.println(employee.generatePayslip());
        }
        catch (EmployeeNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void updateEmployee() {

        System.out.println("\n--- Update Employee ---");

        int id = readPositiveInt("Enter Employee ID: ");

        try {
            Employee employee = PAYROLL.findById(id);

            System.out.println("Current Name: " + employee.getName());
            System.out.println("Current Department: " + employee.getDepartment());
            System.out.printf("Current Salary: %.2f%n",
                    employee.getBasicSalary());

            System.out.println("\nPress Enter to keep the old value.");

            System.out.print("New Name: ");
            String name = SCANNER.nextLine().trim();

            System.out.print("New Department: ");
            String department = SCANNER.nextLine().trim();

            System.out.print("New Salary/Stipend: ");
            String salaryInput = SCANNER.nextLine().trim();

            double salary = -1;

            if (!salaryInput.isEmpty()) {
                try {
                    salary = Double.parseDouble(salaryInput);

                    if (salary < 0) {
                        System.out.println("Salary cannot be negative.");
                        return;
                    }
                }
                catch (NumberFormatException e) {
                    System.out.println("Invalid salary.");
                    return;
                }
            }

            PAYROLL.updateEmployee(id, name, department, salary);

            System.out.println("Employee updated successfully.");

        }
        catch (EmployeeNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void deleteEmployee() {

        System.out.println("\n--- Delete Employee ---");

        int id = readPositiveInt("Enter Employee ID: ");

        try {
            PAYROLL.deleteEmployee(id);
            System.out.println("Employee deleted successfully.");
        }
        catch (EmployeeNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void searchDepartment() {

        System.out.println("\n--- Search Department ---");

        String department = readText("Enter Department: ");

        List<Employee> result =
                PAYROLL.searchByDepartment(department);

        if (result.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        printHeader();

        for (Employee employee : result) {
            System.out.println(employee.toRow());
        }
    }

    static void departmentSummary() {

        System.out.println("\n--- Department Payroll Summary ---");

        Map<String, Double> summary =
                PAYROLL.getDepartmentWisePayroll();

        if (summary.isEmpty()) {
            System.out.println("No employee records found.");
            return;
        }

        System.out.printf("%-20s %-15s%n",
                "Department", "Net Payroll");

        System.out.println("------------------------------");

        for (Map.Entry<String, Double> entry : summary.entrySet()) {
            System.out.printf("%-20s %.2f%n",
                    entry.getKey(), entry.getValue());
        }

        System.out.printf(
                "\nTotal Company Payroll: %.2f%n",
                PAYROLL.getTotalPayroll()
        );
    }

    static void sortBySalary() {

        System.out.println("\n--- Employees by Net Salary ---");

        List<Employee> employees =
                PAYROLL.getEmployeesSortedBySalaryDesc();

        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        printHeader();

        for (Employee employee : employees) {
            System.out.println(employee.toRow());
        }
    }

    static void printHeader() {

        System.out.printf(
                "%-5s %-18s %-15s %-12s %-12s %-12s%n",
                "ID",
                "Name",
                "Department",
                "Type",
                "Basic",
                "Net Salary"
        );

        System.out.println(
                "---------------------------------------"
        );
    }

    static int readInt(String message) {

        while (true) {

            System.out.print(message);
            String input = SCANNER.nextLine().trim();

            try {
                return Integer.parseInt(input);
            }
            catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    static int readPositiveInt(String message) {

        while (true) {

            int value = readInt(message);

            if (value > 0) {
                return value;
            }

            System.out.println("Value must be greater than 0.");
        }
    }

    static int readNonNegativeInt(String message) {

        while (true) {

            int value = readInt(message);

            if (value >= 0) {
                return value;
            }

            System.out.println("Value cannot be negative.");
        }
    }

    static double readPositiveDouble(String message) {

        while (true) {

            System.out.print(message);
            String input = SCANNER.nextLine().trim();

            try {
                double value = Double.parseDouble(input);

                if (value > 0) {
                    return value;
                }

                System.out.println("Value must be greater than 0.");
            }
            catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    static String readText(String message) {

        while (true) {

            System.out.print(message);
            String value = SCANNER.nextLine().trim();

            if (!value.isEmpty()) {
                return value.replace(",", " ");
            }

            System.out.println("This field cannot be empty.");
        }
    }
}

abstract class Employee implements Comparable<Employee> {

    protected int id;
    protected String name;
    protected String department;
    protected double basicSalary;

    public Employee(int id, String name,
                    String department, double basicSalary) {

        this.id = id;
        this.name = name;
        this.department = department;
        this.basicSalary = basicSalary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setBasicSalary(double salary) {
        this.basicSalary = salary;
    }

    public abstract double calculateGrossSalary();

    public abstract double calculateDeductions();

    public abstract String getType();

    public double calculateNetSalary() {
        return calculateGrossSalary() - calculateDeductions();
    }

    public String toRow() {

        return String.format(
                "%-5d %-18s %-15s %-12s %-12.2f %-12.2f",
                id,
                name,
                department,
                getType(),
                basicSalary,
                calculateNetSalary()
        );
    }

    public String generatePayslip() {

        StringBuilder payslip = new StringBuilder();

        payslip.append("\n==============================\n");
        payslip.append("              PAYSLIP\n");
        payslip.append("================================\n");

        payslip.append(String.format("Employee ID : %d%n", id));
        payslip.append(String.format("Name        : %s%n", name));
        payslip.append(String.format("Department  : %s%n", department));
        payslip.append(String.format("Type        : %s%n", getType()));

        payslip.append("--------------------------\n");

        payslip.append(String.format(
                "Basic Salary: %.2f%n", basicSalary));

        payslip.append(String.format(
                "Gross Salary: %.2f%n",
                calculateGrossSalary()));

        payslip.append(String.format(
                "Deductions  : %.2f%n",
                calculateDeductions()));

        payslip.append("-----------------------------\n");

        payslip.append(String.format(
                "NET SALARY  : %.2f%n",
                calculateNetSalary()));

        payslip.append("=======================");

        return payslip.toString();
    }

    public abstract String toCSV();

    @Override
    public int compareTo(Employee other) {
        return Integer.compare(this.id, other.id);
    }
}

class FullTimeEmployee extends Employee {

    protected static final double HRA = 20.0;
    protected static final double DA = 10.0;
    protected static final double TAX = 10.0;
    protected static final double PF = 1800.0;

    public FullTimeEmployee(int id, String name,
                            String department, double salary) {

        super(id, name, department, salary);
    }

    @Override
    public double calculateGrossSalary() {

        double hra = basicSalary * HRA / 100;
        double da = basicSalary * DA / 100;

        return basicSalary + hra + da;
    }

    @Override
    public double calculateDeductions() {

        double tax = calculateGrossSalary() * TAX / 100;

        return tax + PF;
    }

    @Override
    public String getType() {
        return "FULLTIME";
    }

    @Override
    public String toCSV() {

        return "FULLTIME," +
                id + "," +
                name + "," +
                department + "," +
                basicSalary + ",0,0";
    }
}

class Manager extends FullTimeEmployee {

    private double bonus;
    private int teamSize;

    public Manager(int id, String name,
                   String department, double salary,
                   double bonus, int teamSize) {

        super(id, name, department, salary);

        this.bonus = bonus;
        this.teamSize = teamSize;
    }

    public double getBonus() {
        return bonus;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    @Override
    public double calculateGrossSalary() {

        return super.calculateGrossSalary() + bonus;
    }

    @Override
    public String getType() {
        return "MANAGER";
    }

    @Override
    public String generatePayslip() {

        StringBuilder payslip = new StringBuilder();

        payslip.append("\n===========**============\n");
        payslip.append("             PAYSLIP\n");
        payslip.append("=============**=============\n");

        payslip.append(String.format("Employee ID : %d%n", id));
        payslip.append(String.format("Name        : %s%n", name));
        payslip.append(String.format("Department  : %s%n", department));
        payslip.append(String.format("Type        : %s%n", getType()));
        payslip.append(String.format("Team Size   : %d%n", teamSize));

        payslip.append("------------------\n");

        payslip.append(String.format(
                "Basic Salary: %.2f%n", basicSalary));

        payslip.append(String.format(
                "Bonus       : %.2f%n", bonus));

        payslip.append(String.format(
                "Gross Salary: %.2f%n",
                calculateGrossSalary()));

        payslip.append(String.format(
                "Deductions  : %.2f%n",
                calculateDeductions()));

        payslip.append("------------------\n");

        payslip.append(String.format(
                "NET SALARY  : %.2f%n",
                calculateNetSalary()));

        payslip.append("=====================");

        return payslip.toString();
    }

    @Override
    public String toCSV() {

        return "MANAGER," +
                id + "," +
                name + "," +
                department + "," +
                basicSalary + "," +
                bonus + "," +
                teamSize;
    }
}

class Intern extends Employee {

    public Intern(int id, String name,
                  String department, double stipend) {

        super(id, name, department, stipend);
    }

    @Override
    public double calculateGrossSalary() {
        return basicSalary;
    }

    @Override
    public double calculateDeductions() {
        return 0;
    }

    @Override
    public String getType() {
        return "INTERN";
    }

    @Override
    public String toCSV() {

        return "INTERN," +
                id + "," +
                name + "," +
                department + "," +
                basicSalary + ",0,0";
    }
}

class DuplicateEmployeeException extends Exception {

    public DuplicateEmployeeException(String message) {
        super(message);
    }
}

class EmployeeNotFoundException extends Exception {

    public EmployeeNotFoundException(String message) {
        super(message);
    }
}

class PayrollManager {

    private List<Employee> employees;
    private final String filePath;

    public PayrollManager(String filePath) {

        this.filePath = filePath;
        employees = new ArrayList<>();

        loadFromFile();
    }

    public void addEmployee(Employee employee)
            throws DuplicateEmployeeException {

        for (Employee e : employees) {

            if (e.getId() == employee.getId()) {
                throw new DuplicateEmployeeException(
                        "Employee ID already exists."
                );
            }
        }

        employees.add(employee);
        saveToFile();
    }

    public List<Employee> getAllEmployees() {

        List<Employee> list = new ArrayList<>(employees);

        Collections.sort(list);

        return list;
    }

    public Employee findById(int id)
            throws EmployeeNotFoundException {

        for (Employee employee : employees) {

            if (employee.getId() == id) {
                return employee;
            }
        }

        throw new EmployeeNotFoundException(
                "No employee found with ID " + id
        );
    }

    public List<Employee> searchByDepartment(String department) {

        List<Employee> result = new ArrayList<>();

        for (Employee employee : employees) {

            if (employee.getDepartment()
                    .equalsIgnoreCase(department)) {

                result.add(employee);
            }
        }

        return result;
    }

    public void updateEmployee(
            int id,
            String name,
            String department,
            double salary)
            throws EmployeeNotFoundException {

        Employee employee = findById(id);

        if (!name.isEmpty()) {
            employee.setName(name);
        }

        if (!department.isEmpty()) {
            employee.setDepartment(department);
        }

        if (salary >= 0) {
            employee.setBasicSalary(salary);
        }

        saveToFile();
    }

    public void deleteEmployee(int id)
            throws EmployeeNotFoundException {

        Employee employee = findById(id);

        employees.remove(employee);

        saveToFile();
    }

    public Map<String, Double> getDepartmentWisePayroll() {

        Map<String, Double> result = new TreeMap<>();

        for (Employee employee : employees) {

            String department = employee.getDepartment();

            result.put(
                    department,
                    result.getOrDefault(department, 0.0)
                            + employee.calculateNetSalary()
            );
        }

        return result;
    }

    public double getTotalPayroll() {

        double total = 0;

        for (Employee employee : employees) {
            total += employee.calculateNetSalary();
        }

        return total;
    }

    public List<Employee> getEmployeesSortedBySalaryDesc() {

        List<Employee> list = new ArrayList<>(employees);

        list.sort((first, second) ->
                Double.compare(second.calculateNetSalary(),
                               first.calculateNetSalary()));

        return list;
    }

    private void loadFromFile() {

        File file = new File(filePath);

        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Employee employee = parseEmployee(line);
                    employees.add(employee);
                }
                catch (Exception e) {
                    System.out.println(
                            "Skipped invalid employee record."
                    );
                }
            }

        }
        catch (IOException e) {

            System.out.println(
                    "Unable to read employee data."
            );
        }
    }

    private Employee parseEmployee(String line) {

        String[] data = line.split(",", -1);

        if (data.length < 7) {
            throw new IllegalArgumentException();
        }

        String type = data[0].trim();

        int id = Integer.parseInt(data[1].trim());

        String name = data[2].trim();

        String department = data[3].trim();

        double salary =
                Double.parseDouble(data[4].trim());

        double extra =
                Double.parseDouble(data[5].trim());

        int team =
                (int) Double.parseDouble(data[6].trim());

        if (type.equals("MANAGER")) {

            return new Manager(
                    id,
                    name,
                    department,
                    salary,
                    extra,
                    team
            );
        }

        if (type.equals("INTERN")) {

            return new Intern(
                    id,
                    name,
                    department,
                    salary
            );
        }

        return new FullTimeEmployee(
                id,
                name,
                department,
                salary
        );
    }

    private void saveToFile() {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(filePath))) {

            for (Employee employee : employees) {

                writer.write(employee.toCSV());
                writer.newLine();
            }

        }
        catch (IOException e) {

            System.out.println(
                    "Unable to save employee data."
            );
        }
    }
}