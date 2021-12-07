package Realtime;

import Realtime.interfaces.Interactible;
import Realtime.interfaces.Renderable;
import Realtime.interfaces.Tickable;
import Realtime.inventory.InventoryGUIManager;
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

import java.nio.charset.StandardCharsets;

public class MainGUIController extends Application implements Runnable{

    //This class will control the scene, stage and the general GUI setup.
    //This class can take in a RoomCollection and display what it contains

    public static Stage mainStage;

    //static elements. Always there.
    private BorderPane bp;  //A borderpane to contain the canvas for easy of moveing the canvas around and/or adding elements around the canvas
    private MouseHandler mouseHandler;  //Dedicated class that takes care of all MouseEvents in the canvas
    private InventoryGUIManager iGUIM;  //Class that displays the contents of the InventoryManager and updates automatically.
    private QuestGUI qGUI;  //Class that displayes all quests for the current room. Updates automatically. Changes colors to indicate when a quest is complete AND when all quests in the room is complete.
    private RoomTitleText roomTitleText;    //Basic renderable element that is hardcoded to show the name of the current room in a specific way in a specific place.
    private RoomDescriptionText currentRoomDescribtion; //Advanced renderable element that uses ExtendedFunction.toLines() to break up Room Descriptions and displays them. Can be clicked through. Shows 4 lines at a time. Automatically closes when there's no more text to show.
    private PerformanceGUI perfGUI;

    //Player Movement
    private OnKeyPressed keyHandlerDown;
    private OnKeyReleased keyHandlerUp;
    private MouseMoveHandler mouseMoveHandler;

    //Graphics stuff
    private Canvas canvas;  //Basic graphical element that is able to display a 2D array of pixels on screen through it's GraphicsContext. Is very Swing-like (java native graphics toolkit) in it's methods to display graphics.
    private GraphicsContext gc; //See above under "Canvas"
    private static RoomCollection currentCollection;    //A data structure designed to keep track of all elements required to make a Room playable.
    private RoomCollection previousRCollection;     //A way of storing all the data associated with the previous room in the case that the current room is changed AS an item is picked up.
    private long logLastCall = 0; //A way of controlling how many times the FPS display is updated by storing the timestamp of last time it was updated.
    private String roomTitle = "Room Title";    //String used by the roomTitleText - kept here as an instance property as that is the best way to keep a default value if loading the first room takes a little longer than expected.

    public static long logTimeRender = 888,logTimeTick = 888,logTimeInter = 888;    //These values is updated across from other class. This is a way to have them have default values and make the available for the other Threads.
    public static boolean isRunning = false, isReady = false, awaitBoolean = false, sceneChangeRequested = false;   //Booleans used to keep track of the game state.
                                                                                                                    //isRunning is checked for by the other threads every frame. If it's ever false, the other threads, if in their main loops, will immediatly stop and shut down.
                                                                                                                    //isReady is used to communicate that even though the thread taking care of the rendering pipeline might be running, it might not currently be able to render to the screen.
                                                                                                                    //sceneChangeRequested is used by the interThread to communicate when the player wants to change room (when a player interacts with a roomExitTrigger)
    public static RoomCollection roomToChangeTo;    //A RoomCollection set by the interThread (unless in case of Game start and Game end) which gives the MainGUIController the data it needs to load in to display the next room

