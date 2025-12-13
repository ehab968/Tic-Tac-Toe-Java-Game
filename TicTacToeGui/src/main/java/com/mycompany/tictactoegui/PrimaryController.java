package com.mycompany.tictactoegui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class PrimaryController implements Initializable {

    @FXML
    private Pane gamePane;
    @FXML
    private GridPane gridPane;
    @FXML
    private ImageView userAvater2;
    @FXML
    private ImageView userAvater1;
    @FXML
    private ImageView userAvater21;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GameController gc = new GameController(gamePane, gridPane);
        
        gc.setOnUserChanged((currentPlayer)->{
                System.out.println("Player changed to " + currentPlayer);
        });
    }
}
