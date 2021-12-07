package Realtime.inventory;

import BackEnd.ContentEngine;
import Realtime.MainGUIController;
import Realtime.QuestType;
import worldofzuul.Game;
import worldofzuul.Quest;

import java.util.ArrayList;
import java.util.Objects;

public class InventoryManager {

    private final ArrayList<CItem> cinventory;
    private final int inventorySize = 10;

    public boolean inventoryChanged = false, questsChanged = false;

    public InventoryManager(){
        cinventory = new ArrayList<>();
    }

    public boolean addCItem(CItem citem){
        boolean success = false;
        boolean wasAlreadyInInventory = false;
        boolean isQuestItem = false;

        if(citem != null){
            if(cinventory.size() < inventorySize) {

                for (Quest q : Game.getCurrentRoom().getQuests()) {
                    isQuestItem = q.evaluateItem(citem, QuestType.PickUP);
                }

                for (CItem c : cinventory) {
                    if (c.getId() == citem.getId()) {
                        wasAlreadyInInventory = true;
                        c.changeAmount(citem.getAmount());
                        success = true;
                    }
                }

                if (!wasAlreadyInInventory) {
                    cinventory.add(citem);
                    //ContentEngine.addItemToCache(citem);
                    success = true;
                }

                inventoryChanged = true;
            }
        }
        return success;
    }


    public boolean removeCItem(CItem citem){       //  Removes item on a GAME level
        boolean removedTheThingFromRoom = false;
        boolean removedTheThingFromInventory = false;
        boolean citemWasRemovedFromPlay = false;

        for(CItem i : cinventory){
            if(i == citem) {

                citemWasRemovedFromPlay = i.destroy();
                removedTheThingFromInventory = cinventory.remove(i);
                removedTheThingFromRoom = MainGUIController.getCurrentRoom().getCitems().remove(this);

                inventoryChanged = removedTheThingFromInventory;
                break;
            }
        }

        return removedTheThingFromInventory && removedTheThingFromRoom && citemWasRemovedFromPlay;
    }
    public void dropCItem(CItem citem){
        if(citem != null){
            citem.reInstate();
            inventoryChanged = true;
            cinventory.remove(citem);
        }
    }
    public boolean useCItem(CItem citem){
        boolean success = false;
        boolean isQuestItem = false;

        if(citem != null){

            for(Quest q : Game.getCurrentRoom().getQuests()){
                success = q.evaluateItem(citem, QuestType.Use);
                isQuestItem = success;
            }

            if(isQuestItem){
                if(!(citem.getId() == 11 || citem.getName().equalsIgnoreCase("Kamera"))) {    //Yay for Nor gates
                    Game.updateQuestGUI = removeCItem(citem);
                    inventoryChanged = Game.updateQuestGUI;

                }else{


                    Game.updateQuestGUI = true;
                    inventoryChanged = true;
                }
            }
        }

        return success;
    }

    public void printInventory(){
        System.out.println();
        for (Item n : cinventory){
            if(n != null) {
                System.out.print(n);
            }
        }
        System.out.println("");
    }

    public ArrayList<CItem> getCInventory(){return cinventory;}
}
