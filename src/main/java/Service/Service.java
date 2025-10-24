package Service;
import DbConnection.Db_conn;
import Model.SalaryDetails;
import Model.ContributionTexSetup;

import java.io.IOException;
import java.sql.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import com.google.gson.*;

@WebServlet("/service")
public class Service extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
       response.setContentType("application/json");
       Gson gson = new Gson();
       JsonObject jsonObject = new JsonObject();
       ContributionTexSetup contributionTexSetup = new ContributionTexSetup();
       SalaryDetails salaryDetails = new SalaryDetails();

       //Contribution
       float employee_epf =-1;
       float employer_epf = -1;
       float employer_etf =-1;

       //salaryDetails
        int emp_id = -1;
        String name = "";
        int salary_month = -1;
        float basic_salary = -1;
        float allowance = -1;
        float loan_deduction = -1;

        //taxSetup
        float upperincome_first =-1;
        float upperincome_second =-1;
        float first_slab_tax =-1;
        float second_slab_tax =-1;
        float third_slab_tax =-1;

        //find
        float grossSalary ;
        float payTax = -1;
        float amountOf_employee_epf;
        float amountOf_employer_epf ;
        float amountOf_employer_etf ;
        float totalContribution ;
        float netSalary  ;

       String sql = "SELECT * FROM contribute_taxsetup ORDER BY id DESC LIMIT 1";

       try(Connection connection = Db_conn.getConnection()) {
           try(PreparedStatement preparedStatement = connection.prepareStatement(sql);
               ResultSet resultSet = preparedStatement.executeQuery()
               ){
               if (resultSet.next()){
                   contributionTexSetup.setEmployee_epf(resultSet.getFloat("employee_epf"));
                   contributionTexSetup.setEmployer_epf(resultSet.getFloat("employer_epf"));
                   contributionTexSetup.setEmployer_etf(resultSet.getFloat("employer_etf"));
                   contributionTexSetup.setUpperincome_first(resultSet.getFloat("upperincome_first"));
                   contributionTexSetup.setUpperincome_second(resultSet.getFloat("upperincome_second"));
                   contributionTexSetup.setFirst_slab_tax(resultSet.getFloat("first_slab_tax"));
                   contributionTexSetup.setSecond_slab_tax(resultSet.getFloat("second_slab_tax"));
                   contributionTexSetup.setThird_slab_tax(resultSet.getFloat("third_slab_tax"));


                   employee_epf = contributionTexSetup.getEmployee_epf();
                   employer_epf = contributionTexSetup.getEmployer_epf();
                   employer_etf = contributionTexSetup.getEmployer_etf();
                   upperincome_first = contributionTexSetup.getUpperincome_first();
                   upperincome_second = contributionTexSetup.getUpperincome_second();
                   first_slab_tax = contributionTexSetup.getFirst_slab_tax();
                   second_slab_tax = contributionTexSetup.getSecond_slab_tax();
                   third_slab_tax = contributionTexSetup.getThird_slab_tax();
               }

           } catch (Exception e) {
               jsonObject.addProperty("status", "error");
               jsonObject.addProperty("message", e.getMessage());
               response.getWriter().write(gson.toJson(jsonObject));
           }

           sql = "SELECT * FROM salary_details ORDER BY emp_id DESC LIMIT 1";
           try(PreparedStatement preparedStatement = connection.prepareStatement(sql);
               ResultSet resultSet = preparedStatement.executeQuery()
           ){
               if (resultSet.next()){
                   salaryDetails.setEmp_id(resultSet.getInt("emp_id"));
                   salaryDetails.setName(resultSet.getString("name"));
                   salaryDetails.setSalary_month(resultSet.getInt("salary_month"));
                   salaryDetails.setBasic_salary(resultSet.getFloat("basic_salary"));
                   salaryDetails.setAllowance(resultSet.getFloat("allowance"));
                   salaryDetails.setLoan_deduction(resultSet.getFloat("loan_deduction"));


                   emp_id = salaryDetails.getEmp_id();
                   name = salaryDetails.getName();
                   salary_month = salaryDetails.getSalary_month();
                   basic_salary = salaryDetails.getBasic_salary();
                   allowance = salaryDetails.getAllowance();
                   loan_deduction = salaryDetails.getLoan_deduction();
               }

           } catch (Exception e) {
               jsonObject.addProperty("status", "error");
               jsonObject.addProperty("message", e.getMessage());
               response.getWriter().write(gson.toJson(jsonObject));
           }

           //calculation gross salary
           grossSalary = (basic_salary + allowance);

           //calculation payTax
           if (grossSalary <= upperincome_first){
               payTax = (first_slab_tax * grossSalary)/100;
           } else if (grossSalary <= upperincome_second) {
               payTax = (second_slab_tax * grossSalary)/100;
           } else if (grossSalary > upperincome_second) {
               payTax = (third_slab_tax * grossSalary)/100;
           }

           //calculation EPF, ETF and total contribution
           amountOf_employee_epf = (basic_salary * employee_epf)/100;
           amountOf_employer_epf = (basic_salary * employer_epf)/100;
           amountOf_employer_etf = (basic_salary * employer_etf)/100;
           totalContribution = amountOf_employee_epf + amountOf_employer_epf + amountOf_employer_etf;

           //calculation of netSalary
           netSalary = (grossSalary - payTax - loan_deduction - amountOf_employee_epf);

           jsonObject.addProperty("Emp_id",emp_id);
           jsonObject.addProperty("name", name);
           jsonObject.addProperty("month", salary_month);
           jsonObject.addProperty("basicSalary", basic_salary);
           jsonObject.addProperty("loanDeduction",loan_deduction);
           jsonObject.addProperty("allowance", allowance);
           jsonObject.addProperty("grossSalary", grossSalary);
           jsonObject.addProperty("payTax",payTax);
           jsonObject.addProperty("employee_epf",amountOf_employee_epf);
           jsonObject.addProperty("employer_epf",amountOf_employer_epf);
           jsonObject.addProperty("employer_etf",amountOf_employer_etf);
           jsonObject.addProperty("totalContribution",totalContribution);
           jsonObject.addProperty("netSalary",netSalary);

           response.getWriter().write(gson.toJson(jsonObject));

       } catch (Exception e) {
           jsonObject.addProperty("status", "error");
           jsonObject.addProperty("message", e.getMessage());
           response.getWriter().write(gson.toJson(jsonObject));

       }

    }
}
