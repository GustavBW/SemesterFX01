package BackEnd;

import Realtime.DistanceTrigger;
import Realtime.RenderableImage;
import Realtime.inventory.CItem;
import worldofzuul.Room;

import java.util.ArrayList;

public class RoomCollection {

    private int id;
    private Room room;
    private ArrayList<RenderableImage> baseImages;
    private ArrayList<CItem> citemsInRoom;
    private ArrayList<DistanceTrigger> triggers;


    public RoomCollection(Room room, ArrayList<RenderableImage> baseImages, ArrayList<CItem> citems){
        this(room,baseImages, citems,null);
    }

    public RoomCollection(Room room, ArrayList<RenderableImage> baseImages, ArrayList<CItem> citems, ArrayList<DistanceTrigger> triggers){
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

    public void setBaseImages(ArrayList<RenderableImage> images){baseImages = images;}
    public void setCitemsInRoom(ArrayList<CItem> citems){citemsInRoom = citems;}
    public void setRoom(Room room){
        this.room = room;
        this.id = room.getId();
    }
}
