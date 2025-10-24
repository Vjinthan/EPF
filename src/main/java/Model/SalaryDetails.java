package Model;

public class SalaryDetails {
    private  int emp_id;
    private String name;
    private int salary_month;
    private float basic_salary;
    private float allowance;
    private float loan_deduction;

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary_month() {
        return salary_month;
    }

    public void setSalary_month(int salary_month) {
        this.salary_month = salary_month;
    }

    public float getBasic_salary() {
        return basic_salary;
    }

    public void setBasic_salary(float basic_salary) {
        this.basic_salary = basic_salary;
    }

    public float getAllowance() {
        return allowance;
    }

    public void setAllowance(float allowance) {
        this.allowance = allowance;
    }

    public float getLoan_deduction() {
        return loan_deduction;
    }

    public void setLoan_deduction(float loan_deduction) {
        this.loan_deduction = loan_deduction;
    }
}
