/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.GameData;
import com.iti.group3.tic_tac_toe_shared.UserData;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author Ahmed Sayed
 */
public class CustomDialog {

    public static Stage stage;

    public static void show(String path) {
        show(path, null);
    }

    public static void show(String path, UserData user) {
        try {
            close();
            FXMLLoader loader = new FXMLLoader(
                    WinDialog.class.getResource("/fxml/" + path)
            );

            Scene scene = new Scene(loader.load());

            stage = new Stage();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

            if (user != null) {
                if (loader.getController() instanceof Accept_requestController) {
                    Accept_requestController controller = loader.getController();
                    controller.setOpponent(user);
                }
                if (loader.getController() instanceof Connection_lost_requestController) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void close() {
        if (stage != null) {
            stage.close();
            stage = null;
        }
    }

}
