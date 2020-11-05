package dao;

import config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {

    static Connection conn;

    public static Connection getConnection() {
        try {
            Class.forName(AppConfig.getInstance().JDBC_Driver);
            conn = DriverManager.getConnection(AppConfig.getInstance().DB_URL + AppConfig.getInstance().DB_NAME + "?serverTimezone=UTC", AppConfig.getInstance().DB_USER, AppConfig.getInstance().DB_PASSWORD);
            return conn;

        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to database", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error Class not found", e);
        }
    }
}
