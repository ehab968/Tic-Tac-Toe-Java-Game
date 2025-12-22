package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.Response;
import com.iti.group3.tic_tac_toe_shared.ResponseType;
import com.iti.group3.tic_tac_toe_shared.UserData;
import javafx.application.Platform;

public class ServerListener extends Thread {

    @Override
    public void run() {
        try {
            while (true) {
                Response response = ClientSocket.read();
                handleResponse(response);
            }
        } catch (Exception e) {
            System.out.println("Server listener stopped");
            e.printStackTrace();
        }
    }

    private void handleResponse(Response response) {

        switch (response.getMessage()) {

            case REQUEST_GAME:
                // TODO: popup Accept / Reject
                System.out.println("Incoming game request");
                break;

            case START_GAME:
                Platform.runLater(() -> {
                    GameNavigator.openGameScreen(
                            (UserData) response.getData()
                    );
                });
                break;
        }
    }
}
