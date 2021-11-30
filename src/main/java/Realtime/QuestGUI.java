package Realtime;

import BackEnd.ContentEngine;
import BackEnd.TextProcessor;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import worldofzuul.Game;
import worldofzuul.Quest;

import java.util.ArrayList;

public class QuestGUI implements Renderable {

    private boolean isReady = false, doDisplay = false;

    private ArrayList<AdvancedRendText> questTitles;
    private ArrayList<AdvancedRendText> questDescriptions;

    private Font fontTitle, fontText;
    private Color descColor, titleColor, qCompleteTitleColor, qCompleteDescColor;

    private final double xTextPosition = Game.WIDTH * 0.8;
    private final int descWidthSymbols = 100;


    public QuestGUI(){
        questTitles = new ArrayList<>();
        questDescriptions = new ArrayList<>();

        fontTitle = new Font("Verdana", 20);
        fontText = new Font("Helvetica", 14);

        descColor = new Color(1,1,1,1);
        titleColor = new Color(1,1,1,1);
        qCompleteTitleColor = new Color(0.1,0.6,0.1,1);
        qCompleteDescColor = new Color(0.4,0.4,0.4,1);


        createNew();
    }

    private void createNew(){
        isReady = false;

        clear();

        TextProcessor tempTextProc = ContentEngine.getTextProcessor();
        boolean firstOne = true;

        if(MainGUIController.getCurrentRoom().getRoom().getQuests() != null){

            for(Quest q : MainGUIController.getCurrentRoom().getRoom().getQuests()){

                if(q.isComplete()){

                    if(firstOne) {
                        questTitles.add(
                                new AdvancedRendText(
                                        tempTextProc.getSingleItem(q.getQuestItemId()).getName(),
                                        new Point2D(xTextPosition, Game.HEIGHT * 0.2),
                                        fontTitle,
                                        qCompleteTitleColor,
                                        100
                        ));

                        questDescriptions.add(
                                new AdvancedRendText(
                                        q.getDesc(),
                                        new Point2D(xTextPosition, Game.HEIGHT * 0.2 + questTitles.get(0).getTotalHeight()),
                                        fontText,
                                        qCompleteDescColor,
                                        descWidthSymbols
                        ));

                        firstOne = false;

                    }else{


                        questTitles.add(
                                new AdvancedRendText(
                                        tempTextProc.getSingleItem(q.getQuestItemId()).getName(),
                                        new Point2D(xTextPosition, Game.HEIGHT * 0.2),
                                        fontTitle,
                                        qCompleteTitleColor,
                                        100
                                ));

                        questDescriptions.add(
                                new AdvancedRendText(
                                        q.getDesc(),
                                        new Point2D(xTextPosition, Game.HEIGHT * 0.2 + questTitles.get(0).getTotalHeight()),
                                        fontText,
                                        qCompleteDescColor,
                                        descWidthSymbols
                                ));

                    }

                }else{


                    questTitles.add(
                            new AdvancedRendText(
                                    tempTextProc.getSingleItem(q.getQuestItemId()).getName(),
                                    new Point2D(Game.WIDTH * 0.8, Game.HEIGHT * 0.2 + questTitles.get(questTitles.size() - 1).getTotalHeight()),
                                    fontTitle,
                                    titleColor,
                                    100
                            ));

                    questDescriptions.add(
                            new AdvancedRendText(
                                    q.getHint(),
                                    new Point2D(0,0),
                                    fontText,
                                    descColor,
                                    descWidthSymbols
                            ));

                    q.getHint();


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
