package com.example.gestoreaulescuolamusica;

import com.example.gestoreaulescuolamusica.Controller.*;
import com.example.gestoreaulescuolamusica.Model.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;


public class InterfaceController implements Initializable {
    ObservableList<String> instruments = FXCollections.observableArrayList("Violino","Viola","Violoncello",
            "Contrabbasso","Flauto","Oboe","Clarinetto","Saxofono","Fagotto","Corno","Tromba","Trombone","Percussioni",
            "Arpa","Chitarra","Fisarmonica","Violoncello Barocco","Viola da gamba","Liuto","Flauto dolce",
            "Pianoforte", "Canto");
    ObservableList<String> hours = FXCollections.observableArrayList("9:00","10:00","11:00","12:00","13:00","14:00","15:00",
            "16:00","17:00","18:00","19:00");
    private Stage stage;
    private Scene scene;
    private Parent root;
    private static LocalDateTime dateTime;
    private static String currentUser;
    private static ArrayList<String> user;
    private static ArrayList<String> bookedRooms;
    private static ArrayList<String> availableRooms;
    private LoginController loginController;
    private StudentController studentController;
    private TeacherController teacherController;
    private AdminController adminController;


    @FXML
    private Label inexistingUserLabel;
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private ChoiceBox<String> instrumentMenu = new ChoiceBox<>();

    @FXML
    private PasswordField passwordField2;

    @FXML
    private ToggleGroup ruolo;

    @FXML
    private Button signInButton;

    @FXML
    private RadioButton studentButton;

    @FXML
    private RadioButton teacherButton;

    @FXML
    private TextField usernameField2;
    @FXML
    private Label signInSuccesful;
    @FXML
    private Button backToLogin;

    @FXML
    private Button Book;

    @FXML
    private ChoiceBox<String> chooseHour = new ChoiceBox<>();

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label BookingLabel;

    @FXML
    private Button checkAvailability;

    @FXML
    private ChoiceBox<String> chooseHourTeacher = new ChoiceBox<>();

    @FXML
    private DatePicker datePickerTeacher;
    @FXML
    private Label selectDate;

    @FXML
    private ListView<String> AvailableRoomList;

    @FXML
    private Button teacherBookingButton;

    @FXML
    private ListView<String> AvailableRoomListAdmin;

    @FXML
    private ListView<String> bookedRoomList;

    @FXML
    private Button CheckReservation;

    @FXML
    private Button NotBookable;

    @FXML
    private ChoiceBox<String> chooseHourAdmin = new ChoiceBox<>();

    @FXML
    private DatePicker datePickerAdmin;

    @FXML
    private Button deleteAdminReservation;

    @FXML
    private Button deleteUserReservation;
    @FXML
    private Label AdminLabel;

    @FXML
    void CheckReservation(MouseEvent event) {
        LocalDate date = datePickerAdmin.getValue();
        if(date != null) {
            AvailableRoomListAdmin.getItems().clear();
            bookedRoomList.getItems().clear();
            LocalTime time = getTime(chooseHourAdmin.getSelectionModel().getSelectedItem());
            dateTime = LocalDateTime.of(date,time);
            availableRooms = adminController.getAvailableRooms(dateTime);
            AvailableRoomListAdmin.getItems().addAll(availableRooms);
            Booking booking = adminController.getReservationTable(dateTime);
            ArrayList<String> bookedRoomsUser = new ArrayList<>();
            bookedRooms = new ArrayList<>();
            user = new ArrayList<>();
            for(String room : booking.getRoomsBooked().keySet()){
                bookedRoomsUser.add(room + "  ->  "+ booking.getRoomsBooked().get(room));
                bookedRooms.add(room);
                user.add(booking.getRoomsBooked().get(room));
            }
            bookedRoomList.getItems().addAll(bookedRoomsUser);

        } else{
            AdminLabel.setText("Seleziona una data prima di prenotare!");
            AdminLabel.setTextFill(Color.web("#ff0000"));
        }

    }

