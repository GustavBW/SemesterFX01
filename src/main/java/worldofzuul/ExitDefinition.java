package worldofzuul;

public class ExitDefinition {

    private int id;
    private String name;
    private int roomId;

    public ExitDefinition(){}

    public ExitDefinition(int id, String name, int room){
        this.id = id;
        this.name = name;
        this.roomId = room;
    }

    public String getDirection(){return name;}
    public int getRoomId(){return roomId;}
    public int getId(){return id;}
}
