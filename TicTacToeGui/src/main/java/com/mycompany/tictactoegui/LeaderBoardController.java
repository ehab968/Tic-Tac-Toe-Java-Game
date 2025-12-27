/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.Request;
import com.iti.group3.tic_tac_toe_shared.RequestType;
import com.iti.group3.tic_tac_toe_shared.Response;
import com.iti.group3.tic_tac_toe_shared.UserData;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
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
        loadLeaderBoard();
    }

    @FXML
    public void onRefresh() {
        loadLeaderBoard();
    }

    private void loadLeaderBoard() {
        new Thread(() -> {
            List<UserData> users = getLeaderBoard();

            Platform.runLater(() -> {
                playersContainer.getChildren().clear();

                for (UserData u : users) {
                    addPlayer(
                            u
                    );
                }
            });
        }).start();
    }

    public void addPlayer(UserData user) {
        GridPane row = createPlayerRow(user);
        playersContainer.getChildren().add(row);
    }

    private GridPane createPlayerRow(UserData user) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);
        grid.setStyle("-fx-background-color: #f1f5f9;");
        grid.setPadding(new Insets(5));

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(35);

        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(35);

        ColumnConstraints c3 = new ColumnConstraints();
        c3.setPercentWidth(10);

        ColumnConstraints c4 = new ColumnConstraints();
        c4.setPercentWidth(20);

        grid.getColumnConstraints().addAll(c1, c2, c3, c4);

        Label nameLbl = new Label(user.getUserName());
        nameLbl.setStyle("-fx-font-weight: bold;");
        nameLbl.setFont(Font.font(13));

        VBox nameBox = new VBox(nameLbl);

        String status = user.getStatus() == 1 ? "Online" : "Offline";
        Label statusLbl = new Label(status);
        statusLbl.setPrefSize(50, 22);
        statusLbl.setStyle(
                "-fx-background-color: #d1fae5;"
                + "-fx-padding: 2 6 2 2;"
                + "-fx-background-radius: 6;"
        );
        if (user.getStatus() == 1) {
            statusLbl.setStyle(
                    "-fx-background-color: #d1fae5;"
                    + "-fx-padding: 2 6 2 2;"
                    + "-fx-background-radius: 6;"
            );
        } else {
            statusLbl.setStyle(
                    "-fx-background-color: #fad1d1;"
                    + "-fx-padding: 2 6 2 2;"
                    + "-fx-background-radius: 6;"
            );

        }

        Label scoreLbl = new Label(String.valueOf(user.getScore()));
        scoreLbl.setStyle("-fx-font-weight: bold;");
        scoreLbl.setFont(Font.font(13));

        boolean canInvite
                = user.getStatus() == 1
                && !user.getUserName().equals(ClientSocket.user.getUserName());

        if (canInvite) {
            Button btn = new Button();
            btn.setText("Invite ⚔");
            btn.setStyle(
                    "-fx-background-color:#2b7cee;"
                    + "-fx-text-fill:white;"
                    + "-fx-background-radius:20;"
            );
            btn.setOnAction(e -> GameRequest.sendRequest(user));
            grid.add(btn, 3, 0);
        }

        grid.add(nameBox, 0, 0);
        grid.add(statusLbl, 1, 0);
        grid.add(scoreLbl, 2, 0);

        return grid;
    }

    private Scene preScene;

    public void setPreScene(Scene preScene) {
        this.preScene = preScene;
    }

    @FXML
    private void backToOnlineuSers(ActionEvent event) {
        try {
            App.setRoot("onLineUsers");
        } catch (IOException ex) {
            System.getLogger(LeaderBoardController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    private List<UserData> getLeaderBoard() {
        try {
            Request request = new Request(RequestType.GET_LEADER_BOARD);
            ClientSocket.write(request);

            Response<List<UserData>> response
                    = (Response<List<UserData>>) ClientSocket.read();

            if (response.isSuccess()) {
                return response.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return List.of();
    }
}
