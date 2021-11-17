package worldofzuul;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainGUIController extends Application {

    //This class will control the scene, stage and the general GUI setup.
    //This class can take in a RoomCollection and display what it contains

    public static Stage mainStage;
    private BorderPane bp;

    private Canvas canvas;
    private RoomCollection currentCollection;

    private final int WIDTH,HEIGHT;

    public MainGUIController(int width, int height){
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    @Override
    public void start(Stage stage) throws Exception {

        mainStage = stage;

        bp = new BorderPane();
        canvas = new Canvas(WIDTH,HEIGHT);
        bp.setCenter(canvas);

        mainStage.setScene(new Scene(bp,WIDTH,HEIGHT));
    }

    @Override
    public void init(){

    }

    public void setup(String[] args){
        launch(args);
    }

    public void setCollection(RoomCollection rc){this.currentCollection = rc;}


    public RoomCollection getCurrentCollection(){return currentCollection;}

}
