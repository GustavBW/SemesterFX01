package BackEnd;

public class CacheItemInfo {

    private int roomId;
    private int itemId;

    public CacheItemInfo(int room, int item){
        this.roomId = room;
        this.itemId = item;
    }

    public int getRoomId(){
        return this.roomId;
    }

    public int getItemId(){
        return this.itemId;
    }
}
