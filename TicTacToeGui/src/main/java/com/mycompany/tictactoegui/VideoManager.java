package com.mycompany.tictactoegui;


import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

/**
 *
 * @author mahmo
 */
public class VideoManager {

    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private String videoPath;

    private int retryCount = 0;

    VideoManager(String videoPath, MediaView mediaView) {
        this.mediaView = mediaView;
        this.videoPath = videoPath;

        loadVideo();
    }

    private void loadVideo() {
        try {

            String resource = getClass().getResource(videoPath).toExternalForm();
            Media media = new Media(resource);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.setOnReady(() -> {
                System.out.println("Video loaded successfully after " + retryCount + " retries.");
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();          
            });

            mediaPlayer.setOnError(() -> {
                System.err.println("GStreamer Error: " + mediaPlayer.getError().getMessage());
                stop();
                if (retryCount < 20) {
                    retryCount++;
                    PauseTransition delay = new PauseTransition(Duration.millis(200));
                    delay.setOnFinished(event -> loadVideo());
                    delay.play();
                }
            });

        } catch (Exception e) {
            System.err.println("Setup Error: " + e.getMessage());
            stop();
            if (retryCount < 5) {
                retryCount++;
                // Wait 200ms before trying again to let the OS breathe
                PauseTransition delay = new PauseTransition(Duration.millis(200));
                delay.setOnFinished(event -> loadVideo());
                delay.play();
            }
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();

            mediaPlayer = null;
        }
    }
}
