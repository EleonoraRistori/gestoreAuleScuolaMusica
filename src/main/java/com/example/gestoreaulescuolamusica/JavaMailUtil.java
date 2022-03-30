package com.example.gestoreaulescuolamusica;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Properties;

public class JavaMailUtil {
    public static void sendDeleteReservation(String recipient, LocalDateTime time, String room) throws MessagingException {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myAccountMail = "noreply.scuoladimusica@gmail.com";
        String password = "progettoswe1!";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(myAccountMail, password);
            }
        });

        Message message = prepareMessageCancellation(session, myAccountMail, recipient, time, room);
        assert message != null;
        Transport.send(message);
    }

    private static Message prepareMessageCancellation(Session session, String myAccountMail, String recipient, LocalDateTime time, String room) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountMail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Prenotazione cancellata");
            message.setText("La tua prenotazione del " + time.getDayOfMonth() +"-"+time.getMonthValue()+"-"+time.getYear()
                    +" alle "+time.getHour()+":00 in "+ room +" è stata cancellata.");
            return message;
        } catch(Exception ex){
            System.out.println("errore");
        }
        return null;
    }

    public static void sendReservationConfirm(String recipient, LocalDateTime time, String room) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myAccountMail = "noreply.scuoladimusica@gmail.com";
        String password = "progettoswe1!";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(myAccountMail, password);
            }
        });

        Message message = prepareReservationMessage(session, myAccountMail, recipient, time, room);
        assert message != null;
        Transport.send(message);
    }
    private static Message prepareReservationMessage(Session session, String myAccountMail, String recipient, LocalDateTime time, String room) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountMail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Prenotazione confermata");
            message.setText("La tua prenotazione del " + time.getDayOfMonth() +"-"+time.getMonthValue()+"-"+time.getYear()
                    +" alle "+time.getHour()+":00 in "+ room +" è confermata.");
            return message;
        } catch(Exception ex){
            System.out.println("errore");
        }
        return null;
    }
}