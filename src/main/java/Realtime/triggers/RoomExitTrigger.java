package Realtime.triggers;

import BackEnd.ContentEngine;
import BackEnd.RoomCollection;
import BackEnd.ExitDefinition;
import Realtime.InteractionHandler;
import Realtime.MainGUIController;
import Realtime.debugging.RenderableCircle;
import Realtime.interfaces.Interactible;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import worldofzuul.Game;
import worldofzuul.Room;

public class RoomExitTrigger extends DistanceTrigger implements Interactible, Renderable {

    private final Room roomToChangeTo;
    private ExitDefinition exit;
    private Point2D position;
    private final int interActionRadius = 100;
    private String direction;
    private Point2D playerNextPosition;
    private RenderableCircle showingRadius;
    private Image arrowImage;

    public RoomExitTrigger(Room rc, ExitDefinition ed) {

        super(0,0,100);

        this.roomToChangeTo = rc;
        this.exit = ed;
        arrowImage = ContentEngine.getExitArrowImage(ed.getDirection());

        if(roomToChangeTo.getId() == 1 || roomToChangeTo.getName().equalsIgnoreCase("Sudkhira Landsby")) {

            ed = new ExitDefinition(9999, "Step into", 1);
            position = new Point2D(Game.WIDTH * 0.13, Game.HEIGHT * 0.8);
            playerNextPosition = new Point2D(Game.WIDTH * 0.35, Game.HEIGHT * 0.5);
            arrowImage = ContentEngine.getExitArrowImage("south");          //Override image in this case

        }else{
            switch (ed.getDirection()) {
                case "south" -> {
                    position = new Point2D(Game.WIDTH / 2.0, Game.HEIGHT);
                    playerNextPosition = new Point2D(Game.WIDTH / 2.0, 100);
                }
                case "east" -> {
                    position = new Point2D(Game.WIDTH, Game.HEIGHT / 2.0);
                    playerNextPosition = new Point2D(100, Game.HEIGHT / 2.0);
                }
                case "north" -> {
                    position = new Point2D(Game.WIDTH / 2.0, 0);
                    playerNextPosition = new Point2D(Game.WIDTH / 2.0, Game.HEIGHT - 100);
                }
                case "west" -> {
                    position = new Point2D(0, Game.HEIGHT / 2.0);
                    playerNextPosition = new Point2D(Game.WIDTH, Game.HEIGHT / 2.0);
                }
            }
        }

        Color someColor = new Color(1,1,1,0.5);
        showingRadius = new RenderableCircle(position, interActionRadius, 100, someColor);

        super.setPosX((int) position.getX());
        super.setPosY((int) position.getY());
    }


    @Override
    public void onInVicinity(){
        String not = " ";

        if(InteractionHandler.interactibleReadyToInteract == null) {
            InteractionHandler.interactibleReadyToInteract = this;
            not = "not";
        }
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

    @Override
    public int getInterRadius() {

        return interActionRadius;
    }

    //Just overriding these two blokes as the MGUIC is taking care of adding them into the appropriate places. Them doing it themselves could cause unintended issues

    @Override
    public void render(GraphicsContext gc) {

        showingRadius.render(gc);
        gc.drawImage(arrowImage, position.getX(), position.getY());
    }

    @Override
    public void onInstancedInter() {}
    @Override
    public void onInstancedRender() {}
}
