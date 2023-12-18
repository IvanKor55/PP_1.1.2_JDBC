package jm.task.core.jdbc.util;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String USERNAME = "Iv";
    private static final String PASSWORD = "i1234567";
    private static final String HOSTNAME = "localhost";
    private static final String DBNAME = "mydatabase";
    private static final String POOL_SIZE_KEY = "5";
    private static final Integer DEFAUL_PULL_SIZE = 10;
    private static BlockingQueue<Connection> pool;
    private static List<Connection> sourceConnections;
    private Util(){
    }

    static {
        initConnectionPool();
    }

    public static void initConnectionPool(){
        int size = POOL_SIZE_KEY == null ? DEFAUL_PULL_SIZE : Integer.parseInt(POOL_SIZE_KEY);
        pool = new ArrayBlockingQueue<>(size);
        sourceConnections = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Connection connection = open();
            Connection proxyConnection = (Connection) Proxy.newProxyInstance(Util.class.getClassLoader(),
                    new Class[]{Connection.class},
                    (proxy, method, args) -> method.getName().equals("close") ?
                            pool.add((Connection) proxy) : method.invoke(connection, args));
            pool.add(proxyConnection);
            sourceConnections.add(connection);
        }
    }
    public static void closePool (){
        try {
            for (Connection sourceConnections : sourceConnections) {
                sourceConnections.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Connection get (){
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private static Connection open(){
        String connectionURL = "jdbc:mysql://" + HOSTNAME + ":3306/" + DBNAME;
        try {
            return DriverManager.getConnection(connectionURL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
