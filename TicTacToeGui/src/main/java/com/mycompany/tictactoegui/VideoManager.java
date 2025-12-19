/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 *
 * @author mahmo
 */
public class VideoManager {

    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private String videoPath;

    VideoManager(String videoPath, MediaView mediaView) {
        this.mediaView = mediaView;
        this.videoPath = videoPath;

        loadVideo();
    }

    private void loadVideo() {
        try {
            String path = getClass()
                    .getResource(videoPath)
                    .toExternalForm();

            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
        } catch (Exception e) {
            System.err.println("Could not load video: " + e.getMessage());
        }
    }

    public void playVideo() {
        // FIX 1: Only play when the media is fully loaded
        if (mediaPlayer.getStatus() == MediaPlayer.Status.READY) {
            mediaPlayer.play();
        } else {
            mediaPlayer.setOnReady(() -> {
                mediaPlayer.play();
            });
        }

        // FIX 2: Handle potential errors (e.g., codec issues)
        mediaPlayer.setOnError(() -> {
            System.err.println("Media error: " + mediaPlayer.getError().getMessage());
        });
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
