package com.mycompany.tictactoegui;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author mahmo
 */
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WinDialog {

    public static void show(char winner) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    WinDialog.class.getResource("/fxml/win_dialog.fxml")
            );

            Scene scene = new Scene(loader.load(), 360, 520);

            WinDialogController controller = loader.getController();
            controller.setWinner(winner);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            //stage.setResizable(false);
            stage.setTitle("Game Result");
            stage.setScene(scene);
            stage.show();
            controller.playVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
