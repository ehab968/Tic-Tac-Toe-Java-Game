package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.GameData;
import com.iti.group3.tic_tac_toe_shared.Request;
import com.iti.group3.tic_tac_toe_shared.RequestType;
import com.iti.group3.tic_tac_toe_shared.GameMove;
import com.iti.group3.tic_tac_toe_shared.UserData;
import com.mycompany.tictactoegui.interfaces.OnUserEvent;
import java.io.IOException;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

public class OnlineGameManager extends GameManager {

    GridPane gridPane;
    Pane gamePane;
    private OnUserEvent onUserChanged;
    private OnUserEvent onUserWinning;
    private char[] charMatrix = new char[9];
    private StackPane[] stackCells = new StackPane[9]; // => كل ستاك بان هنا ريفرنس للسيل اللى موجودة فى الجريد بان
    public boolean isWin = false;
    private char mySymbol;
    private char lastMoveSymbol;
    static int choosenScore;
    private int userScore = 0;
    private int opponentScore = 0;
    GameData game;
    UserData user;
    UserData opponent;
    boolean isMyTurn = false;

    OnlineGameManager(Pane gamePane, GridPane gridPane) {
        this.gridPane = gridPane;
        this.gamePane = gamePane;
        game = ClientStreamSocket.currentGame;
        user = ClientStreamSocket.user;
        if (game.playerX.getUserName().equals(user.getUserName())) {
            opponent = game.playerO;
            mySymbol = 'X';
            isMyTurn = true;
        } else {
            opponent = game.playerX;
            mySymbol = 'O';
            isMyTurn = false;
        }
        initiateGrid();
    }

    private void initiateGrid() {
        int cellId = -1;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                cellId++;
                StackPane cell = new StackPane();
                stackCells[cellId] = cell;
                final int id = cellId;
                if (cell.isDisable() == false && !isWin) {
                    cell.setOnMouseClicked((MouseEvent e) -> {
                        if (!isMyTurn || isWin || cell.isDisable()) {
                            return;
                        }
                        doClickSound();
                        e.consume();
                        GameMove move = new GameMove(id, mySymbol);
                        try {
                            ClientStreamSocket.write(new Request(RequestType.MOVE, move));
                        } catch (IOException ex) {
                            System.getLogger(GameManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                        } catch (ClassNotFoundException ex) {
                            System.getLogger(GameManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                        }

                    });
                    gridPane.add(cell, col, row);
                }
            }
        }
    }

    public void handleMove(GameMove move) {
        int cellId = move.getCellId();
        char player = move.getCharacter();
        lastMoveSymbol = player;
        StackPane cell = stackCells[cellId];
        addShapeToCell(cell, player);
        charMatrix[cellId] = player;
        cell.setDisable(true);
        checkWin();
        if (!isWin) {
            isMyTurn = (player != mySymbol);
            if (onUserChanged != null) {
                onUserChanged.handle(player);
            }
        }
    }

    private void doClickSound() {
        if (mySymbol == 'X') {
            SoundPlayer.PlayerXClick();
        } else if (mySymbol == 'O') {
            SoundPlayer.playerOClick();
        }
    }

    @Override
    public final void setOnUserChanged(OnUserEvent listener) {
        this.onUserChanged = listener;
    }

    @Override
    public final void setOnUserWinning(OnUserEvent listener) {
        this.onUserWinning = listener;
    }

    @Override
    public void addShapeToCell(StackPane cell, char player) {
        if (player == 'X') {
            XShape x = new XShape(45);
            cell.getChildren().add(x);
        } else {
            OShape o = new OShape(15);
            cell.getChildren().add(o);
        }
    }

    @Override
    public void changePlayer() {
        mySymbol = (mySymbol == 'X') ? 'O' : 'X';

        if (onUserChanged != null) {
            onUserChanged.handle(mySymbol);
        }
    }

    public UserData getUserData() {
        return user;
    }

    public UserData getOpponentData() {
        return opponent;
    }

    public char getMySymbol() {
        return mySymbol;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    @Override
    public void restartGame() {
        gamePane.getChildren().removeIf(n -> n instanceof Line);
        lastMoveSymbol = '\0';
        isWin = false;
        for (int i = 0; i < 9; i++) {
            charMatrix[i] = '\0';
            StackPane cell = stackCells[i];
            cell.getChildren().clear();
            cell.setDisable(false);
        }
        if (mySymbol == 'X') {
            isMyTurn = true;
        } else {
            isMyTurn = false;
        }
        if (onUserChanged != null) {
            onUserChanged.handle(mySymbol);
        }
        WinDialogController.instance.close();

    }

    public boolean allCellsFull() {
        for (char c : charMatrix) {
            if (c == '\0') {
                return false;
            }
        }
        return true;
    }

    @Override
    public void exitGame() {
        try {
            if (WinDialogController.instance != null) {
                WinDialogController.instance.close();
            }
            App.setRoot("onLineUsers");
        } catch (IOException ex) {
            System.getLogger(PrimaryController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    /*
        0   1   2
        3   4   5
        6   7   8
     */
    @Override
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

    private void drawWinningLine(int[] winIndex) {
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

    @Override
    public void onUserWon(int[] winningLine) {
        isWin = true;
        drawWinningLine(winningLine);
        boolean iWon = (lastMoveSymbol == mySymbol);
        if (iWon) {
            game.setWinner(user);
            try {
                ClientStreamSocket.write(new Request(RequestType.UPDATE_SCORE, game.winner));
            } catch (IOException | ClassNotFoundException ex) {
                System.getLogger(OnlineGameManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        } else {
            game.setWinner(opponent);
        }
        showWinningDialog(game);
        if (onUserWinning != null) {
            onUserWinning.handle(lastMoveSymbol);
        }

    }

    @Override
    public void startNewGame() {
    }

    private void showWinningDialog(GameData game) {
        WinDialog.show(game, this);
    }
}
