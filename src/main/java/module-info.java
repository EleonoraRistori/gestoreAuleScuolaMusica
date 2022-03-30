module com.example.gestoreaylescuolamusica {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;


    opens com.example.gestoreaulescuolamusica to javafx.fxml;
    exports com.example.gestoreaulescuolamusica;
}