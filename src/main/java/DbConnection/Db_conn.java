package DbConnection;

import java.sql.*;

public class Db_conn  {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/Demo";
    private static final String USER = "root";
    private static final String PASS = "Viji6941@";

    public static Connection getConnection() throws SQLException{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL,USER,PASS);
        } catch (ClassNotFoundException e){
            throw new SQLException(e);
            System.out.println("hii");
        }
    }
}
