module com.mycompany.tictactoegui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.tictactoegui to javafx.fxml;
    exports com.mycompany.tictactoegui;
}
