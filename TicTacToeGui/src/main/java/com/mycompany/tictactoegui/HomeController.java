/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoegui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mahmo
 */
public class HomeController implements Initializable {

    @FXML
    private ImageView userAvater2;
    @FXML
    private Button singlePlayerBTN;
    @FXML
    private Button multiplayerBTN;
    @FXML
    private Button onlineBTN;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void onSelectSinglePlayer(ActionEvent event) {
    }

    @FXML
    private void onSelectMultiplayer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/primary.fxml"));
            Parent root = loader.load();
            PrimaryController controller = loader.getController();

            // Pass the path to a method you will create in PrimaryController
            controller.gc.showChosseScoreDialog();

            // Show the scene
            Platform.runLater(() -> {
                App.getScene().setRoot(root);
            });
        } catch (IOException ex) {
            System.getLogger(HomeController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void onSelectOnline(ActionEvent event) {
        try {
            if (ClientSocket.user == null) {
                App.setRoot("login");
            } else {
                App.setRoot("onLineUsers");
            }
        } catch (IOException ex) {
            System.getLogger(HomeController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void onPlayRecord(ActionEvent event) {
        File recordsDir = new File("game_records");
        if (recordsDir.exists() && recordsDir.isDirectory()) {
            File[] files = recordsDir.listFiles((dir, name) -> name.endsWith(".txt"));

            if (files != null && files.length > 0) {
                // Option: Show a custom Dialog with a ListView of these files
                showCustomRecordPicker(files);
            } else {
                System.out.println("No records found.");
            }
        }
    }

    private void showCustomRecordPicker(File[] files) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        ListView<String> listView = new ListView<>();
        for (File f : files) {
            String fileName = f.getName().replace(".txt", "");
            listView.getItems().add(fileName);
        }

        Button playBtn = new Button("Play Selected");
        playBtn.setOnAction(e -> {
            String selectedName = listView.getSelectionModel().getSelectedItem();
            if (selectedName != null) {
                dialog.close();
                switchToGameAndPlay(selectedName);
            }
        });

        VBox layout = new VBox(10, new javafx.scene.control.Label("Select a Record"), listView, playBtn);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #f0f4f8;");

        dialog.setScene(new Scene(layout, 300, 400));
        dialog.show();
    }

    private void switchToGameAndPlay(String filePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/primary.fxml"));
            Parent root = loader.load();
            PrimaryController controller = loader.getController();

            // Pass the path to a method you will create in PrimaryController
            System.out.println("setRecordPath= " + filePath);
            controller.setRecordPath(filePath);

            // Show the scene
            Platform.runLater(() -> {
                App.getScene().setRoot(root);
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
