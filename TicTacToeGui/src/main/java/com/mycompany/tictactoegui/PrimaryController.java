package com.mycompany.tictactoegui;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
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
    @FXML
    private ImageView userAvater2;
    @FXML
    private ImageView userAvater1;
    @FXML
    private ImageView userAvater21;

    private boolean isWin = false;
    private char currentPlayer = 'X';
    private HashMap<Integer, Character> gridInputs = new HashMap();// to save user inputs

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int cellId = 0;
        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 3; row++) {
                cellId++;
                StackPane cell = new StackPane();
                cell.setPrefSize(120, 120);
                cell.setId(Integer.toString(cellId));

                cell.setOnMouseClicked((MouseEvent e) -> {
                    if (cell.isDisable() == false && !isWin) {
                        addShapeToCell(cell, currentPlayer);
                        checkWinning(cell);

                        cell.setDisable(true);
                        cell.setOnMouseClicked(null);
                        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                    }
                });

                gridPane.add(cell, row, col);
            }
        }

    }

    private void addShapeToCell(StackPane cell, char player) {
        int cellId = Integer.parseInt(cell.getId());

        if (player == 'X') {
            XShape x = new XShape(45);
            cell.getChildren().add(x);
            gridInputs.put(cellId, 'X');
        } else {
            OShape o = new OShape(15);
            cell.getChildren().add(o);
            gridInputs.put(cellId, 'O');
        }
    }

    private void checkWinning(StackPane cell) {

        int cellId = Integer.parseInt(cell.getId());
        char cellShape = gridInputs.get(cellId);

        int[][] wins = {
            //Horizontal
            {1, 2, 3}, {4, 5, 6}, {7, 8, 9},
            //Vertical
            {1, 4, 7}, {2, 5, 8}, {3, 6, 9},
            // diagonal
            {1, 5, 9}, {3, 5, 7}
        };

        for (int[] line : wins) {
            if (gridInputs.get(line[0]) != null
                    && gridInputs.get(line[1]) != null
                    && gridInputs.get(line[2]) != null
                    && gridInputs.get(line[0]) == cellShape
                    && gridInputs.get(line[1]) == cellShape
                    && gridInputs.get(line[2]) == cellShape) {

                userWon(line);
                return;
            }
        }

    }

    private void userWon(int[] winCells) {
        isWin = true;
        drawWinningLine(winCells);
        System.out.println("User " + currentPlayer + " won the Game!!!");
    }

    private void drawWinningLine(int[] winCells) {
        int first = winCells[0];
        int last = winCells[2];

        double[] start = getCellCenter(first);
        double[] end = getCellCenter(last);

        if (start == null || end == null) {
            return;
        }
        Line line = new Line();
        line.setStartX(start[0]);
        line.setStartY(start[1]);
        line.setEndX(end[0]);
        line.setEndY(end[1]);

        line.setStrokeWidth(6);
        line.setStyle("-fx-stroke: red;");

        gamePane.getChildren().add(line);
    }

    private double[] getCellCenter(int cellId) {
        gridPane.applyCss();
        gridPane.layout();
        for (Node n : gridPane.getChildren()) {
            if (!(n instanceof StackPane)) {
                continue;
            }

            if (n.getId() != null && Integer.parseInt(n.getId()) == cellId) {
                System.out.println(n);
                // Get center in local coordinates
                double centerX = n.getBoundsInLocal().getWidth() / 2;
                double centerY = n.getBoundsInLocal().getHeight() / 2;
                System.out.println("centerX = " + centerX + "centerY = " + centerY);
                // Convert to gamePane coordinates
                javafx.geometry.Point2D p = n.localToScene(centerX, centerY);
                javafx.geometry.Point2D pInGamePane = gamePane.sceneToLocal(p);

                return new double[]{pInGamePane.getX(), pInGamePane.getY()};
            }
        }
        return null;
    }
}
