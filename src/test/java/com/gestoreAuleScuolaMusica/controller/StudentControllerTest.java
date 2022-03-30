package com.gestoreAuleScuolaMusica.controller;

import com.example.gestoreaulescuolamusica.Controller.StudentController;
import com.example.gestoreaulescuolamusica.DAO.ConnectionManager;
import com.example.gestoreaulescuolamusica.Model.Booking;
import com.example.gestoreaulescuolamusica.Model.GraphManager;
import com.example.gestoreaulescuolamusica.Model.UserManager;
import org.junit.jupiter.api.*;

import javax.mail.MessagingException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StudentControllerTest {
    StudentController studentController;
    ConnectionManager connectionManager;

    @BeforeEach
    void setUp(){
        try {
            GraphManager graphManager = new GraphManager();
            UserManager userManager = new UserManager();
            connectionManager = ConnectionManager.getConnectionManager();
            studentController = new StudentController(graphManager,userManager);
            userManager.addUser("user_test_piano","password", "Pianoforte",2);
            userManager.addUser("user_test_perc","password", "Percussioni",2);
            userManager.addUser("user_test_viol","password", "Violino",2);
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    @AfterEach
    void tearDown(){
        try {
            Connection connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            String sql = "delete from utente where username = 'user_test_piano' or username = 'user_test_perc' or username = 'user_test_viol'";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Test
    void getAvailableRoomsTest() {
        ArrayList<String> availableRooms = studentController.getAvailableRooms("user_test_piano", LocalDateTime.of(2000,1,1,9,0));
        Assertions.assertEquals(5, availableRooms.size());
        Assertions.assertTrue(availableRooms.contains("Aula 04 Cappella"));
        Assertions.assertTrue(availableRooms.contains("Aula 07"));
        Assertions.assertTrue(availableRooms.contains("Aula 10"));
        Assertions.assertTrue(availableRooms.contains("Aula 15 Pizzetti"));
        Assertions.assertTrue(availableRooms.contains("Aula 20 Torre"));
        availableRooms = studentController.getAvailableRooms("user_test_perc", LocalDateTime.of(2000,1,1,9,0));
        Assertions.assertEquals(3, availableRooms.size());
        Assertions.assertTrue(availableRooms.contains("Aula 05"));
        Assertions.assertTrue(availableRooms.contains("Aula 13 Nencetti"));
        Assertions.assertTrue(availableRooms.contains("Aula 15 Pizzetti"));
        availableRooms = studentController.getAvailableRooms("user_test_viol", LocalDateTime.of(2000,1,1,9,0));
        Assertions.assertEquals(11, availableRooms.size());
    }




    @Test
    void bookRoomTest() {
        try {
            try {
                studentController.bookRoom("user_test_piano", LocalDateTime.of(2000, 1, 1, 9, 0));
            }catch(Exception e){
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
            Connection connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            String sql = "select * from utilizzo where utente = 'user_test_piano'";
            ResultSet rs = statement.executeQuery(sql);
            Assertions.assertTrue(rs.next());
            Assertions.assertEquals(Timestamp.valueOf(LocalDateTime.of(2000, 1, 1, 9, 0)), rs.getTimestamp("data_ora"));
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Test
    void deleteReservationTest() {
        try{
            studentController.bookRoom("user_test_piano", LocalDateTime.of(2000, 1, 1, 9, 0));
            studentController.deleteReservation("user_test_piano",LocalDateTime.of(2000, 1, 1, 9, 0));
            Connection connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            String sql = "select * from utilizzo where utente = 'user_test'";
            ResultSet rs = statement.executeQuery(sql);
            Assertions.assertFalse(rs.next());
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}