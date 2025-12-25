/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoegui;

import com.iti.group3.tic_tac_toe_shared.UserData;
import com.mycompany.tictactoegui.CustomDialog;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.text.Text;
/**
 * FXML Controller class
 *
 * @author Ahmed Sayed
 */
public class Waiting_requestController implements Initializable {


    @FXML
    private Text opponentName;
    
        private Stage stage;
        
        UserData opponent;

        public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setOpponent(UserData opponent) {
        this.opponent = opponent;
        opponentName.setText(opponent.getUserName());
    }
    @FXML
    private void cancelInviteAction(ActionEvent event) {
        if (stage != null) {
            stage.close();
        }
        CustomDialog.close();
    }

}
