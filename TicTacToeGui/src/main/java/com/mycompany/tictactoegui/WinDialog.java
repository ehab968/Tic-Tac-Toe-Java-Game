package com.mycompany.tictactoegui;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author mahmo
 */
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class WinDialog {

    public static void show(char winner, GameController gameController) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    WinDialog.class.getResource("/fxml/win_dialog.fxml")
            );

            Scene scene = new Scene(loader.load());

            WinDialogController controller = loader.getController();
            controller.setWinner(winner);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNIFIED);
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(final WindowEvent event) {
                    controller.onBackToMainMenu();
                }
            });

            controller.playVideo();
            controller.setgameController(gameController);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
