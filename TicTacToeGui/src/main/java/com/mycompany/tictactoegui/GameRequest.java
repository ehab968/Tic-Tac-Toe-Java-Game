/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.GameData;
import com.iti.group3.tic_tac_toe_shared.Request;
import com.iti.group3.tic_tac_toe_shared.RequestType;
import com.iti.group3.tic_tac_toe_shared.Response;
import com.iti.group3.tic_tac_toe_shared.ResponseType;
import static com.iti.group3.tic_tac_toe_shared.ResponseType.INVITE_ACCEPTED;
import com.iti.group3.tic_tac_toe_shared.UserData;
import java.io.IOException;
import javafx.application.Platform;

/**
 *
 * @author Ahmed Sayed
 */
public class GameRequest {

    public static void sendRequest(UserData user2) {
        try {
            Request request = new Request(RequestType.INVITE_USER, user2);
            ClientStreamSocket.write(request);
        } catch (IOException ex) {
            System.getLogger(GameRequest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (ClassNotFoundException ex) {
            System.getLogger(GameRequest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public static void invitationAccepted(UserData userData2) {

        try {
            startOnlineGame(new GameData("1", ClientSocket.user, userData2));
            App.setRoot("primary");
        } catch (IOException ex) {
            System.getLogger(GameRequest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public static void invitationRejected() {
        CustomDialog.show("rejected_request.fxml");
    }

    public static void invitationDropped() {
        CustomDialog.show("connection_lost_request.fxml");
    }

    public static void startOnlineGame(GameData game) {
        Platform.runLater(() -> {
            try {
                App.setRoot("primary");
            } catch (IOException ex) {
                System.getLogger(GameRequest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
    }

    public static void RequestReceived(Response response) {
        UserData user1 = (UserData) response.getData();
        Request request = new Request(RequestType.ACCEPT_INVITE, user1);
        try {
            ClientStreamSocket.write(request);
        } catch (IOException ex) {
            System.getLogger(GameRequest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (ClassNotFoundException ex) {
            System.getLogger(GameRequest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

}
