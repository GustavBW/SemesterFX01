package worldofzuul;

import BackEnd.ExitDefinition;
import Realtime.inventory.Item;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;


public class Room {
    private final int id;
    private final String longDesc;
    private final String shortDesc;
    private final String roomName;

    private boolean questsSolved = false;                   //Whether or not you've solved the quest in here
    private boolean hasVisitedBefore = false;

    private final HashMap<String, Room> exits;
    private final ArrayList<Item> itemsInRoom;             //Array for items the room spawns with
    private final ArrayList<Item> itemsUsedInRoom;         //Array for items the player has tried to use in this room

    public static ArrayList<Room> roomsList = new ArrayList<>();

    private ArrayList<Quest> quests;
    private ArrayList<ExitDefinition> exitDefinitions = new ArrayList<>();



    public Room(String roomName, String description) {
        this.id = 0;
        this.longDesc = description;
        this.roomName = roomName;
        this.shortDesc = "No Short Description Given";
        exits = new HashMap<>();

        itemsInRoom = new ArrayList<>();

        itemsUsedInRoom = new ArrayList<Item>();    //As long as we do not have over 100 unique items - we're good.

        roomsList.add(this);
    }

    public Room(int roomId, String name, String shortDesc, String longDesc, ArrayList<Quest> questsInRoom, ArrayList<Item> itemsInRoom, ArrayList<ExitDefinition> allExits){
        this.id = roomId;
        this.itemsInRoom = itemsInRoom;
        this.longDesc = longDesc;
        this.roomName = name;
        this.shortDesc = shortDesc;
        this.quests = questsInRoom;
        this.exitDefinitions = allExits;

        itemsUsedInRoom = new ArrayList<Item>();
        exits = new HashMap<>();

        roomsList.add(this);
    }

    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    public String getName(){return this.roomName;}
    public String getLongDescription()
    {
        return longDesc;
    }
    public String getShortDescription()
    {
        return "You are " + shortDesc + "\n" + getExitString();
    }
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }


    public void investigate(){
        System.out.println(longDesc);

        if(itemsInRoom.size() > 0) {
            System.out.print("Her ser du: ");

            for (Item i : itemsInRoom) {
                System.out.print(i.getName() + " " + i.getAmount() +  " | " );
            }
            System.out.println();
        }else{
            System.out.println("Der er ikke rigtigt noget interessant her.");
        }
    }

    public void setAllExits(){
        for (ExitDefinition exit : exitDefinitions){
            this.setExit(exit.getDirection(), roomsList.get(exit.getRoomId()));
        }
    }
    public String getExitString() {
        StringBuilder returnString = new StringBuilder("Exits:");
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString.append(" ").append(exit).append(" ").append("(").append(exits.get(exit).getName()).append(")").append(" | ");
        }
        return returnString.toString();
    }
    public Item takeItem(String name){
        Item itemWeNeed = null;                                                 //prepare new placeholder item to return
        for (Item i : itemsInRoom) {
            if (i != null) {                                                               //This is kind of redundant as we're now using ArrayLists. No item in here should be null. However, it might be a good thing to have sticking around.
                if (i.getName().equalsIgnoreCase(name)) {                                  //Using null-safe Objects.equals(a,b) on strings is better
                    itemWeNeed = new Item(i.getId(),i.getName(),i.getAmount());            //Make new item as a clone of the one in the room.
                    itemsInRoom.remove(i);                                                                  //Remove the old item.
                    break;
                }
            }
        }
        if(itemWeNeed == null) {System.out.println("Du søger, men finder ikke det du leder efter");}
        return itemWeNeed;                                       //This method may return null and the inventoryManager is set up to handle that.
    }
    public Item takeItem(Item item){
        Item itemWeNeed = null;
        for (int i = 0; i < itemsInRoom.size(); i++) {
            if (itemsInRoom.get(i).getName().equalsIgnoreCase(item.getName())) {
                itemWeNeed = new Item(itemsInRoom.get(i).getId(),itemsInRoom.get(i).getName(),itemsInRoom.get(i).getAmount());
                itemsInRoom.remove(i);
                break;
            }
        }
        if(itemWeNeed == null){System.out.println("Du søger, men finder ikke det du leder efter.");}
        return itemWeNeed;                              //This method may return null, but that's alright.
    }

    public void addItem(Item item){             //This guy adds a single item back to the room. Called from the Inventory Manager when the player inventory is full.
        boolean contains = false;

        if(item != null) {                          //No funny business here
            for (Item roomItem : itemsInRoom) {     //traverse the array
                if (roomItem.getName().equalsIgnoreCase(item.getName())) {       //If the item already exists in the room "Add more amount" of it.
                    roomItem.changeAmount(item.getAmount());
                    contains = true;
                    break;
                }
            }
            if(!contains){                                          //If it does not exist in the room
                itemsInRoom.add(new Item(item.getId(),item.getName(),item.getAmount()));    //Put a copy of the item there - to prevent errors don't put "it" there.
            }
        }
    }
    public boolean useItem(Item item){
        boolean hasTriedBefore = false;
        boolean isQuestItem = false;
        boolean success = false;
        Quest foundQuest = null;

        if(item != null) {                                                          //Just... to make sure u'know
            for (Quest q : quests){
                if (q.getQuestItemId() == item.getId()){
                    isQuestItem = true;
                    foundQuest = q;
                    break;
                }
            }
            if (!isQuestItem) {
                for (Item itemUsed : itemsUsedInRoom) {                                 //Check and see if the player already attempted this
                    if (itemUsed != null && itemUsed.getId() == item.getId()) {                         //See if its in the array already
                        System.out.println("Du har allerede forsøgt at bruge " + item.getName() + " i " + roomName);
                        hasTriedBefore = true;
                        addItem(item);                                              //Adds the item back into itemsInRoom array to be picked up
                        break;
                    }
                }
            }
            if (!hasTriedBefore) {                                                      //If the player hasnt attempted this before in this room
                if(!isQuestItem) {
                    itemsUsedInRoom.add(new Item(item.getId(), item.getName(), item.getAmount()));                        //Add a copy of the item to the array
                    System.out.println("Godt arbejde, du brugte " + item.getName() + " i " + roomName);
                }

                if (foundQuest != null && !foundQuest.isComplete()) {                                          //Check if the item used is the same as the needed item in this room

                    foundQuest.changeAmountNeeded(-1 * item.getAmount());

                    if(foundQuest.getAmountNeeded() <= 0) {
                        foundQuest.setQuestStatus(true);
                        System.out.println(foundQuest.getDesc());
                        success = true;
                    }else {
                        System.out.println("Det lader til at gøre en forskel, men det er ikke helt nok.");
                        success = true;
                    }
                }
            }
            if(questsSolved){
                System.out.println("Du har allerede gjort alt hvad du kunne her.");
            }
            if (isQuestsSolved() && !questsSolved){
                System.out.println(roomName + " er nu færdig!");
                questsSolved = true;
            }
        }else{
            System.out.println("Error: Item provided to Room.useItem is null " + Thread.currentThread().getStackTrace()[2].getLineNumber());
        }
        return success;
    }

    public ArrayList<Quest> getQuests(){return quests;}
    public Quest getNextQuest(){
        Quest questToReturn = null;
        for(Quest q : quests){
            if(!q.isComplete()){
                questToReturn = q;
                break;
            }
        }
        return questToReturn;
    }
    public Boolean isQuestsSolved(){
        boolean someBool = true;
        for(Quest q : quests){
            if(q.isComplete()){

            }else{
                someBool = false;
            }
        }
        return someBool;
    }

    public int getId(){return id;}

    public ArrayList<ExitDefinition> getExits(){return this.exitDefinitions;}
}

