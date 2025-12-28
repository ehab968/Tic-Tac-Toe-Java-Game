package com.mycompany.tictactoegui;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author mahmo
 */

import com.iti.group3.tic_tac_toe_shared.GameData;
import com.iti.group3.tic_tac_toe_shared.UserData;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class WinDialog {


    public static void show(GameData game, GameManager gameController) {
        try {

            FXMLLoader loader = new FXMLLoader(
                    WinDialog.class.getResource("/fxml/win_dialog.fxml")
            );

            Scene scene = new Scene(loader.load());

            WinDialogController controller = loader.getController();
            controller.setGameData(game);

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
            controller.setgameController(gameController);
            controller.playVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
