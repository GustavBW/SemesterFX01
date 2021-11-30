package worldofzuul;

import Realtime.QuestType;
import Realtime.inventory.Item;

public class Quest {
    private int id;
    private String desc;
    private String hint;
    private int itemId;
    private boolean completed = false;
    private int amountNeeded, originalAmount;
    private QuestType type;

    public Quest(int id, String desc, String hint, int itemId, int amount) {
        this.id = id;
        this.desc = desc;
        this.hint = hint;
        this.itemId = itemId;

        if(amount <= 0){
            this.amountNeeded = 1;
            this.originalAmount = 1;
        } else {
            this.amountNeeded = amount;
            this.originalAmount = amount;
        }

        this.type = QuestType.Use;
    }

    public Quest(int id, String desc, String hint, int itemId, int amount, String type){
        this(id, desc, hint, itemId, amount);

        if(type == null) {
            this.type = QuestType.Use;

        }else{
            if(type.equalsIgnoreCase("PickUp")) {
                this.type = QuestType.PickUP;
            }else{
                this.type = QuestType.Use;
            }
        }

    }

    public Quest(int id, String desc, String hint, int itemId) {
        this(id, desc, hint, itemId, 1);
    }
    public Quest(){
        this(999999, "Test Quest", "Test Quest", 999999, 1);
    }

    public String getDesc() {
        return this.desc;
    }
    public String getHint() {
        String toReturn = "Du har gjort alt hvad du kunne her";

        if(!completed){
            toReturn = hint;
        }

        return toReturn;
    }
    public boolean isComplete() {
        return this.completed;
    }

    public boolean evaluateItemOnPickUp(Item item){
        boolean success = false;

        if (item.getId() == itemId && type == QuestType.PickUP){
            amountNeeded -= item.getAmount();

            if(amountNeeded <= 0){
                completed = true;
            }

            success = true;
        }

        return success;
    }

    public int getQuestItemId(){
        return itemId;
    }
    public int getAmountNeeded(){return amountNeeded;}
    public int getId(){return id;}

    public void changeAmountNeeded(int i){amountNeeded += i;}
    public void setQuestStatus(boolean status){
        completed = status;
    }

}
