package com.example.boersenapplikation.controller;

import com.example.boersenapplikation.Boersenapplikation;
import com.example.boersenapplikation.Messages;
import com.example.boersenapplikation.Shareholder;
import com.example.boersenapplikation.Sql;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AnmeldeController {
    @FXML
    private Label loginTitleLbl;
    @FXML
    private Button loginBtn;
    @FXML
    private Button switchLoginBtn;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField repeatPasswordField;

    Sql sql = new Sql();

    public void onLoginBtnClick(ActionEvent actionEvent) throws IOException {
        sql.createTableIfNotExists();
        boolean canNextWindowBeOpened = true;
        Shareholder shareholder = new Shareholder(usernameField.getText(), passwordField.getText());
        if (loginTitleLbl.getText().equals("Registrieren")) {
            if (sql.checkIfUsernameIsAvailable(usernameField.getText())) {
                if (passwordField.getText().equals(repeatPasswordField.getText())) {
                    shareholder.registerShareholder(repeatPasswordField.getText());
                } else {
                    new Messages().getErrorMessage("Die Passwörter stimmen nicht überein.");
                    canNextWindowBeOpened = false;
                }
            } else {
                new Messages().getErrorMessage("Dieser Username ist nicht verfügbar. Bitte versuchen Sie einen anderen Username und\n" +
                        "stellen Sie sicher, dass Sie keinen Leerschlag benutzen und der Username 3 oder mehr Zeichen beträgt.");
                canNextWindowBeOpened = false;
            }
        } else {
            canNextWindowBeOpened = shareholder.loginShareholder();
        }

        if (canNextWindowBeOpened) {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Boersenapplikation.class.getResource("uebersicht_view.fxml"));
            Parent root = fxmlLoader.load();
            UebersichtController controller = fxmlLoader.getController();
            controller.setUpController(shareholder);
            stage.setTitle("Übersicht");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

        }

    }

    public void onSwitchLoginBtnClick(ActionEvent actionEvent) {
        if (loginTitleLbl.getText().equals("Registrieren")) {
            switchLoginBtn.setText("Noch kein Konto?");
            loginTitleLbl.setText("Login");
            loginBtn.setText("Login");
            usernameField.clear();
            passwordField.clear();
            repeatPasswordField.clear();
            repeatPasswordField.setVisible(false);
        } else {
            switchLoginBtn.setText("Bereits ein Konto?");
            loginBtn.setText("Registrieren");
            loginTitleLbl.setText("Registrieren");
            usernameField.clear();
            passwordField.clear();
            repeatPasswordField.setVisible(true);
        }
    }
}