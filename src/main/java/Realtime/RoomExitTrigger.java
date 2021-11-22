package Realtime;

import BackEnd.RoomCollection;
import BackEnd.ExitDefinition;
import javafx.geometry.Point2D;
import worldofzuul.Game;

public class RoomExitTrigger extends DistanceTrigger{

    private final RoomCollection roomToChangeTo;
    private final ExitDefinition exit;
    private Point2D position;
    private final int interActionRadius = 50;

    public RoomExitTrigger(RoomCollection rc, ExitDefinition ed) {

        this.roomToChangeTo = rc;
        this.exit = ed;

        switch (ed.getDirection()) {
            case "south" -> position = new Point2D(Game.WIDTH / 2.0, Game.HEIGHT);
            case "east" -> position = new Point2D(Game.WIDTH, Game.WIDTH / 2.0);
            case "north" -> position = new Point2D(Game.WIDTH / 2.0, 0);
            case "west" -> position = new Point2D(0, Game.HEIGHT / 2.0);
        }

        super.setPosX((int) position.getX());
        super.setPosY((int) position.getY());
        super.setInterRadius(interActionRadius);
    }


    @Override
    public void onInVicinity(){
        if(InteractionHandler.interactibleReadyToInteract == null) {
            InteractionHandler.interactibleReadyToInteract = this;
        }
    }
    @Override
    public void onInteraction(){
        System.out.println("You just interacted with a Room Trigger");

        MainGUIController.roomToChangeTo = roomToChangeTo;
        MainGUIController.sceneChangeRequested = true;
    }
    public String getExitString(){
        return exit.getDirection() + ": " + roomToChangeTo.getRoom().getName();
    }
}
