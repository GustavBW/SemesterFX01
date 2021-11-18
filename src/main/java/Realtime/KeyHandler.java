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
            case W -> Game.player.changeVelY(-1D);
            case A -> Game.player.changeVelX(-1D);
            case S -> Game.player.changeVelY(1D);
            case D -> Game.player.changeVelX(1D);
            case E -> isInteraction = true;
            case P -> Game.onPause = !Game.onPause;
        }
    }
}
