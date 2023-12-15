package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String USERNAME = "Iv";
    private static final String PASSWORD = "i1234567";
    private static final String HOSTNAME = "localhost";
    private static String dbName = "mydatabase";
    private Util(){
    }

    public static Connection getConnection(){
        // Declare the class Driver for MySQL DB
        // This is necessary with Java 5 (or older)
        // Java6 (or newer) automatically find the appropriate driver.
        // If you use Java> 5, then this line is not needed.
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }

        String connectionURL = "jdbc:mysql://" + HOSTNAME + ":3306/";
//        String connectionURL = "jdbc:mysql://" + HOSTNAME + ":3306/" + dbName;
        try {
            return DriverManager.getConnection(connectionURL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
