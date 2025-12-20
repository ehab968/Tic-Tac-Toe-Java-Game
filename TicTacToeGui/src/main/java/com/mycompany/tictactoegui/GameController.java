package com.mycompany.tictactoegui;

import com.mycompany.tictactoegui.interfaces.OnUserEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

public class GameController {

    GridPane gridPane;
    Pane gamePane;
    private OnUserEvent onUserChanged;
    private OnUserEvent onUserWinning;
    private char[] charMatrix = new char[9];
    private StackPane[] stackCells = new StackPane[9]; // => كل ستاك بان هنا ريفرنس للسيل اللى موجودة فى الجريد بان
    private boolean isWin = false;
    private char currentPlayer = 'X';

    GameController(Pane gamePane, GridPane gridPane) {
        this.gridPane = gridPane;
        this.gamePane = gamePane;
        initiateGrid();
    }

    private void initiateGrid() {
        int cellId = -1;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                cellId++;
                StackPane cell = new StackPane();
                stackCells[cellId] = cell;
                cell.setPrefSize(120, 120);
                cell.setId(Integer.toString(cellId));
                final int id = cellId;
                if (cell.isDisable() == false && !isWin) {
                    cell.setOnMouseClicked((MouseEvent e) -> {
                        doClickSound();
                        e.consume();
                        if (!cell.isDisable() && !isWin) {
                            addShapeToCell(cell, currentPlayer);
                            charMatrix[id] = currentPlayer;
                            cell.setDisable(true);

                            checkWin();
                            if (!isWin) {
                                changePlayer();
                            }
                        }
                    });
                    gridPane.add(cell, col, row);
                }
            }
        }
    }

    private void doClickSound() {
        if (currentPlayer == 'X') {
            SoundPlayer.PlayerXClick();
        } else if (currentPlayer == 'O') {
            SoundPlayer.playerOClick();
        }
    }

    public final void setOnUserChanged(OnUserEvent listener) {
        this.onUserChanged = listener;
    }

    public final void setOnUserWinning(OnUserEvent listener) {
        this.onUserWinning = listener;
    }

    public void onUserWon(int[] winningLine) {
        isWin = true;
        drawWinningLine(winningLine);

        if (onUserWinning != null) {
            onUserWinning.handle(currentPlayer);
        }
    }

    private void addShapeToCell(StackPane cell, char player) {
        if (player == 'X') {
            XShape x = new XShape(45);
            cell.getChildren().add(x);
        } else {
            OShape o = new OShape(15);
            cell.getChildren().add(o);
        }
    }

    private void changePlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';

        if (onUserChanged != null) {
            onUserChanged.handle(currentPlayer);
        }
    }

    public final void restartGame() {
        gamePane.getChildren().removeIf(n -> n instanceof Line);
        isWin = false;

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
    }

    /*
        0   1   2
        3   4   5
        6   7   8
     */
    private void checkWin() {
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
}
