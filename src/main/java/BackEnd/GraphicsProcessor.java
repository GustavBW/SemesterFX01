package BackEnd;

import java.io.*;
import java.util.ArrayList;

import Realtime.RenderableImage;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class GraphicsProcessor {

        //Room stuff
        private final String graphicsDirectory = "/Graphics";
        private final String[] subDirectories = {"/TestDirectory"};
        private final String[] imageTypes = {"/TestPicture.png","/BASE.png","MIDDLE.png","/SHADOW.png","/TOP.png"};

        //CItem stuff
        private final String cItemDirectory = "/CItem";
        private final String cItemPositions = "cItemPositions.csv";
        private final String cItemSplitBy = ";";

        public ArrayList<RenderableImage> getBaseForRoom(int roomId){
                ArrayList<RenderableImage> images = new ArrayList<>();

                try{
                        for(String s : imageTypes) {
                                /*ImageInput picture = new ImageInput (new InputStreamReader(getClass().getResourceAsStream(graphicsDirectory + "/" + roomId + s)));


                                images.add(br.read());
                                //Hjælp
                                br.close();

                                 */
                        }


                }catch (Exception e){
                        e.printStackTrace();
                }


                return images;
        }

        public PosPicCombo getCItemPosPic(int itemId){

                Point2D position = null;
                Image picture = null;

                try{
                        BufferedReader pictureReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(graphicsDirectory + cItemDirectory + "/" + itemId)));
                        BufferedReader positionReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(graphicsDirectory + cItemDirectory + cItemPositions)));

                        String line;

                        while((line = positionReader.readLine()) != null){
                                String[] currentLine = line.split(cItemSplitBy);

                                if(currentLine[0].equals(String.valueOf(itemId))){
                                        position = new Point2D(Integer.parseInt(currentLine[1]), Integer.parseInt(currentLine[2]));
                                        break;
                                }
                        }

                        //picture = (Image) (pictureReader.read());


                        pictureReader.close();
                        positionReader.close();
                }catch (Exception e){
                        e.printStackTrace();
                }


                return new PosPicCombo(picture,position);
        }


        public Image getPlayerGraphics(){
                Image output;
                output = new Image(getClass().getResourceAsStream("/Graphics/Player/PlayerTest.png"));
                return output;
        }

        public Image getBaseImage(int roomId){
                Image output;
                output = new Image(getClass().getResourceAsStream("/Graphics/" + roomId + "/BASE.png"));
                return output;
        }

        public Image getMiddleImage(int roomId){
                Image output;
                output = new Image(getClass().getResourceAsStream("/Graphics/" + roomId + "/MIDDLE.png"));
                return output;
        }

        public Image getShadowImage(int roomId){
                Image output;
                output = new Image(getClass().getResourceAsStream("/Graphics/" + roomId + "/SHADOW.png"));
                return output;
        }

        public Image getTopImage(int roomId){
                Image output;
                output = new Image(getClass().getResourceAsStream("/Graphics/" + roomId + "/TOP.png"));
                return output;
        }

}
