package worldofzuul;

import BackEnd.ContentEngine;
import BackEnd.GraphicsProcessor;
import BackEnd.RoomCollection;
import BackEnd.TextProcessor;
import Realtime.*;
import Realtime.interfaces.Tickable;
import Realtime.inventory.InventoryGUIManager;
import Realtime.inventory.InventoryManager;

import java.util.ArrayList;

public class Game implements Runnable{

    //A Game by Alexander Birkwald, Emil Frederik Coyte Orland, Gustav Bundgaard Wanscher

    private final Parser parser;

    private final static TextProcessor tp = new TextProcessor();
    private final static GraphicsProcessor gp = new GraphicsProcessor();
    private static MainGUIController mGUIC;
    public static InventoryManager inventoryManager;
    private static InventoryGUIManager iGUIM;
    private static QuestGUI qGUI;
    private final InteractionHandler interHandler;
    public final ArrayList<Room> allRooms = new ArrayList<>();
    private Thread tickThread, interThread, renderThread;

    private int helpCount = 0, hintCount = 0, turnCount = 0, endGameRoomId = 5;
    private boolean hasSwappedToEndgame = false;
    public static double interpolation = 1; //Interpolation is a factor that tells you how long the last tick took compared to having 1 tick per second
                                            //This thus makes it possible to just multiply any movement-vectors with the interpolation, to get all speeds
                                            //As say pixels per second. (It will always be a frame behind, but that's just how it works)
    private long deltaNSInterpolation = 0;
    final int TICKS_PER_SECOND = 60, SKIP_TICKS = 1000 / TICKS_PER_SECOND,MAX_FRAMESKIP = 5;

    public static final int WIDTH = 1500, HEIGHT = 1000, DELAY = 50,  titleScreenRoomId = 8;
    public static Player player;
    public static Room currentRoom;
    public static boolean isRunning = false,onPause = false, isAwaiting = false, awaitBoolean = false, updateQuestGUI = false;

    public static void main(String[] args) {
        new Game();
    }

    public Game() {

        player = new Player( (int) (WIDTH * 0.5)  - 64, (int) (HEIGHT * 0.33), gp.getPlayerGraphics());

        interHandler = new InteractionHandler(player);

        createRooms();
        parser = new Parser();
        inventoryManager = new InventoryManager();
        iGUIM = new InventoryGUIManager(inventoryManager, true);
        mGUIC = new MainGUIController();

        MainGUIController.roomToChangeTo = ContentEngine.getRoomCollection(currentRoom);
        MainGUIController.sceneChangeRequested = true;

        qGUI = new QuestGUI();  //This guy requests MainGUIController.getCurrentRoom() which will return MainGUIController.roomToChange if sceneChangeRequest == true.
                                //However that means this guy can only be instantiated AFTER roomToChangeTo & sceneChangeRequested has been set. Else it gives a nullpointer exception.
        start();
    }

