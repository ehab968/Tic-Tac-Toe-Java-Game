/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoegui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author COMPUMARTS
 */
public class OnlineUsersController implements Initializable {

    @FXML
    private VBox userListContainer;
    @FXML
    private Button leaderBoardButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }

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
        if (status.equals("Playing...")) {
            statusLabel.setTextFill(Color.ORANGE);
        } else if (status.equals("Online")) {
            statusLabel.setTextFill(Color.GREEN);
        } else {
            statusLabel.setTextFill(Color.YELLOW);
        }

        vbox.getChildren().addAll(nameLabel, statusLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btn = new Button();
        if (canInvite) {
            btn.setText("Invite ⚔");
            btn.setStyle("-fx-background-color:#2b7cee; -fx-text-fill:white; -fx-background-radius:20;");
        } else {
            btn.setText("Spectate 👀");
            btn.setStyle("-fx-background-color:#f3f4f6; -fx-text-fill:#4b5563; -fx-background-radius:20;");
        }

        hbox.getChildren().addAll(vbox, spacer, btn);

        userListContainer.getChildren().add(hbox);
    }

    @FXML
    private void navToLeaderBoard(ActionEvent event) {
         try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/leaderBoard.fxml")
            );
            Parent root = loader.load();
            
            LeaderBoardController controller = loader.getController();
            
            Scene currentScene = ((Node) event.getSource()).getScene();
            controller.setPreScene(currentScene);
            
            Stage stage = (Stage) currentScene.getWindow();
            stage.setScene(new Scene(root));
            controller.setPreScene(currentScene);

        } catch (IOException ex) {
            System.getLogger(OnlineUsersController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }

    @FXML
    private void backToOnlinePage(ActionEvent event) {
        try {
            App.setRoot("home");
        } catch (IOException ex) {
            System.getLogger(HomeController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

}
