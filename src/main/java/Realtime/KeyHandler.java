package Realtime;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import worldofzuul.Game;

public class KeyHandler implements EventHandler<KeyEvent> {

    boolean isInteraction; //Just tracking rn. Figuring out how to get this result to the InteractionHandler to determine wether to call onInteraction or not.

    public KeyHandler(){

    }

    @Override
    public void handle(KeyEvent keyEvent) {

        switch (keyEvent.getCode()) {
            case W -> Game.player.changeVelY(-1);
            case A -> Game.player.changeVelX(-1);
            case S -> Game.player.changeVelY(1);
            case D -> Game.player.changeVelX(1);
            case E -> {
                if(InteractionHandler.interactibleReadyToInteract != null){
                InteractionHandler.interactibleReadyToInteract.onInteraction();}
            }
            case P -> Game.onPause = !Game.onPause;
        }
    }
}
