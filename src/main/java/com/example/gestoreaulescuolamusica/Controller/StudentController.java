package com.example.gestoreaulescuolamusica.Controller;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import com.example.gestoreaulescuolamusica.Model.*;
import com.example.gestoreaulescuolamusica.JavaMailUtil;

public class StudentController extends UserController {


    public StudentController(GraphManager graphManager, UserManager userManager) throws SQLException {
        this.graphManager = graphManager;
        this.userManager = userManager;
        defaultGraph = graphManager.getDefaultGraph();
        tags = graphManager.getTags();
    }

    private boolean isUpperFloorRoom(String room){
        for (Tag tag : tags){
            if(Objects.equals(tag.getAula(), room) && Objects.equals(tag.getCaratteristica(), "Piani superiori"))
                return true;
        }
        return false;
    }

    public String bookRoom(String studentName, LocalDateTime time) throws SQLException, MessagingException {
        User student = userManager.getUser(studentName);
        Instrument instrument = student.getInstrument();
        ArrayList<String> rooms = getAvailableRooms(studentName, time);

        if(!rooms.isEmpty()){
            ArrayList<Integer> handicaps = new ArrayList<>();
            for (String room : rooms) {
                int handicap = 0;
                ArrayList<String> adjacentRooms = defaultGraph.getAdjacentRooms(room);
                for (String adjacentRoom : adjacentRooms) {
                    if (userManager.isNoisyUser(bookings.getRoomsBooked().get(adjacentRoom))) {
                        handicap = handicap + 3;
                    }
                }
                if (instrument.isHeavy() && isUpperFloorRoom(room)) {
                    handicap = handicap + 2;
                }
                handicaps.add(handicap);
            }
            int min = handicaps.get(0);
            for (Integer handicap : handicaps) {
                if (min > handicap)
                    min = handicap;
            }
            String tmp = rooms.get(handicaps.indexOf(min));
            graphManager.addReservation(time, tmp, student.getUsername());
            JavaMailUtil.sendReservationConfirm(studentName, time, tmp);
            return tmp;
        }
        return null;
    }



}
