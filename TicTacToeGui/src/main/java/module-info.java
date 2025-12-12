module com.mycompany.tictactoegui {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.tictactoegui to javafx.fxml;
    exports com.mycompany.tictactoegui;
}
