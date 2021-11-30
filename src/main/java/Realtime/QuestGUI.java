package Realtime;

import BackEnd.ContentEngine;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import worldofzuul.Game;
import worldofzuul.Quest;

import java.util.ArrayList;

public class QuestGUI implements Renderable {

    private boolean isReady = false, doDisplay = false;

    private ArrayList<AdvancedRendText> questTitles;
    private ArrayList<AdvancedRendText> questDescriptions;


    private Font fontTitle, fontText;

    public QuestGUI(){
        questTitles = new ArrayList<>();
        questDescriptions = new ArrayList<>();

        fontTitle = new Font("Verdana", 20);
        fontText = new Font("Helvetica", 14);

        createNew();
    }

    private void createNew(){
        isReady = false;
        clear();

        if(MainGUIController.getCurrentRoom().getRoom().getQuests() != null){

            for(Quest q : MainGUIController.getCurrentRoom().getRoom().getQuests()){

                if(q.isComplete()){

                    questTitles.add(new AdvancedRendText(q.getDesc(), new Point2D(Game.WIDTH * 0.8, Game.HEIGHT * 0.2), fontTitle, Color.GREEN, 100));



                }else{


                    q.getDesc();



                }
            }
        }

        isReady = true;
    }

    private void clear(){
        questTitles.clear();

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
