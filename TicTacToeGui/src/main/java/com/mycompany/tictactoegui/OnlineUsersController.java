package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.Request;
import com.iti.group3.tic_tac_toe_shared.RequestType;
import com.iti.group3.tic_tac_toe_shared.Response;
import com.iti.group3.tic_tac_toe_shared.UserData;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class OnlineUsersController implements Initializable {

    public static String myUserName;

    @FXML
    private VBox userListContainer;

    @FXML
    private Button leaderBoardButton;

    @FXML
    private Button reloadButton;
    @FXML
    private Label screenTitle;

    @FXML
    private Label userName;

    @FXML
    private Label onlineUsersText;

    private boolean isLeaderBoard = false;
    @FXML
    private Label descriptionText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadOnlineUsers();
        onlineUsersText.setText(String.valueOf(0) + " Online");
        userName.setText(ClientSocket.user.getUserName());
    }

    // ================= Networking =================
    private List<UserData> getOnlineUsers() {
        try {
            Request request = new Request(RequestType.GetOnlineUsers);
            ClientSocket.write(request);

            Response<List<UserData>> response
                    = (Response<List<UserData>>) ClientSocket.read();

            if (response.isSuccess()) {
                return response.getData();
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.getLogger(OnlineUsersController.class.getName());
        }
        return new ArrayList<>();
    }

    public void loadOnlineUsers() {
        new Thread(() -> {
            ClientStreamSocket.startStream();
            List<UserData> onlineUsers = getOnlineUsers();

            Platform.runLater(() -> {
                userListContainer.getChildren().clear();

                if (onlineUsers.isEmpty()) {
                    showAlert("No Users Online", "");
                } else {
                    for (UserData u : onlineUsers) {
                        if (myUserName != null
                                && u.getUserName().equals(myUserName)) {
                            continue;
                        }
                        addUser(u, "Online", true);
                        onlineUsersText.setText(String.valueOf(onlineUsers.size() - 1) + " Online");

                    }
                }
            });
        }).start();
    }

    @FXML

    private void onlineUserReload(ActionEvent event) {
        loadOnlineUsers();
        onlineUsersText.setText(String.valueOf(0) + " Online");

    }

    // ================= UI =================
    public void addUser(UserData user, String status, boolean canInvite) {

        HBox hbox = new HBox(15);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setStyle(
                "-fx-background-color: #ffffff;"
                + "-fx-background-radius: 15;"
                + "-fx-padding: 12;"
                + "-fx-border-color: #e5e7eb;"
                + "-fx-border-radius: 15;"
        );

        GridPane infoGrid = new GridPane();
        infoGrid.setVgap(4);

        ColumnConstraints col = new ColumnConstraints();
        col.setMinWidth(180);
        infoGrid.getColumnConstraints().add(col);

        Label nameLabel = new Label(user.getUserName());
        nameLabel.setStyle(
                "-fx-font-size: 15px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #111827;"
        );

        Label scoreLabel = new Label("Score: " + user.getScore());
        scoreLabel.setStyle(
                "-fx-font-size: 13px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #374151;"
        );

        infoGrid.add(nameLabel, 0, 0);
        infoGrid.add(scoreLabel, 0, 1);

        Label statusLabel = new Label(status);
        statusLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        if ("Playing...".equals(status)) {
            statusLabel.setTextFill(Color.ORANGE);
        } else if ("Online".equals(status)) {
            statusLabel.setTextFill(Color.GREEN);
        } else {
            statusLabel.setTextFill(Color.GRAY);
        }

        VBox statusBox = new VBox(statusLabel);
        statusBox.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btn = new Button();
        if (canInvite) {
            btn.setText("Invite ⚔");
            btn.setStyle(
                    "-fx-background-color:#2b7cee;"
                    + "-fx-text-fill:white;"
                    + "-fx-background-radius:20;"
                    + "-fx-padding:6 14;"
            );
            btn.setOnAction(e -> GameRequest.sendRequest(user));
        } else {
            btn.setText("Spectate 👀");
            btn.setStyle(
                    "-fx-background-color:#f3f4f6;"
                    + "-fx-text-fill:#4b5563;"
                    + "-fx-background-radius:20;"
                    + "-fx-padding:6 14;"
            );
        }

        hbox.getChildren().addAll(infoGrid, spacer, statusBox, btn);
        userListContainer.getChildren().add(hbox);
    }

    // ================= Navigation =================
    @FXML

    private void navToLeaderBoard(ActionEvent event) {
        try {
            App.setRoot("leaderBoard");
        } catch (IOException ex) {
            System.getLogger(OnlineUsersController.class.getName());
            System.getLogger(OnlineUsersController.class.getName());

        }
    }

    @FXML
    private void backToOnlinePage(ActionEvent event) {
        try {
            App.setRoot("home");
        } catch (IOException ex) {
            System.getLogger(OnlineUsersController.class.getName());
        }
    }

    // ================= Utils =================
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
