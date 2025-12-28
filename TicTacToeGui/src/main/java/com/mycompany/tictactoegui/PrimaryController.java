package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.Request;
import com.iti.group3.tic_tac_toe_shared.RequestType;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
    GameManager gc;
    OnlineGameManager ogc;

    public static PrimaryController getInstance() {
        return instance;
    }

    public OnlineGameManager getOnlineGameController() {
        return ogc;
    }

    public void setRecordPath(String recordPath) {
        System.out.println("setting recordPath= " + recordPath);
        if (recordPath != null && gc != null) {
            gc.playRecord(recordPath);
        }
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
            if (ClientSocket.user != null) {
                userNameText.setText(ClientSocket.user.getUserName());
            }

            gc = new GameManager(gamePane, gridPane);

            gc.setModeType(modeType);
            gc.setDifficultyLevel(difficulty);
            gc.setOnUserChanged((currentPlayer) -> {
                onUserChange(currentPlayer);
            });

            gc.setOnUserWinning((currentPlayer) -> {
                onUserWinning(currentPlayer);
            });

            restartGameButton.setOnAction((action) -> {
                restartGame();
            });

            gc.setOnScoreChanged((uScore, oScore) -> {
                userScoreText.setText("Score: " + uScore);
                opponentScoreText.setText("Score: " + oScore);
                if (uScore == 0 && oScore == 0) {
                    restartGameButton.setText("Restart the round");
                } else {
                    restartGameButton.setText("continue");
                }

            });

        } else {
            ogc = new OnlineGameManager(gamePane, gridPane);
            setPlayersNames(ogc.getUserData().getUserName(), ogc.getOpponentData().getUserName());
            userScoreText.setText("Score: " + ogc.getUserData().getScore());
            opponentScoreText.setText("Score: " + ogc.getOpponentData().getScore());
            onUserChange('x');
            ogc.setOnUserChanged((currentPlayer) -> {
                onUserChange(currentPlayer);
            });

            ogc.setOnUserWinning((currentPlayer) -> {
                onUserWinning(currentPlayer);
            });

            restartGameButton.setOnAction((action) -> {
                restartGame();
            });
        }
    }

    public void updateRestartButtonText(String text) {
        Platform.runLater(() -> {
            restartGameButton.setText(text);
            System.out.println("Button text updated!");
        });
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
        exitGame();
    }

    public void restartGame() {
        if (isOnline) {
            if (ogc.isWin || ogc.allCellsFull()) {
                try {
                    ClientStreamSocket.write(new Request(RequestType.RESTART_GAME, null));
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                restartGameButton.setText("Restart the round");
            } else {
                showAlert("restart error", "the game doesn't finish yet");
            }
        } else {
            gc.restartGame();
            restartGameButton.setText("Restart the round");
        }
    }

    public void exitGame() {
        if (isOnline) {
            if (ogc.isWin || ogc.allCellsFull()) {
                try {
                    ClientStreamSocket.write(new Request(RequestType.END_GAME, null));
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                ClientStreamSocket.isInGame = false;
            } else {
                showAlert("no exit", "finish the game first and exit");
            }
        } else {
            gc.exitGame();
            ClientStreamSocket.isInGame = false;
        }
    }

    public void setDifficultyLevel(int difficulty) {
        this.difficulty = difficulty;
        System.out.println("difficulty" + difficulty);

        switch (difficulty) {
            case 1:
                opponentNameText.setText("CPU_Easy");
                break;
            case 2:
                opponentNameText.setText("CPU_MEDIUM");
                break;
            case 3:
                opponentNameText.setText("CPU_Hard");
                break;
        }
        if (gc != null) {
            gc.setDifficultyLevel(difficulty);
        }
    }

    public void setModeType(int modeType) {
        this.modeType = modeType;

        if (gc != null) {
            gc.setModeType(modeType);
        }
        ClientStreamSocket.isInGame = false;
        gc.exitGame();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
