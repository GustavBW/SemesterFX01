package BackEnd;

import Realtime.RenderableImage;
import Realtime.debugging.TestingCItem;
import Realtime.triggers.RoomExitTrigger;
import Realtime.inventory.CItem;
import Realtime.inventory.Item;
import javafx.scene.image.Image;
import worldofzuul.*;

import java.lang.reflect.Array;
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


    public static ArrayList<CItem> getCItems(int roomId){
        TextProcessor tp = new TextProcessor();
        GraphicsProcessor gp = new GraphicsProcessor();
        ArrayList<CItem> citems = new ArrayList<>();
        ArrayList<Item> items = tp.getItemsForRoom(roomId);

        for(Item i : items){
            citems.add(new CItem(i, gp.getCItemPosPic(i.getId())));
        }

        return citems;
    }

    public static RoomCollection getRoomCollection(Room room){
        RoomCollection rc = new RoomCollection(room);        //Gotta parse the RoomExitTriggers here as well. Forgot them

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


    public static ArrayList<RoomExitTrigger> getExitTriggers(Room room){
        TextProcessor tp = new TextProcessor();
        ArrayList<Room> allRooms = tp.getAllRooms();
        ArrayList<RoomExitTrigger> output = new ArrayList<>();

        for(ExitDefinition ex : room.getExits()){
            output.add(new RoomExitTrigger(allRooms.get(ex.getRoomId()), ex));
        }

        return output;
    }



}
