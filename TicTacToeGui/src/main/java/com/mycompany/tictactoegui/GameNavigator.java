package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.UserData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameNavigator {

    public static void openGameScreen(UserData opponent) {
        try {
            FXMLLoader loader =
                new FXMLLoader(GameNavigator.class
                        .getResource("/fxml/game.fxml"));

            Parent root = loader.load();

            GameController controller = loader.getController();
            //controller.setOpponent(opponent);

            //Stage stage = App.getStage();
            //stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
