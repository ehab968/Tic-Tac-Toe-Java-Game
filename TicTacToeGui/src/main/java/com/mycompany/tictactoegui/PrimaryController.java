package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.Request;
import com.iti.group3.tic_tac_toe_shared.RequestType;
import java.io.IOException;
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
    @FXML
    private Text userScoreText;
    @FXML
    private Text opponentScoreText;

    private int modeType;
    private int difficulty;

    private static PrimaryController instance;
    GameController gc;
    OnlineGameController ogc;

    public static PrimaryController getInstance() {
        return instance;
    }

    public OnlineGameController getOnlineGameController() {
        return ogc;
    }

    private void setPlayersNames(String userName, String opponentName) {
        userNameText.setText(userName);
        opponentNameText.setText(opponentName);
    }
    static boolean isOnline = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        if (isOnline == false) {
            gc = new GameController(gamePane, gridPane);
            gc.setModeType(modeType);
            gc.setDifficultyLevel(difficulty);
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
        } else {
            ogc = new OnlineGameController(gamePane, gridPane);
            setPlayersNames(ogc.getUserData().getUserName(), ogc.getOpponentData().getUserName());
            userScoreText.setText("Score: " + ogc.getUserData().getScore());
            opponentScoreText.setText("Score: " + ogc.getUserData().getScore());

            ogc.setOnUserChanged((currentPlayer) -> {
                onUserChange(currentPlayer);
            });

            ogc.setOnUserWinning((currentPlayer) -> {
                onUserWinning(currentPlayer);
            });

            restartGameButton.setOnAction((action) -> {
                try {
                    ClientStreamSocket.write(new Request(RequestType.RESTART_GAME, null));
                } catch (IOException ex) {
                    System.getLogger(PrimaryController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                } catch (ClassNotFoundException ex) {
                    System.getLogger(PrimaryController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
                restartGameButton.setText("Restart the round");
            });
        }
    }

    public void updateScoreUI(int userScore, int opponentScore) {
        userScoreText.setText("Score: " + userScore);
        opponentScoreText.setText("Score: " + opponentScore);
    }

    private void onUserChange(char currentPlayer) {

        if (isOnline) {
            if (ogc.isMyTurn()) {
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
        } else {
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

    }

    private void onUserWinning(char winnerSymbol) {
        if (isOnline) {
            boolean iWin = (winnerSymbol == ogc.getMySymbol());

            if (iWin) {
                userStatusText.setText("WON 🎉🎉");
                userStatusText.setFill(Color.GREEN);
                opponentStatusText.setText("LOST");
                opponentStatusText.setFill(Color.RED);
            } else {
                userStatusText.setText("LOST");
                userStatusText.setFill(Color.RED);
                opponentStatusText.setText("WON 🎉🎉");
                opponentStatusText.setFill(Color.GREEN);
            }
        } else {
            if (winnerSymbol == 'X') {
                userStatusText.setText("WON 🎉🎉");
                userStatusText.setFill(Color.GREEN);
                opponentStatusText.setText("LOST");
                opponentStatusText.setFill(Color.RED);
            } else {
                userStatusText.setText("LOST");
                userStatusText.setFill(Color.RED);
                opponentStatusText.setText("WON 🎉🎉");
                opponentStatusText.setFill(Color.GREEN);
            }
        }
    }

    @FXML
    private void onExitPressed(ActionEvent event) {
        if (isOnline) {
            try {
                ClientStreamSocket.write(new Request(RequestType.END_GAME, null));
            } catch (IOException ex) {
                System.getLogger(PrimaryController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (ClassNotFoundException ex) {
                System.getLogger(PrimaryController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        } else {
            gc.exitGame();
        }
    }

    public void setDifficultyLevel(int difficulty) {
        this.difficulty = difficulty;
        System.out.println("difficulty" + difficulty);

        if (gc != null) {
            gc.setDifficultyLevel(difficulty);
        }
    }

    public void setModeType(int modeType) {
        this.modeType = modeType;

        if (gc != null) {
            gc.setModeType(modeType);
        }
    }

}
