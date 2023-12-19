package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String CONNECTIONURL = "jdbc:mysql://localhost:3306/mydatabase";

    private static final String USERNAME = "Iv";
    private static final String PASSWORD = "i1234567";
    public static Connection getMyConnection(){
        try {
//            Statement statement = connection.createStatement());
//            statement.execute("USE mydatabase");
            return DriverManager.getConnection(CONNECTIONURL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
