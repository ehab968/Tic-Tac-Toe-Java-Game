package com.mycompany.tictactoegui;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;



public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        SoundPlayer.setbuttonVolume(0);
        Platform.runLater(() -> SoundPlayer.playbuttonClick());
        scene = new Scene(loadFXML("home"), 800, 600);
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
    public static void setRoot(Parent root) {
    scene.setRoot(root);
}


    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
