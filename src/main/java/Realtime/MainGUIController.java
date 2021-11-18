package Realtime;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import worldofzuul.Game;
import BackEnd.RoomCollection;

public class MainGUIController extends Application implements Runnable{

    //This class will control the scene, stage and the general GUI setup.
    //This class can take in a RoomCollection and display what it contains

    public static Stage mainStage;
    private static Text renderFPSText,interFPSText,tickFPSText;
    private HBox hboxTop;

    private BorderPane bp;
    private KeyHandler keyHandler;
    private MouseHandler mouseHandler;

    private Canvas canvas;
    private GraphicsContext gc;
    private RoomCollection currentCollection;
    public static boolean isRunning = false, isReady = false;

    @Override
    public void start(Stage stage) throws Exception {
        isRunning = true;
        bp = new BorderPane();
        createLoggingTexts();

        mainStage = stage;
        mainStage.setTitle("WorldOfToxins");


        canvas = new Canvas(Game.WIDTH,Game.HEIGHT);
        gc = canvas.getGraphicsContext2D();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(!Game.isRunning || !MainGUIController.isRunning){
                    this.stop();
                }
                onUpdate();
            }
        };
        timer.start();

        bp.setCenter(canvas);
        keyHandler = new KeyHandler();
        mouseHandler = new MouseHandler();
        Scene scene = new Scene(bp,Game.WIDTH,Game.HEIGHT);
        scene.setOnKeyPressed(e -> keyHandler.handle(e));
        scene.setOnMouseClicked(e -> mouseHandler.handle(e));

        mainStage.setScene(scene);
        mainStage.setOnCloseRequest(e -> stop());
        mainStage.requestFocus();
        isReady = true;
        mainStage.show();
    }

    private void createLoggingTexts() {

        renderFPSText = new Text("R FPS: ...");
        renderFPSText.setDisable(true);
        interFPSText = new Text("I FPS: ...");
        interFPSText.setDisable(true);
        tickFPSText = new Text("T FPS: ...");
        tickFPSText.setDisable(true);

        hboxTop = new HBox(5);
        hboxTop.getChildren().addAll(renderFPSText,interFPSText,tickFPSText);
        hboxTop.setAlignment(Pos.CENTER);

        bp.setTop(hboxTop);
    }

    public static void updateLogText(int id, long deltaNS){
        long fps = 1_000_000_000 / (deltaNS + 1);

        try {
            switch (id) {
                case 1 -> renderFPSText.setText("R FPS: " + fps + " ");
                case 2 -> interFPSText = new Text("I FPS: " + fps + " ");
                case 3 -> tickFPSText = new Text("T FPS: " + fps + " ");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void stop(){
        System.out.println("MGUIC.Stop() called");
        isRunning = false;
        mainStage.close();
    }

    private void onUpdate(){

        long timeA = System.nanoTime();

        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,Game.WIDTH,Game.HEIGHT);

        for(Renderable r : Renderable.renderables){
            r.render(gc);
        }

        long timeB = System.nanoTime();

        try {
            updateLogText(1, timeB - timeA);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void init(){

    }

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void run(){
        isRunning = true;
        String[] args = new String[]{};
        launch(args);
    }

    public void setCollection(RoomCollection rc){this.currentCollection = rc;}

    public RoomCollection getCurrentCollection(){return currentCollection;}

}
