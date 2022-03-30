package com.gestoreAuleScuolaMusica.model;

import com.example.gestoreaulescuolamusica.Model.RoomGraph;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RoomGraphTest {
    static RoomGraph roomGraph;

    @BeforeAll
    static void setUp() {
        roomGraph = new RoomGraph();
    }

    @Test
    void addRoomTest() {
        roomGraph.addRoom("aula_test1");
        Assertions.assertTrue(roomGraph.getAdjacentRooms().containsKey("aula_test1"));
    }

    @Test
    void addAdjacentRoomsTest() {
        roomGraph.addAdjacentRooms("aula_test2", "aula_adiacente");
        Assertions.assertTrue(roomGraph.getAdjacentRooms().containsKey("aula_test2"));
        Assertions.assertTrue(roomGraph.getAdjacentRooms().get("aula_test2").contains("aula_adiacente"));
    }


}