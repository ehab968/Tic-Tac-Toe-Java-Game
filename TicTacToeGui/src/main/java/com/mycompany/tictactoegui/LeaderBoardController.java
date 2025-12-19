/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoegui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author COMPUMARTS
 */
public class LeaderBoardController implements Initializable {

    @FXML
    private VBox playersContainer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void addPlayer(String username,String status,int score,boolean canInvite) {
    GridPane row = createPlayerRow(username, status, score, canInvite);
    playersContainer.getChildren().add(row);
}

   private GridPane createPlayerRow(String username,String status,int score,boolean canInvite) {
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(5);
    grid.setStyle("-fx-background-color: #f1f5f9;");
    grid.setPadding(new Insets(5));

    ColumnConstraints c1 = new ColumnConstraints();
    c1.setPercentWidth(50);

    ColumnConstraints c2 = new ColumnConstraints();
    c2.setPercentWidth(22);

    ColumnConstraints c3 = new ColumnConstraints();
    c3.setPercentWidth(20);

    ColumnConstraints c4 = new ColumnConstraints();
    c4.setPercentWidth(8);

    grid.getColumnConstraints().addAll(c1, c2, c3, c4);

    Label nameLbl = new Label(username);
    nameLbl.setStyle("-fx-font-weight: bold;");
    nameLbl.setFont(Font.font(13));

    VBox nameBox = new VBox(nameLbl);

    Label statusLbl = new Label(status);
    statusLbl.setPrefSize(68, 22);
    statusLbl.setStyle(
            "-fx-background-color: #d1fae5;" +
            "-fx-padding: 2 6 2 6;" +
            "-fx-background-radius: 6;"
    );

    Label scoreLbl = new Label(String.valueOf(score));
    scoreLbl.setStyle("-fx-font-weight: bold;");
    scoreLbl.setFont(Font.font(13));

    Button btn = new Button();

    if (canInvite) {
        btn.setText("Invite ⚔");
        btn.setStyle(
                "-fx-background-color:#2b7cee;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:20;"
        );
    } else {
        btn.setText("Spectate 👀");
        btn.setStyle(
                "-fx-background-color:#f3f4f6;" +
                "-fx-text-fill:#4b5563;" +
                "-fx-background-radius:20;"
        );
    }

  
    grid.add(nameBox, 0, 0);
    grid.add(statusLbl, 1, 0);
    grid.add(scoreLbl, 2, 0);
    grid.add(btn, 3, 0);

    return grid;
}

}
