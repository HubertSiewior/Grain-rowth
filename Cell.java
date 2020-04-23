package sample;

import javafx.scene.paint.Color;
import java.util.Random;

public class Cell {
    int id;
    Color color=Color.WHITE;
    int stan;
    int counter=0;
    double centerOfGravityX;
    double centerOfGravityY;
    Random generator=new Random();


    public Cell( int stan) {
        this.id = counter;
        this.stan = stan;
        counter++;
        centerOfGravityX=Math.abs(generator.nextDouble()%1);
        centerOfGravityY=Math.abs(generator.nextDouble()%1);
    }


    public Cell(Color color, int stan) {
        this.color = color;
        this.stan = stan;
    }

    public int getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public int getStan() {
        return stan;
    }
}