    @Override
    public void start(Stage stage) throws Exception {
        isRunning = true;       //Although it's running, it's not ready. Thus the other threads will await, but will continuesly check for isReady.
        bp = new BorderPane();

        iGUIM = Game.getiGUIM();    //Initiated by Game as these GUI controllers needs some data the MainGUIController doesn't have access to.
        qGUI = Game.getqGUI();
        perfGUI = new PerformanceGUI(); //Initiating the logging texts used to display how well the game is performing right now

        mainStage = stage;
        mainStage.setTitle("World Of Toxins");

        canvas = new Canvas(Game.WIDTH,Game.HEIGHT);
        gc = canvas.getGraphicsContext2D(); //DrawGraphics - a 2D array of pixels each individually overwritable. Displayed on screen every frame

        //The JavaFX Animation Timer throws events as fast as possible. "now" being the timestamp of this frame in nanoseconds.
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(!Game.isRunning || !MainGUIController.isRunning){
                    stop();
                }
                onUpdate(); //Calling the render pipeline below every time Application throws an event of this type.
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
        mouseMoveHandler = new MouseMoveHandler(this);

        //Lambda "Kalkyle" passing off any events recieved to the functions in the appropriate classes
        scene.setOnKeyPressed(e -> keyHandlerDown.handle(e));
        scene.setOnKeyReleased(e -> keyHandlerUp.handle(e));
        scene.setOnMouseMoved(e -> mouseMoveHandler.handle(e));

        mainStage.setScene(scene);
        mainStage.setOnCloseRequest(e -> stop());   //Changing the default close operation to call the stop() method below as the method below takes the other threads into account, where's the default does not.
        mainStage.requestFocus();   //Moving the game window up front.
        mainStage.show();
        isReady = true;
        System.out.println("MGUIC has finished setup at " + System.nanoTime()); //Logging the time the MainGUIController finished up to keep track of performance.
    }

