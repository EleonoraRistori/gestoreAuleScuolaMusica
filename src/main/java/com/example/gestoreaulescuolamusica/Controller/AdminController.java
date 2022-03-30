package com.example.gestoreaulescuolamusica.Controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.example.gestoreaulescuolamusica.Model.*;
import com.example.gestoreaulescuolamusica.JavaMailUtil;

public class AdminController {
    private GraphManager graphManager;
    private Booking bookings;
    private RoomGraph defaultGraph;

    public AdminController(GraphManager graphManager) throws SQLException {
        this.graphManager = graphManager;
        defaultGraph = graphManager.getDefaultGraph();
    }

    public Booking getReservationTable(LocalDateTime time){
        return graphManager.pullBookings(time);
    }

    public void deleteReservation(LocalDateTime time, String room, String username) throws Exception {
        graphManager.deleteReservation(time, room, username);
        graphManager.addReservation(time,room, "admin");
        JavaMailUtil.sendDeleteReservation(username, time, room);

    }
    public ArrayList<String> getAvailableRooms(LocalDateTime time){
        bookings = graphManager.pullBookings(time);

        ArrayList<String> rooms = new ArrayList<>(defaultGraph.getAdjacentRooms().keySet());

        for(int i= rooms.size()-1; i>=0; i--){
            if(bookings.getRoomsBooked().containsKey(rooms.get(i))){
                rooms.remove(i);
            }
        }
        return rooms;
    }

    public void setNotBookable(String room, LocalDateTime time) throws SQLException {
        graphManager.addReservation(time,room, "admin");
    }
    public void setBookable(String room, LocalDateTime time) throws SQLException {
        graphManager.deleteReservation(time, room, "admin");
    }
}


