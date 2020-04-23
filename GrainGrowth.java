package sample;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GrainGrowth {
    Grid grid;
    int width;
    int height;
    int radius;
    int howMany;
    Boolean periodic;
    List<Color> colorList ;
    List<Color> colors ;
    Random generator;
    private String type;

    public GrainGrowth(Grid grid, int width, int height, int radius, int howMany, Boolean periodic) {
        colorList= new ArrayList<>();
        colors=new ArrayList<>();
        this.grid = grid;
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.howMany = howMany;
        this.periodic = periodic;
        generator=new Random();
    }

    public void setStartGrid(Grid grid2) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid2.myGrid[i][j]=new Cell(grid.myGrid[i][j].color,grid.myGrid[i][j].stan);
            }
        }
    }

    public Grid step(String string) {
        Grid grid2=new Grid(height,width);
        setStartGrid(grid2);
        type=string;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid2.myGrid[i][j].color == Color.WHITE) {
                    if (string.equals("VonNeumann")) {
                        grid2.myGrid[i][j].color = getVonNeumann(i, j);
                        if (grid2.myGrid[i][j].color != Color.WHITE) grid2.myGrid[i][j].stan = 1;
                    }
                    else if(string.equals("Moore")){
                        grid2.myGrid[i][j].color = getMoore(i, j);
                        if (grid2.myGrid[i][j].color != Color.WHITE) grid2.myGrid[i][j].stan = 1;
                    }
                    else if(string.equals("PentagonalrRandom")){
                        grid2.myGrid[i][j].color = getPentagonalrRandom(i, j);
                        if (grid2.myGrid[i][j].color != Color.WHITE) grid2.myGrid[i][j].stan = 1;
                    }
                    else if (string.equals("HexagonalRight")||string.equals("HexagonalLeft")||string.equals("HexagonalRandom")){
                        grid2.myGrid[i][j].color = getHexagonal(i, j);
                        if (grid2.myGrid[i][j].color != Color.WHITE) grid2.myGrid[i][j].stan = 1;
                    }
                    else if(string.equals("WithRadius")){
                            grid2.myGrid[i][j].color = getWithRadius(i, j);
                        if (grid2.myGrid[i][j].color != Color.WHITE) grid2.myGrid[i][j].stan = 1;
                    }
                    else if(string.equals("Random")) {
                        int nextInt=generator.nextInt(5);
                        switch (nextInt){
                            case 0:
                                grid2.myGrid[i][j].color = getVonNeumann(i, j);
                                if (grid2.myGrid[i][j].color != Color.WHITE) grid2.myGrid[i][j].stan = 1;
                                break;
                            case 1:
                                grid2.myGrid[i][j].color = getMoore(i, j);
                                if (grid2.myGrid[i][j].color != Color.WHITE) grid2.myGrid[i][j].stan = 1;
                                break;
                            case 2:
                                grid2.myGrid[i][j].color = getPentagonalrRandom(i, j);
                                if (grid2.myGrid[i][j].color != Color.WHITE) grid2.myGrid[i][j].stan = 1;
                                break;
                            case 3:
                                grid2.myGrid[i][j].color = getHexagonal(i, j);
                                if (grid2.myGrid[i][j].color != Color.WHITE) grid2.myGrid[i][j].stan = 1;
                                break;
                            case 4:
                                grid2.myGrid[i][j].color = getWithRadius(i, j);
                                if (grid2.myGrid[i][j].color != Color.WHITE) grid2.myGrid[i][j].stan = 1;
                                break;
                            default:
                                break;
                        }

                    }
                }
            }
        }
        return grid2;
    }
    public Color getMoore(int i,int j){
        colorList.clear();colors.clear();
        int x,y;
        if(!periodic) {
            if (correct(i + 1, j)) colorList.add(grid.myGrid[i + 1][j].color);
            if (correct(i - 1, j)) colorList.add(grid.myGrid[i - 1][j].color);
            if (correct(i, j + 1)) colorList.add(grid.myGrid[i][j + 1].color);
            if (correct(i, j - 1)) colorList.add(grid.myGrid[i][j - 1].color);
            if (correct(i - 1, j - 1)) colorList.add(grid.myGrid[i - 1][j - 1].color);
            if (correct(i - 1, j + 1)) colorList.add(grid.myGrid[i - 1][j + 1].color);
            if (correct(i + 1, j - 1)) colorList.add(grid.myGrid[i + 1][j - 1].color);
            if (correct(i + 1, j + 1)) colorList.add(grid.myGrid[i + 1][j + 1].color);
        }
        else {
            x=pbcX(i+1);y=pbcY(j); colorList.add(grid.myGrid[x][y].color);
            x=pbcX(i-1);y=pbcY(j); colorList.add(grid.myGrid[x][y].color);
            x=pbcX(i);y=pbcY(j+1); colorList.add(grid.myGrid[x][y].color);
            x=pbcX(i);y=pbcY(j-1); colorList.add(grid.myGrid[x][y].color);
            x=pbcX(i-1);y=pbcY(j-1); colorList.add(grid.myGrid[x][y].color);
            x=pbcX(i-1);y=pbcY(j+1); colorList.add(grid.myGrid[x][y].color);
            x=pbcX(i+1);y=pbcY(j-1); colorList.add(grid.myGrid[x][y].color);
            x=pbcX(i+1);y=pbcY(j+1); colorList.add(grid.myGrid[x][y].color);
        }
        for (Color color : colorList) {
            if (color != Color.WHITE) {
                colors.add(color);
            }
        }
        return getMaxColor(colors);
    }
    public int pbcX(int indexX){
        if(indexX<0) return height+indexX;
        if(indexX>height-1) return indexX%height;
        return indexX;
    }
    public int pbcY(int indexY){
        if(indexY<0) return width+indexY;
        if(indexY>width-1) return indexY%width;
        return indexY;
    }


    public Color getVonNeumann(int i, int j) {
        colorList.clear();colors.clear();
        int x,y;
        if(!periodic) {
            if (correct(i + 1, j)) colorList.add(grid.myGrid[i + 1][j].color);
            if (correct(i - 1, j)) colorList.add(grid.myGrid[i - 1][j].color);
            if (correct(i, j + 1)) colorList.add(grid.myGrid[i][j + 1].color);
            if (correct(i, j - 1)) colorList.add(grid.myGrid[i][j - 1].color);
        }
        else {
            x=pbcX(i+1);y=pbcY(j); colorList.add(grid.myGrid[x][y].color);
            x=pbcX(i-1);y=pbcY(j); colorList.add(grid.myGrid[x][y].color);
            x=pbcX(i);y=pbcY(j+1); colorList.add(grid.myGrid[x][y].color);
            x=pbcX(i);y=pbcY(j-1); colorList.add(grid.myGrid[x][y].color);
        }
        for (Color color : colorList) {
            if (color != Color.WHITE) {
                colors.add(color);
            }
        }
        return getMaxColor(colors);
    }
    public Color getPentagonalrRandom(int i,int j){
        colorList.clear();colors.clear();
        int nextInt=generator.nextInt(4);
        switch (nextInt){
            case 0:
                setListPentagonal(i,j-1,i-1,j-1,i-1,j,i-1,j+1,i,j+1);
                break;
            case 1:
                setListPentagonal(i-1,j,i-1,j+1,i,j+1,i+1,j+1,i+1,j);
                break;
            case 2:
                setListPentagonal(i,j+1,i+1,j+1,i+1,j,i+1,j-1,i,j-1);
                break;
            case 3:
                setListPentagonal(i+1,j,i+1,j-1,i,j-1,i-1,j-1,i-1,j);
                break;
            default:
                break;
        }
        for (Color color : colorList) {
            if (color != Color.WHITE) {
                colors.add(color);
            }
        }
        return getMaxColor(colors);
    }
    public void setListPentagonal(int i1,int j1,int i2,int j2,int i3,int j3,int i4,int j4,int i5,int j5){
        if(!periodic) {
            if (correct(i1, j1)) colorList.add(grid.myGrid[i1][j1].color);
            if (correct(i2, j2)) colorList.add(grid.myGrid[i2][j2].color);
            if (correct(i3, j3)) colorList.add(grid.myGrid[i3][j3].color);
            if (correct(i4, j4)) colorList.add(grid.myGrid[i4][j4].color);
            if (correct(i5, j5)) colorList.add(grid.myGrid[i5][j5].color);
        }
        else {
            i1=pbcX(i1);j1=pbcY(j1); colorList.add(grid.myGrid[i1][j1].color);
            i2=pbcX(i2);j2=pbcY(j2);colorList.add(grid.myGrid[i2][j2].color);
            i3=pbcX(i3);j3=pbcY(j3); colorList.add(grid.myGrid[i3][j3].color);
            i4=pbcX(i4);j4=pbcY(j4); colorList.add(grid.myGrid[i4][j4].color);
            i5=pbcX(i5);j5=pbcY(j5); colorList.add(grid.myGrid[i5][j5].color);
        }

    }
    public void setListHexagonal(int i1,int j1,int i2,int j2,int i3,int j3,int i4,int j4,int i5,int j5,int i6,int j6){
        if(!periodic){
        if (correct(i1 , j1)) colorList.add(grid.myGrid[i1][j1].color);
        if (correct(i2 , j2)) colorList.add(grid.myGrid[i2][j2].color);
        if (correct(i3 , j3)) colorList.add(grid.myGrid[i3][j3].color);
        if (correct(i4 , j4)) colorList.add(grid.myGrid[i4][j4].color);
        if (correct(i5 , j5)) colorList.add(grid.myGrid[i5][j5].color);
        if (correct(i6 , j6)) colorList.add(grid.myGrid[i6][j6].color);
        }
        else {
            i1=pbcX(i1);j1=pbcY(j1); colorList.add(grid.myGrid[i1][j1].color);
            i2=pbcX(i2);j2=pbcY(j2);colorList.add(grid.myGrid[i2][j2].color);
            i3=pbcX(i3);j3=pbcY(j3); colorList.add(grid.myGrid[i3][j3].color);
            i4=pbcX(i4);j4=pbcY(j4); colorList.add(grid.myGrid[i4][j4].color);
            i5=pbcX(i5);j5=pbcY(j5); colorList.add(grid.myGrid[i5][j5].color);
            i6=pbcX(i6);j6=pbcY(j6); colorList.add(grid.myGrid[i6][j6].color);
        }


    }
    private Color getHexagonal(int i,int j){
        colorList.clear();colors.clear();
        int nextInt=generator.nextInt(2);
        if(type.equals("HexagonalRandom")) {
            switch (nextInt) {
                case 0:
                    setListHexagonal(i - 1, j, i - 1, j + 1, i, j + 1, i + 1, j, i + 1, j - 1, i, j - 1);
                    break;
                case 1:
                    setListHexagonal(i, j - 1, i - 1, j - 1, i - 1, j, i, j + 1, i + 1, j + 1, i + 1, j);
                    break;
                default:
                    break;
            }
        }
        else if(type.equals("HexagonalRight")){
            setListHexagonal(i - 1, j, i - 1, j + 1, i, j + 1, i + 1, j, i + 1, j - 1, i, j - 1);
        }
        else {
            setListHexagonal(i, j - 1, i - 1, j - 1, i - 1, j, i, j + 1, i + 1, j + 1, i + 1, j);
        }
        for (Color color : colorList) {
            if (color != Color.WHITE) {
                colors.add(color);
            }
        }

        return getMaxColor(colors);
    }
    public Color getWithRadius(int i,int j){
        int x,y;
        double length;
        colorList.clear();colors.clear();
        for (int k = i-radius; k <=i+radius ; k++) {
            for (int l = j-radius; l <=j+radius ; l++) {
                if(!periodic) {
                    if (correct(k, l)) {
                        length =  Math.sqrt(Math.pow(i+grid.myGrid[i][j].centerOfGravityX - k+grid.myGrid[k][l].centerOfGravityX, 2)
                                + Math.pow(j+grid.myGrid[i][j].centerOfGravityY - l+grid.myGrid[k][l].centerOfGravityX, 2));
                        if (length <= radius && length > 0) {
                            if (grid.myGrid[k][l].color != Color.WHITE) {
                                colors.add(grid.myGrid[k][l].color);
                            }
                        }
                    }
                }
                else {
                    x=pbcX(k); y=pbcY(l);
                    if(correct(x,y)) {
                        length = Math.sqrt(Math.pow(i + grid.myGrid[i][j].centerOfGravityX - k + grid.myGrid[x][y].centerOfGravityX, 2)
                                + Math.pow(j + grid.myGrid[i][j].centerOfGravityY - l + grid.myGrid[x][y].centerOfGravityX, 2));
                        if (length <= radius && length > 0) {
                            if (grid.myGrid[x][y].color != Color.WHITE) {
                                colors.add(grid.myGrid[x][y].color);
                            }
                        }
                    }
                }
            }

        }

        return getMaxColor(colors);
    }

    public Color getMaxColor(List<Color> colorList){
        if(colorList.isEmpty()) return Color.WHITE;
        int max=0;
        Color cellColor = Color.WHITE;
        for (Color color:colorList) {
            int counter=1;
            for (Color color1:colorList) {
                if(color==color1){
                    counter++;
                }
            }
            if(counter>max){
                cellColor=color;
            }
        }
        return cellColor;
    }

    public boolean correct(int i,int j){
        if(i<0||j<0||i>height-1||j>width-1) return false;
        return true;
    }

    public void printGrid(){
        System.out.println("====================================================================");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(grid.myGrid[i][j].stan+"\t");
            }
            System.out.println();
        }
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }
}
