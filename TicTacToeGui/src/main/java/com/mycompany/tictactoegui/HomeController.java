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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mahmo
 */
public class HomeController implements Initializable {

    @FXML
    private ImageView userAvater2;
    @FXML
    private Button singlePlayerBTN;
    @FXML
    private Button multiplayerBTN;
    @FXML
    private Button onlineBTN;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void onSelectSinglePlayer(ActionEvent event) {
        PrimaryController.isOnline = false;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chooseDifficulty.fxml"));
            Parent root = loader.load();
            ChooseDifficultyController controller = loader.getController();
            Scene currentScene = ((Node) event.getSource()).getScene();

            controller.setModeType(1);
            controller.setPreviousRoot(((Node) event.getSource()).getScene().getRoot());
            Stage stage = (Stage) currentScene.getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            System.getLogger(HomeController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void onSelectMultiplayer(ActionEvent event) {
        PrimaryController.isOnline = false;
        try {
            App.setRoot("primary");
        } catch (IOException ex) {
            System.getLogger(HomeController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void onSelectOnline(ActionEvent event) {
        try {
            if (ClientSocket.user == null) {
                App.setRoot("login");
            } else {
                App.setRoot("onLineUsers");
            }
        } catch (IOException ex) {
            System.getLogger(HomeController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

}
