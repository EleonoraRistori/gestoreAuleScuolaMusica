package com.gestoreAuleScuolaMusica.model;

import com.example.gestoreaulescuolamusica.DAO.ConnectionManager;
import com.example.gestoreaulescuolamusica.DAO.UserDAO;
import com.example.gestoreaulescuolamusica.Model.Booking;
import com.example.gestoreaulescuolamusica.Model.GraphManager;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.LocalDateTime;

class GraphManagerTest {
    GraphManager graphManager;
    ConnectionManager connectionManager;
    UserDAO userDAO;

    @BeforeEach
    void setUp() {
        try {
            connectionManager = ConnectionManager.getConnectionManager();
            graphManager = new GraphManager();
            userDAO = new UserDAO();
            userDAO.addUser("user_test","password", "Pianoforte",2);
        }catch(SQLException e){
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
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
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    void addReservationTest() {
        try{
            Booking booking1 = graphManager.pullBookings(LocalDateTime.of(2000,1,1,9,0));
            if(!booking1.getRoomsBooked().containsKey("Aula 04 Cappella")) {
                graphManager.addReservation(LocalDateTime.of(2000, 1, 1, 9, 0), "Aula 04 Cappella", "user_test");
                Connection connection = connectionManager.getConnection();
                Statement statement = connection.createStatement();
                String sql = "select * from utilizzo where utente = 'user_test'";
                ResultSet rs = statement.executeQuery(sql);
                Assertions.assertTrue(rs.next());
                Assertions.assertEquals(Timestamp.valueOf(LocalDateTime.of(2000, 1, 1, 9, 0)), rs.getTimestamp("data_ora"));
                Assertions.assertEquals( "Aula 04 Cappella", rs.getString("aula"));
                statement.close();
                Booking booking2 = graphManager.pullBookings(LocalDateTime.of(2000, 1, 1, 9, 0));
                Assertions.assertTrue(booking2.getRoomsBooked().containsKey("Aula 04 Cappella"));
                Assertions.assertEquals("user_test", booking2.getRoomsBooked().get("Aula 04 Cappella"));
            }
        } catch (SQLException e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }

    @Test
    void deleteReservationTest() {
        try{
            Booking booking1 = graphManager.pullBookings(LocalDateTime.of(2000,1,1,9,0));
            if(!booking1.getRoomsBooked().containsKey("Aula 07")) {
                graphManager.addReservation(LocalDateTime.of(2000, 1, 1, 9, 0), "Aula 07", "user_test");
                graphManager.deleteReservation(LocalDateTime.of(2000, 1, 1, 9, 0), "Aula 07", "user_test");
                Connection connection = connectionManager.getConnection();
                Statement statement = connection.createStatement();
                String sql = "select * from utilizzo where utente = 'user_test'";
                ResultSet rs = statement.executeQuery(sql);
                Assertions.assertFalse(rs.next());
                statement.close();
                Booking booking = graphManager.pullBookings(LocalDateTime.of(2000, 1, 1, 9, 0));
                Assertions.assertFalse(booking.getRoomsBooked().containsKey("Aula 07"));
            }
        } catch (SQLException e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
    }
}