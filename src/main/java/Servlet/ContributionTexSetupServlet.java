package Servlet;
import DbConnection.Db_conn;
import Model.ContributionTexSetup;

import java.io.IOException;
import java.sql.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import com.google.gson.*;

@WebServlet("/contribute_tax")
public class ContributionTexSetupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        ContributionTexSetup contribution = gson.fromJson(request.getReader(), ContributionTexSetup.class);


        String sql = "INSERT INTO contribute_taxsetup (employee_epf, employer_epf, employer_etf, upperincome_first, upperincome_second, first_slab_tax, second_slab_tax, third_slab_tax ) VALUES(?,?,?,?,?,?,?,?)";

        try(Connection connection = Db_conn.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setFloat(1, contribution.getEmployee_epf());
            preparedStatement.setFloat(2,contribution.getEmployer_epf());
            preparedStatement.setFloat(3,contribution.getEmployer_etf());
            preparedStatement.setFloat(4, contribution.getUpperincome_first());
            preparedStatement.setFloat(5, contribution.getUpperincome_second());
            preparedStatement.setFloat(6, contribution.getFirst_slab_tax());
            preparedStatement.setFloat(7, contribution.getSecond_slab_tax());
            preparedStatement.setFloat(8, contribution.getThird_slab_tax());

            int rows = preparedStatement.executeUpdate();
            if (rows > 0){
                response.getWriter().write("{\"status\":\"success\",\"message\":\"Details record inserted\"}");
            } else {
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Insert failed\"}");
            }

        } catch (SQLException e) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", e.getMessage());
            response.getWriter().write(gson.toJson(jsonObject));
        } catch (Exception e) {
            jsonObject.addProperty("status" , "error");
            jsonObject.addProperty("message", e.getMessage());
            response.getWriter().write(gson.toJson(jsonObject));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        Gson gson = new Gson();
        JsonObject jsonobject = new JsonObject();
        ContributionTexSetup contribution = new ContributionTexSetup();

        String sql = "SELECT * FROM contribute_taxsetup ORDER BY id DESC LIMIT 1";

        try(Connection connection = Db_conn.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql); // prevent to sql injection
            ResultSet resultSet = preparedStatement.executeQuery();
            ){

            if(resultSet.next()){
                contribution.setId(resultSet.getInt("id"));
                contribution.setEmployee_epf(resultSet.getFloat("employee_epf"));
                contribution.setEmployer_epf(resultSet.getFloat("employer_epf"));
                contribution.setEmployer_etf(resultSet.getFloat("employer_etf"));
                contribution.setUpperincome_first(resultSet.getFloat("upperincome_first"));
                contribution.setUpperincome_second(resultSet.getFloat("upperincome_second"));
                contribution.setFirst_slab_tax(resultSet.getFloat("first_slab_tax"));
                contribution.setSecond_slab_tax(resultSet.getFloat("second_slab_tax"));
                contribution.setThird_slab_tax(resultSet.getFloat("third_slab_tax"));

            } else {
                jsonobject.addProperty("Status","error");
                jsonobject.addProperty("message","No contribution data found");
                response.getWriter().write(gson.toJson(jsonobject));
            }
            response.getWriter().write(gson.toJson(contribution));

        } catch(Exception e){
            jsonobject.addProperty("Status","Error");
            jsonobject.addProperty("message",e.getMessage());
            response.getWriter().write(gson.toJson(jsonobject));
        }
    }
}
