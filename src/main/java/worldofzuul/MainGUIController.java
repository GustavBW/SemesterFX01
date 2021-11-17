package worldofzuul;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainGUIController extends Application implements Runnable {

    //This class will control the scene, stage and the general GUI setup.
    //This class can take in a RoomCollection and display what it contains

    public static Stage mainStage;
    private BorderPane bp;

    private Canvas canvas;
    private GraphicsContext gc;
    private RoomCollection currentCollection;

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
        mainStage.show();
    }

    private void onUpdate(){

        gc.setFill(Color.BLACK);
        gc.fillRect(300,300,300,300);

        for(Renderable r : Renderable.renderables){
            r.render(gc);
        }
    }

    @Override
    public void init(){

    }

    public static void main(String[] args){
        launch(args);
    }

    public void setCollection(RoomCollection rc){this.currentCollection = rc;}

    public RoomCollection getCurrentCollection(){return currentCollection;}

    @Override
    public void run() {
        launch();
    }
}
