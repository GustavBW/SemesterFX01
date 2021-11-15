package worldofzuul;

public class Quest {
    private int id;
    private String desc;
    private String hint;
    private int itemId;
    private boolean completed = false;
    private int amountNeeded;

    public Quest(int id, String desc, String hint, int itemId, int amount) {
        this.id = id;
        this.desc = desc;
        this.hint = hint;
        this.itemId = itemId;
        this.amountNeeded = amount;
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
