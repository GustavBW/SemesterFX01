package BackEnd;

import Realtime.RenderableImage;
import Realtime.triggers.RoomExitTrigger;
import Realtime.inventory.CItem;
import Realtime.inventory.Item;
import javafx.scene.image.Image;
import worldofzuul.*;

import java.util.ArrayList;

public class ContentEngine {

    private TextProcessor tp = new TextProcessor();
    private GraphicsProcessor gp;

    public ContentEngine(){

        tp = new TextProcessor();
        gp = new GraphicsProcessor();

    }

    public static ArrayList<Room> allRoomes() {
        ArrayList<Room> rooms = new ArrayList<>();
        TextProcessor textprocessor = new TextProcessor();
        rooms = textprocessor.getAllRooms();
        return rooms;
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
        RoomCollection rc = new RoomCollection(room,getCItems(room.getId()), (ArrayList<RoomExitTrigger>) null);        //Gotta parse the RoomExitTriggers here as well. Forgot them

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

    public static ArrayList<RenderableImage> specificRoomGraphics(int roomId){
        ArrayList<RenderableImage> output = new ArrayList<>();
        GraphicsProcessor gp = new GraphicsProcessor();
        output.add(convertToRenderableImage(gp.getBaseImage(roomId)));
        output.add(convertToRenderableImage(gp.getMiddleImage(roomId)));
        output.add(convertToRenderableImage(gp.getShadowImage(roomId)));
        output.add(convertToRenderableImage(gp.getTopImage(roomId)));
        output.add(convertToRenderableImage(gp.getPlayerGraphics()));

        return output;
    }

    private static RenderableImage convertToRenderableImage(Image image){
        RenderableImage output = new RenderableImage(image);
        return output;
    }




}
