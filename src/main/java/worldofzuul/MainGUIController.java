package worldofzuul;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainGUIController extends Application {

    //This class will control the scene, stage and the general GUI setup.
    //This class can take in a RoomCollection and display what it contains

    private RoomCollection currentCollection;

    private final int WIDTH,HEIGHT;

    public MainGUIController(int width, int height){
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    @Override
    public void start(Stage stage) throws Exception {

    }









    public void setCollection(RoomCollection rc){this.currentCollection = rc;}


    public RoomCollection getCurrentCollection(){return currentCollection;}

}
