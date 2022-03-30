package com.example.gestoreaulescuolamusica.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static String url;
    private static String user;
    private static String password;
    private static ConnectionManager instance = null;
    private static Connection connection;

    private ConnectionManager(){
        url = "jdbc:postgresql://localhost:5432/ProgettoSWE";
        user = "webuser";
        password = "password";
    }

    public static ConnectionManager getConnectionManager() throws SQLException {
        if(ConnectionManager.instance == null){
            ConnectionManager.instance = new ConnectionManager();
            connection = ConnectionManager.createConnection();
        }
        return ConnectionManager.instance;
    }

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() {
        return connection;
    }
}
