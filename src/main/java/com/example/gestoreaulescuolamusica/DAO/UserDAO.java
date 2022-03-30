package com.example.gestoreaulescuolamusica.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import com.example.gestoreaulescuolamusica.Model.*;

public class UserDAO {
    ConnectionManager connectionManager;
    Connection connection;

    public UserDAO() throws SQLException{
        this.connectionManager = ConnectionManager.getConnectionManager();
    }

    public HashMap<String, Instrument> pullInstruments() throws SQLException {
        connection = connectionManager.getConnection();
        Statement statement = connection.createStatement();
        String sql = "select * from strumento";
        ResultSet rs = statement.executeQuery(sql);
        HashMap<String, Instrument> instruments = new HashMap<>();
        while(rs.next()){
            instruments.put(rs.getString("nome"), new Instrument(rs.getString("nome"), rs.getBoolean("pesante"),rs.getBoolean("rumoroso")) );
        }
        statement.close();
        return instruments;
    }

    public ArrayList<User> pullUsers() throws SQLException {
        HashMap<String, Instrument> instruments = pullInstruments();
        connection = connectionManager.getConnection();
        Statement statement = connection.createStatement();
        String sql = "select * from utente";
        ResultSet rs = statement.executeQuery(sql);
        ArrayList<User> users = new ArrayList<>();
        while(rs.next()){
            String username = rs.getString("username");
            String password = rs.getString("passcode");
            String instrumentName = rs.getString("strumento");
            int type = rs.getInt("tipo");
            users.add(new User(username, password, instruments.get(instrumentName),type));
        }
        statement.close();
        return users;
    }

    public void addUser(String username, String password, String instrument, int type) throws SQLException {
        connection = connectionManager.getConnection();
        Statement statement = connection.createStatement();
        String sql = "insert into utente values('"+ username + "','" + password + "','" + instrument + "'," + type + ")";
        statement.executeUpdate(sql);
        statement.close();
    }
}
