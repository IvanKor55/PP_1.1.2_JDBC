package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static Statement statement;
    private static Connection connection;

    static  {
        connection = Util.getConnection();
        try {
            statement = connection.createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS mydatabase");
            statement.execute("USE mydatabase");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public void createUsersTable() {
        try {
            statement.execute("""
                              CREATE TABLE IF NOT EXISTS user(
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(50),
                                     lastName VARCHAR(50),
                                     age TINYINT);
                              """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try {
            statement.execute("DROP TABLE IF EXISTS user;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            String sql = String.format("INSERT user(name, lastName, age) VALUES ('%1$s', '%2$s', %3$s);"
                    ,name,lastName,age);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try {
            statement.executeUpdate("DELETE FROM user WHERE (id = " + id + ");");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT name, lastName, age FROM User;");
            List<User> user = new ArrayList<>();
            while (resultSet.next()){
                user.add(new User(resultSet.getString("name"),
                                resultSet.getString("lastName"),
                                resultSet.getByte("age")));
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanUsersTable() {
        try {
            statement.executeUpdate("DELETE FROM user;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
