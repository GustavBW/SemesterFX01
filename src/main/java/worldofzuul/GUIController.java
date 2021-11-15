package worldofzuul;

import javafx.application.Application;
import javafx.stage.Stage;

public class GUIController extends Application {

    //This class will control the scene, stage and the general GUI setup.
    //This class can take in a RoomCollection and display what it contains

    private RoomCollection currentCollection;

    public GUIController(){
        initializeGUI();
    }

    @Override
    public void start(Stage stage) throws Exception {

    }

    public boolean initializeGUI(){
        boolean success = false;

        return success;
    }







    public void setCollection(RoomCollection rc){this.currentCollection = rc;}


    public RoomCollection getCurrentCollection(){return currentCollection;}

}
