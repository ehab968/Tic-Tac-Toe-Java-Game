package com.mycompany.tictactoegui;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class XShape extends Group {

    public XShape(double size) {

        double padding = size * 0.2;    // مسافة من الأطراف
        double end = size - padding;    // نقطة النهاية

        Line line1 = new Line(padding, padding, end, end);
        Line line2 = new Line(end, padding, padding, end);

        line1.setStroke(Color.RED);
        line2.setStroke(Color.RED);

        line1.setStrokeWidth(5);
        line2.setStrokeWidth(5);

        getChildren().addAll(line1, line2);
    }
}