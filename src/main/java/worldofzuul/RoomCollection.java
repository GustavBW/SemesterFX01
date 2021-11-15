package worldofzuul;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class RoomCollection {

    private int id;

    private Room room;
    private ArrayList<Image> baseImages;


    public RoomCollection(Room room, ArrayList<Image> baseImages){
        this.id = room.getId();
        this.room = room;
        this.baseImages = baseImages;
    }

}
