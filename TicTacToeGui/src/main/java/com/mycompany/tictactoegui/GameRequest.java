/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.Request;
import com.iti.group3.tic_tac_toe_shared.RequestType;
import com.iti.group3.tic_tac_toe_shared.Response;
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
                    invitationAccepted();
                    break;
                case INVITE_REJECTED:
                    invitationRejected();
                    break;
                case INVITE_DROPPED:
                default:
                    invitationDropped();
                    break;

            }

        } catch (IOException ex) {
            System.getLogger(GameRequest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (ClassNotFoundException ex) {
            System.getLogger(GameRequest.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    public static void invitationAccepted() {
        
        
    }

    public static void invitationRejected() {
    }

    public static void invitationDropped() {
    }

}
