/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.GameData;
import com.iti.group3.tic_tac_toe_shared.GameMove;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author mahmo
 */
public class GameRecorder {

    private static final String RECORD_DIR = "game_records";
    private static final String EXTENSION = ".tictac";

    public int saveRecord(ArrayList<GameMove> moves, GameData gameData) {
        File directory = new File(RECORD_DIR);
        if (!directory.exists()) {
            directory.mkdir();
        }
        gameData.setDate(LocalDateTime.now());
        String timestamp = gameData.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        String fileName = RECORD_DIR + "/" + "game_" + gameData.getId() + "_" + timestamp + EXTENSION;

        // Using ObjectOutputStream to save the list as a binary object
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(moves);
            System.out.println("Game saved as custom file: " + fileName);
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
    private Timeline activeTimeline;

    public void stopPlayback() {
        if (activeTimeline != null) {
            activeTimeline.stop();
            activeTimeline = null;
            System.out.println("Playback stopped.");
        }
    }

    public void playRecord(String fileName, GameManager gameController) {
        ArrayList<GameMove> recordedMoves = loadRecord(fileName);

        if (recordedMoves == null || recordedMoves.isEmpty()) {
            return;
        }

        gameController.restartGame();
        for (StackPane cell : gameController.stackCells) {
            cell.setDisable(true);
        }

        activeTimeline = new Timeline();
        for (int i = 0; i < recordedMoves.size(); i++) {
            GameMove move = recordedMoves.get(i);
            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(i + 1),
                    e -> {
                        StackPane cell = gameController.stackCells[move.getCellId()];
                        gameController.handleCellPress(cell);
                    }
            );
            activeTimeline.getKeyFrames().add(keyFrame);
        }
        activeTimeline.play();
    }

    private ArrayList<GameMove> loadRecord(String fileName) {
        File file = new File(RECORD_DIR, fileName + EXTENSION);

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<GameMove>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading custom record: " + e.getMessage());
            return null;
        }
    }

    private File[] loadRecordFiles() {
        File recordsDir = new File(RECORD_DIR);
        if (recordsDir.exists() && recordsDir.isDirectory()) {
            File[] files = recordsDir.listFiles((dir, name) -> name.endsWith(EXTENSION));
            return files;
        }
        return null;
    }

    public void showCustomRecordPicker() {
        File[] files = loadRecordFiles();

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        ListView<String> listView = new ListView<>();
        for (File f : files) {
            String fileName = f.getName().replace(EXTENSION, "");
            listView.getItems().add(fileName);
        }

        Button playBtn = new Button("Play Selected");
        playBtn.setOnAction(e -> {
            String selectedName = listView.getSelectionModel().getSelectedItem();
            if (selectedName != null) {
                dialog.close();
                goToGameAndPlay(selectedName);
            }
        });

        VBox layout = new VBox(10, new javafx.scene.control.Label("Select a Record"), listView, playBtn);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #f0f4f8;");

        dialog.setScene(new Scene(layout, 300, 400));
        dialog.show();
    }

    public void goToGameAndPlay(String filePath) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/primary.fxml"));
                Parent root = loader.load();
                PrimaryController controller = loader.getController();

                // Pass the path to a method you will create in PrimaryController
                System.out.println("setRecordPath= " + filePath);
                controller.setRecordPath(filePath);

                // Show the scene
                App.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
