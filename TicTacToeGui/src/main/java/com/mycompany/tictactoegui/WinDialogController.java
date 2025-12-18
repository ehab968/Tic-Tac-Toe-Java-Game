/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

/**
 *
 * @author mahmo
 */
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import javafx.fxml.FXML;

public class WinDialogController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label subTitleLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private MediaView mediaView;

    private MediaPlayer mediaPlayer;

    public void initialize() {

    }

    public void setWinner(char winner) {
        subTitleLabel.setText("Player " + winner + " won the game");
    }

    public void onBackToMainMenu() {
        close();
    }

    public void onPlayAgain() {
        close();
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
}