    private void createRooms() {
        allRooms.addAll(ContentEngine.allRoomes());

        for(Room r : allRooms){
           r.setAllExits();
        }

        currentRoom = allRooms.get(titleScreenRoomId);
    }
    public void play()
    {            
        printWelcome();

        awaitBoolean = false;
        while(!MainGUIController.isReady){
            onAwait();
        }
        awaitBoolean = false;

        int loops;
        double next_game_tick = System.currentTimeMillis();

        while(MainGUIController.isRunning && Game.isRunning){
            loops = 0;

            while(!MainGUIController.isReady || MainGUIController.sceneChangeRequested){
                onAwait();
            }
            isAwaiting = false;


            while (System.currentTimeMillis() > next_game_tick
                    && loops < MAX_FRAMESKIP) {
                    //Limiting the loop to an almost constant framerate (60fps)
                    //to help with player movement.
                long timeA = System.nanoTime();

                next_game_tick += SKIP_TICKS;
                loops++;

                if(onPause){continue;}  //Disable tickable evaluation (player) if paused

                if(evaluateGameCompletion() && !hasSwappedToEndgame){
                    MainGUIController.roomToChangeTo = ContentEngine.getRoomCollection(ContentEngine.specificRoom(endGameRoomId));
                    MainGUIController.sceneChangeRequested = true;
                    hasSwappedToEndgame = !hasSwappedToEndgame;
                }
                //Each frame, the tickThread evaluates wether or not to change to the "endgame" rooms

                for(Tickable t : Tickable.tickables){
                    t.tick();
                }
                //Updating GUI's (temporarily disabling their render function to avoid issues).
                if(inventoryManager.inventoryChanged){          //Its the Tick threads (this one) that actually updates the Inventory GUI.
                    iGUIM.createNew(true);
                    inventoryManager.inventoryChanged = false;
                }
                if(updateQuestGUI){             //This boolean is set by the InventoryManager to the value of whether or not using an item was successfull.
                    qGUI.updateQuests();
                    updateQuestGUI = false;
                }

                long timeB = System.nanoTime();
                deltaNSInterpolation = timeB - timeA;
                MainGUIController.logTimeTick = (deltaNSInterpolation);
                interpolation = Math.min((deltaNSInterpolation / 1_000_000_000.0) , 1);      //Updating interpolation.

            }
        }
        quit(new Command(CommandWord.QUIT, null ));

        //Command command = parser.getCommand();
        //finished = processCommand(command);

    }
    private void onAwait(){
        isAwaiting = true;
        if(!awaitBoolean){
            System.out.println("Game was asked to Await. Now awaiting at: " + System.nanoTime());
            awaitBoolean = !awaitBoolean;
        }
        System.out.print("");
    }
    private void onExitFlagSleep(){
        isAwaiting = false;
        System.out.println("Game continued from flag-sleep at SysTimeNS: " + System.nanoTime());
    }

    private void printWelcome()
    {
        System.out.println();
        System.out.println("Velkommen, til Savar distriktet i Bangladesh");
        System.out.println("I dette spil fors??ger vi at kommunikere den nuv??rende tilstand.");
        System.out.println("Skriv '" + CommandWord.HELP + "' for hj??lp.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    private void printHelp() 
    {
        System.out.println("Du farer forvildet om i heden,");
        System.out.println("du kunne nok godt bruge hj??lp.");
        System.out.println();
        System.out.println("Det her, er hvad du har mulighed for:");
        parser.showCommands();
    }
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("Hvor vil du gerne hen ad?");
            return;
        }

        String direction = command.getSecondWord();
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("Det er ikke muligt at g?? denne vej!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }
    public static Room getCurrentRoom(){
        return currentRoom;
    }

    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit hvad?");
            return false;
        }
        else {

            System.out.println();
            System.out.println("Tak for din opm??rksomhed!");
            System.out.println();
            System.out.println("---Spil statestik:---");
            System.out.println("Hj??lp:      " + helpCount);
            System.out.println("Hints:      " + hintCount);
            System.out.println("Handlinger: " + turnCount);

            System.out.println("Game.Stop() called");
            stop();

            return true;
        }
    }
    public synchronized void stop(){
        try{

            tickThread.join();
            interThread.join();
            renderThread.join();
            isRunning = false;
            System.exit(420);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public synchronized void start(){
        isRunning = true;

        interThread = new Thread(interHandler);

        tickThread = new Thread(this);
        renderThread = new Thread(mGUIC);

        interThread.start();
        tickThread.start();
        renderThread.start();

    }

    public boolean evaluateGameCompletion() {
        boolean roomsComplete = true;
        boolean allRoomsVisited = false;

        for (RoomCollection rc : ContentEngine.collections) {
            if (!rc.getRoom().isQuestsSolved()) {
                roomsComplete = false;
            }
        }
        if(ContentEngine.collections.size() >= allRooms.size() - 3){
            allRoomsVisited = true; //The "endgame" rooms consists of 3 rooms.
        }                           //Thus they're removed from the equation.
        return roomsComplete && allRoomsVisited;
    }

    @Override
    public void run() {
        play();
    }

    public static InventoryGUIManager getiGUIM(){return iGUIM;}
    public static MainGUIController getMGUIC(){return mGUIC;}
    public static InventoryManager getInventoryManager(){return inventoryManager;}
    public static QuestGUI getqGUI(){return qGUI;}
}
