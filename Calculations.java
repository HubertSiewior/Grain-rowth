package sample;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Calculations {
    private int quantityInRow;
    private int quantityInKol;
    private int width;
    private int height;
    private Grid grid;
    private int counter;
    private int radius;
    private boolean aBoolean;
    private Map<Integer,Color> colorMap;

    public Calculations(int quantityInRow, int quantityInKol, int width, int height, int radius, int howMany) {
        this.quantityInRow = quantityInRow;
        this.quantityInKol = quantityInKol;
        this.radius = radius;
        this.width = width;
        this.height = height;
        grid = new Grid(height, width);
        this.counter = howMany;
        aBoolean = false;
        setStartGrid();
        colorMap=new HashMap<>();
    }

    public void setStartGrid() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid.myGrid[i][j] = new Cell(0);
            }
        }
    }

    public void setHomogeneousGrid() {
        int qRow = width / quantityInRow, qKol = height / quantityInKol;
        if (quantityInRow == 1) qRow /= 2;
        if (quantityInKol == 1) qKol /= 2;
        int x = width - qRow * quantityInRow;
        int y = height - qKol * quantityInKol;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i >= y / 2 && j >= x / 2 && i < height - y / 2 && j < width - x / 2) {
                    if ((i + 1) % qKol == 0 && (j + 1) % qRow == 0) {
                        setState(i, j);
                    }
                }
            }
        }
    }

    public void setRandomGrid() {
        Random generator = new Random();
        int limit=1000;
        while (counter > 0&&limit>0) {
            int i = generator.nextInt(height), j = generator.nextInt(width);
            if (grid.myGrid[i][j].stan == 0) {
                setState(i, j);
                counter--;
            }
            limit--;
        }
    }

    public void printGrid() {
        System.out.println("====================================================================");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(grid.myGrid[i][j].stan + "\t");
            }
            System.out.println();
        }
    }

    public void setRadiusGrid() {
        Random generator = new Random();
        int limit = 1000;
        while (counter > 0 && limit > 0) {
            int x = generator.nextInt(height);
            int y = generator.nextInt(width);
            if (grid.myGrid[x][y].stan == 0) {
                boolean taken = false;
                for (int i = x-radius; i <= x+radius; i++) {
                    for (int j = y-radius; j <= y+radius; j++) {
                        if(correct(i,j)){
                            double length =  Math.sqrt(Math.pow(x+grid.myGrid[x][y].centerOfGravityX - i+grid.myGrid[i][j].centerOfGravityX, 2)
                                    + Math.pow(y+grid.myGrid[x][y].centerOfGravityY - j+grid.myGrid[i][j].centerOfGravityY, 2));
                            if (length <= radius && length > 0 && !aBoolean) {
                                if (grid.myGrid[i][j].color != Color.WHITE) {
                                    taken = true;
                                }
                            }
                        }
                    }
                }
                if (!taken) {
                    setState(x, y);
                    counter--;
                }
            }
            limit--;
        }
    }
    public boolean correct(int i,int j){
        if(i<0||j<0||i>height-1||j>width-1) return false;
        return true;
    }

    public void setState(int i, int j) {
        Random generator = new Random();
        boolean add=true;
        while(add){
            int r = Math.abs(generator.nextInt() % 256);
            int g = Math.abs(generator.nextInt() % 256);
            int b = Math.abs(generator.nextInt() % 256);
            if(!colorMap.containsValue(Color.rgb(r, g, b))){
                colorMap.put(grid.myGrid[i][j].id,Color.rgb(r, g, b));
                grid.myGrid[i][j].stan = 1;
                grid.myGrid[i][j].color = Color.rgb(r, g, b);
                add=false;
            }
        }
    }

    public Grid getGrid() {
        return grid;
    }
}

