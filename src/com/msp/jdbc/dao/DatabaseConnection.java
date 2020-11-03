package com.msp.jdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USERNAME = "root";
    public static final String PASSWORD = "hanoi@2020";
    public static final String URL = "jdbc:mysql://localhost:3306/student_management";

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection connection() {
        Connection con = null;
        try {
            System.out.println("Connecting to DB...");
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Fail to connect to DB!");
            e.printStackTrace();
        }
        System.out.println("Connect successful");
        return con;
    }
}
