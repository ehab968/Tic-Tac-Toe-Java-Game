/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.LoginData;
import com.iti.group3.tic_tac_toe_shared.LoginResponse;
import com.iti.group3.tic_tac_toe_shared.UserData;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 *
 * @author COMPUMARTS
 */
public class LoginController implements Initializable {
    Socket s;
    ObjectInputStream ear;
    ObjectOutputStream mouth;
     @FXML
    private Button loginButton;
    @FXML
    private TextField UserNameTextFiled;
    @FXML
    private TextField PasswordTextFiled;

     
    public LoginController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }

    @FXML
    private void Login(ActionEvent event) {
        loginButton.setDisable(true);

        new Thread(()->{
                
         try {
             String userName=UserNameTextFiled.getText();
             String password =PasswordTextFiled.getText();
             LoginData request = new LoginData(userName, password);
             s=new Socket(InetAddress.getLocalHost(),5005);
             mouth = new ObjectOutputStream(s.getOutputStream());
             ear = new ObjectInputStream(s.getInputStream());
             mouth.writeObject(request);
             
           LoginResponse response = (LoginResponse) ear.readObject();

            if(response.isSuccess()) {
                System.out.println("Welcome " + response.getUser().getName());
            } else {
                System.out.println(response.getMessage());
            }
               System.out.println("Login response is "+response);
               
            ear.close();
            mouth.close();
            s.close();
         } catch (IOException ex) {
             System.getLogger(LoginController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
         } catch (ClassNotFoundException ex) {
            System.getLogger(LoginController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        }).start();

        loginButton.setDisable(true);

    }

      
}
