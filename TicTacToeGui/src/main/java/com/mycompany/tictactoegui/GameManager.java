package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.GameData;
import com.iti.group3.tic_tac_toe_shared.GameMove;
import com.iti.group3.tic_tac_toe_shared.UserData;
import com.mycompany.tictactoegui.interfaces.OnUserEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.BiConsumer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameManager {

    protected GridPane gridPane;
    protected Pane gamePane;
    protected OnUserEvent onUserChanged;
    protected OnUserEvent onUserWinning;
    protected char[] charMatrix = new char[9];
    protected StackPane[] stackCells = new StackPane[9]; // => كل ستاك بان هنا ريفرنس للسيل اللى موجودة فى الجريد بان
    protected boolean isWin = false;
    protected char currentPlayer;
    static int choosenScore;
    private int userScore = 0;
    private int opponentScore = 0;
    private BiConsumer<Integer, Integer> onScoreChanged;
    protected GameData game;
    private int modeType;
    private int difficulty;
    SingleMode singleMode;

    ArrayList<GameMove> gameMoves;
    protected int actionOrder;
    boolean isWantToSaveRecord;
    GameRecorder gameRecorder;

    public int getUserScore() {
        return userScore;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public GameManager() {
    }

    public GameManager(Pane gamePane, GridPane gridPane) {

        this.gridPane = gridPane;
        this.gamePane = gamePane;
        gameMoves = new ArrayList(0);
        actionOrder = 0;
        currentPlayer = 'X';
        UserData user = new UserData("Player X");
        if (ClientSocket.user != null) {
            user = ClientSocket.user;
        }
        UserData opponent = new UserData("Player O");
        String gameId = UUID.randomUUID().toString();
        game = new GameData(gameId, user, opponent, null);
        singleMode = new SingleMode(charMatrix, stackCells, this);

        initiateGrid();
    }

    protected void initiateGrid() {

        int cellId = -1;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                cellId++;
                StackPane cell = new StackPane();
                stackCells[cellId] = cell;
                cell.setPrefSize(120, 120);
                cell.setId(Integer.toString(cellId));
                cell.setOnMouseClicked((MouseEvent e) -> {
                    System.out.println("Clicking");
                    e.consume();
                    handleCellPress(cell);
                });
                gridPane.add(cell, col, row);
            }
        }
    }

    public void handleCellPress(StackPane cell) {
        if (!isWin) {
            doClickSound(currentPlayer);

            addShapeToCell(cell, currentPlayer);
            int cellId = Integer.parseInt(cell.getId());
            charMatrix[cellId] = currentPlayer;
            cell.setDisable(true);

            checkWin();
            if (!isWin) {
                changePlayer();
            }
        }
    }

    protected void doClickSound(char symbol) {
        if (symbol == 'X') {
            SoundPlayer.PlayerXClick();
        } else if (symbol == 'O') {
            SoundPlayer.playerOClick();
        }
    }

    public void setOnUserChanged(OnUserEvent listener) {
        this.onUserChanged = listener;
    }

    public void setOnUserWinning(OnUserEvent listener) {
        this.onUserWinning = listener;
    }

    public void addShapeToCell(StackPane cell, char player) {
        UserData movePlayer;
        if (player == 'X') {
            XShape x = new XShape(45);
            cell.getChildren().add(x);
            movePlayer = game.playerX;
        } else {
            OShape o = new OShape(15);
            cell.getChildren().add(o);
            movePlayer = game.playerO;
        }

        actionOrder++;
        GameMove gameMove = new GameMove(Integer.parseInt(cell.getId()), player, actionOrder, movePlayer);
        gameMoves.add(gameMove);
    }

    public void changePlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';

        if (onUserChanged != null) {
            onUserChanged.handle(currentPlayer);
        }

        if (modeType == 1 && currentPlayer == 'O') {
            switch (difficulty) {
                case 1 ->
                    singleMode.computerRoleEasy();
                case 2 ->
                    singleMode.computerRoleMeduim();
                case 3 ->
                    singleMode.computerRoleHard();
            }
        }

    }

    public void restartGame() {
        System.out.println("restart");
        gamePane.getChildren().removeIf(n -> n instanceof Line);
        isWin = false;
        actionOrder = 0;

        if (gameRecorder != null) {
            gameRecorder.stopPlayback();
        }
        for (int i = 0; i < 9; i++) {
            charMatrix[i] = '\0';
            StackPane cell = stackCells[i];
            cell.getChildren().clear();
            cell.setDisable(false);
        }

        currentPlayer = 'X';
        if (onUserChanged != null) {
            onUserChanged.handle(currentPlayer);
        }
        if (onScoreChanged != null) {
            onScoreChanged.accept(userScore, opponentScore);
        }
        if (WinDialogController.instance != null) {
            WinDialogController.instance.close();
        }
    }

    public void startNewGame() {

        userScore = opponentScore = 0;
        restartGame();

        if (onScoreChanged != null) {
            onScoreChanged.accept(userScore, opponentScore);
        }

        currentPlayer = 'X';
        if (onUserChanged != null) {
            onUserChanged.handle(currentPlayer);
        }
    }

    public void exitGame() {
        if (gameRecorder != null) {
            gameRecorder.stopPlayback();
        }

        try {
            App.setRoot("home");
        } catch (IOException ex) {
            System.getLogger(PrimaryController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    /*
        0   1   2
        3   4   5
        6   7   8
     */
    public void checkWin() {
        int[][] wins = {
            // Horizontal
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            // Vertical
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            // Diagonal
            {0, 4, 8}, {2, 4, 6}
        };

        for (int[] line : wins) {
            //{0, 1, 2}
            char first = charMatrix[line[0]];
            char second = charMatrix[line[1]];
            char third = charMatrix[line[2]];

            if (first != '\0' && first == second && second == third) {
                onUserWon(line);
                return;
            }
        }
        return;
    }

    protected void drawWinningLine(int[] winIndex) {
        //wait until the UI is stable to get the correct X and Y
        gridPane.applyCss();
        gridPane.layout();

        StackPane _1stCell = stackCells[winIndex[0]];
        StackPane _3rdCell = stackCells[winIndex[2]];

        Bounds b1 = _1stCell.localToScene(_1stCell.getBoundsInLocal());
        Bounds b3 = _3rdCell.localToScene(_3rdCell.getBoundsInLocal());
        Point2D p1 = gamePane.sceneToLocal(b1.getCenterX(), b1.getCenterY());
        Point2D p3 = gamePane.sceneToLocal(b3.getCenterX(), b3.getCenterY());
        Line line = new Line(p1.getX(), p1.getY(), p3.getX(), p3.getY());
        line.setStrokeWidth(6);

        if (charMatrix[winIndex[0]] == 'X') {
            line.setStyle("-fx-stroke: red;");
        } else {
            line.setStyle("-fx-stroke: blue;");
        }
        gamePane.getChildren().add(line);
    }

    public void onUserWon(int[] winningLine) {
        isWin = true;
        drawWinningLine(winningLine);

        if (currentPlayer == 'X') {
            userScore++;
            game.setWinner(game.playerX);
            userWonWholeGame(userScore);
        } else {
            opponentScore++;
            game.setWinner(game.playerO);
            userWonWholeGame(opponentScore);

        }
        if (onScoreChanged != null) {
            onScoreChanged.accept(userScore, opponentScore);
        }

        if (onUserWinning != null) {
            onUserWinning.handle(currentPlayer);
        }
        if (isWantToSaveRecord) {
            saveRecord();
        }
    }

    private void userWonWholeGame(int userScore) {
        if (userScore > choosenScore / 2) {
            PauseTransition pause = new PauseTransition(Duration.seconds(.5));
            pause.setOnFinished(event -> {
                showWinningDialog(game);

                PrimaryController.getInstance().updateRestartButtonText("Restart the round");
            });
            pause.play();

        }
    }

    public int getChoosenScore() {
        return choosenScore;
    }

    public void setOnScoreChanged(BiConsumer<Integer, Integer> listener) {
        this.onScoreChanged = listener;
    }

    protected void showWinningDialog(GameData game) {
        WinDialog.show(game, this);

    }

    public boolean showChosseScoreDialog() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/chooseScore.fxml"));
            Parent dialogRoot = loader.load();
            ChooseScoreController chooseScoreController = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Choose Score");
            dialogStage.setScene(new Scene(dialogRoot));
            dialogStage.setResizable(false);

            dialogStage.setOnCloseRequest(event -> {

                event.consume();

                dialogStage.close();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        exitGame();
                    }
                });

            }
            );

            dialogStage.showAndWait();

            choosenScore = chooseScoreController.getScore();
            isWantToSaveRecord = chooseScoreController.isWantToRecord();
            return true;
        } catch (IOException ex) {
            System.getLogger(GameManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;

    }

    private void saveRecord() {
        gameRecorder = new GameRecorder();
        gameRecorder.saveRecord(gameMoves, game);
    }

    public void playRecord(String recordPath) {
        gameRecorder = new GameRecorder();
        gameRecorder.playRecord(recordPath, this);
    }

    public void setDifficultyLevel(int difficulty) {
        this.difficulty = difficulty;

        switch (difficulty) {
            case 1:
                game.playerO = new UserData("CPU_Easy");
                break;
            case 2:
                game.playerO = new UserData("CPU_MEDIUM");
                break;
            case 3:
                game.playerO = new UserData("CPU_Hard");
                break;
        }
    }

    public void setModeType(int modeType) {
        this.modeType = modeType;
        if (modeType == 1) {
            showChosseScoreDialog();
        }
    }

    public boolean getIsWin() {
        return isWin;
    }

}
