package com.example.gestoreaulescuolamusica.Controller;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import com.example.gestoreaulescuolamusica.Model.*;
import com.example.gestoreaulescuolamusica.JavaMailUtil;


public class TeacherController extends UserController {

    public TeacherController(GraphManager graphManager, UserManager userManager) throws SQLException {
        this.graphManager = graphManager;
        this.userManager = userManager;
        defaultGraph = graphManager.getDefaultGraph();
        tags = graphManager.getTags();
    }



    public void chooseRoom(String room, LocalDateTime time, String teacher) throws SQLException, MessagingException {
        graphManager.addReservation(time, room, teacher);
        JavaMailUtil.sendReservationConfirm(teacher, time, room);
    }

}

