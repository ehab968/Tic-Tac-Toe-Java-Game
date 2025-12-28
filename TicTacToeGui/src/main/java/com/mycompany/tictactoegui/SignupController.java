package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.AuthData;
import com.iti.group3.tic_tac_toe_shared.Request;
import com.iti.group3.tic_tac_toe_shared.RequestType;
import com.iti.group3.tic_tac_toe_shared.Response;
import com.iti.group3.tic_tac_toe_shared.ResponseType;
import com.iti.group3.tic_tac_toe_shared.UserData;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class SignupController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private HBox errorBox;
    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String userName = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(false, "All fields are required");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showAlert(false, "Password Doesn't match");
            return;
        }

        AuthData loginData = new AuthData(userName, password);
        new Thread(() -> {
            try {

                Request request = new Request(RequestType.REGISTER, loginData);
                ClientSocket.write(request);
                Response<UserData> response = (Response<UserData>) ClientSocket.read();
                if (response.isSuccess()) {
                    showAlert(true, "Account created successfully!");
                    clearFields();
                    App.setRoot("login");
                } else {
                    if (response.getMessage() == ResponseType.USERNAME_EXISTS) {
                        showAlert(false, "Sorry This account is used before");
                        System.out.println("-------------------------------");
                        System.out.println("********************************");
                    } else {
                        showAlert(false, "Registration failed");
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

    private void showAlert(boolean success, String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                errorLabel.setText(msg);
                errorBox.setVisible(true);
                errorBox.setManaged(true);

                if (success) {
                    errorBox.setStyle("-fx-background-color: #ecfdf5; -fx-border-color: #a7f3d0;");
                    errorLabel.setStyle("-fx-text-fill: #065f46; -fx-font-size: 11; -fx-font-weight: bold;");
                } else {
                    errorBox.setStyle("-fx-background-color: #fef2f2; -fx-border-color: #fee2e2;");
                    errorLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11; -fx-font-weight: bold;");
                }

            }
        }
        );
    }

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
