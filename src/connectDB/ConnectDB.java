package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectDB {
    public static void main(String[] args) {
    	try {
    		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    		String url = "jdbc:sqlserver://LAPTOP-C6921LGV:1433;" +
    	             "databaseName=QuanLyQuanCafe;" +
    	             "encrypt=true;" +
    	             "trustServerCertificate=true";
    		String userName = "sa";
    		String password = "123456789";
    		
    		Connection connection = DriverManager.getConnection(url,userName ,password);
    		String sql = "select * from Ban";
    		Statement statement = connection.createStatement();
    		ResultSet rs = statement.executeQuery(sql);
    		System.out.print("ket noi thanh cong");
    		while(rs.next()) {
    			System.out.print(rs.getInt(1));
    			System.out.print(rs.getInt(3));
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
