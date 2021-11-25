package Realtime.inventory;

import worldofzuul.Game;

import java.util.ArrayList;

public class InventoryManager {

    private final ArrayList<Item> inventory;
    private final ArrayList<CItem> cinventory;
    private final int inventorySize = 9;

    public InventoryManager(){
        inventory = new ArrayList<>();
        cinventory = new ArrayList<>();
    }

    public boolean removeItem(String name){
        boolean foundItem = false;
        for (Item i : inventory){
            if(i.getName().equalsIgnoreCase(name)){
                inventory.remove(i);
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
                    i.destroy();
                    doesAlreadyContain = true;
                    break;
                }
            }
            if (!doesAlreadyContain) {
                cinventory.add(new CItem(citem.getItem(), citem.getPosPic()));
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
                foundTheThing = true;
                break;
            }
        }
    }
    public void dropCItem(CItem citem){
        for(CItem i : cinventory){
            if(i == citem){
                i.reInstate();
                cinventory.remove(i);
                break;
            }
        }
    }
    public void useCItem(CItem citem){
        if(citem != null) {
            if (useItem(citem.getItem().getName())) {
                citem.destroy();
            }
        }
    }

    public void addItem(Item item){

        boolean containsItem = false;
        boolean ableToFit = false;

        if (inventory.size() <= inventorySize) {
            if (item != null) {                                      //Firstly check if the item is null
                for (int i = 0; i < inventory.size(); i++) {         //Secondly check if it exists in the inventory already
                    if (inventory.get(i).getName().equalsIgnoreCase(item.getName())) {
                        inventory.get(i).changeAmount(item.getAmount());        //And simply increase the amount of it in the inventory
                        containsItem = true;
                        ableToFit = true;
                        System.out.println("Du fandt mere: " + item.getName());
                        break;
                    }
                }
                if (!containsItem) {
                    inventory.add(new Item(item.getId(),item.getName(), item.getAmount()));                                //And put it there
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
            Game.getCurrentRoom().useItem(itemToUse);                                   //Send the item along to currentRoom to be used
            success = true;
        }
        return success;
    }



    public ArrayList<CItem> getCInventory(){return cinventory;}
}
