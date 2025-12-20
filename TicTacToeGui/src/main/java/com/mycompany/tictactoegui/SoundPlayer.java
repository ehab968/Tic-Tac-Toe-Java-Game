package com.mycompany.tictactoegui;
import javafx.scene.media.AudioClip;

public class SoundPlayer {
    static final AudioClip PlayerXSound = new AudioClip(SoundPlayer.class.getResource("/sounds/game.wav").toString());
    static final AudioClip PlayerOSound = new AudioClip(SoundPlayer.class.getResource("/sounds/game2.wav").toString());
    static final AudioClip ButtonSound = new AudioClip(SoundPlayer.class.getResource("/sounds/button_click.wav").toString());
    public static void PlayerXClick(){
        PlayerXSound.play();
    }
    public static void playbuttonClick(){
        ButtonSound.play();
    }
    public static void playerOClick(){
        PlayerOSound.play();
    }
    public static void setbuttonVolume(double vol){
        ButtonSound.setVolume(vol);
    }
    
}
