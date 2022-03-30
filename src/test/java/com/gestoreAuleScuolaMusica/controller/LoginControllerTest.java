package com.gestoreAuleScuolaMusica.controller;

import com.example.gestoreaulescuolamusica.Controller.LoginController;
import com.example.gestoreaulescuolamusica.DAO.ConnectionManager;
import com.example.gestoreaulescuolamusica.Model.UserManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {
    LoginController loginController;
    UserManager userManager;
    ConnectionManager connectionManager;

    @BeforeEach
    void setUp() {
        try {
            userManager = new UserManager();
            loginController = new LoginController(userManager);
            connectionManager = ConnectionManager.getConnectionManager();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            Connection connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            String sql = "delete from utente where username = 'user_test'";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Test
    void addUserTest() {
        try{
            loginController.addUser("user_test","password","Pianoforte",2);
            Connection connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            String sql = "select * from utente where username = 'user_test'";
            ResultSet rs = statement.executeQuery(sql);
            Assertions.assertTrue(rs.next());
            Assertions.assertEquals( "password",rs.getString("passcode"));
            Assertions.assertEquals( "Pianoforte", rs.getString("strumento"));
            Assertions.assertEquals(2, rs.getInt("tipo"));
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Test
    void isAnExistingUserTest() {
        try{
            loginController.addUser("user_test","password","Pianoforte",2);
            Assertions.assertTrue(loginController.isAnExistingUser("user_test"));
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Test
    void getUserPasswordTest() {
        try{
            loginController.addUser("user_test","password","Pianoforte",2);
            Assertions.assertEquals("password", loginController.getUserPassword("user_test"));
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }


    @Test
    void getUserTypeTest() {
        try{
            loginController.addUser("user_test","password","Pianoforte",2);
            Assertions.assertEquals(2,loginController.getUserType("user_test"));
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}