public class Employee {
    private String name;
    private String hireDate;
    private double salary;
    private Department department;

    public Employee(String name, String hireDate, double salary) {
        this.name = name;
        this.hireDate = hireDate;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }
    public String getHireDate() {
        return hireDate;
    }
    public double getSalary() {
        return salary;
    }
    public Department getDepartment() {
        return department;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }
    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }
    public void setDepartment(Department department) {
        this.department = department;
    }

}
