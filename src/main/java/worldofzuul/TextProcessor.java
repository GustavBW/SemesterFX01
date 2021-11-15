package worldofzuul;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;


public class TextProcessor {
    
    private String itemsFilePath = "/Data/Items.csv";
    private String roomsFilePath = "/Data/Rooms.csv";
    private String exitsFilePath = "/Data/Exits.csv";
    private String roomQuestFilePath = "/Data/RoomQuest.csv";


    public ArrayList<Room> getAllRooms()
    {

        ArrayList<Room> output = new ArrayList<>();
        String line;
        String splitBy = ";";

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(roomsFilePath)));
            while((line = br.readLine()) != null)
            {
                String[] room = line.split(splitBy);
                ArrayList<Item> itemsInRoom = allItemsInRoom(room[6]);
                ArrayList<ExitDefinition> allExits = getExits(room[5]);
                Room newRoom = new Room(Integer.parseInt(room[0]),          //Room id
                                        room[1],                            //Room name
                                        room[2],                            //Long desc
                                        room[3],                            //Short desc
                                        getQuests(room[4]),
                                        itemsInRoom,
                                        allExits);                      //adding items to room should be done directly
                output.add(newRoom);                                            //in the constructor instead method in class
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return  output;

    }




    public ArrayList<Item> allItemsInRoom(String itemsRequested)
    {
        String splitByComma = ",";
        String secondSplitBy = "'";
        ArrayList<Item> output = new ArrayList<>();
        ArrayList<Item> allItems = getAllItems();

        String[] singleItemAmount = itemsRequested.split(splitByComma);

        for(String s : singleItemAmount)
        {
            try
            {
                String[] idAmount = s.split(secondSplitBy);
                int itemId = Integer.parseInt(idAmount[0]);
                int amount = Integer.parseInt(idAmount[1]);
                Item currentItem = new Item(allItems.get(itemId).getId(), allItems.get(itemId).getName(), amount );
                output.add(currentItem);

            }catch(NumberFormatException e)
            {
                e.printStackTrace();
            }
        }
        return output;
    }


    public ArrayList<Item> getAllItems()
    {
        ArrayList<Item> output = new ArrayList<>();
        String line;
        String splitBy = ";";
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(itemsFilePath)));
            while((line = br.readLine()) != null)
            {
                String[] itemAtributes = line.split(splitBy);
                Item item = new Item(Integer.parseInt(itemAtributes[0]), itemAtributes[1]);
                output.add(item);
            }

            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return output;
    }






    public ArrayList<Quest> getQuests(String ids){
        ArrayList<Quest> output = new ArrayList<>();
        String splitBy = ";";
        String line;

        String[] questIdsStrings = ids.split(",");
        ArrayList<Integer> idNumbers = new ArrayList<Integer>();
        for(String s: questIdsStrings){
            idNumbers.add(Integer.parseInt(s));
        }

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(roomQuestFilePath)));
            while((line = br.readLine()) != null)
            {
                String[] atributes = line.split(splitBy);
                for(Integer i: idNumbers){
                    if(Integer.parseInt(atributes[0]) == i)
                    {
                        Quest quest = new Quest(Integer.parseInt(atributes[0]),
                                atributes[1],
                                atributes[2],
                                Integer.parseInt(atributes[3]));
                        output.add(quest);
                    }
                }

            }
            br.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return output;
    }







    public ArrayList<ExitDefinition> getAllExits(){
        ArrayList<ExitDefinition> output = new ArrayList<ExitDefinition>();
        String splitBy = ";";
        String line;

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(exitsFilePath)));
            while((line = br.readLine()) != null)
            {
                String[] s = line.split(splitBy);
                ExitDefinition exit = new ExitDefinition(Integer.parseInt(s[0]),
                                                        s[1],
                                                        Integer.parseInt(s[2]));
                output.add(exit);
            }
            br.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }

        return output;
    }

    public ArrayList<ExitDefinition> getExits(String exits){
        ArrayList<ExitDefinition> output = new ArrayList<>();
        ArrayList<ExitDefinition> allexits = getAllExits();

        String[] i = exits.split(",");
        for(String s: i){
            output.add(allexits.get(Integer.parseInt(s)));
        }
        return output;
    }
}
