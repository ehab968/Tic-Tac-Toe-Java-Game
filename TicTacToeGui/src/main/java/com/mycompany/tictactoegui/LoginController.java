/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.LoginData;
import com.iti.group3.tic_tac_toe_shared.Response;
import com.iti.group3.tic_tac_toe_shared.UserData;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;


public class LoginController implements Initializable {
    Socket s;
    ObjectInputStream ear;
    ObjectOutputStream mouth;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private HBox errorBox;
    @FXML
    private Label errorLabel;
    public LoginController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

  
    @FXML
    private void handleLogin(ActionEvent event) {
     
         String userName=usernameField.getText();
         String password =passwordField.getText();
         if (userName.isEmpty() || password.isEmpty()) {
                        System.out.println("Null Fields");
                        showMessage("Please enter username and password", false);
                        return;
                    }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                   
                    LoginData request = new LoginData(userName, password);
                    s=new Socket(InetAddress.getLocalHost(),5005);
                    mouth = new ObjectOutputStream(s.getOutputStream());
                    ear = new ObjectInputStream(s.getInputStream());
                    mouth.writeObject(request);
                    
                    Response<UserData> response = (Response) ear.readObject();
                    
                   
                     if(response.isSuccess()) {
                        showMessage("Welcome " + response.getData().getUserName(), true);
                        System.out.println("Welcome " + response.getData().getUserName());
                        App.setRoot("onLineUsers");
                        
                    } else {
                        showMessage(response.getMessage().toString(), false);
                        System.out.println(response.getMessage());
                    }
                  
                    ear.close();
                    mouth.close();
                    s.close();
                }catch (ConnectException e) {
                    showMessage("Server is not available, try again later", false);
                }  catch (IOException ex) {
                    showMessage("Connection error", false);
                    System.getLogger(LoginController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                } catch (ClassNotFoundException ex) {
                        showMessage("Client-server version mismatch", false);

                    System.getLogger(LoginController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }   }
        }).start();

   

    }
    private void showMessage(String message, boolean success) {
    Platform.runLater(() -> {
    errorLabel.setText(message);
    errorBox.setVisible(true);
    errorBox.setManaged(true);

    if (success) {
        errorBox.setStyle("-fx-background-color: #ecfdf5; -fx-border-color: #a7f3d0;");
        errorLabel.setStyle("-fx-text-fill: #065f46; -fx-font-size: 11; -fx-font-weight: bold;");
    } else {
        errorBox.setStyle("-fx-background-color: #fef2f2; -fx-border-color: #fee2e2;");
        errorLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11; -fx-font-weight: bold;");
    }
    
  
    });
  
}


    @FXML
    private void goToRegister(ActionEvent event) {
        try {
            App.setRoot("signup");
        } catch (IOException ex) {
            System.getLogger(LoginController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
}
