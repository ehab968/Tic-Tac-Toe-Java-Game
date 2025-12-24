/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.GameData;
import com.iti.group3.tic_tac_toe_shared.Request;
import com.iti.group3.tic_tac_toe_shared.RequestType;
import com.iti.group3.tic_tac_toe_shared.Response;
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
            CustomDialog.show("waiting_request.fxml",user2);

        } catch (IOException ex) {
            System.getLogger(GameRequest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (ClassNotFoundException ex) {
            System.getLogger(GameRequest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public static void invitationAccepted(UserData userData2) {
        Platform.runLater(() -> {
            CustomDialog.close();
        });
    }

    public static void invitationRejected(UserData userData2) {
        CustomDialog.show("rejected_request.fxml",userData2);
    }

    public static void invitationDropped() {
        CustomDialog.show("connection_lost_request.fxml",null);
    }

    public static void startOnlineGame(GameData game) {
        Platform.runLater(() -> {
            try {
                CustomDialog.close();
                App.setRoot("primary");
            } catch (IOException ex) {
                System.getLogger(GameRequest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
    }

    public static void RequestReceived(Response response) {
        UserData sender = (UserData) response.getData();

        Platform.runLater(() -> {
            PendingInvite.setSender(sender);
            CustomDialog.show("accept_request.fxml", sender);
        });
    }

    public static void handleInviteResponse(Response response) {
        Platform.runLater(() -> {
            System.out.println("entering switch" + response);
            switch (response.getMessage()) {
                case INVITE_ACCEPTED:
                    UserData opponent = (UserData) response.getData();
                    startOnlineGame(new GameData("1", ClientSocket.user, opponent));
                    break;
                case INVITE_REJECTED:
                 UserData opponent2 = (UserData) response.getData();

                    invitationRejected(opponent2);
                    break;
                case INVITE_DROPPED:
                case SERVER_FAILURE:
                    invitationDropped();
                    break;
            }
        });
    }
}
