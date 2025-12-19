module com.mycompany.tictactoegui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires tic.tac.toe.shared;
    requires javafx.media; 
    opens com.mycompany.tictactoegui to javafx.fxml;
    exports com.mycompany.tictactoegui;
}
