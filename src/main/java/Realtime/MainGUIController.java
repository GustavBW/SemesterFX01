package Realtime;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import worldofzuul.Game;
import BackEnd.RoomCollection;

public class MainGUIController extends Application implements Runnable{

    //This class will control the scene, stage and the general GUI setup.
    //This class can take in a RoomCollection and display what it contains

    public static Stage mainStage;
    private BorderPane bp;

    private Canvas canvas;
    private GraphicsContext gc;
    private RoomCollection currentCollection;
    public static boolean isRunning = false;

    @Override
    public void start(Stage stage) throws Exception {

        mainStage = stage;
        mainStage.setTitle("WorldOfToxins");

        bp = new BorderPane();
        canvas = new Canvas(Game.WIDTH,Game.HEIGHT);
        gc = canvas.getGraphicsContext2D();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();

        bp.setCenter(canvas);

        mainStage.setScene(new Scene(bp,Game.WIDTH,Game.HEIGHT));
        mainStage.setOnCloseRequest(e -> stop());
        mainStage.show();
    }

    public synchronized void stop(){
        isRunning = false;
    }

    private void onUpdate(){

        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,Game.WIDTH,Game.HEIGHT);

        for(Renderable r : Renderable.renderables){
            r.render(gc);
        }
    }

    @Override
    public void init(){

    }

    public static void main(String[] args) {
        isRunning = true;
        launch(args);
    }

    @Override
    public void run(){
        String[] args = new String[]{};
        main(args);
    }

    public void setCollection(RoomCollection rc){this.currentCollection = rc;}

    public RoomCollection getCurrentCollection(){return currentCollection;}

}
