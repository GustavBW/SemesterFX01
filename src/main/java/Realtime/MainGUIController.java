package Realtime;

import BackEnd.GraphicsProcessor;
import Realtime.interfaces.Interactible;
import Realtime.interfaces.Renderable;
import Realtime.interfaces.Tickable;
import Realtime.inventory.CItem;
import Realtime.inventory.InventoryGUIManager;
import Realtime.triggers.DistanceTrigger;
import Realtime.triggers.RoomExitTrigger;
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

public class MainGUIController extends Application implements Runnable{

    //This class will control the scene, stage and the general GUI setup.
    //This class can take in a RoomCollection and display what it contains

    public static Stage mainStage;
    private Text renderFPSText,interFPSText,tickFPSText;
    private HBox hboxTop;

    //static elements. Always there.
    private BorderPane bp;
    private MouseHandler mouseHandler;
    private InventoryGUIManager iGUIM;
    private RoomTitleText roomTitleText;

    //Player Movement
    private OnKeyPressed keyHandlerDown;
    private OnKeyReleased keyHandlerUp;

    //Graphics stuff
    private Canvas canvas;
    private GraphicsContext gc;
    private static RoomCollection currentCollection;
    private RoomCollection previousRCollection;             //Important for some race conditions and Interactibles and such
    private long logLastCall = 0, distTrigLastTextCall = 0, roomTrigLastCall = 0;
    private String roomTitle = "Room Title";

    public static long logTimeRender = 888,logTimeTick = 888,logTimeInter = 888;
    public static boolean isRunning = false, isReady = false, awaitBoolean = false, sceneChangeRequested = false;
    public static RoomCollection roomToChangeTo;

