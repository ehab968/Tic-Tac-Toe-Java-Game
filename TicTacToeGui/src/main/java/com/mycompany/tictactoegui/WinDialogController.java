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

import javafx.fxml.FXML;

public class WinDialogController {

    @FXML
    private Label titleLabel;

    private Label subTitleLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private MediaView mediaView;

    private MediaPlayer mediaPlayer;
    private GameController gameController;
    @FXML
    private Label videoText;
    Random rand;
    @FXML
    private Label opponentName;

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

    public void onBackToMainMenu() {
        close();
        gameController.exitGame();
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
        String path = getClass()
                .getResource("/videos_audios/bravo.mp4")
                .toExternalForm();

        Media media = new Media(path);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        mediaPlayer.play();
    }

    private void close() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Stage stage = (Stage) mediaView.getScene().getWindow();
        stage.close();
    }

    private Runnable Runnable() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
