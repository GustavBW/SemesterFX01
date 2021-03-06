package Realtime;

import BackEnd.ContentEngine;
import BackEnd.TextProcessor;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import worldofzuul.Game;
import worldofzuul.Quest;

import java.util.ArrayList;

public class QuestGUI implements Renderable {

    private boolean isReady = false, doDisplay = false;

    private final ArrayList<AdvancedRendText> questTitles;
    private final ArrayList<AdvancedRendText> questDescriptions;
    private AdvancedRendText mainGUITitle;

    private final Font fontTitle, fontText, fontMainTitle;
    private final Color mainTitleColor, descColor, titleColor, qCompleteTitleColor, qCompleteDescColor, backgroundColor1, backgroundColor2;

    //The great thing about procedually generating the GUI : No whacky numbers! Only conditionally scaling values. Edit: Almost no whacky values
    private final double xTitlePosition = Game.WIDTH * 0.8, xDescPosition = xTitlePosition + 20, yOffsetFromScreen = Game.HEIGHT * 0.2, yOffsetBetweenTexts = 0, yOffsetBetweenSegments = 10;
    private double totalDisplaceMent = 0, totalDisplayHeight = 10;
    private final int descWidthSymbols = 45;

    private boolean roomSolved;
    private final Image checkMarkImage;


    public QuestGUI(){
        questTitles = new ArrayList<>();
        questDescriptions = new ArrayList<>();

        fontMainTitle = Font.font( "Impact", 26);
        fontTitle = Font.font("Impact", FontPosture.REGULAR, 20);
        fontText = Font.font("Helvetica", 14);

        mainTitleColor = new Color(209 / 255.0,153 / 255.0,0,1);
        descColor = new Color(1,1,1,1);
        titleColor = new Color(194 / 255.0,110 / 255.0,1 / 255.0,1);
        qCompleteTitleColor = new Color(32 / 255.0,158 / 255.0,0,1);
        qCompleteDescColor = new Color(95 / 255.0,130 / 255.0,0.4,1);
        backgroundColor1 = new Color(1,1,1,0.5);
        backgroundColor2 = new Color(0,0,0,0.5);

        checkMarkImage = ContentEngine.getCheckMark();

        mainGUITitle = new AdvancedRendText("Quests", new Point2D(xTitlePosition + ((Game.WIDTH - Game.WIDTH * 0.85) / 2), yOffsetFromScreen + 20), fontMainTitle, mainTitleColor, 100);

        createNew();
    }

    private void createNew(){
        isReady = false;
        int counter = 0;
        double displacement = 0;
        boolean questsFound = false;

        clear();

        TextProcessor tempTextProc = ContentEngine.getTextProcessor();
        AdvancedRendText previouslyAddedText = mainGUITitle;

        if(MainGUIController.getCurrentRoom().getRoom().getQuests() != null){

            for(Quest q : MainGUIController.getCurrentRoom().getRoom().getQuests()){

                String title = q.getTitle();
                String resolvedDescribtion = q.getDesc();
                String notResolvedDesc = q.getHint();

                if(q.isComplete()){

                    questTitles.add(
                            previouslyAddedText = new AdvancedRendText(
                                    title,
                                    new Point2D(xTitlePosition, previouslyAddedText.getTotalHeight() + previouslyAddedText.getY() + yOffsetBetweenTexts + displacement),
                                    fontTitle,
                                    qCompleteTitleColor,
                                    100
                    ));

                    questDescriptions.add(
                            previouslyAddedText = new AdvancedRendText(
                                    resolvedDescribtion,
                                    new Point2D(xDescPosition, previouslyAddedText.getTotalHeight() + previouslyAddedText.getY() + yOffsetBetweenTexts),
                                    fontText,
                                    qCompleteDescColor,
                                    descWidthSymbols
                    ));

                    counter++;
                    displacement = yOffsetBetweenSegments * counter;
                    questsFound = true;
                }else{

                    questTitles.add(
                            previouslyAddedText = new AdvancedRendText(
                                    title,
                                    new Point2D(xTitlePosition, previouslyAddedText.getTotalHeight() + previouslyAddedText.getY() + yOffsetBetweenTexts + displacement),
                                    fontTitle,
                                    titleColor,
                                    100
                            ));

                    questDescriptions.add(
                            previouslyAddedText = new AdvancedRendText(
                                    notResolvedDesc,
                                    new Point2D(xDescPosition, previouslyAddedText.getTotalHeight() + previouslyAddedText.getY() + yOffsetBetweenTexts),
                                    fontText,
                                    descColor,
                                    descWidthSymbols
                            ));

                    counter++;
                    displacement = yOffsetBetweenSegments * counter;
                    questsFound = true;
                }
            }
            roomSolved = MainGUIController.getCurrentRoom().getRoom().isQuestsSolved();
        }

        totalDisplaceMent = displacement;
        totalDisplayHeight = calculateTotalDisplayHeight();
        doDisplay = questsFound;   //Setting doDisplay to true here, will cause the QuestGUI to pop up whenever somethings changed.
        isReady = true;
    }

    private void clear(){
        questTitles.clear();
        questDescriptions.clear();
    }

    @Override
    public void render(GraphicsContext gc) {
        if(doDisplay && isReady){

            gc.setFill(backgroundColor1);
            gc.fillRoundRect(xTitlePosition - 10, yOffsetFromScreen - 10, Game.WIDTH + 100, totalDisplayHeight + 20, 10 , 10);

            gc.setFill(backgroundColor2);
            gc.fillRoundRect(xTitlePosition - 5, yOffsetFromScreen - 5, Game.WIDTH + 100, totalDisplayHeight + 10, 10 , 10);

            mainGUITitle.render(gc);

            for(AdvancedRendText aRT : questTitles){
                aRT.render(gc);
            }

            for(AdvancedRendText aRT : questDescriptions){
                aRT.render(gc);
            }

            if(roomSolved){
                gc.drawImage(checkMarkImage, xTitlePosition + 120, yOffsetFromScreen + 50);
            }
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
    public boolean getDisplayStatus(){return doDisplay;}
    public void updateQuests(){createNew();}

    private double calculateTotalDisplayHeight(){
        double totalHeight = 10;

        for(AdvancedRendText art : questTitles){
            totalHeight += ( art == null ?  10 : art.getTotalHeight()) + yOffsetBetweenTexts; //Enjoy the ternaries while they last
        }

        for(AdvancedRendText art : questDescriptions){
            totalHeight += ( art == null ?  10 : art.getTotalHeight()) + yOffsetBetweenTexts;
        }


        return (totalHeight + mainGUITitle.getTotalHeight() + totalDisplaceMent);
    }

    @Override
    public void onInstancedRender() {

    }
}
