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
                String lines;
                String splitBy = ";";
                PosPicCombo output = new PosPicCombo();

                try{
                        BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Graphics/CItem/cItemPositions.csv")));
                        while((lines = br.readLine()) != null){
                                String[] line = lines.split(splitBy);
                                if(Integer.parseInt(line[0]) == itemId){
                                        picture = citemImage(Integer.parseInt(line[0]));
                                        position = citemPositon(line[1]);
                                        output = new PosPicCombo(picture,position);
                                        return output;
                                }else{

                                }
                        }

                }catch (Exception e){
                        e.printStackTrace();
                }


                return output;
        }

        private Image citemImage(int itemId){
                Image output;
                output = new Image(getClass().getResourceAsStream("/Graphics/CItem/" + itemId + ".png"));
                return output;
        }

        private Point2D citemPositon(String position){
                Point2D output;
                String splitBy = ",";
                String[] xAndY = position.split(splitBy);
                output = new Point2D(Double.parseDouble(xAndY[1]), Double.parseDouble(xAndY[1]));
                return output;
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
