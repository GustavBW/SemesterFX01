package worldofzuul;

public enum CommandWord
{
    GO("go"),
    QUIT("quit"),
    HELP("help"),
    UNKNOWN("?"),
    INVENTORY("inventory"),                     //Prints out whats in the inventory. InventoryManager method called from Game.processCommand
    GATHER("gather"),                           //Gathers a specified item from the current room
    USE("use"),                                 //"Uses" a specified item in the current room
    DROP("drop"),
    INVESTIGATE("investigate"),
    HINT("hint"),
    EXITS("exits");
    
    private String commandString;
    
    CommandWord(String commandString)
    {
        this.commandString = commandString;
    }
    
    public String toString()
    {
        return commandString;
    }
}
