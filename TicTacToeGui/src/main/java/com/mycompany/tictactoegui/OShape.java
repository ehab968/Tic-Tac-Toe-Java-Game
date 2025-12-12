package com.mycompany.tictactoegui;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class OShape extends Circle {

    public OShape(double size) {
        super(size);
        setStroke(Color.BLUE);
        setFill(Color.TRANSPARENT);
        setStrokeWidth(5);
    }
}
