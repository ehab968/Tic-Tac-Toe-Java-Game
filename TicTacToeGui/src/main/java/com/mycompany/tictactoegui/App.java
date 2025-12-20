package com.mycompany.tictactoegui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;


public class App extends Application {

    private static Scene scene;

    
    @Override
    public void start(Stage stage) throws IOException {
        SoundPlayer.setbuttonVolume(0);
        Platform.runLater(() -> SoundPlayer.playbuttonClick());
        scene = new Scene(loadFXML("home"), 640, 600);
        scene.addEventFilter(ActionEvent.ACTION, (event) -> {
            if (event.getTarget() instanceof Button) {
                SoundPlayer.setbuttonVolume(1);
                SoundPlayer.playbuttonClick();
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
