package com.mycompany.tictactoegui;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class WaitingRequestDialog {

    private static Stage stage;

    public static void show() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    WinDialog.class.getResource("/fxml/rejected_request.fxml")
            );

            Scene scene = new Scene(loader.load());

            WinDialogController controller = loader.getController();
            //controller.setGameData(game,user);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(final WindowEvent event) {
                    controller.onBackToMainMenu();
                }
            });

            stage.show();
            //controller.setgameController(gameController);
            controller.playVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
