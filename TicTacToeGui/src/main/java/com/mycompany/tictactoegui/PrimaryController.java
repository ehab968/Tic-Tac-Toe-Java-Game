package com.mycompany.tictactoegui;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class PrimaryController implements Initializable {

    @FXML
    private Pane gamePane;
    @FXML
    private GridPane gridPane;
    private char currentPlayer = 'X';

    private HashMap<Integer, Character> gridInputs = new HashMap();

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
                    if (cell.isDisable() == false) {
                        addShapeToCell(cell, currentPlayer);
                        didWin(cell);
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

    private void didWin(StackPane cell) {

        int cellId = Integer.parseInt(cell.getId());
        char cellShape = gridInputs.get(cellId);
        int shapeAlign = 1;

        //check horizontal
        int ptr = cellId;
        while (ptr % 3 != 0) {// if ptr == 3,6,9 don't enter
            ptr++;
            if (gridInputs.get(ptr) != null && gridInputs.get(ptr) == cellShape) {
                shapeAlign++;
            } else {
                break;
            }
        }

        ptr = cellId - 1;
        while (ptr % 3 != 0) {  // if ptr == 0,3,6,9 don't enter as cellId = 1,4,7
            if (gridInputs.get(ptr) != null && gridInputs.get(ptr) == cellShape) {
                shapeAlign++;
            } else {
                break;
            }
            ptr--;
        }

        if (shapeAlign == 3) {
            userWon();
            return;
        }

        //check vertical
        shapeAlign = 1;
        ptr = cellId + 3;
        while (ptr <= 9) {
            if (gridInputs.get(ptr) != null && gridInputs.get(ptr) == cellShape) {
                shapeAlign++;
            } else {
                break;
            }
            ptr += 3;
        }

        ptr = cellId - 3;
        while (ptr >= 1) {
            if (gridInputs.get(ptr) != null && gridInputs.get(ptr) == cellShape) {
                shapeAlign++;
            } else {
                break;
            }
            ptr -= 3;
        }
        if (shapeAlign == 3) {
            userWon();
            return;
        }

        //Check Diagonal
        int[][] diagonalWins = {{1, 5, 9}, {3, 5, 7}};

        for (int[] line : diagonalWins) {
            if (gridInputs.get(line[0]) != null
                    && gridInputs.get(line[1]) != null
                    && gridInputs.get(line[2]) != null
                    && gridInputs.get(line[0]) == cellShape
                    && gridInputs.get(line[1]) == cellShape
                    && gridInputs.get(line[2]) == cellShape) {

                userWon();
                return;
            }
        }

    }

    private void userWon() {
        System.out.println("User " + currentPlayer + " won the Game!!!");
    }

}
