package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public static Connection connection = Util.getMyConnection();
    private static final String CREATEUSERSTABLE = """
                              CREATE TABLE IF NOT EXISTS user(
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(50),
                                     lastName VARCHAR(50),
                                     age TINYINT);
                              """;
    private static final String DROPUSERSTABLE = "DROP TABLE IF EXISTS user;";
    private static final String SAVEUSER = "INSERT user(name, lastName, age) VALUES (?, ?, ?);";
    private static final String REMOVEUSERBYID = "DELETE FROM user WHERE (id = ?);";
    private static final String GETALLUSERS = "SELECT name, lastName, age FROM User;";
    private static final String CLEANUSERSTABLE = "DELETE FROM user;";

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATEUSERSTABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(DROPUSERSTABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVEUSER)) {
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,lastName);
            preparedStatement.setByte(3,age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(REMOVEUSERBYID)) {
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GETALLUSERS);
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
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CLEANUSERSTABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
