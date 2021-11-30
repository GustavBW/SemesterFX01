package Realtime;

import BackEnd.ContentEngine;
import BackEnd.RoomCollection;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import worldofzuul.Game;

public class OnKeyReleased implements EventHandler<KeyEvent> {

    public OnKeyReleased(){}

    @Override
    public void handle(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case W: {
                Game.player.setUpKeyPressed(false);
                break;
            }
            case D: {
                Game.player.setRightKeyPressed(false);
                break;
            }
            case S: {
                Game.player.setDownKeyPressed(false);
                break;
            }
            case A: {
                Game.player.setLeftKeyPressed(false);
                break;
            }
        }
    }
}
