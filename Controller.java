package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable {
    @FXML
    TextField getWidth;
    @FXML
    TextField getHeight;
    @FXML
    TextField inRow;
    @FXML
    TextField inKol;
    @FXML
    TextField getHowManyCell;
    @FXML
    TextField getRadius;
    @FXML
    ComboBox<String> layout;
    @FXML
    ComboBox<String> growth;
    @FXML
    CheckBox PBC;
    @FXML
    Button init;
    @FXML
    Button start;
    @FXML
    Button stop;
    @FXML
    Canvas canvas;

    private int width;
    private int height;
    private int radius;
    private int quantityInKol;
    private int quantityInRow;
    private Grid grid;
    private boolean periodic;
    private Affine affine;
    private Calculations calculations;
    private int conter;
    private boolean inInit=false;
    private GrainGrowth grainGrowth;
    private Map<Integer,Color> colorMap;
    private AnimationTimer animationTimer;
    private boolean inAnimationTimer=false;
    private Boolean paused=false;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        layout.setItems(FXCollections.observableArrayList("Homogeneous", "WithRadius", "Clicking", "Random"));
        growth.setItems(FXCollections.observableArrayList("VonNeumann","PentagonalrRandom","HexagonalRight","HexagonalLeft",
                "HexagonalRandom","Moore","WithRadius","Random"));
        layout.setValue("Random");
        growth.setValue("VonNeumann");
        getHeight.setText("300");
        getWidth.setText("300");
        getRadius.setText("5");
        inKol.setText("20");
        inRow.setText("10");
        getHowManyCell.setText("100");
        colorMap=new HashMap<>();
    }

    public void initButtom(){
        if(inAnimationTimer){
            animationTimer.stop();
            inAnimationTimer=false;
        }
        affine=new Affine();
        width=Integer.parseInt(getWidth.getText());
        height=Integer.parseInt(getHeight.getText());
        radius=Integer.parseInt(getRadius.getText());
        periodic=PBC.isSelected();
        quantityInKol=Integer.parseInt(inKol.getText());
        quantityInRow=Integer.parseInt(inRow.getText());
        conter=Integer.parseInt(getHowManyCell.getText());
        calculations=new Calculations(quantityInRow,quantityInKol,width,height,radius,conter);
        if(layout.getValue().equals("Random"))calculations.setRandomGrid();
        else if(layout.getValue().equals("WithRadius"))calculations.setRadiusGrid();
        else if(layout.getValue().equals("Homogeneous"))calculations.setHomogeneousGrid();
        else if(layout.getValue().equals("Clicking")){
            inInit=true;
            calculations.setStartGrid();
        }
        grid=calculations.getGrid();
        canvas.setOnMousePressed(this::handleDraw);
        affine.appendScale(canvas.getWidth()/width,canvas.getHeight()/height);
        Drawing.drawGrid(canvas,grid,width,height);
        grainGrowth=new GrainGrowth(grid,width,height,radius,conter,periodic);
    }

    private void handleDraw(MouseEvent mouseEvent) {
        if(inInit) {
            double mouseX=mouseEvent.getX();
            double mouseY=mouseEvent.getY();
            try {
                Point2D simCoord = affine.inverseDeltaTransform(mouseX, mouseY);
                int simX = (int) simCoord.getX();
                int simY = (int) simCoord.getY();
                setList(simX, simY);
            } catch (NonInvertibleTransformException e) {
                e.printStackTrace();
            }
        }
    }
    public void setList(int i,int j){
        Random generator = new Random();
        if(grid.myGrid[j][i].stan==0){
            grid.myGrid[j][i].stan=1;
            Boolean add=true;
            while (add){
                int r=Math.abs(generator.nextInt()%256);
                int g=Math.abs(generator.nextInt()%256);
                int b=Math.abs(generator.nextInt()%256);
                if(!colorMap.containsValue(Color.rgb(r,g,b))){
                    colorMap.put(grid.myGrid[j][i].id,Color.rgb(r,g,b));
                    grid.myGrid[j][i].color= Color.rgb(r,g,b);
                    add=false;
                }
            }
        }else {
            grid.myGrid[j][i].color=Color.WHITE;
            grid.myGrid[j][i].stan=0;
        }
        Drawing.drawGrid(canvas,grid,width,height);

    }
    public void startButtom(){
        paused=false;
        inAnimationTimer=true;
        inInit=false;
        animationTimer=new AnimationTimer() {
            @Override
            public void handle(long l) {
                    drawStep();
            }
        };
        try { animationTimer.start();
        } catch (NullPointerException nullException) {
            nullException.printStackTrace();
        }
    }
    public void drawStep(){
        try {
            grid=grainGrowth.step(growth.getValue());
            Drawing.drawGrid(canvas,grid,width,height);
            grainGrowth.setGrid(grid);
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void stopButton(){
        if(!paused){
            try { animationTimer.stop();
                paused=true;
            } catch (NullPointerException nullException) {
                nullException.printStackTrace();
            }
        } else{
            try { animationTimer.start();
                paused=false;
            } catch (NullPointerException nullException) {
                nullException.printStackTrace();
            }
        }
    }



    public static void closeProgram() {
        Platform.exit();
        System.exit(0);
    }
}
