package Realtime.triggers;

import BackEnd.RoomCollection;
import BackEnd.ExitDefinition;
import Realtime.InteractionHandler;
import Realtime.MainGUIController;
import javafx.geometry.Point2D;
import worldofzuul.Game;

public class RoomExitTrigger extends DistanceTrigger{

    private final RoomCollection roomToChangeTo;
    private ExitDefinition exit;
    private Point2D position;
    private final int interActionRadius = 50;
    private String direction;

    public RoomExitTrigger(RoomCollection rc, ExitDefinition ed) {

        this.roomToChangeTo = rc;
        this.exit = ed;

        switch (ed.getDirection()) {
            case "south" -> position = new Point2D(Game.WIDTH / 2.0, Game.HEIGHT);
            case "east" -> position = new Point2D(Game.WIDTH, Game.HEIGHT / 2.0);
            case "north" -> position = new Point2D(Game.WIDTH / 2.0, 0);
            case "west" -> position = new Point2D(0, Game.HEIGHT / 2.0);
        }

        super.setPosX((int) position.getX());
        super.setPosY((int) position.getY());
        super.setInterRadius(interActionRadius);
    }


    public RoomExitTrigger(RoomCollection rc, Point2D position, String direction){      //We might wanna place exits in other places than the four above
        roomToChangeTo = rc;
        this.position = position;
        this.direction = direction;

        super.setPosX((int) position.getX());   //Yeah so this is scuffed dont @ me
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
    @Override
    public String getPopUpText(){

        if(exit == null){
            return direction + ": " + roomToChangeTo.getRoom().getName();
        }else {
            return exit.getDirection() + ": " + roomToChangeTo.getRoom().getName();
        }
    }

        //Just overriding these two blokes as the MGUIC is taking care of adding them into the appropriate places. Them doing it themselves could cause unintended issues
    @Override
    public void onInstancedInter() {

    }
    @Override
    public void onInstancedRender() {

    }
}
