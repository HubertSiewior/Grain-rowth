package sample;

public class Grid {
    int width;
    int height;
    Cell[][]myGrid;
    public Grid(int height,int width){
        this.width=width;
        this.height=height;
        myGrid=new Cell[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell[][] getMyGrid() {
        return myGrid;
    }
}
