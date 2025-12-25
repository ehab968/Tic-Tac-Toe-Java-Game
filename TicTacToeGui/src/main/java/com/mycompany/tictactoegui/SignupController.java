package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.AuthData;
import com.iti.group3.tic_tac_toe_shared.Request;
import com.iti.group3.tic_tac_toe_shared.RequestType;
import com.iti.group3.tic_tac_toe_shared.Response;
import com.iti.group3.tic_tac_toe_shared.ResponseType;
import com.iti.group3.tic_tac_toe_shared.UserData;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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

        AuthData loginData = new AuthData(userName, password);
        new Thread(() -> {
            try {

                Request request = new Request(RequestType.REGISTER, loginData);
                ClientSocket.write(request);
                Response<UserData> response = (Response<UserData>) ClientSocket.read();
                if (response.isSuccess()) {
                    showAlert("Success", "Account created successfully!");
                    clearFields();
                    App.setRoot("login");
                } else {
                    if (response.getMessage() == ResponseType.USERNAME_EXISTS) {
                        showAlert("Error", "Sorry This account is used before");
                        System.out.println("-------------------------------");
                        System.out.println("********************************");
                    } else {
                        showAlert("Error", "Registration failed");
                    }
                }
            } catch (IOException ex) {
                System.getLogger(SignupController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (ClassNotFoundException ex) {
                System.getLogger(SignupController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }).start();
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                usernameField.clear();
                passwordField.clear();
                confirmPasswordField.clear();
            }
        }
        );
    }

    private void showAlert(String title, String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(msg);
                alert.showAndWait();
            }
        }
        );
    }

    @FXML
    public void onBackPressed() {
        Platform.runLater(() -> {
            try {
                App.setRoot("home");
            } catch (IOException ex) {
                System.getLogger(SignupController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
        );
    }

}
