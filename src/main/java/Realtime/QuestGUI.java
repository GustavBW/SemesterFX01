package Realtime;

import Realtime.interfaces.Renderable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import worldofzuul.Quest;

import java.util.ArrayList;

public class QuestGUI implements Renderable {

    private boolean isReady = false, doDisplay = false;

    private ArrayList<AdvancedRendText> questTitles;
    private ArrayList<AdvancedRendText> questDescriptions;


    private Font fontTitle, fontText;

    public QuestGUI(){



        fontTitle = new Font("Verdana", 20);
        fontText = new Font("Helvetica", 14);
    }

    private void createNew(){

        clear();

        if(MainGUIController.getCurrentRoom().getRoom().getQuests() != null){

            for(Quest q : MainGUIController.getCurrentRoom().getRoom().getQuests()){

                if(q.isComplete()){



                }else{






                }
            }
        }
    }

    private void clear(){

    }



    @Override
    public void render(GraphicsContext gc) {
        if(doDisplay && isReady){




        }
    }

    public void setDisplayStatus(boolean status){

        //Might seem redundant, but I'mma use it to check for some stuff shortly.

        if(status){

            doDisplay = true;
        }else{

            doDisplay = false;
        }
    }

    @Override
    public void onInstancedRender() {

    }
}