    private void updateLogText() {
        if(System.currentTimeMillis() >= logLastCall + 500) {       //Triggers log updated ~5 times a second
            long rFPS = 1_000_000_000 / (logTimeRender + 1);        //Super fast method of avoiding dividing by zero
            long iFPS = 1_000_000_000 / (logTimeInter + 1);         //Unfortunatly it will make the times occur slower
            long tFPS = 1_000_000_000 / (logTimeTick + 1);          //By a very small fraction

            perfGUI.update(rFPS, iFPS, tFPS);
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

        long timeA = System.nanoTime();

        //STEP 0 - Check for a scene change BEFORE rendering the entire scene
        if(sceneChangeRequested && roomToChangeTo != null){     //Just adding a bit more redundancy here.
            changeScene(roomToChangeTo);
            displayRoomDescription();
        }

        //STEP 1 - Get new drawGraphics from the canvas to have a fresh "canvas" to display
        gc = canvas.getGraphicsContext2D();

        //STEP 2 - Render all room elements in order of which is on top of one another.
        for(Renderable r : Renderable.renderLayer0){    //BASE images - ground.
            r.render(gc);
        }
        for(Renderable r : Renderable.renderLayer1){    //CItems
            r.render(gc);
        }
        for(Renderable r : Renderable.renderLayer2){    //Player
            r.render(gc);
        }
        for(Renderable r : Renderable.renderLayer3){    //Shadows
            r.render(gc);
        }
        for(Renderable r : Renderable.renderLayer4){    //In-room UI like text as well as things above the player like tree-tops and house roofs.
            r.render(gc);
        }

        //STOP 3 - Render all persistent GUI's (GUI's that remain through all rooms) on top of everything else.
        iGUIM.render(gc);
        qGUI.render(gc);
        perfGUI.render(gc);
        roomTitleText.render(gc);

        //STEP 4 - Update PerformanceGUI and clean up temporary renderables.
        updateLogText();
        cleanUpExpired();

        long timeB = System.nanoTime();
        logTimeRender = timeB - timeA;

    }

    public void displayRoomDescription(){

        String text = currentCollection.getRoom().getLongDescription();

        //If there's 1 or less character of description, it wont begin rendering the gui element. The length of a empty string is 1.
        if(text.getBytes(StandardCharsets.UTF_8).length > 2) {
            currentRoomDescribtion = new RoomDescriptionText(text, 50_000); //50 seconds lifetime. Lifetime is disabled though, but still required as it extends from TemporaryRenderable
        }

    }
    public void cleanUpExpired(){
        //All these instances adds themselves to a class-wide arraylist that is here looped through. The elements can't automatically destroy themselves
        //since that would cause issues as they'd remove themselves from the arraylist from which they we're accessed. Thus here it is done in controlled
        //manner right before the next frame is rendered.

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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void run(){
        isRunning = true;
        String[] args = new String[]{}; //Tricking the Application to launch even though this is not initiated by the System thread.
        launch(args);
    }

    public void setCollection(RoomCollection rc){currentCollection = rc;}
    private void changeScene(RoomCollection rc){
        //This function takes awhile due to the fact that the rest of the threads has to stop what they're doing first.
        //At first it signals to the other threads (that are always looking for this static boolean) that somethings up.

        isReady = false;
        awaitBoolean = false;

        while(!(Game.isAwaiting && InteractionHandler.isAwaiting)){
            //Afterwards it goes into a busy-sleep giving a little message
            //in the log awaiting the other threads to declare that they're now waiting
            onAwait();
        }
        onExitFlagSleep();    //Since the step above can anywhere from .1 millisecond to a couple of seconds
                              // (it really shouldn't, but might happen) it writes a message to the log again.

        long timeA = System.nanoTime();     //Timing starts. To keep track of how long it takes.

        clearAllTicksRendersInters();               //Clear all the lists. Tickables, Renderables, Interactable. NOT Clickable as clickables are UI elements and are updated elsewhere (take the Inventory GUI).
                                                    //Also this is the step that would cause a lot of issues if the other threads were running at this point.

        previousRCollection = currentCollection;    //Keeping track of the previous collection is necessary due to the way CItems can be put back into the world. If done during
                                                    //a scene change, trying to add it to the new Room you're entering, WILL cause a ConcurrentModificationsException. (As you're adding to an arraylist whilest
                                                    //its elements are being read and executed in other places. Also this is only an issue since the Interaction system is on another thread.
        currentCollection = rc;                     //Updating current RC - which is also static meaning you can reference all current room components (base images, CItems, triggers...) from anywhere

        addNewRenderables(rc);                      //Then read from that RoomCollection and add all elements back in.
        addNewInteractibles(rc);
        getThatPlayerBackInThere();
        Game.currentRoom = rc.getRoom();            //Updating the variable stored in the Game class as some functionality is running in parallel using the variable in Room and not here. (InventoryManager)

        if(currentRoomDescribtion != null){
            currentRoomDescribtion.destroy();
        }

        roomTitleText.setText(rc.getRoom().getName());  //Updating the Room title

        long timeB = System.nanoTime();
        System.out.println("MGUIC.changeScene() took: " + (timeB - timeA) + "ns");  //Logging info to keep track of performance

        //resetting conditions that caused the scene change
        roomToChangeTo = null;
        sceneChangeRequested = false;
        isReady = true; //With this flag set - which the other threads are looking for - they'll resume their tasks.
    }
    private void clearAllTicksRendersInters(){
        Interactible.interactibles.clear();
        Tickable.tickables.clear();
        Renderable.renderLayer0.clear();    //Ground layer
        Renderable.renderLayer1.clear();    //Item + NPC + houseWalls
        Renderable.renderLayer2.clear();    //Welp we add the player back in again anyways, so might aswell make sure to just clean up shop.
        Renderable.renderLayer3.clear();    //Rooftops + ExitArrows
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


        Renderable.renderLayer3.addAll(rc.getExitTriggers());

        if(success) {   //Also again redundant. But I have a feeling we'll have some hickups
            System.out.println("MGUIC succesfully added new renderables during scene change.");
        }else{
            System.out.println("MGUIC failed to add base image(s): " + status + " during scene change for RoomCollection: " + rc.getRoom().getName());
        }
    }
    private void addNewInteractibles(RoomCollection rc){
        Interactible.interactibles.addAll(rc.getExitTriggers());
        Interactible.interactibles.addAll(rc.getCitems());
    }
    private void getThatPlayerBackInThere(){
        Tickable.tickables.add(Game.player);
        Renderable.renderLayer2.add(Game.player);
    }
    private void onAwait(){
        System.out.print("");       //Tricking the compiler to not skip the while loop
        if(!awaitBoolean){
            System.out.println("MGUIC is Awaiting Game and InteractionHandler at: " + System.nanoTime());
        }
        System.out.print("");
        awaitBoolean = true;
    }
    private void onExitFlagSleep(){
        System.out.println("MGUIC continued from flag sleep at: " + System.nanoTime());
        awaitBoolean = false;
    }
    public void toggleInventoryGUI(){
        iGUIM.setDoDisplay(!iGUIM.getDisplayStatus());
    }
    public void toggleQuestGUI(){
        qGUI.setDisplayStatus(!qGUI.getDisplayStatus());
    }
    public void togglePerformanceGUI(){
        perfGUI.toggleDisplayStatus(!perfGUI.getDisplayStatus());
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
