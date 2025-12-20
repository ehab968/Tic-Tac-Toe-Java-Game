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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class OnlineUsersController implements Initializable {

    @FXML
    private VBox userListContainer;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadOnlineUsers();
    }

    @FXML

    private List<UserData> getOnlineUsers() {
        try {
            socket = new Socket("localhost", 5005);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            Request request = new Request(RequestType.GetOnlineUsers);
            out.writeObject(request);
            out.flush();

            Response<List<UserData>> response = (Response<List<UserData>>) in.readObject();
            if (response.isSuccess()) {
                return response.getData();
            }
        } catch (IOException ex) {
            System.getLogger(OnlineUsersController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (ClassNotFoundException ex) {
            System.getLogger(OnlineUsersController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return new ArrayList<>();
    }

    public void loadOnlineUsers() {
        new Thread(() -> {
            List<UserData> onlineUsers = getOnlineUsers();

            Platform.runLater(() -> {
                userListContainer.getChildren().clear();

                if (onlineUsers.isEmpty()) {
                    showAlert("No Users Online", "");
                } else {
                    for (UserData u : onlineUsers) {
                        addUser(
                                u.getUserName(),
                                "Online",
                                true
                        );
                    }
                }
            });
        }).start();
    }

    public void addUser(String username, String status, boolean canInvite) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-radius: 15; -fx-padding: 10; -fx-border-color: transparent;");

        VBox vbox = new VBox(2);
        Label nameLabel = new Label(username);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #111827;");
        Label statusLabel = new Label(status);
        statusLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
        if (status.equals("Playing...")) {
            statusLabel.setTextFill(Color.ORANGE);
        } else if (status.equals("Online")) {
            statusLabel.setTextFill(Color.GREEN);
        } else {
            statusLabel.setTextFill(Color.YELLOW);
        }

        vbox.getChildren().addAll(nameLabel, statusLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btn = new Button();
        if (canInvite) {
            btn.setText("Invite ⚔");
            btn.setStyle("-fx-background-color:#2b7cee; -fx-text-fill:white; -fx-background-radius:20;");
        } else {
            btn.setText("Spectate 👀");
            btn.setStyle("-fx-background-color:#f3f4f6; -fx-text-fill:#4b5563; -fx-background-radius:20;");
        }

        hbox.getChildren().addAll(vbox, spacer, btn);

        userListContainer.getChildren().add(hbox);
    }

    private void showAlert(String title, String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(msg);
                alert.showAndWait();
            }
        }
        );
    }
}
