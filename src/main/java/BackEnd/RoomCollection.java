package BackEnd;

import Realtime.RenderableImage;
import Realtime.triggers.RoomExitTrigger;
import Realtime.inventory.CItem;
import worldofzuul.Room;

import java.util.ArrayList;

public class RoomCollection {

    private int id;
    private Room room;
    private ArrayList<RenderableImage> baseImages;
    private ArrayList<CItem> citemsInRoom;
    private ArrayList<RoomExitTrigger> triggers;

    public RoomCollection(Room room){
        this.id = room.getId();
        this.room = room;
        this.baseImages = ContentEngine.specificRoomGraphics(room.getId());
        this.citemsInRoom = ContentEngine.getCItems(room.getId());
        this.triggers = ContentEngine.getExitTriggers(room);
    }



    public RoomCollection(Room room, ArrayList<RenderableImage> baseImages, ArrayList<CItem> citems, ArrayList<RoomExitTrigger> triggers){
        this.id = room.getId();
        this.room = room;
        this.baseImages = baseImages;
        this.citemsInRoom = citems;
        this.triggers = triggers;
    }

    public ArrayList<RenderableImage> getBaseImages(){
        return baseImages;
    }
    public ArrayList<CItem> getCitems(){return citemsInRoom;}
    public Room getRoom(){return room;}
    public int getId(){return id;}
    public ArrayList<RoomExitTrigger> getExitTriggers(){return triggers;}

    public void setExits(ArrayList<RoomExitTrigger> RETs){triggers = RETs;}
    public void setBaseImages(ArrayList<RenderableImage> images){baseImages = images;}
    public void setCitemsInRoom(ArrayList<CItem> citems){citemsInRoom = citems;}
    public void setRoom(Room room){
        this.room = room;
        this.id = room.getId();
    }
}