    @FXML
    void NotBookable(MouseEvent event) {
        try {
            int selectedID = AvailableRoomListAdmin.getSelectionModel().getSelectedIndex();
            bookedRooms.add(availableRooms.get(selectedID));
            user.add("admin");
            adminController.setNotBookable(availableRooms.get(selectedID), dateTime);
            AvailableRoomListAdmin.getItems().remove(selectedID);
            bookedRoomList.getItems().add(availableRooms.remove(selectedID)+"  ->  admin");

        }catch (SQLException e){
            AdminLabel.setText("Errore di connessione al database");
            AdminLabel.setTextFill(Color.web("#ff0000"));
        }


    }

    @FXML
    void deleteAdminReservation(MouseEvent event) {
        try {
            int selectedID = bookedRoomList.getSelectionModel().getSelectedIndex();
            if(Objects.equals(user.get(selectedID), "admin")){
                availableRooms.add(bookedRooms.get(selectedID));
                user.remove(selectedID);
                adminController.setBookable(bookedRooms.get(selectedID), dateTime);
                AdminLabel.setText("Stanza nuovamente disponibile!");
                AdminLabel.setTextFill(Color.web("#0076a3"));
                AvailableRoomListAdmin.getItems().add(bookedRooms.remove(selectedID));
                bookedRoomList.getItems().remove(selectedID);
            }
            else{
                AdminLabel.setText("L'aula selezionata è già prenotata!");
                AdminLabel.setTextFill(Color.web("#0076a3"));
            }

        }catch(SQLException e) {
            AdminLabel.setText("Errore di connessione al database");
            AdminLabel.setTextFill(Color.web("#ff0000"));
        }
    }

    @FXML
    void deleteUserReservation(MouseEvent event) {
        try {
            int selectedID = bookedRoomList.getSelectionModel().getSelectedIndex();
            if(!Objects.equals(user.get(selectedID), "admin")) {
                adminController.deleteReservation(dateTime, bookedRooms.get(selectedID), user.get(selectedID));
                user.set(selectedID, "admin");
                bookedRoomList.getItems().set(selectedID, bookedRooms.get(selectedID)+"  ->  admin");
                AdminLabel.setText("Cancellazione riuscita!");
                AdminLabel.setTextFill(Color.web("#0076a3"));
            }else{
                AdminLabel.setText("Per cancellare la prenotazione usa l'altro pulsante");
                AdminLabel.setTextFill(Color.web("#0076a3"));
            }
        }catch(SQLException e) {
            AdminLabel.setText("Errore di connessione al database");
            AdminLabel.setTextFill(Color.web("#ff0000"));
        }catch(Exception e){
            AdminLabel.setText("Problemi con il servizio di mailing");
            AdminLabel.setTextFill(Color.web("#ff0000"));
        }
    }

    @FXML
    void teacherRoomBooked(MouseEvent event) {
        try {
            teacherController.chooseRoom(AvailableRoomList.getSelectionModel().getSelectedItem(), dateTime, currentUser);
            int selectedID = AvailableRoomList.getSelectionModel().getSelectedIndex();
            AvailableRoomList.getItems().remove(selectedID);
            selectDate.setText("Prenotazione confermata");
            selectDate.setTextFill(Color.web("#0076a3"));
        }catch(SQLException e) {
            selectDate.setText("Errore di connessione al database");
            selectDate.setTextFill(Color.web("#ff0000"));
        }catch (Exception e){
            selectDate.setText("Problemi con il servizio di mailing");
            selectDate.setTextFill(Color.web("#ff0000"));
        }
    }


    @FXML
    void verifyAvailability(MouseEvent event) throws IOException {
        LocalDate date = datePickerTeacher.getValue();
        AvailableRoomList.getItems().clear();
        if(date != null) {
            LocalTime time = getTime(chooseHourTeacher.getSelectionModel().getSelectedItem());
            dateTime = LocalDateTime.of(date,time);
            ArrayList<String> rooms = teacherController.getAvailableRooms(currentUser, dateTime);
            AvailableRoomList.getItems().addAll(rooms);
        }
        else{
            selectDate.setText("Seleziona una data prima di prenotare!");
            selectDate.setTextFill(Color.web("#ff0000"));
        }
    }

