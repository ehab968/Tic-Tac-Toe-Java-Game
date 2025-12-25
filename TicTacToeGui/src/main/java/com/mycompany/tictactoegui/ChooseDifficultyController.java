package com.mycompany.tictactoegui;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author COMPUMARTS
 */
public class ChooseDifficultyController {

    @FXML
    private Button easyBtn;

    @FXML
    private Button mediumBtn;

    @FXML
    private Button hardBtn;

    @FXML
    private Button backBtn;
    private int modeType;
    private int difficulty;
    private Parent previousRoot;
    

    public void goToPrimary(int difficulty) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/primary.fxml"));
            Parent root = loader.load();
            PrimaryController controller = loader.getController();
            controller.setModeType(modeType);
            controller.setDifficultyLevel(difficulty);
            App.setRoot(root);

        } catch (IOException ex) {
            System.getLogger(ChooseDifficultyController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void onEasyClicked(ActionEvent event) {
        
        goToPrimary(1);

    }

    @FXML
    private void onMeduimClicked(ActionEvent event) {
       
        goToPrimary(2);

    }

    @FXML
    private void onHardClicked(ActionEvent event) {
        goToPrimary(3);

    }

   

    @FXML
    private void onBackClicked(ActionEvent event) {
        if (previousRoot != null) {
        App.setRoot(previousRoot);
    }

    }

    public void setModeType(int modeType) {
        this.modeType = modeType;

    }
    public void setDifficulityLevel(int modeType) {
        this.modeType = modeType;

    }

     public void setPreviousRoot(Parent root) {
    this.previousRoot = root;
}
}
