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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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

    @FXML
    private Circle statusCircle;
    @FXML
    private Label statusLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (ClientSocket.user == null) {
            setOnline(false);
        } else {
            setOnline(true);
        }

    }

    public void setOnline(boolean isOnline) {
       
        if (isOnline) {
            statusLabel.setText("Online");
            statusCircle.setFill(Color.web("#22e822"));
        } else {
            statusLabel.setText("Offline");
            statusCircle.setFill(Color.RED);
        }
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
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/primary.fxml"));
        Parent root = loader.load();
        PrimaryController controller = loader.getController();

        App.getScene().setRoot(root);

        Stage primaryStage = (Stage) App.getScene().getWindow();

        boolean proceed = controller.gc.showChosseScoreDialog();

        if (!proceed) {
            FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            Parent homeRoot = homeLoader.load();
            App.getScene().setRoot(homeRoot);
        }

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

    @FXML
    private void onPlayRecord(ActionEvent event) {
        GameRecorder gameRecorder = new GameRecorder();
        gameRecorder.showCustomRecordPicker();
    }
}
