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
    private RoomCollection previousRCollection;

    private static long logRequestCount1, logRequestCount2,logRequestCount3;
    public static boolean isRunning = false, isReady = false, awaitBoolean = false, sceneChangeRequested = false;
    public static RoomCollection roomToChangeTo;

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
        mainStage.show();
        isReady = true;
        System.out.println("MGUIC has finished setup at " + System.nanoTime());
    }

    private void createLoggingTexts() {

        renderFPSText = new Text("R FPS: Inactive");
        renderFPSText.setDisable(true);
        interFPSText = new Text("I FPS: Inactive");
        interFPSText.setDisable(true);
        tickFPSText = new Text("T FPS: Inactive");
        tickFPSText.setDisable(true);

        hboxTop = new HBox(5);
        hboxTop.getChildren().addAll(renderFPSText,interFPSText,tickFPSText);
        hboxTop.setAlignment(Pos.CENTER);

        bp.setTop(hboxTop);
    }

    public static void updateLogText(int id, long deltaNS){
        long fps = 1_000_000_000 / (deltaNS + 1);

        switch (id) {
            case 1 -> {
                logRequestCount1++;
                if (logRequestCount1 % 100 == 0) {
                    renderFPSText.setText("R FPS: " + fps + " ");
                }
            }
            case 2 -> {
                logRequestCount2++;
                if (logRequestCount2 % 100 == 0) {
                    interFPSText.setText("I FPS: " + fps + " ");
                }
            }
            case 3 -> {
                logRequestCount3++;
                if (logRequestCount3 % 100 == 0) {
                    tickFPSText.setText("T FPS: " + fps + " ");
                }
            }
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
        gc.setFill(Color.DARKRED);
        gc.fillRect(0,0,Game.WIDTH,Game.HEIGHT);

        for(Renderable r : Renderable.renderLayer0){
            r.render(gc);
        }
        for(Renderable r : Renderable.renderLayer1){
            r.render(gc);
        }
        for(Renderable r : Renderable.renderLayer2){
            r.render(gc);
        }
        for(Renderable r : Renderable.renderLayer3){
            r.render(gc);
        }
        for(Renderable r : Renderable.renderLayer4){
            r.render(gc);
        }

        long timeB = System.nanoTime();
        updateLogText(1, timeB - timeA);

        if(sceneChangeRequested){
            changeScene(roomToChangeTo);
        }
    }

    @Override
    public void init(){

    }                                               //This one loads the first RoomCollection in through the CE
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
    private void changeScene(RoomCollection rc){
        isReady = false;
        awaitBoolean = false;
        while(!(Game.isAwaiting && InteractionHandler.isAwaiting)){
            onAwait();
        }
        onExitFlagSleep();
        previousRCollection = currentCollection;
        currentCollection = rc;

        long timeA = System.nanoTime();

        clearAllTicksRendersInters();
        addNewRenderables(rc);
        Game.currentRoom = rc.getRoom();

        long timeB = System.nanoTime();
        System.out.println("MGUIC.changeScene() took: " + (timeB - timeA) + "ns");

        isReady = true;
    }
    private void clearAllTicksRendersInters(){
        Interactible.interactibles.clear();
        Tickable.tickables.clear();
        Renderable.renderLayer0.clear();    //Ground layer
        Renderable.renderLayer1.clear();    //Item + NPC + houseWalls
                                            //Layer2 isn't cleared as that is the player layer
        Renderable.renderLayer3.clear();    //Rooftops
        Renderable.renderLayer4.clear();    //Uh. Something. I've forgotten.
    }
    private void addNewRenderables(RoomCollection rc){
        Renderable.renderLayer0.add(rc.getBaseImages().get(0));
        Renderable.renderLayer1.add(rc.getBaseImages().get(1));
                                                                    //Layer2 is the player layer, it isn't cleared and isn't modified.
        Renderable.renderLayer3.add(rc.getBaseImages().get(3));
        Renderable.renderLayer4.add(rc.getBaseImages().get(4));

        Renderable.renderLayer1.addAll(rc.getCitems());
    }
    private void onAwait(){
        System.out.print(" ");       //Tricking the compiler to not skip the while loop
        if(!awaitBoolean){
            System.out.println("MGUIC is Awaiting Game and InteractionHandler at: " + System.nanoTime());
        }
        System.out.println(" ");
        awaitBoolean = true;
    }
    private void onExitFlagSleep(){
        System.out.println("MGUIC continued from flag sleep at: " + System.nanoTime());
        awaitBoolean = false;
    }
}
