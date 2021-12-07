package worldofzuul;

import Realtime.QuestType;
import Realtime.inventory.Item;

public class Quest {
    private int id;
    private String desc;
    private String hint;
    private String title;
    private int itemId, gateId;
    private boolean completed = false, isGated = false;
    private int amountNeeded, originalAmount;
    private QuestType type;

    //TODO show image when using Kamera in room
    //TODO add some clouds and get them moving


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
        this.title = "Use";
    }

    public Quest(int id,  String title, String desc, String hint, int itemId, int amount, String type, int gateId){
        this(id, desc, hint, itemId, amount, type);

        if(gateId == -1){
            isGated = true;
        }

        this.gateId = gateId;
        this.title = title;
    }

    public Quest(int id, String desc, String hint, int itemId, int amount, String type){
        this(id, desc, hint, itemId, amount);

        if(type == null) {
            this.type = QuestType.Use;

        }else{
            if(type.equalsIgnoreCase("PickUp")) {
                this.title = "Pick Up";
                this.type = QuestType.PickUP;

            }else{
                this.type = QuestType.Use;
                this.title = "Use";
            }
        }

    }

    public Quest(int id, String desc, String hint, int itemId) {
        this(id, desc, hint, itemId, 1);
    }
    public Quest(){
        this(999999, "Test Quest", "Test Quest", 999999, 1);
    }

    public String getTitle(){return this.title;}
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

    public boolean evaluateItem(Item item, QuestType type){
        boolean success = false;
        boolean gateCondition = true;

        if(isGated) {
            if (!Game.player.getResovledQuests().contains(gateId)) {
                gateCondition = false;
            }
        }

        if (this.type == type && gateCondition) {
            if (item.getId() == itemId) {
                amountNeeded -= item.getAmount();

                if (amountNeeded <= 0) {
                    completed = true;
                    Game.player.questResolved(id);
                    Game.updateQuestGUI = true;
                }

                success = true;
            }
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
