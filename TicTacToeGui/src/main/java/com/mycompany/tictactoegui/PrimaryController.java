package com.mycompany.tictactoegui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

public class PrimaryController implements Initializable {

    @FXML
    private Pane gamePane;
    @FXML
    private GridPane gridPane;
    private char currentPlayer = 'X';
    private boolean isWin = false;
    private char[][] charMatrix = new char[3][3];
    private StackPane[][] stackCells = new StackPane[3][3]; // => كل ستاك بان هنا ريفرنس للسيل اللى موجودة فى الجريد بان

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                StackPane cell = new StackPane();
                stackCells[row][col] = cell;
                cell.setPrefSize(120, 120);
                final int r = row;
                final int c = col;
                cell.setOnMouseClicked((MouseEvent e) -> {
                    if (cell.isDisable() == false && !isWin) {
                        addShapeToCell(cell, currentPlayer);
                        charMatrix[r][c] = currentPlayer;
                        cell.setDisable(true);
                        cell.setOnMouseClicked(null);
                        int[][] winIndexes = checkWin();
                        if (checkWin() != null) {
                            drawWinningLine(winIndexes);
                        }
                        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                    }
                });
                gridPane.add(cell, r, c);
            }
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

    private int[][] checkWin() {
        for (int r = 0; r < 3; r++) {
            if (charMatrix[r][0] != '\0' && charMatrix[r][0] == charMatrix[r][1] && charMatrix[r][1] == charMatrix[r][2]) {
                isWin = true;
                int[][] res = {{r, 0}, {r, 1}, {r, 2}};
                return res;
            }
        }
        for (int c = 0; c < 3; c++) {
            if (charMatrix[0][c] != '\0' && charMatrix[0][c] == charMatrix[1][c] && charMatrix[1][c] == charMatrix[2][c]) {
                isWin = true;
                int[][] res = {{0, c}, {1, c}, {2, c}};
                return res;
            }
        }
        if (charMatrix[0][0] != '\0' && charMatrix[0][0] == charMatrix[1][1] && charMatrix[1][1] == charMatrix[2][2]) {
            isWin = true;
            int[][] res = {{0, 0}, {1, 1}, {2, 2}};
            return res;
        }
        if (charMatrix[0][2] != '\0' && charMatrix[0][2] == charMatrix[1][1] && charMatrix[1][1] == charMatrix[2][0]) {
            isWin = true;
            int[][] res = {{0, 2}, {1, 1}, {2, 0}};
            return res;
        }
        return null;
    }

    private void drawWinningLine(int[][] winIndex) {
        // {0,0} {0,1} {0,2} => win indexs
        int r1 = winIndex[0][0]; // => 0
        int c1 = winIndex[0][1]; // => 0
        int r3 = winIndex[2][0]; // => 0
        int c3 = winIndex[2][1]; // => 2       
        StackPane _1stCell = stackCells[r1][c1];
        StackPane _3rdCell = stackCells[r3][c3];

        Bounds b1 = _1stCell.localToScene(_1stCell.getBoundsInLocal());
        Bounds b3 = _3rdCell.localToScene(_3rdCell.getBoundsInLocal());
        Point2D p1 = gamePane.sceneToLocal(b1.getCenterX(), b1.getCenterY());
        Point2D p3 = gamePane.sceneToLocal(b3.getCenterX(), b3.getCenterY());
        Line line = new Line(p1.getX(), p1.getY(), p3.getX(), p3.getY());
        line.setStrokeWidth(3);
        gamePane.getChildren().add(line);
    }
}
