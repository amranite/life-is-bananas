package com.lifeisbananas.database;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Initialize dotenv to load environment variables from .env file
    private static final Dotenv dotenv = Dotenv.load();

    // Database credentials
    private static final String URL = dotenv.get("DB_URL"); // Gets the database URL from environment variable
    private static final String USER = dotenv.get("DB_USER");  // Gets the username from environment variable
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");;  // Gets the password from environment variable

    // Singleton instance
    private static Connection connection = null;

    // Get database connection (creates if doesn't exist)
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load MySQL driver (optional in newer versions)
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Create connection
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully!");

            } catch (ClassNotFoundException e) {
                System.err.println("MySQL Driver not found!");
                throw new SQLException("Driver not found", e);
            }
        }
        return connection;
    }

    // Close database connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // Test connection
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("Connection test successful!");
            System.out.println("Database: " + conn.getCatalog());
            closeConnection();
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}