package com.example.gestoreaulescuolamusica.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Timestamp;
import com.example.gestoreaulescuolamusica.Model.*;

public class ModelDAO {
    ConnectionManager connectionManager;
    Connection connection;

    public ModelDAO() throws SQLException{
        this.connectionManager = ConnectionManager.getConnectionManager();
    }

    public RoomGraph pullDefaultGraph() throws SQLException {
        connection = connectionManager.getConnection();
        Statement statement = connection.createStatement();
        String sql = "select * from aula";
        ResultSet rs = statement.executeQuery(sql);
        RoomGraph roomGraph = new RoomGraph();
        while (rs.next()){
            roomGraph.addRoom(rs.getString("nome"));
        }
        sql = "select * from aule_adiacenti";
        rs = statement.executeQuery(sql);
        while (rs.next()){
            roomGraph.addAdjacentRooms(rs.getString("aula"), rs.getString("adiacente"));
        }
        statement.close();
        return roomGraph;
    }

    public HashMap<LocalDateTime, Booking> pullScheduledBookings() throws SQLException {
        connection = connectionManager.getConnection();
        Statement statement = connection.createStatement();
        String sql = "select * from utilizzo order by data_ora";
        ResultSet rs = statement.executeQuery(sql);
        HashMap<LocalDateTime, Booking> scheduledRoomBooked = new HashMap<>();
        while (rs.next()) {
            String room = rs.getString("aula");
            String user = rs.getString("utente");
            LocalDateTime time = rs.getTimestamp("data_ora").toLocalDateTime();
            if(!scheduledRoomBooked.containsKey(time)){
                Booking booking = new Booking();
                booking.addRoomBooked(room, user);
                scheduledRoomBooked.put(time, booking);
            } else {
                scheduledRoomBooked.get(time).addRoomBooked(room, user);
            }
        }
        statement.close();
        return scheduledRoomBooked;
    }

    public ArrayList<Tag> pullTags() throws SQLException {
        connection = connectionManager.getConnection();
        Statement statement = connection.createStatement();
        String sql = "select * from tag";
        ResultSet rs = statement.executeQuery(sql);
        ArrayList<Tag> tags = new ArrayList<>();
        while (rs.next()){
            tags.add(new Tag(rs.getString("aula"),rs.getString("caratteristica") ));
        }
        statement.close();
        return tags;
    }

    public void addReservation(LocalDateTime time, String room, String username) throws SQLException {
        connection = connectionManager.getConnection();
        Statement statement = connection.createStatement();
        String sql = "insert into utilizzo values('"+ room + "', TO_TIMESTAMP('" + Timestamp.valueOf(time) + "','YYYY-MM-DD HH24:MI:SS'),'" + username + "')";
        statement.executeUpdate(sql);
        statement.close();
    }

    public void removeReservation(LocalDateTime time, String room) throws SQLException {
        connection = connectionManager.getConnection();
        Statement statement = connection.createStatement();
        String sql = "delete from utilizzo where data_ora = '"+ Timestamp.valueOf(time) + "' and aula = '" + room +"'" ;
        statement.executeUpdate(sql);
        statement.close();
    }
}

