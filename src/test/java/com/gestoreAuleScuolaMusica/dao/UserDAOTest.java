package com.gestoreAuleScuolaMusica.dao;

import com.example.gestoreaulescuolamusica.DAO.ConnectionManager;
import com.example.gestoreaulescuolamusica.DAO.ModelDAO;
import com.example.gestoreaulescuolamusica.DAO.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    static ConnectionManager connectionManager;
    static UserDAO userDAO;




    @Test
    void addUserTest() {
        try {
            userDAO = new UserDAO();
            connectionManager = ConnectionManager.getConnectionManager();
            userDAO.addUser("user_test","password", "Pianoforte",2);
            Connection connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            String sql = "select * from utente where username = 'user_test'";
            ResultSet rs = statement.executeQuery(sql);
            Assertions.assertTrue(rs.next());
            Assertions.assertEquals("password", rs.getString("passcode"));
            Assertions.assertEquals("Pianoforte", rs.getString("strumento"));
            Assertions.assertEquals(2 , rs.getInt("tipo"));
            sql = "delete from utente where username = 'user_test'";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}