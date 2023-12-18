package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final UserDaoJDBCImpl INSTANCE = new UserDaoJDBCImpl();
    private UserDaoJDBCImpl() {
    }

    public static UserDaoJDBCImpl getInstance(){
        try (Connection connection = Util.get();
             Statement statement = connection.createStatement()) {
            statement.execute("USE mydatabase");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return INSTANCE;
    }

    public void createUsersTable() {
        try (Connection connection = Util.get();
             Statement statement = connection.createStatement()) {
            System.out.println(connection);
            System.out.println(statement);
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
        try (Connection connection = Util.get();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS user;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT user(name, lastName, age) VALUES (?, ?, ?);";
        try (Connection connection = Util.get();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,lastName);
            preparedStatement.setByte(3,age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM user WHERE (id = ?);";
        try (Connection connection = Util.get();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        try (Connection connection = Util.get();
             Statement statement = connection.createStatement()) {
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
        try (Connection connection = Util.get();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM user;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