    @Override
    public void start(Stage stage) throws Exception {
        isRunning = true;
        bp = new BorderPane();
        createLoggingTexts();

        iGUIM = Game.getiGUIM();

        mainStage = stage;
        mainStage.setTitle("WorldOfToxins");

        canvas = new Canvas(Game.WIDTH,Game.HEIGHT);
        gc = canvas.getGraphicsContext2D();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(!Game.isRunning || !MainGUIController.isRunning){
                    stop();
                }
                onUpdate();
            }
        };
        timer.start();

        roomTitleText = new RoomTitleText(roomTitle, 36);

        bp.setCenter(canvas);
        mouseHandler = new MouseHandler();
        Scene scene = new Scene(bp,Game.WIDTH,Game.HEIGHT);
        scene.setOnMouseClicked(e -> mouseHandler.handle(e));

        //Movement/Keyboard
        keyHandlerDown = new OnKeyPressed(this);
        keyHandlerUp = new OnKeyReleased();
        scene.setOnKeyPressed(e -> keyHandlerDown.handle(e));
        scene.setOnKeyReleased(e -> keyHandlerUp.handle(e));


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
        System.exit(69);
    }
    private void onUpdate(){

        GraphicsProcessor gp = new GraphicsProcessor();
        long timeA = System.nanoTime();

        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillRect(0,0,Game.WIDTH,Game.HEIGHT);

        for(Renderable r : Renderable.renderLayer0){
            r.render(gc);
        }
        //for(Renderable r : Renderable.renderLayer1){
        //    r.render(gc);
        //}
        for(Renderable r : Renderable.renderLayer2){
            r.render(gc);
        }
        for(Renderable r : Renderable.renderLayer3){
            r.render(gc);
        }
        for(Renderable r : Renderable.renderLayer4){
            r.render(gc);
        }

        if(InteractionHandler.interactibleReadyToInteract != null){
            displayTemporaryText(InteractionHandler.interactibleReadyToInteract);
        }

        iGUIM.render(gc);

        updateLogText();
        cleanUpExpired();

        long timeB = System.nanoTime();
        logTimeRender = timeB - timeA;

        if(sceneChangeRequested && roomToChangeTo != null){     //Just adding a bit more redundancy here.
            changeScene(roomToChangeTo);
        }
    }               //THIS IS THE GUY YOU'RE LOOKING FOR

    public void displayTemporaryText(Interactible i){

        //I'mma split them up like this, as to be able to customize their looks later

        if(i instanceof DistanceTrigger || i instanceof CItem){
            if(System.currentTimeMillis() > distTrigLastTextCall + 500) {
                new RenderableText(i.getPopUpText(), new Point2D(i.getPosX(), i.getPosY()), 500);
                distTrigLastTextCall = System.currentTimeMillis();
            }
        }
        if(i instanceof RoomExitTrigger){
            if(System.currentTimeMillis() > roomTrigLastCall + 500){
                new RenderableText(i.getPopUpText(), new Point2D(i.getPosX(), i.getPosY()), 500);
                roomTrigLastCall = System.currentTimeMillis();
            }
        }
    }
    public void displayRoomDescription(){

        String text = currentCollection.getRoom().getLongDescription();

        new RoomDescriptionText(text, 50_000);

    }
    public void cleanUpExpired(){
        for(RenderableText t : RenderableText.deadRendText){
            t.destroy();
        }
        RenderableText.deadRendText.clear();

        for(RenderableButton rB : RenderableButton.deadRendButton){
            rB.destroy();
        }
        RenderableButton.deadRendButton.clear();

        for(TemporaryRenderable tR : TemporaryRenderable.tempRends){
            tR.destroy();
        }
        TemporaryRenderable.tempRends.clear();
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
    private void changeScene(RoomCollection rc){

        //This function takes awhile due to the fact that the rest of the threads has to stop what they're doing first.
        //At first it signals to the other threads (that are always looking for this static booleans) that somethings up.
        isReady = false;
        awaitBoolean = false;
        while(!(Game.isAwaiting && InteractionHandler.isAwaiting)){ //Afterwards it goes into a busy-sleep giving a little message in the log awaiting the other threads to declare that they're now waiting
            onAwait();
        }
        onExitFlagSleep();           //Since the step above can anywhere from .1 millisecond to a couple of seconds (it shouldn't, but might happen) it writes a message to the log again.

        long timeA = System.nanoTime(); //Thus the timing starts. Now the fun happens. (The timing is only for me really, so I can see if somethings up

        previousRCollection = currentCollection;    //Keeping track of the previous collection is necessary due to the way CItems can be put back into the world. If done during
                                                    //a scene change, trying to add it to the new Room you're entering, WILL cause a ConcurrentModificationsException. (As you're adding to an arraylist whilest
                                                    //its elements are being read and executed in other places. Also this is only an issue since the Interaction system is on another thread.
        currentCollection = rc;                     //Updating current RC - which is also static meaning you can technically reference all current room components (base images, CItems, triggers...) from anywhere

        clearAllTicksRendersInters();               //Wipe all the lists. Tickables, Renderables, Interactable. NOT Clickable as clickables are UI elements and are updated elsewhere (take the Inventory GUI).
                                                    //Also this is the step that would fuck everything up if the other threads were running at this point. And oh gosh darn I don't want anymore race conditions.
        addNewRenderables(rc);                      //Then read from the arraylist and adds them in.
        addNewInteractibles(rc);
        getThatPlayerBackInThere();                 //Since it might be for the best to just wipe everything. It's easiest to just add the player in afterwards.
        Game.currentRoom = rc.getRoom();            //Kinda redundant. But it's a nice touch.

        roomTitleText.setText(rc.getRoom().getName());

        displayRoomDescription();

        long timeB = System.nanoTime();
        System.out.println("MGUIC.changeScene() took: " + (timeB - timeA) + "ns");  //Hopefully we wont ever reach scene changes taking more than half a second. But we for sure will know exactly how long each was.

        sceneChangeRequested = false;
        isReady = true; //With this flag set - which the other threads are looking for - they'll resume their tasks.
    }
    private void clearAllTicksRendersInters(){
        Interactible.interactibles.clear();
        Tickable.tickables.clear();
        Renderable.renderLayer0.clear();    //Ground layer
        Renderable.renderLayer1.clear();    //Item + NPC + houseWalls
        Renderable.renderLayer2.clear();    //Welp we add the player back in again anyways, so might aswell make sure to just clean up shop.
        Renderable.renderLayer3.clear();    //Rooftops
        Renderable.renderLayer4.clear();    //UI
    }
    private void addNewRenderables(RoomCollection rc){

        //Might seem unecessary, but, it's mostly a matter of redundancy. No need to break the game if a picture wasn't loaded.
        //Problem is, the Renderthread will stop IMMEDIATLY if you try and call "render" on a null-object.

        String status = "";
        boolean success = true;

        if(rc.getBaseImages().get(0) != null) {
            Renderable.renderLayer0.add(rc.getBaseImages().get(0));
        }else{ status += " 0,"; success = false;}

        if(rc.getBaseImages().get(1) != null) {
            Renderable.renderLayer1.add(rc.getBaseImages().get(1));
        }else{ status += " 1,"; success = false;}

        if(rc.getBaseImages().get(2) != null) {                                                            //Layer2 is the player layer, it isn't cleared and isn't modified.
            Renderable.renderLayer3.add(rc.getBaseImages().get(2));
        }else{ status += " 2,"; success = false;}

        if(rc.getBaseImages().get(3) != null) {
            Renderable.renderLayer4.add(rc.getBaseImages().get(3));
        }else{ status += " 3,"; success = false;}

        Renderable.renderLayer1.addAll(rc.getCitems());

        if(success) {   //Also again redundant. But I have a feeling we'll have some hickups
            System.out.println("MGUIC succesfully added new renderables during scene change.");
        }else{
            System.out.println("MGUIC failed to add base image(s): " + status + " during scene change for RoomCollection: " + rc.getRoom().getName());
        }
    }
    private void addNewInteractibles(RoomCollection rc){
        Interactible.interactibles.addAll(rc.getExitTriggers());
    }
    private void getThatPlayerBackInThere(){
        Tickable.tickables.add(Game.player);
        Renderable.renderLayer2.add(Game.player);
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

    public void toggleInventoryGUI(){
        iGUIM.setDoDisplay(!iGUIM.getDisplayStatus());
    }

    public InventoryGUIManager getiGUIM(){return iGUIM;}
    public static RoomCollection getCurrentRoom(){
        if(sceneChangeRequested) {
            return roomToChangeTo;
        }else{
            return currentCollection;
        }
    }
}
