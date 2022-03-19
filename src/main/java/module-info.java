module com.example.boersenapplikation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires YahooFinanceAPI;


    opens com.example.boersenapplikation to javafx.fxml;
    exports com.example.boersenapplikation;
    exports com.example.boersenapplikation.controller;
    opens com.example.boersenapplikation.controller to javafx.fxml;
}