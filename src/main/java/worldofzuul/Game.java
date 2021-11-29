package worldofzuul;

import BackEnd.GraphicsProcessor;
import BackEnd.TextProcessor;
import Realtime.*;
import Realtime.interfaces.Tickable;
import Realtime.inventory.InventoryGUIManager;
import Realtime.inventory.InventoryManager;

import java.util.ArrayList;

public class Game implements Runnable{
    private final Parser parser;

    private final static TextProcessor tp = new TextProcessor();
    private final static GraphicsProcessor gp = new GraphicsProcessor();
    private static MainGUIController mGUIC;
    private static InventoryManager inventoryManager;
    private static InventoryGUIManager iGUIM;
    private final InteractionHandler interHandler;
    private final ArrayList<Room> allRooms = new ArrayList<>();
    private Thread tickThread, interThread, renderThread;

    private int helpCount = 0, hintCount = 0, turnCount = 0;
    public static double interpolation = 1; //Interpolation is a factor that tells you how long the last tick took compared to having 1 tick per second
                                            //This thus makes it possible to just multiply any movement-vectors with the interpolation, to get all speeds
                                            //As say pixels per second. (It will always be a frame behind, but that's just how it works)
    private long deltaNSInterpolation = 0;
    final int TICKS_PER_SECOND = 60, SKIP_TICKS = 1000 / TICKS_PER_SECOND,MAX_FRAMESKIP = 5;

    public static final int WIDTH = 1500, HEIGHT = 1000, DELAY = 50;
    public static Player player;
    public static Room currentRoom;
    public static boolean isRunning = false,onPause = false, isAwaiting = false, awaitBoolean = false;

    public static void main(String[] args) {
        new Game();
    }

    public Game() {

        player = new Player( WIDTH / 2,  HEIGHT / 2, gp.getImage());

        interHandler = new InteractionHandler(player);

        createRooms();
        parser = new Parser();
        inventoryManager = new InventoryManager();
        iGUIM = new InventoryGUIManager(inventoryManager, false);
        mGUIC = new MainGUIController();

        start();
        //MainGUIController.main(args);
    }

    private void createRooms() {
        allRooms.addAll(tp.getAllRooms());

        for(Room r : allRooms){
           r.setAllExits();
        }

        currentRoom = allRooms.get(0);
    }
    public void play()
    {            
        printWelcome();

        awaitBoolean = false;
        while(!MainGUIController.isReady){
            onAwait();
        }
        try{Thread.sleep(500);
        }catch (Exception e){e.printStackTrace();}
        onExitFlagSleep();

        int loops;
        double next_game_tick = System.currentTimeMillis();

        while(MainGUIController.isRunning && Game.isRunning){
            loops = 0;

            while(!MainGUIController.isReady){
                onAwait();
            }
            isAwaiting = false;


            while (System.currentTimeMillis() > next_game_tick
                    && loops < MAX_FRAMESKIP) {

                long timeA = System.nanoTime();

                next_game_tick += SKIP_TICKS;
                loops++;


                if(onPause){continue;}

                for(Tickable t : Tickable.tickables){
                    t.tick();
                }
                if(inventoryManager.inventoryChanged){          //Its the Tick threads (this one) that actually updates the Inventory GUI.
                    iGUIM.createNew(false);
                    inventoryManager.inventoryChanged = false;
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
        System.out.print(" ");
    }
    private void onExitFlagSleep(){
        isAwaiting = false;
        System.out.println("Game continued from flag-sleep at SysTimeNS: " + System.nanoTime());
    }

    private void printWelcome()
    {
        System.out.println();
        System.out.println("Velkommen, til Savar distriktet i Bangladesh");
        System.out.println("I dette spil forsøger vi at kommunikere den nuværende tilstand.");
        System.out.println("Skriv '" + CommandWord.HELP + "' for hjælp.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }
    private boolean processCommand(Command command)         //I feel like this should be a switch-case - GBW
    {
        turnCount++;
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        if(commandWord == CommandWord.UNKNOWN) {
            System.out.println("Hvad er det du gerne vil?...");
            return false;
        }
        if (commandWord == CommandWord.HELP) {
            printHelp();
            helpCount++;
        }
        if (commandWord == CommandWord.INVENTORY){
            inventoryManager.printInventory();
        }
        if (commandWord == CommandWord.GATHER){
            inventoryManager.addItem(currentRoom.takeItem(command.getSecondWord()));
        }
        if (commandWord == CommandWord.USE){
            inventoryManager.useItem(command.getSecondWord());
        }
        if (commandWord == CommandWord.INVESTIGATE){
            currentRoom.investigate();
        }
        if (commandWord == CommandWord.DROP){
            currentRoom.addItem(inventoryManager.getItem(command.getSecondWord()));
            if(inventoryManager.removeItem(command.getSecondWord())){
                System.out.println("Du efterlod " + command.getSecondWord() + " i " + currentRoom.getName());
            }
        }
        if (commandWord == CommandWord.HINT){
            if(currentRoom.isQuestsSolved()){
                System.out.println("Du har allerede gjort alt du kunne her.");
            }else {
                System.out.println(currentRoom.getNextQuest().getHint());
            }

            hintCount++;
        }
        if(commandWord == CommandWord.EXITS){
            System.out.println("Du har følgende steder du kan tage hen: " + currentRoom.getExitString());
        }
        else if (commandWord == CommandWord.GO) {
            goRoom(command);
        }
        else if (commandWord == CommandWord.QUIT) {
            wantToQuit = quit(command);
        }
        return wantToQuit;
    }
    private void printHelp() 
    {
        System.out.println("Du farer forvildet om i heden,");
        System.out.println("du kunne nok godt bruge hjælp.");
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
            System.out.println("Det er ikke muligt at gå denne vej!");
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
            System.out.println("Tak for din opmærksomhed!");
            System.out.println();
            System.out.println("---Spil statestik:---");
            System.out.println("Hjælp:      " + helpCount);
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

    @Override
    public void run() {
        play();
    }

    public static InventoryGUIManager getiGUIM(){return iGUIM;}
    public static MainGUIController getMGUIC(){return mGUIC;}
    public static InventoryManager getInventoryManager(){return inventoryManager;}
}
