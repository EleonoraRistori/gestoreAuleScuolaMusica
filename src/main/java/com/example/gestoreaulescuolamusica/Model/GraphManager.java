package com.example.gestoreaulescuolamusica.Model;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import com.example.gestoreaulescuolamusica.DAO.ModelDAO;

public class GraphManager {
    private final ModelDAO modelDAO;
    private final RoomGraph defaultGraph;
    private final HashMap<LocalDateTime, Booking> scheduledRoomBooked;
    private final ArrayList<Tag> tags;



    public GraphManager() throws SQLException {
        modelDAO = new ModelDAO();
        defaultGraph = modelDAO.pullDefaultGraph();
        scheduledRoomBooked = modelDAO.pullScheduledBookings();
        tags = modelDAO.pullTags();
    }

    public ArrayList<Tag> getTags() {
        return new ArrayList<>(tags);
    }

    public Booking pullBookings(LocalDateTime time){
        if (!scheduledRoomBooked.containsKey(time)){
            Booking booking = new Booking();
            scheduledRoomBooked.put(time, booking);
        }
        return  scheduledRoomBooked.get(time);
    }

    public RoomGraph getDefaultGraph(){
        return defaultGraph;
    }

    public void addReservation(LocalDateTime time, String room, String username) throws SQLException {
        scheduledRoomBooked.get(time).addRoomBooked(room,username);
        modelDAO.addReservation(time, room, username);
    }

    public void deleteReservation(LocalDateTime time, String room, String username) throws SQLException {
        scheduledRoomBooked.get(time).removeRoomBooked(room, username);
        modelDAO.removeReservation(time, room);
    }
}

