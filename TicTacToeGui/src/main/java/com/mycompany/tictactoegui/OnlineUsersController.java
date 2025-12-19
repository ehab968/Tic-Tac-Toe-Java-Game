/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoegui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author COMPUMARTS
 */
public class OnlineUsersController implements Initializable {

    @FXML
    private VBox userListContainer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @FXML

public void addUser(String username, String status, boolean canInvite) {
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.CENTER_LEFT);
    hbox.setSpacing(10);
    hbox.setStyle("-fx-background-radius: 15; -fx-padding: 10; -fx-border-color: transparent;");

    VBox vbox = new VBox(2);
    Label nameLabel = new Label(username);
    nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #111827;");
    Label statusLabel = new Label(status);
    statusLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
    if(status.equals("Playing...")) statusLabel.setTextFill(Color.ORANGE);
    else if(status.equals("Online")) statusLabel.setTextFill(Color.GREEN);
    else statusLabel.setTextFill(Color.YELLOW);

    vbox.getChildren().addAll(nameLabel, statusLabel);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Button btn = new Button();
    if(canInvite) {
        btn.setText("Invite ⚔");
        btn.setStyle("-fx-background-color:#2b7cee; -fx-text-fill:white; -fx-background-radius:20;");
    } else {
        btn.setText("Spectate 👀");
        btn.setStyle("-fx-background-color:#f3f4f6; -fx-text-fill:#4b5563; -fx-background-radius:20;");
    }

    hbox.getChildren().addAll(vbox, spacer, btn);

    userListContainer.getChildren().add(hbox);
}

       
}
