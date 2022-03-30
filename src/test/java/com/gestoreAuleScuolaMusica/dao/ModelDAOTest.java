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

class ModelDAOTest {
    static ConnectionManager connectionManager;
    static ModelDAO modelDAO;
    static UserDAO userDAO;


    @BeforeEach
    void setUp() {
        try {
            connectionManager = ConnectionManager.getConnectionManager();
            modelDAO = new ModelDAO();
            userDAO = new UserDAO();
            userDAO.addUser("user_test","password","Pianoforte",2);
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
        try {
            modelDAO.addReservation(LocalDateTime.of(2000,1,1,9,0), "Aula 04 Cappella", "user_test");
            Connection connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            String sql = "select * from utilizzo where utente = 'user_test'";
            ResultSet rs = statement.executeQuery(sql);
            Assertions.assertTrue(rs.next());
            Assertions.assertEquals(Timestamp.valueOf(LocalDateTime.of(2000, 1, 1, 9, 0)),rs.getTimestamp("data_ora"));
            Assertions.assertEquals( "Aula 04 Cappella", rs.getString("aula"));
            statement.close();
        } catch(SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Test
    void removeReservationTest() {
        try {
            Connection connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            String sql = "insert into utilizzo values('Aula 04 Cappella' , TO_TIMESTAMP('" + Timestamp.valueOf(LocalDateTime.of(2000,1,1,9,0)) + "','YYYY-MM-DD HH24:MI:SS'), 'user_test')";
            statement.executeUpdate(sql);
            modelDAO.removeReservation(LocalDateTime.of(2000,1,1,9,0), "Aula 04 Cappella");
            sql = "select * from utilizzo where utente = 'user_test'";
            ResultSet rs = statement.executeQuery(sql);
            Assertions.assertFalse(rs.next());
            statement.close();
        } catch(SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}