package com.mycompany.tictactoegui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(120, 120);
                final int r = row;
                final int c = col;
                cell.setOnMouseClicked((MouseEvent e) -> {
                    if (cell.isDisable() == false) {
                        addShapeToCell(cell, currentPlayer);
                        cell.setDisable(true);
                        cell.setOnMouseClicked(null);
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
}
