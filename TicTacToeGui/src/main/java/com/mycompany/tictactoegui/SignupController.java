package com.mycompany.tictactoegui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void handleRegister(ActionEvent event) {
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        try {
            App.setRoot("login");
        } catch (IOException ex) {
            System.getLogger(SignupController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }
    
}
