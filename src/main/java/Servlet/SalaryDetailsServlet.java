package Servlet;

import DbConnection.Db_conn;
import Model.SalaryDetails;
import java.io.IOException;
import java.sql.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.google.gson.*;

@WebServlet("/salary_details")
public class SalaryDetailsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("application/json");
        Gson gson = new Gson();
        SalaryDetails salaryDetails = gson.fromJson(request.getReader(), SalaryDetails.class);
        JsonObject jsonobject = new JsonObject();

        String sql = "INSERT INTO salary_details (emp_id, name, salary_month, basic_salary, allowance, loan_deduction) VALUES(?,?,?,?,?,?)";

        try(Connection connection = Db_conn.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, salaryDetails.getEmp_id());
            preparedStatement.setString(2, salaryDetails.getName());
            preparedStatement.setInt(3, salaryDetails.getSalary_month());
            preparedStatement.setFloat(4, salaryDetails.getBasic_salary());
            preparedStatement.setFloat(5, salaryDetails.getAllowance());
            preparedStatement.setFloat(6, salaryDetails.getLoan_deduction());

            int rows = preparedStatement.executeUpdate();

            if (rows>0){
                jsonobject.addProperty("status","success");
                jsonobject.addProperty("message","salary details successfully added");
            } else {
                jsonobject.addProperty("status","failed");
                jsonobject.addProperty("message","Do not have any salary details");
            }
            response.getWriter().write(gson.toJson(jsonobject));

        } catch (Exception e) {
            jsonobject.addProperty("status", "error");
            jsonobject.addProperty("message", e.getMessage());
            response.getWriter().write(gson.toJson(jsonobject));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request ,HttpServletResponse response)throws IOException{
        response.setContentType("application/json");
        Gson gson = new Gson();
        SalaryDetails salaryDetails = new SalaryDetails();
        JsonObject jsonObject = new JsonObject();

        String sql = "SELECT * from salary_details ORDER BY emp_id DESC LIMIT 1";

        try(Connection connection = Db_conn.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            if (resultSet.next()){
                salaryDetails.setEmp_id(resultSet.getInt("emp_id"));
                salaryDetails.setName(resultSet.getString("name"));
                salaryDetails.setSalary_month(resultSet.getInt("salary_month"));
                salaryDetails.setBasic_salary(resultSet.getFloat("basic_salary"));
                salaryDetails.setAllowance(resultSet.getFloat("allowance"));
                salaryDetails.setLoan_deduction(resultSet.getFloat("loan_deduction"));

                response.getWriter().write(gson.toJson(salaryDetails));
            } else {
                jsonObject.addProperty("status", "faild");
                jsonObject.addProperty("message", "Do not have any salary details");
                response.getWriter().write(gson.toJson(jsonObject));
            }
        } catch (Exception e) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", e.getMessage());
            response.getWriter().write(gson.toJson(jsonObject));
        }

    }
}
