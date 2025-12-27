/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

/**
 *
 * @author mahmo
 */
import com.iti.group3.tic_tac_toe_shared.GameData;
import com.iti.group3.tic_tac_toe_shared.UserData;
import java.util.Random;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class WinDialogController {

    @FXML

    private Label scoreLabel;
    @FXML
    private Region topStrap;
    @FXML
    private Label topTitle;
    @FXML
    private Label descriptionTxt;
    @FXML
    private Label updatedScoreLabel;
    @FXML
    private MediaView mediaView;
    private VideoManager videoManager;

    private GameManager gameController;
    @FXML
    private Label videoText;
    @FXML
    private Label opponentName;
    @FXML
    private StackPane root;

    Random rand;
    GameData game;
    private UserData user;
    private UserData opponent;

    private int totalScore;
    public static WinDialogController instance;

    public void initialize() {
        instance = this;
        rand = new Random();
    }

    public void setgameController(GameManager gameController) {
        this.gameController = gameController;
    }

    public void setGameData(GameData game, UserData user) {
        this.game = game;
        this.user = user;
        if (game.playerX == user) {
            this.opponent = game.playerO;
        } else {
            this.opponent = game.playerX;
        }
    }

    @FXML
    private void onCloseButton(ActionEvent event) {
        onBackToMainMenu();
    }

    public void onBackToMainMenu() {
        close();
        PrimaryController.getInstance().exitGame();
    }

    public void onPlayAgain() {
        close();
        PrimaryController.getInstance().restartGame();
    }

    public void playVideo() {
        if (game.winner == user) {
            showWinningDialog();
        } else {
            showLosingDialog();
        }
    }

    private void showWinningDialog() {
        topTitle.setText("Winner 🎉🎉");
        descriptionTxt.setText("You won against");
        opponentName.setText(opponent.getUserName());
        scoreLabel.setText(Integer.toString(user.getScore()));

        videoText.setText(getRandomCongratsText());
        Platform.runLater(() -> {
            videoManager = new VideoManager("/videos_audios/bravo.mp4", mediaView);
        });
    }

    private void showLosingDialog() {
        topStrap.setStyle("-fx-background-color: red;");
        topTitle.setText("Lost!!");
        descriptionTxt.setText("You Lost against ");
        opponentName.setText(opponent.getUserName());
        descriptionTxt.setTextFill(Color.RED);

        updatedScoreLabel.setText("-20");
        updatedScoreLabel.setTextFill(Color.RED);
        scoreLabel.setText(Integer.toString(user.getScore()));

        videoText.setText(getRandomLosingText());
        Platform.runLater(() -> {
            videoManager = new VideoManager("/videos_audios/losing.mp4", mediaView);
        });
    }

    public void close() {
        videoManager.stop();
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    private String getRandomCongratsText() {
        String userName = user.getUserName();
        String[] congrats = {
            "عاش يا لعييييب " + userName,
            "Player " + userName + " is Just Amazing",
            "عاجل اللاعب " + userName + "فاز بالمباراة "
        };

        return congrats[rand.nextInt(congrats.length)];
    }

    private String getRandomLosingText() {
        String userName = user.getUserName();
        String[] congrats = {
            "خييبه" + userName
        };

        // return congrats[rand.nextInt(congrats.length)];
        return "";
    }
}
