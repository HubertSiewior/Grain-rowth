package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Drawing {
    public static void drawGrid(Canvas canvas, Grid grid, int width, int height) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                gc.setFill(grid.myGrid[i][j].getColor());
                gc.fillRect((canvas.getWidth() * j) / width, (canvas.getHeight() * i) / height, canvas.getWidth() / width, canvas.getHeight() / height);
            }
        }
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(0.05);
        for (int x = 0; x <= width; x++) {
            gc.strokeLine((canvas.getWidth() * x) / width, 0, (canvas.getWidth() * x) / width, canvas.getWidth());
        }
        for (int y = 0; y <= height; y++) {
            gc.strokeLine(0, (canvas.getHeight() * y) / height, canvas.getWidth(), (canvas.getHeight() * y) /height);
        }
    }

}
