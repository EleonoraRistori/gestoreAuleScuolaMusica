package com.gestoreAuleScuolaMusica.controller;

import com.example.gestoreaulescuolamusica.Controller.StudentController;
import com.example.gestoreaulescuolamusica.Controller.TeacherController;
import com.example.gestoreaulescuolamusica.DAO.ConnectionManager;
import com.example.gestoreaulescuolamusica.Model.GraphManager;
import com.example.gestoreaulescuolamusica.Model.UserManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.mail.MessagingException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TeacherControllerTest {
    TeacherController teacherController;
    GraphManager graphManager;
    UserManager userManager;
    ConnectionManager connectionManager;

    @BeforeEach
    void setUp() {
        try {
            graphManager = new GraphManager();
            userManager = new UserManager();
            connectionManager = ConnectionManager.getConnectionManager();
            teacherController = new TeacherController(graphManager, userManager);
            userManager.addUser("user_test", "password", "Pianoforte", 3);
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
    void chooseRoomTest() {
        try {
            ArrayList<String> availableRooms = teacherController.getAvailableRooms("user_test", LocalDateTime.of(2000, 1, 1, 9, 0));
            if (!availableRooms.isEmpty()) {
                try {
                    teacherController.chooseRoom(availableRooms.get(0), LocalDateTime.of(2000, 1, 1, 9, 0), "user_test");
                }catch(Exception e){
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                }
                Connection connection = connectionManager.getConnection();
                Statement statement = connection.createStatement();
                String sql = "select * from utilizzo where utente = 'user_test'";
                ResultSet rs = statement.executeQuery(sql);
                Assertions.assertTrue(rs.next());
                Assertions.assertEquals(Timestamp.valueOf(LocalDateTime.of(2000, 1, 1, 9, 0)),rs.getTimestamp("data_ora"));
                Assertions.assertEquals(availableRooms.get(0), rs.getString("aula"));
                statement.close();
            }
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}