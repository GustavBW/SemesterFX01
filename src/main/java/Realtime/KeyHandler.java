package Realtime;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import worldofzuul.Game;

public class KeyHandler implements EventHandler<KeyEvent> {

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
                    InteractionHandler.interactibleReadyToInteract.onInteraction();
                }
            }
            case P -> Game.onPause = !Game.onPause;
            case I -> MainGUIController.showInventory = !MainGUIController.showInventory;
        }
    }
}
