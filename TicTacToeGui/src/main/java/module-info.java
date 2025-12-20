module com.mycompany.tictactoegui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires tic.tac.toe.shared;
    requires java.sql;
    requires derbyclient;
    opens com.mycompany.tictactoegui to javafx.fxml;
    exports com.mycompany.tictactoegui;
}
