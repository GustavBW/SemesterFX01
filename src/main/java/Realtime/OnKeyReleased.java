package Realtime;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import worldofzuul.Game;

public class OnKeyReleased implements EventHandler<KeyEvent> {

    public OnKeyReleased(){}

    @Override
    public void handle(KeyEvent keyEvent) {
        switch (keyEvent.getText()) {
            case "w": {
                Game.player.setUpKeyPressed(false);
                break;
            }
            case "d": {
                Game.player.setRightKeyPressed(false);
                break;
            }
            case "s": {
                Game.player.setDownKeyPressed(false);
                break;
            }
            case "a": {
                Game.player.setLeftKeyPressed(false);
                break;
            }
        }
    }
}
