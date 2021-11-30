package Realtime.inventory;

import Realtime.MainGUIController;
import worldofzuul.Game;
import worldofzuul.Quest;

import java.util.ArrayList;
import java.util.Objects;

public class InventoryManager {

    private final ArrayList<Item> inventory;
    private final ArrayList<CItem> cinventory;
    private final int inventorySize = 9;

    public boolean inventoryChanged = false, questsChanged = false;

    public InventoryManager(){
        inventory = new ArrayList<>();
        cinventory = new ArrayList<>();
    }

    public boolean removeItem(String name){
        boolean foundItem = false;
        for (Item i : inventory){
            if(i.getName().equalsIgnoreCase(name)){
                inventory.remove(i);
                inventoryChanged = true;
                foundItem = true;
                break;
            }
        }
        if(!foundItem){
            System.out.println("Sådan en ting har du ikke på dig");
        }
        return foundItem;
    }

    public void addCItem(CItem citem){
        boolean doesAlreadyContain = false;
        if(cinventory.size() < inventorySize) {
            for (CItem i : cinventory) {
                if(i == citem) {
                    i.changeAmount(citem.getAmount());
                    inventoryChanged = true;
                    i.destroy();
                    doesAlreadyContain = true;
                    break;
                }
            }
            if (!doesAlreadyContain) {
                cinventory.add(new CItem(citem.getItem(), citem.getPosPic()));
                Game.updateQuestGUI = evaluateItemOnPickUp(citem);
                    //evaluateItemOnPickUp goes through all quests and sees if this item is relevant AND if the quest type is PickUp. If it aint QuestType.PickUp, Quest.evaluateItemOnPickUp
                    //will always return false. Thus not asking Game to update the Quest GUI.

                inventoryChanged = true;
                citem.destroy();
            }
        }else{
            citem.reInstate();
        }
        addItem(citem.getItem());
    }
    public void removeCItem(CItem citem){
        boolean foundTheThing = false;
        for(CItem i : cinventory){
            if(i == citem) {
                i.destroy();
                cinventory.remove(i);
                inventoryChanged = true;
                foundTheThing = true;
                break;
            }
        }
    }
    public void dropCItem(CItem citem){
        for(CItem i : cinventory){
            if(i == citem){
                i.reInstate();
                inventoryChanged = true;
                cinventory.remove(i);
                break;
            }
        }
    }
    public boolean useCItem(CItem citem){
        if(citem != null) {
            if (useItem(citem.getItem().getName())) {
                inventoryChanged = true;

                if(!citem.getItem().getName().equalsIgnoreCase("Kamera")) {
                    citem.destroy();
                }
            }else{
                if(!citem.getItem().getName().equalsIgnoreCase("Kamera")) {
                    citem.reInstate();
                }
            }
        }
        return false;
    }

    public void addItem(Item item){

        boolean containsItem = false;
        boolean ableToFit = false;

        if (inventory.size() <= inventorySize) {
            if (item != null) {                                      //Firstly check if the item is null
                for (int i = 0; i < inventory.size(); i++) {         //Secondly check if it exists in the inventory already
                    if (inventory.get(i).getName().equalsIgnoreCase(item.getName())) {
                        inventory.get(i).changeAmount(item.getAmount());        //And simply increase the amount of it in the inventory
                        inventoryChanged = true;
                        containsItem = true;
                        ableToFit = true;
                        System.out.println("Du fandt mere: " + item.getName());
                        break;
                    }
                }
                if (!containsItem) {
                    inventory.add(new Item(item.getId(),item.getName(), item.getAmount()));                                //And put it there
                    inventoryChanged = true;
                    System.out.println("Du har nu en ny ting: " + item.getName());
                    ableToFit = true;

                }
            }
        }
        if(!ableToFit && item != null){                                     //If you inventory does not contain the item, and doesn't have space for it.
            System.out.println("Du har ikke mere plads i din rygsæk!");
            Game.getCurrentRoom().addItem(item);            //Put that thing back where you found it.
        }
    }
    public Item getItem(String name){
        Item itemFound = null;
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(name)) {
                itemFound = item;
            }
        }
        return itemFound;
    }
    public void printInventory(){
        System.out.println();
        for (Item n : inventory){
            if(n != null) {
                System.out.print(n.getName() + " " + n.getAmount() + " | ");
            }
        }
        System.out.println("");
    }
    public boolean useItem(String name){           //Called from Game.processCommand, triggered by CMD Word USE
        boolean success = false;
        Item itemToUse = null;                  //Prepare an item
        boolean dontEvenThinkAboutIt = false;

        if(name != null) {                      //Just to be sure
            for (Item i : inventory) {                                                   //traverse array
                if (i.getName().equalsIgnoreCase(name)) {                                 //Null-safe equals. Checking if you got the item in the first place
                    itemToUse = new Item(i.getId(),i.getName(), i.getAmount());     //If you do, initiate itemToUse as a clone of that item
                    if(!name.equalsIgnoreCase("Kamera")) {
                        inventory.remove(i);                                                 //Now remove that item from your inventory
                        inventoryChanged = true;
                    }
                    break;
                }
            }
        }else {                                                                         //If you forget to write the name of the item
            System.out.println("Det er en mærkelig ting at bruge her. Hvad med om du er lidt mere specifik?");
            dontEvenThinkAboutIt = true;
        }
        if(itemToUse == null && !dontEvenThinkAboutIt){                                                          //If you dont have any of the item youre trying to use
            System.out.println("Du har ikke noget " + name);
        }
        if(itemToUse != null){
            success = Game.getCurrentRoom().useItem(itemToUse);                                   //Send the item along to currentRoom to be used
        }
        Game.updateQuestGUI = true;
        return success;
    }

    private boolean evaluateItemOnPickUp(CItem citem){
        return evaluateItemOnPickUp(citem.getItem());
    }
    private boolean evaluateItemOnPickUp(Item item){
        boolean isIt = false;
        for(Quest q : MainGUIController.getCurrentRoom().getRoom().getQuests()){
            isIt = q.evaluateItemOnPickUp(item);
        }
        return isIt;
    }


    public ArrayList<CItem> getCInventory(){return cinventory;}
}
