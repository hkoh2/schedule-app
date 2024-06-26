package com.hankoh.scheduleapp.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost:8081/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    // Driver reference
    private static final String userName = "sauser";
    // Username
    private static final String password = "sauserpw";
    private static Connection connection = null;
    // Connection Interface
    public static void makeConnection() {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("Connection successful!");
        }
        catch(ClassNotFoundException | SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
    public static Connection getConnection() {
        return connection;
    }
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}