    @FXML
    void BookRoom(MouseEvent event) {
        LocalDate date = datePicker.getValue();
        if(date != null) {
            LocalTime time = getTime(chooseHour.getSelectionModel().getSelectedItem());
            try {
                String room = studentController.bookRoom(currentUser, LocalDateTime.of(date, time));
                if (room != null) {
                    BookingLabel.setText("Hai prenotato l'" + room);
                    BookingLabel.setTextFill(Color.web("#0076a3"));
                } else {
                    BookingLabel.setText("Non ci sono posti disponibili nello slot selezionato");
                    BookingLabel.setTextFill(Color.web("#ff0000"));
                }
            } catch (SQLException e) {
                BookingLabel.setText("Errore di connessione al database");
                BookingLabel.setTextFill(Color.web("#ff0000"));
            }catch (Exception e){
                BookingLabel.setText("Problemi con il servizio di mailing");
                BookingLabel.setTextFill(Color.web("#ff0000"));
            }
        }
        else{
            BookingLabel.setText("Seleziona una data prima di prenotare!");
            BookingLabel.setTextFill(Color.web("#ff0000"));
        }
    }

    private LocalTime getTime(String hour){
        int time = 0;
        switch (hour) {
            case "9:00" -> time = 9;
            case "10:00" -> time = 10;
            case "11:00" -> time = 11;
            case "12:00" -> time = 12;
            case "13:00" -> time = 13;
            case "14:00" -> time = 14;
            case "15:00" -> time = 15;
            case "16:00" -> time = 16;
            case "17:00" -> time = 17;
            case "18:00" -> time = 18;
            case "19:00" -> time = 19;
        }
        return LocalTime.of(time,0);
    }

    @FXML
    void logInButtonPressed(MouseEvent event) throws IOException {
        currentUser = usernameField.getText();
        if(loginController.isAnExistingUser(currentUser)) {
            String password = loginController.getUserPassword(currentUser);
            if (Objects.equals(password, passwordField.getText())) {
                if (loginController.getUserType(currentUser) == 2) {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("student-reservation-view.fxml")));

                } else if (loginController.getUserType(currentUser) == 3) {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("teacher-reservation-view.fxml")));
                } else {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admin-view.fxml")));
                }
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }else{
                inexistingUserLabel.setText("Password errata");
                inexistingUserLabel.setTextFill(Color.web("#ff0000"));
            }
        }else{
            inexistingUserLabel.setText("Nome utente inesistente");
            inexistingUserLabel.setTextFill(Color.web("#ff0000"));
        }
    }


    @FXML
    void signInButtonPressed(MouseEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("sign-up-view.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void signInButtonPressed2(MouseEvent event) {
        int type = 3;
        RadioButton button = (RadioButton) ruolo.getSelectedToggle();
        if(Objects.equals(button.getText(), "Studente"))
            type = 2;
        try {
            boolean registration = loginController.addUser(usernameField2.getText(), passwordField2.getText(), instrumentMenu.getSelectionModel().getSelectedItem(), type);
            if(registration){
                signInSuccesful.setText("Registrazione completata!");
                signInSuccesful.setTextFill(Color.web("#0076a3"));
            }else{
                signInSuccesful.setText("Nome utente già in uso!");
                signInSuccesful.setTextFill(Color.web("#ff0000"));
            }

        } catch (SQLException e){
            signInSuccesful.setText("Registrazione fallita!");
            signInSuccesful.setTextFill(Color.web("#ff0000"));
        }
    }

    @FXML
    void backToLogin(MouseEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(instrumentMenu.getItems().isEmpty()) {
            instrumentMenu.getItems().addAll(instruments);
            instrumentMenu.setValue("Violino");
        }
        if(chooseHour.getItems().isEmpty()){
            chooseHour.getItems().addAll(hours);
            chooseHour.setValue("9:00");
        }

        if(chooseHourTeacher.getItems().isEmpty()){
            chooseHourTeacher.getItems().addAll(hours);
            chooseHourTeacher.setValue("9:00");
        }

        if(chooseHourAdmin.getItems().isEmpty()){
            chooseHourAdmin.getItems().addAll(hours);
            chooseHourAdmin.setValue("9:00");
        }

        try {
            UserManager userManager = new UserManager();
            GraphManager graphManager = new GraphManager();
            loginController = new LoginController(userManager);
            studentController = new StudentController(graphManager, userManager);
            teacherController = new TeacherController(graphManager, userManager);
            adminController = new AdminController(graphManager);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



