package com.example.gestoreaulescuolamusica.Controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import com.example.gestoreaulescuolamusica.Model.*;

public class UserController {
    GraphManager graphManager;
    RoomGraph defaultGraph;
    ArrayList<Tag> tags;
    UserManager userManager;
    Booking bookings;


    public void deleteReservation(String username, LocalDateTime time) throws SQLException {
        bookings = graphManager.pullBookings(time);
        for(String room : bookings.getRoomsBooked().keySet()){
            if(Objects.equals(bookings.getRoomsBooked().get(room), username))
                graphManager.deleteReservation(time, room, username);

        }
    }

    public ArrayList<String> getAvailableRooms(String username, LocalDateTime time){
        User user = userManager.getUser(username);
        bookings = graphManager.pullBookings(time);
        Instrument instrument = user.getInstrument();
        ArrayList<String> rooms = new ArrayList<>();
        if (Objects.equals(instrument.getName(), "Pianoforte")){
            for (Tag tag : tags) {
                if (Objects.equals(tag.getFeature(), "Aula con pianoforte"))
                    rooms.add(tag.getRoom());
            }
        }
        else  if (Objects.equals(instrument.getName(), "Percussioni")){
            for (Tag tag : tags) {
                if (Objects.equals(tag.getFeature(), "Aula con percussioni"))
                    rooms.add(tag.getRoom());
            }
        }
        else {
            rooms.addAll(defaultGraph.getAdjacentRooms().keySet());
        }
        for(int i= rooms.size()-1; i>=0; i--){
            if(bookings.getRoomsBooked().containsKey(rooms.get(i))){
                rooms.remove(i);
            }
        }
        return rooms;
    }


}

