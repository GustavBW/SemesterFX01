package BackEnd;

import Realtime.RenderableImage;
import javafx.scene.image.Image;
import Realtime.CItem;
import worldofzuul.Room;

import java.util.ArrayList;

public class RoomCollection {

    private int id;
    private Room room;
    private ArrayList<RenderableImage> baseImages;
    private ArrayList<CItem> citemsInRoom;


    public RoomCollection(Room room, ArrayList<RenderableImage> baseImages, ArrayList<CItem> citems){
        this.id = room.getId();
        this.room = room;
        this.baseImages = baseImages;
        this.citemsInRoom = citems;
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
