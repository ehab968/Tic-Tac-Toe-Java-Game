package com.mycompany.tictactoegui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class PrimaryController implements Initializable {

    @FXML
    private Pane gamePane;
    @FXML
    private GridPane gridPane;
    @FXML
    private Text userNameText;
    @FXML
    private Text userStatusText;
    @FXML
    private Text opponentNameText;
    @FXML
    private Text opponentStatusText;
    @FXML
    private Button restartGameButton;

    GameController gc;
    @FXML
    private Text userScoreText;
    @FXML
    private Text opponentScoreText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gc = new GameController(gamePane, gridPane);

        gc.setOnUserChanged((currentPlayer) -> {
            onUserChange(currentPlayer);
        });

        gc.setOnUserWinning((currentPlayer) -> {
            onUserWinning(currentPlayer);
        });

        restartGameButton.setOnAction((action) -> {
            gc.restartGame();
            restartGameButton.setText("Restart the round");
        });

        gc.setOnScoreChanged((uScore, oScore) -> {
            userScoreText.setText("Score: " + uScore);
            opponentScoreText.setText("Score: " + oScore);
            restartGameButton.setText("Continue");

        });

    }

    private void onUserChange(char currentPlayer) {
        System.out.println("Player changed to " + currentPlayer);

        if (currentPlayer == 'X') {
            userStatusText.setText("Your Turn");
            userStatusText.setFill(Color.GREEN);
            opponentStatusText.setText("Waiting...");
            opponentStatusText.setFill(Color.RED);
        } else {
            userStatusText.setText("Waiting...");
            userStatusText.setFill(Color.RED);
            opponentStatusText.setText("Playing...");
            opponentStatusText.setFill(Color.GREEN);
        }
    }

    private void onUserWinning(char currentPlayer) {
        if (currentPlayer == 'X') {
            userStatusText.setText("WON🎉🎉");
            userStatusText.setFill(Color.GREEN);
            opponentStatusText.setText("LOST");
            opponentStatusText.setFill(Color.RED);
        } else {
            userStatusText.setText("LOST");
            userStatusText.setFill(Color.RED);
            opponentStatusText.setText("WON🎉🎉");
            opponentStatusText.setFill(Color.GREEN);
        }
    }

    @FXML
    private void onExitPressed(ActionEvent event) {
        gc.exitGame();
    }

}
