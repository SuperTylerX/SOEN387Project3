package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static config.AppConfig.*;

public class DBConnection {

    static Connection conn;

    public static Connection getConnection() {
        try {
            Class.forName(JDBC_Driver);
            conn = DriverManager.getConnection(DB_URL + DB_NAME + "?serverTimezone=UTC", DB_USER, DB_PASSWORD);
            return conn;

        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to database", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error Class not found", e);
        }
    }
}
