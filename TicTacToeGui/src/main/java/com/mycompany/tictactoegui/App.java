package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.Request;
import com.iti.group3.tic_tac_toe_shared.RequestType;
import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        SoundPlayer.setbuttonVolume(0);
        Platform.runLater(() -> SoundPlayer.playbuttonClick());
        scene = new Scene(loadFXML("home"), 800, 600);

        scene.getStylesheets().add(
                getClass().getResource("/styles/styles.css").toExternalForm()
        );

        scene.addEventFilter(ActionEvent.ACTION, (event) -> {
            if (event.getTarget() instanceof Button) {
                SoundPlayer.setbuttonVolume(1);
                SoundPlayer.playbuttonClick();
            }
        });
        stage.setScene(scene);
        stage.show();
        checkEntryFile();
    }

    private void checkEntryFile() {
        Parameters params = getParameters();

        if (!params.getRaw().isEmpty()) {
            String arg = params.getRaw().get(0);
            File file = new File(arg);
            if (file.getName().endsWith(".tictac")) {
                String entryFile = file.getName().replace(".tictac", "");
                Timeline delay = new Timeline(new KeyFrame(Duration.millis(50), e -> {
                    new GameRecorder().goToGameAndPlay(entryFile);
                }));
                delay.play();
            }
        }

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (ClientStreamSocket.isInGame) {
            ClientStreamSocket.write(new Request(RequestType.END_GAME, null));
        }
        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ex) {
                System.getLogger(App.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            ClientStreamSocket.stopStream();
        }).start();

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

    public static Scene getScene() {
        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
