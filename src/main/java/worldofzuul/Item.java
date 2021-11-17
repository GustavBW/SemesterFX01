package worldofzuul;

import java.util.Objects;

public class Item {

    private String itemName;
    private int itemAmount;
    private final int id;

    public Item(Item item){
        this.itemName = item.getName();
        this.itemAmount = item.getAmount();
        this.id = item.getId();
    }

    public Item(int id, String name){
        this.id = id;
        this.itemName = name;
        this.itemAmount = 1;
    }

    public Item(int itemId, String name, int amount){
        this.id = itemId;
        this.itemName = name;
        this.itemAmount = amount;
    }


    public String getName(){return this.itemName;}
    public int getAmount(){return this.itemAmount;}
    public int getId(){return this.id;}

    public void setAmount(int amount){
        this.itemAmount = amount;
    }

    public void setName(String newName){this.itemName = newName;}
    public void changeAmount(int i){this.itemAmount += i;}
}
