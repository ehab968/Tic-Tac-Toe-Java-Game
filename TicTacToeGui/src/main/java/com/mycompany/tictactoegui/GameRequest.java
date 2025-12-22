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

/**
 *
 * @author Ahmed Sayed
 */
public class GameRequest {

    public static void sendRequest(UserData user2) {
        Request request = new Request(RequestType.INVITE_USER, user2);
        try {
            ClientSocket.write(request);

            Response response = ClientSocket.read();

            switch (response.getMessage()) {
                case INVITE_ACCEPTED:
                    invitationAccepted(user2);
                    break;
                case INVITE_REJECTED:
                    invitationRejected();
                    break;
                case INVITE_DROPPED:
                default:
                    invitationAccepted(user2);
                    break;

            }

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
    }

    public static void listenToRequest() {

        new Runnable() {

            @Override
            public void run() {
                try {
                    //Client 2
                   while(true){
                   Response response = ClientSocket.read();
                    
                  if(response.getMessage() == ResponseType.REQUEST_GAME){
                   ClientSocket.write(new Request(RequestType.ACCEPT_INVITE,null));
                    App.setRoot("primary");
                  }
                   }
                } catch (IOException ex) {
                    System.getLogger(GameRequest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                } catch (ClassNotFoundException ex) {
                    System.getLogger(GameRequest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
        }.run();

    }

}
