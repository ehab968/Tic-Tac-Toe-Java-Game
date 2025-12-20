package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.Command;
import com.iti.group3.tic_tac_toe_shared.CommandType;
import com.iti.group3.tic_tac_toe_shared.UserData;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

public class SignupController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    @Override
    public void initialize(URL url, ResourceBundle rb) { }

    @FXML
    private void handleRegister(ActionEvent event) {
        String userName = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Validation Error", "All fields are required");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showAlert("Password Validation Error", "Password Doesn't match");
            return;
        }

        UserData us = new UserData(userName, password, 0, 0, 0, 0, 0);

        try {
            socket = new Socket("localhost", 5005);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            
            out.writeObject(us);
            out.flush();
            Object response = in.readObject();
            Command cmd = (Command) response;
            if (cmd.getType() == CommandType.REGISTER_SUCCESS) {
                showAlert("Success", "Account created successfully!");
                clearFields();
                out.close();
                in.close();
                socket.close();
                App.setRoot("login");
            } else if (cmd.getType() == CommandType.USERNAME_EXISTS) {
                showAlert("Error", "Sorry This account is used before");
                System.out.println("-------------------------------");
                System.out.println("********************************");
            } else {
                showAlert("Error", "Registration failed");
            }
            
        } catch (IOException ex) {
            System.getLogger(SignupController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (ClassNotFoundException ex) {
            System.getLogger(SignupController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    @FXML
    private void goToLogin(ActionEvent event) {
        try {
            App.setRoot("login");
        } catch (IOException ex) {
            System.getLogger(SignupController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
