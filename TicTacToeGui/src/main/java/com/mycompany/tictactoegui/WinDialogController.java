/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

/**
 *
 * @author mahmo
 */
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import java.util.Random;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class WinDialogController {

    @FXML
    private Label titleLabel;

    private Label subTitleLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private MediaView mediaView;
    private VideoManager videoManager;

    private GameController gameController;
    @FXML
    private Label videoText;
    Random rand;
    @FXML
    private Label opponentName;
    @FXML
    private StackPane root;

    public void initialize() {
        rand = new Random();
    }

    public void setgameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setWinner(char winner) {
        //subTitleLabel.setText("Player " + winner + " won the game");
        videoText.setText(getRandomCongratsText(winner));
    }

    @FXML
    private void onCloseButton(ActionEvent event) {
        onBackToMainMenu();
    }

    public void onBackToMainMenu() {
        close();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gameController.exitGame();
            }
        });

    }

    public void onPlayAgain() {
        close();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gameController.restartGame();
            }
        });

    }

    public void playVideo() {
        videoManager = new VideoManager("/videos_audios/bravo.mp4", mediaView);
    }

    private void close() {
        videoManager.stop();
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    private String getRandomCongratsText(char winner) {
        String[] congrats = {
            "عاش يا لعييييب " + winner,
            "Player " + winner + " is Just Amazing",
            "عاجل اللاعب " + winner + "فاز بالمباراة"
        };

        return congrats[rand.nextInt(congrats.length)];
    }
}
