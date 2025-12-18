/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 *
 * @author mahmo
 */
public class VideoManager {

    public MediaView createVideoView() {
        String path = getClass()
                .getResource("/videos_audios/bravo.mp4")
                .toExternalForm();

        Media media = new Media(path);
        MediaPlayer player = new MediaPlayer(media);

        MediaView mediaView = new MediaView(player);
        mediaView.setFitWidth(280);
        mediaView.setPreserveRatio(true);

        player.play();
        return mediaView;
    }
}
//
//public class VideoManager {
//
//    Pane gamePane;
//
//    VideoManager(Pane gamePane) {
//        this.gamePane = gamePane;
//    }
//
//    public void playVidoe() {
//        try {
//            String path = getClass()
//                    .getResource("/videos_audios/bravo.mp4")
//                    .toExternalForm();
//
//            Media media = new Media(path);
//            MediaPlayer mediaPlayer = new MediaPlayer(media);
//
//            MediaView mediaView = new MediaView(mediaPlayer);
//            mediaView.setFitWidth(400);
//            mediaView.setFitHeight(300);
//            mediaView.setPreserveRatio(true);
//
//            // Center video
//            mediaView.setLayoutX(
//                    (gamePane.getWidth() - 400) / 2
//            );
//            mediaView.setLayoutY(
//                    (gamePane.getHeight() - 300) / 2
//            );
//
//            gamePane.getChildren().add(mediaView);
//
//            mediaPlayer.play();
//
//            // Remove video after finishing
//            mediaPlayer.setOnEndOfMedia(() -> {
//                mediaPlayer.stop();
//                gamePane.getChildren().remove(mediaView);
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
