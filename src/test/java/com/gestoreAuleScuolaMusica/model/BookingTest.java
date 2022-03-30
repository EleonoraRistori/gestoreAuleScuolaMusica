package com.gestoreAuleScuolaMusica.model;

import com.example.gestoreaulescuolamusica.Model.Booking;
import org.junit.jupiter.api.*;

class BookingTest {
    static Booking booking;

    @BeforeAll
    static void setUp() {
        booking = new Booking();
    }

    @Test
    void addRoomBookedTest() {
        booking.addRoomBooked("aula_test", "user_test");
        Assertions.assertTrue(booking.getRoomsBooked().containsKey("aula_test"));
        Assertions.assertEquals("user_test", booking.getRoomsBooked().get("aula_test"));
    }

    @Test
    void removeRoomBookedTest() {
        booking.addRoomBooked("aula_test", "user_test");
        booking.removeRoomBooked("aula_test", "user_test");
        Assertions.assertFalse(booking.getRoomsBooked().containsKey("aula_test"));
    }
}