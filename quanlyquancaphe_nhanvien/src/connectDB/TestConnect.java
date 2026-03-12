package connectDB;

import java.sql.Connection;

public class TestConnect {

    public static void main(String[] args) {

        Connection conn = ConnectDB.getConnection();

        if(conn != null){
            System.out.println("Connected!");
        }
    }
}