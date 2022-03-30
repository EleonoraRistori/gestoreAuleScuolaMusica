package com.example.gestoreaulescuolamusica.Model;

import java.util.HashMap;

public class Booking {
    private HashMap<String, String> roomsBooked;

    public Booking() {
        roomsBooked = new HashMap<>();
    }

    public void addRoomBooked(String room, String username){
        roomsBooked.put(room, username);
    }

    public void removeRoomBooked(String room, String username){
        roomsBooked.remove(room, username);
    }

    public HashMap<String, String> getRoomsBooked() {
        return roomsBooked;
    }
}
