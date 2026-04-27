package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {

    public static Connection getConnection() {

        Connection conn = null;

        try {

            String url = "jdbc:sqlserver://localhost:1433;databaseName=QLCAFE;encrypt=false";
            String user = "sa"; 
            String password = "123"; //mật khẩu tài khoản sa của bạn

            conn = DriverManager.getConnection(url, user, password);

            System.out.println("Kết nối thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}