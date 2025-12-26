/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Ahmed Sayed
 */
public class GitIpController {

    @FXML
    private BorderPane gamePane;
    @FXML
    private TextField addIpField;

    public static String SERVER_IP;
    
    
    @FXML
    private void addServerIp(ActionEvent event) {
        String ip = addIpField.getText().trim();

        if (ip.isEmpty()) {
            showAlert("Error", "Please enter IP address");
            return;
        }

        if (!isValidIp(ip)) {
            showAlert("Invalid IP", "Please enter a valid IPv4 address");
            return;
        }

        SERVER_IP = ip;
        System.out.println("Server IP saved: " + SERVER_IP);

        try {
            App.setRoot("login");
        } catch (IOException ex) {
            System.getLogger(HomeController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }


    private boolean isValidIp(String ip) {
        String ipRegex =
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
            "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";
        return ip.matches(ipRegex);
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}


