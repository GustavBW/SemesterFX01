package Realtime;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import worldofzuul.Game;

public class OnKeyPressed implements EventHandler<KeyEvent> {


    public OnKeyPressed(){
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        switch (keyEvent.getText()) {
            case "w": {
                Game.player.setUpKeyPressed(true);
                break;
            }
            case "d": {
                Game.player.setRightKeyPressed(true);
                break;
            }
            case "s": {
                Game.player.setDownKeyPressed(true);
                break;
            }
            case "a": {
                Game.player.setLeftKeyPressed(true);
                break;
            }
        }
    }
}
