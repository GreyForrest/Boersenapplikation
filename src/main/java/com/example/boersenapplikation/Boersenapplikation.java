package com.example.boersenapplikation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Boersenapplikation extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Boersenapplikation.class.getResource("anmelde_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Anmelden");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}