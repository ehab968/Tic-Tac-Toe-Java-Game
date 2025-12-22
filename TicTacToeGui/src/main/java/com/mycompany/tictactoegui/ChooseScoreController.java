/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoegui;

/**
 *
 * @author COMPUMARTS
 */
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author COMPUMARTS
 */
public class ChooseScoreController implements Initializable {

    @FXML
    private TextField scoreField;
    @FXML
    private HBox errorBox;
    @FXML
    private Label errorLabel;
       private int choosenScore;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handlePressed(MouseEvent event) {

        try {
             choosenScore = parseInt(scoreField.getText());
            Button b = (Button) event.getSource();
            Stage stage = (Stage) b.getScene().getWindow();
            if (choosenScore <= 0) {
                errorLabel.setText("Please enter a number greater than 0");
                return;
            }
            stage.close();
        } catch (NumberFormatException ex) {
            errorLabel.setText("Please enter a number greater than 0");

        }
    }
     public int getScore() {
        return choosenScore;
    }

}
