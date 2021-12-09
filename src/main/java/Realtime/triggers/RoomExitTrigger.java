package Realtime.triggers;

import BackEnd.ContentEngine;
import BackEnd.RoomCollection;
import BackEnd.ExitDefinition;
import Realtime.AdvancedRendText;
import Realtime.InteractionHandler;
import Realtime.MainGUIController;
import Realtime.debugging.RenderableCircle;
import Realtime.interfaces.Interactible;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import worldofzuul.Game;
import worldofzuul.Room;

import java.nio.charset.StandardCharsets;

public class RoomExitTrigger extends DistanceTrigger implements Interactible, Renderable {

    private final Room roomToChangeTo;
    private String text;
    private ExitDefinition exit;
    private Point2D position, imagePosition, textPosition;
    private final int interActionRadius = 100;
    private String direction;
    private Point2D playerNextPosition;
    private AdvancedRendText advancedRendText, dropShadow;
    private Image arrowImage;
    private boolean showTextToo = false;
    private final int textSize;
    private final double textHeightToWidthFactor;

    public RoomExitTrigger(Room rc, ExitDefinition ed) {

        super(0,0,100);
        this.textSize = 22;
        this.textHeightToWidthFactor = 0.125;
        this.text = rc.getName();
        this.roomToChangeTo = rc;
        this.exit = ed;
        arrowImage = ContentEngine.getExitArrowImage(ed.getDirection());

        if(roomToChangeTo.getId() == 1 || roomToChangeTo.getName().equalsIgnoreCase("Sudkhira Landsby")) {

            position = new Point2D(Game.WIDTH * 0.13, Game.HEIGHT * 0.8);
            playerNextPosition = new Point2D(Game.WIDTH * 0.35, Game.HEIGHT * 0.5);
            arrowImage = ContentEngine.getExitArrowImage("south");          //Override image in this case
            imagePosition = new Point2D(position.getX() - (arrowImage.getWidth() / 2.0), position.getY() - (arrowImage.getHeight() * 0.8));
            textPosition = new Point2D(imagePosition.getX() - (text.getBytes(StandardCharsets.UTF_8).length * (textSize * textHeightToWidthFactor)), imagePosition.getY() - 10);

        }else{
            switch (ed.getDirection()) {
                case "south" -> {
                    position = new Point2D(Game.WIDTH / 2.0, Game.HEIGHT);
                    playerNextPosition = new Point2D(Game.WIDTH / 2.0, 100);
                    imagePosition = new Point2D(position.getX() - (arrowImage.getWidth() / 2.0), position.getY() - arrowImage.getHeight());
                    textPosition = new Point2D(imagePosition.getX() - ((text.getBytes(StandardCharsets.UTF_8).length * (textSize * textHeightToWidthFactor)) / 2.0) ,imagePosition.getY() - 10);

                }
                case "east" -> {
                    position = new Point2D(Game.WIDTH, Game.HEIGHT / 2.0);
                    playerNextPosition = new Point2D(100, Game.HEIGHT / 2.0);
                    imagePosition = new Point2D(position.getX() - arrowImage.getWidth(), position.getY() - (arrowImage.getWidth() / 2.0));
                    textPosition = new Point2D(imagePosition.getX() - (text.getBytes(StandardCharsets.UTF_8).length * ( textSize * textHeightToWidthFactor * 2)) , imagePosition.getY());

                }
                case "north" -> {
                    position = new Point2D(Game.WIDTH / 2.0, 0);
                    playerNextPosition = new Point2D(Game.WIDTH / 2.0, Game.HEIGHT - 100);
                    imagePosition = new Point2D(position.getX() - (arrowImage.getWidth() / 2.0), arrowImage.getHeight() * 0.2);
                    textPosition = new Point2D(imagePosition.getX() - ((text.getBytes(StandardCharsets.UTF_8).length * (textSize * textHeightToWidthFactor)) / 2.0) , imagePosition.getY() + arrowImage.getHeight() + 20);

                }
                case "west" -> {
                    position = new Point2D(0, Game.HEIGHT / 2.0);
                    playerNextPosition = new Point2D(Game.WIDTH - 100, Game.HEIGHT / 2.0);
                    imagePosition = new Point2D(position.getX(), position.getY() - (arrowImage.getHeight() / 2.0));
                    textPosition = new Point2D(imagePosition.getX() + 20, imagePosition.getY());
                }
            }
        }
        if (roomToChangeTo.getId() == Game.titleScreenRoomId || roomToChangeTo.getName().equalsIgnoreCase("T")) {

            position = new Point2D(Game.WIDTH * 0.5, Game.HEIGHT * 0.5);
            playerNextPosition = new Point2D(Game.WIDTH * 0.6, Game.HEIGHT * 0.1);
            arrowImage = ContentEngine.getExitArrowImage("west");
            imagePosition = new Point2D(Game.WIDTH * 0.46, Game.HEIGHT * 0.46);
            textPosition = new Point2D(imagePosition.getX(), imagePosition.getY() - 50);
        }

        super.setPosX((int) position.getX());
        super.setPosY((int) position.getY());

        Font textFont = Font.font("Lucida Sans Unicode", FontWeight.BOLD,FontPosture.ITALIC, 20);
        advancedRendText = new AdvancedRendText(text, textPosition, textFont, Color.WHITE, 11);
        dropShadow = advancedRendText.getDropShadow(2);
    }

    @Override
    public void onInVicinity(){

        showTextToo = true;
        if(InteractionHandler.interactibleReadyToInteract == null) {
            InteractionHandler.interactibleReadyToInteract = this;
        }
    }
    @Override
    public void onInteraction(){
        MainGUIController.roomToChangeTo = ContentEngine.getRoomCollection(roomToChangeTo);
        MainGUIController.sceneChangeRequested = true;
        Game.updateQuestGUI = true;

        Game.player.setPosX((int) playerNextPosition.getX());
        Game.player.setPosY((int) playerNextPosition.getY());

    }
    @Override
    public String getPopUpText(){

        if(exit == null){
            return direction + ": " + roomToChangeTo.getName();
        }else {
            return exit.getDirection() + ": " + roomToChangeTo.getName();
        }
    }

    @Override
    public int getInterRadius() {

        return interActionRadius;
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.drawImage(arrowImage, imagePosition.getX(), imagePosition.getY());

        if(showTextToo){
            dropShadow.render(gc);
            advancedRendText.render(gc);
            showTextToo = false;
        }
    }

    @Override
    public void onInstancedInter() {}
    @Override
    public void onInstancedRender() {}
}
