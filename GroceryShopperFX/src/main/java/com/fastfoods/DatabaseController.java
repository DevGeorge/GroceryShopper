package com.fastfoods;
//DatabaseController.java
// This class creates and returns a connection with the db.

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseController {
    public Connection databaseConnection;

    public Connection getConnection() {
        String databaseName = "groceryshopper";
        String databaseUser = "gshopper";
        String databasePassword = "info2413group5";
        String url = "jdbc:mysql://groceryshopper.caysbz7wkiym.us-east-2.rds.amazonaws.com:3306/" + databaseName;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            databaseConnection = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return databaseConnection;

    }
}