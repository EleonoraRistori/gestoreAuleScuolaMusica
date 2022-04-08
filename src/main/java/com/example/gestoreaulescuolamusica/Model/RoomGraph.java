package com.example.gestoreaulescuolamusica.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class RoomGraph {
    private final HashMap<String, ArrayList<String>> adjacentRooms;

    public RoomGraph() {
        adjacentRooms = new HashMap<>();
    }

    public void addAdjacentRooms(String room, String adjacentRoom){
        addRoom(room);
        adjacentRooms.get(room).add(adjacentRoom);
    }

    public void addRoom(String room) {
        if (!adjacentRooms.containsKey(room)) {
            adjacentRooms.put(room, new ArrayList<>());
        }
    }

    public HashMap<String, ArrayList<String>> getAdjacentRooms() {
        return adjacentRooms;
    }
    public  ArrayList<String> getAdjacentRooms(String room){
        return adjacentRooms.get(room);
    }
}

