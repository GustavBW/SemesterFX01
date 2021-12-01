package Realtime.triggers;

import BackEnd.ContentEngine;
import BackEnd.RoomCollection;
import BackEnd.ExitDefinition;
import Realtime.InteractionHandler;
import Realtime.MainGUIController;
import javafx.geometry.Point2D;
import worldofzuul.Game;
import worldofzuul.Room;

public class RoomExitTrigger extends DistanceTrigger{

    private final Room roomToChangeTo;
    private ExitDefinition exit;
    private Point2D position;
    private final int interActionRadius = 50;
    private String direction;
    private Point2D playerNextPosition;

    public RoomExitTrigger(Room rc, ExitDefinition ed) {

        super(0,0,50);

        this.roomToChangeTo = rc;
        this.exit = ed;

        switch (ed.getDirection()) {
            case "south" -> {position = new Point2D(Game.WIDTH / 2.0, Game.HEIGHT); playerNextPosition = new Point2D(Game.WIDTH / 2.0, 0); }
            case "east" -> {position = new Point2D(Game.WIDTH, Game.HEIGHT / 2.0); playerNextPosition = new Point2D(0, Game.HEIGHT / 2.0); }
            case "north" -> {position = new Point2D(Game.WIDTH / 2.0, 0); playerNextPosition = new Point2D(Game.WIDTH / 2.0, Game.HEIGHT); }
            case "west" -> {position = new Point2D(0, Game.HEIGHT / 2.0); playerNextPosition = new Point2D(Game.WIDTH, Game.HEIGHT / 2.0); }
        }

        super.setPosX((int) position.getX());
        super.setPosY((int) position.getY());
    }


    public RoomExitTrigger(Room rc, Point2D position, String direction){      //We might wanna place exits in other places than the four above

        super(position.getX(), position.getY(), 50);

        roomToChangeTo = rc;
        this.position = position;
        this.direction = direction;

        super.setPosX((int) position.getX());   //Yeah so this is scuffed dont @ me
        super.setPosY((int) position.getY());
    }


    @Override
    public void onInVicinity(){
        String not = " ";
        System.out.println("You're in vicinity of a Room ExitTrigger");
        if(InteractionHandler.interactibleReadyToInteract == null) {
            InteractionHandler.interactibleReadyToInteract = this;
            not = "not";
        }
        System.out.print(", however InteractionHandler.interactibleReadyToInteract is not null");
    }
    @Override
    public void onInteraction(){
        System.out.println("You just interacted with a Room Trigger");

        MainGUIController.roomToChangeTo = ContentEngine.getRoomCollection(roomToChangeTo);
        MainGUIController.sceneChangeRequested = true;

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

        //Just overriding these two blokes as the MGUIC is taking care of adding them into the appropriate places. Them doing it themselves could cause unintended issues
    @Override
    public void onInstancedInter() {

    }
}
