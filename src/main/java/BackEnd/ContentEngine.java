package BackEnd;

import Realtime.inventory.CItem;
import Realtime.inventory.Item;
import worldofzuul.*;

import java.util.ArrayList;

public class ContentEngine {

    private TextProcessor tp;
    private GraphicsProcessor gp;

    public ContentEngine(){

        tp = new TextProcessor();
        gp = new GraphicsProcessor();

    }

    public ArrayList<CItem> getCItems(int roomId){
        ArrayList<CItem> citems = new ArrayList<>();
        ArrayList<Item> items = tp.getItemsForRoom(roomId);

        for(Item i : items){
            citems.add(new CItem(i, gp.getCItemPosPic(i.getId())));
        }

        return citems;
    }

    public RoomCollection getRoomCollection(Room room){
        RoomCollection rc = new RoomCollection(room,gp.getBaseForRoom(room.getId()),getCItems(room.getId()));

        return rc;
    }

    public ArrayList<RoomCollection> getAllRoomCollections(){
        ArrayList<RoomCollection> allRoomCollections = new ArrayList<>();
        ArrayList<Room> allRooms = tp.getAllRooms();

        for(Room r : allRooms){
            allRoomCollections.add(getRoomCollection(r));
        }

        return allRoomCollections;
    }




}
