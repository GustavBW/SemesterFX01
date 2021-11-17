package worldofzuul;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class RoomCollection {

    private int id;
    private Room room;
    private ArrayList<Image> baseImages;
    private ArrayList<CItem> citemsInRoom;


    public RoomCollection(Room room, ArrayList<Image> baseImages, ArrayList<CItem> citems){
        this.id = room.getId();
        this.room = room;
        this.baseImages = baseImages;
        this.citemsInRoom = citems;
    }

    public ArrayList<Image> getBaseImages(){
        return baseImages;
    }
    public ArrayList<CItem> getCitems(){return citemsInRoom;}
    public Room getRoom(){return room;}
    public int getId(){return id;}

    public void setBaseImages(ArrayList<Image> images){baseImages = images;}
    public void setCitemsInRoom(ArrayList<CItem> citems){citemsInRoom = citems;}
    public void setRoom(Room room){
        this.room = room;
        this.id = room.getId();
    }

}
