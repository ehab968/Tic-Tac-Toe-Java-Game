/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.Request;
import com.iti.group3.tic_tac_toe_shared.RequestType;
import com.iti.group3.tic_tac_toe_shared.UserData;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Ahmed Sayed
 */
public class Accept_requestController implements Initializable {

    @FXML
    private Label opponentName;
    @FXML
    private Button acceptBtn;
    @FXML
    private Button backArrowBtn;

    UserData opponent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void acceptBtnAction(ActionEvent event) {

        try {
            Request request = new Request(
                    RequestType.ACCEPT_INVITE,
                    PendingInvite.getSender()
            );
            ClientStreamSocket.write(request);

            Platform.runLater(() -> {
                CustomDialog.close();
            });
            PendingInvite.clear();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backArrowBtnAction(ActionEvent event) {

        try {
            Request request = new Request(
                    RequestType.REJECT_INVITE,
                    PendingInvite.getSender()
            );
            ClientStreamSocket.write(request);

            Platform.runLater(() -> {
                CustomDialog.close();
            });

            PendingInvite.clear();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setOpponent(UserData opponent) {
        this.opponent = opponent;
        opponentName.setText(opponent.getUserName());
    }
}
