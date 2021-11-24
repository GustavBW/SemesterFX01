package Realtime;

import Realtime.interfaces.Interactible;
import Realtime.interfaces.Renderable;
import Realtime.interfaces.Tickable;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
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
import worldofzuul.InventoryManager;

import java.util.ArrayList;

public class MainGUIController extends Application implements Runnable{

    //This class will control the scene, stage and the general GUI setup.
    //This class can take in a RoomCollection and display what it contains

    public static Stage mainStage;
    private Text renderFPSText,interFPSText,tickFPSText;
    private HBox hboxTop;

    private BorderPane bp;
    private KeyHandler keyHandler;
    private MouseHandler mouseHandler;
    private ArrayList<CItem> cinventory;

    //DisplayInventory Stuff
    final int mainFrameWidth = 300, mainFrameHeight = 400, buttonSize = 30, buttonPadding = 5;
    final double mainFramePosX = (Game.WIDTH / 2.0) - (mainFrameWidth / 2.0);
    final double mainFramePosY = (Game.HEIGHT / 2.0) - (mainFrameHeight / 2.0);
    long lastCallForInventoryContent = 0;

    //Graphics stuff
    private Canvas canvas;
    private GraphicsContext gc;
    private static RoomCollection currentCollection;
    private RoomCollection previousRCollection;             //Redundant. But might get important for some race conditions
    private long logLastCall = 0, distTrigLastTextCall = 0;

    public static long logTimeRender = 888,logTimeTick = 888,logTimeInter = 888;
    public static boolean isRunning = false, isReady = false, awaitBoolean = false, sceneChangeRequested = false, showInventory = false;
    public static RoomCollection roomToChangeTo;

    @Override
    public void start(Stage stage) throws Exception {
        isRunning = true;
        bp = new BorderPane();
        createLoggingTexts();

        cinventory = Game.getInventoryManager().getCInventory();

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

        new DistanceTrigger(Game.WIDTH / 2,Game.HEIGHT / 2,400);

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

        renderFPSText = new Text("R FPS: Inactive ");
        renderFPSText.setDisable(true);
        interFPSText = new Text("I FPS: Inactive ");
        interFPSText.setDisable(true);
        tickFPSText = new Text("T FPS: Inactive ");
        tickFPSText.setDisable(true);

        hboxTop = new HBox(5);
        hboxTop.getChildren().addAll(renderFPSText,interFPSText,tickFPSText);
        hboxTop.setAlignment(Pos.CENTER);

        bp.setTop(hboxTop);
    }

    private void updateLogText() {
        if(System.currentTimeMillis() >= logLastCall + 500) {       //Triggers log updated ~2 times a second
            long rFPS = 1_000_000_000 / (logTimeRender + 1);        //Super fast method of avoiding dividing by zero
            long iFPS = 1_000_000_000 / (logTimeInter + 1);         //Unfortunatly it will make the times occur slower
            long tFPS = 1_000_000_000 / (logTimeTick + 1);          //By a very small fraction

            renderFPSText.setText(" R/s : " + rFPS + " ");
            interFPSText.setText(" I/s : " + iFPS + " ");
            tickFPSText.setText(" T/s : " + tFPS + " ");

            logLastCall = System.currentTimeMillis();
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

        if(showInventory){
            displayInventory();
        }

        if(InteractionHandler.interactibleReadyToInteract != null){
            displayTemporaryText(InteractionHandler.interactibleReadyToInteract);
        }

        updateLogText();
        cleanUpExpired();

        long timeB = System.nanoTime();
        logTimeRender = timeB - timeA;

        if(sceneChangeRequested){
            changeScene(roomToChangeTo);
        }
    }

    private void displayInventory(){

        gc.setFill(new Color(0,0,0,0.3));
        gc.fillRoundRect(mainFramePosX,mainFramePosY,mainFrameWidth,mainFrameHeight,50,50);

        if(System.currentTimeMillis() > lastCallForInventoryContent + 100) {
            /*
            for (int i = 0; i < cinventory.size(); i++) {
                Point2D position = new Point2D(mainFramePosX + buttonSize,((mainFramePosY + (mainFrameHeight * 0.11)) + (i * (buttonSize + buttonPadding))) - (buttonSize / 2.0));
                new CItemButton(cinventory.get(i), position, Game.getInventoryManager(), buttonSize, buttonSize, 100);

                position = new Point2D(mainFramePosX + buttonSize + (buttonSize * 2),(mainFramePosY + (mainFrameHeight * 0.11)) + (i * (buttonSize + buttonPadding)));   //Reenable this when there's some Citems
                new RenderableText(cinventory.get(i).getName() + " " + cinventory.get(i).getAmount(), position, 100);
            }
             */

            for (int i = 0; i < 10; i++) {
                Point2D position = new Point2D(mainFramePosX + buttonSize,((mainFramePosY + (mainFrameHeight * 0.11)) + (i * (buttonSize + buttonPadding))) - (buttonSize / 2.0));   //Disable this when there's some Citems
                new CItemButton(null, position, Game.getInventoryManager(), buttonSize, buttonSize, 100);

                position = new Point2D(mainFramePosX + buttonSize + (buttonSize * 2),(mainFramePosY + (mainFrameHeight * 0.11)) + (i * (buttonSize + buttonPadding)));
                new RenderableText("Stuff & Amount", position, 100);
            }

            lastCallForInventoryContent = System.currentTimeMillis();
        }


    }

    public void displayTemporaryText(Interactible i){
        if(i instanceof DistanceTrigger || i instanceof CItem){
            if(System.currentTimeMillis() > distTrigLastTextCall + 500) {
                new RenderableText(i.getPopUpText(), new Point2D(i.getPosX(), i.getPosY()), 500);
                distTrigLastTextCall = System.currentTimeMillis();
            }
        }
    }

    public void cleanUpExpired(){
        for(RenderableText t : RenderableText.deadRendText){
            t.destroy();
        }
        RenderableText.deadRendText.clear();
        for(CItemButton b : CItemButton.deadItemButtons){
            b.destroy();
        }
        CItemButton.deadItemButtons.clear();
    }

    @Override
    public void init(){

    }//This one loads the first RoomCollection in through the CE

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void run(){
        isRunning = true;
        String[] args = new String[]{};
        launch(args);
    }

    public void setCollection(RoomCollection rc){currentCollection = rc;}
    public RoomCollection getCurrentCollection(){return currentCollection;}
    private void changeScene(RoomCollection rc){
        isReady = false;
        awaitBoolean = false;
        while(!(Game.isAwaiting && InteractionHandler.isAwaiting)){
            onAwait();
        }
        onExitFlagSleep();

        long timeA = System.nanoTime();

        previousRCollection = currentCollection;
        currentCollection = rc;
        
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
        Renderable.renderLayer3.add(rc.getBaseImages().get(2));
        Renderable.renderLayer4.add(rc.getBaseImages().get(3));

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

    public static RoomCollection getCurrentRoom(){
        if(sceneChangeRequested) {
            return roomToChangeTo;
        }else{
            return currentCollection;
        }
    }
}
