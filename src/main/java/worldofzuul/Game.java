package worldofzuul;

import java.util.ArrayList;

import static javafx.application.Application.launch;

public class Game implements Runnable{
    private Parser parser = null;
    private static Room currentRoom;
    private static TextProcessor tp = new TextProcessor();
    private InventoryManager inventoryManager = null;
    private InteractionHandler interHandler;
    private Player player;

    private final ArrayList<Room> allRooms = new ArrayList<>();

    public static Thread tickThread, interThread;

    private int helpCount = 0, hintCount = 0, turnCount = 0;
    public static final int WIDTH = 1500, HEIGHT = 1000;
    public static boolean isRunning = false;

    public static void main(String[] args) {
        new Game(args);
    }

    public Game(String[] args) {

        isRunning = true;

        player = new Player((int) WIDTH / 2, (int) HEIGHT / 2, null);

        interHandler = new InteractionHandler(player);
        interThread = new Thread(interHandler);
        interThread.start();

        tickThread = new Thread(this);
        tickThread.start();

        MainGUIController.main(args);
    }

    private void createRooms() {
        for(Room r : tp.getAllRooms()){
            allRooms.add(r);
        }

        for(Room r : allRooms){
           r.setAllExits();
        }

        currentRoom = allRooms.get(0);
    }

    public void play() 
    {            
        printWelcome();

                
        boolean finished = false;
        while (! finished) {
            if(!MainGUIController.isRunning){
                quit(new Command(CommandWord.QUIT, null ));
            }
            Command command = parser.getCommand();
            finished = processCommand(command);
            System.out.println("looooooop");
        }
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

            isRunning = false;

            System.out.println();
            System.out.println("Tak for din opmærksomhed!");
            System.out.println();
            System.out.println("---Spil statestik:---");
            System.out.println("Hjælp:      " + helpCount);
            System.out.println("Hints:      " + hintCount);
            System.out.println("Handlinger: " + turnCount);

            stop();

            return true;
        }
    }

    public synchronized void stop(){
        try{
            tickThread.join();
            interThread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        createRooms();
        parser = new Parser();
        inventoryManager = new InventoryManager();

        play();
    }
}
