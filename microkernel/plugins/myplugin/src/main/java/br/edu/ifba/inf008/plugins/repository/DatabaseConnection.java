package br.edu.ifba.inf008.plugins.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private static final String URL = "jdbc:mariadb://localhost:3306/ecommerce_inf008";
    private static final String USER = "inf008";
    private static final String PASSWORD = "inf008";

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MariaDB JDBC driver not found: " + e.getMessage());
        }
    }

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}