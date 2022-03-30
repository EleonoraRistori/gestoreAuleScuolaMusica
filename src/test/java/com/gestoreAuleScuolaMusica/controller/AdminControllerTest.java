package com.gestoreAuleScuolaMusica.controller;

import com.example.gestoreaulescuolamusica.Controller.AdminController;
import com.example.gestoreaulescuolamusica.Controller.StudentController;
import com.example.gestoreaulescuolamusica.DAO.ConnectionManager;
import com.example.gestoreaulescuolamusica.Model.Booking;
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

class AdminControllerTest {
    AdminController adminController;
    GraphManager graphManager;
    ConnectionManager connectionManager;

    @BeforeEach
    void setUp() {
        try {
            graphManager = new GraphManager();
            connectionManager = ConnectionManager.getConnectionManager();
            adminController = new AdminController(graphManager);
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }



    @Test
    void deleteReservation() {
        try{
            UserManager userManager = new UserManager();
            userManager.addUser("user_test","password","Pianoforte",2);
            Booking booking1 = graphManager.pullBookings(LocalDateTime.of(2000,1,1,9,0));
            if(!booking1.getRoomsBooked().containsKey("Aula 04 Cappella")) {
                graphManager.addReservation(LocalDateTime.of(2000, 1, 1, 9, 0), "Aula 04 Cappella", "user_test");
                try {
                    adminController.deleteReservation(LocalDateTime.of(2000, 1, 1, 9, 0), "Aula 04 Cappella", "user_test");
                }catch(Exception e){
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                }
                Connection connection = connectionManager.getConnection();
                Statement statement = connection.createStatement();
                String sql = "select * from utilizzo where utente = 'user_test'";
                ResultSet rs = statement.executeQuery(sql);
                Assertions.assertFalse(rs.next());
                ArrayList<String> availableRooms = adminController.getAvailableRooms(LocalDateTime.of(2000,1,1,9,0));
                Assertions.assertFalse(availableRooms.contains("Aula 04 Cappella"));
                sql = "delete from utente where username = 'user_test'";
                statement.executeUpdate(sql);
                sql = "delete from utilizzo where aula = 'Aula 04 Cappella' and data_ora = TO_TIMESTAMP('2000-01-01 09:00:00','YYYY-MM-DD HH24:MI:SS' )";
                statement.executeUpdate(sql);
                statement.close();
            }
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Test
    void getAvailableRooms() {
        ArrayList<String> availableRooms = adminController.getAvailableRooms(LocalDateTime.of(2000,1,1,9,0));
        Assertions.assertEquals(11, availableRooms.size());
    }

    @Test
    void setNotBookable() {
        try {
            ArrayList<String> availableRooms = adminController.getAvailableRooms(LocalDateTime.of(2000,1,1,9,0));
            String room = availableRooms.get(0);
            adminController.setNotBookable(room, LocalDateTime.of(2000, 1, 1, 9, 0));
            availableRooms = adminController.getAvailableRooms(LocalDateTime.of(2000,1,1,9,0));
            Assertions.assertFalse(availableRooms.contains(room));
            Connection connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            String sql = "delete from utilizzo where aula = '"+room+"' and data_ora = TO_TIMESTAMP('2000-01-01 09:00:00','YYYY-MM-DD HH24:MI:SS' )";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Test
    void setBookable() {
        try{
            ArrayList<String> availableRooms = adminController.getAvailableRooms(LocalDateTime.of(2000,1,1,9,0));
            String room = availableRooms.get(0);
            adminController.setNotBookable(room, LocalDateTime.of(2000, 1, 1, 9, 0));
            adminController.setBookable(room, LocalDateTime.of(2000, 1, 1, 9, 0));
            availableRooms = adminController.getAvailableRooms(LocalDateTime.of(2000,1,1,9,0));
            Assertions.assertTrue(availableRooms.contains(room));
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}