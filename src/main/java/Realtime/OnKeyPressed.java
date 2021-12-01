package Realtime;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import worldofzuul.Game;

public class OnKeyPressed implements EventHandler<KeyEvent> {

    private MainGUIController mGUIC;

    public OnKeyPressed(MainGUIController mGUIC){
        this.mGUIC = mGUIC;
    }

    @Override
    public void handle(KeyEvent keyEvent) {

        switch (keyEvent.getCode()) {
            case W: {
                Game.player.setUpKeyPressed(true);
                break;
            }
            case D: {
                Game.player.setRightKeyPressed(true);
                break;
            }
            case S: {
                Game.player.setDownKeyPressed(true);
                break;
            }
            case A: {
                Game.player.setLeftKeyPressed(true);
                break;
            }
            case SHIFT: {
                Game.player.setRunning(true);
                break;
            }
            case E: {
                if (InteractionHandler.interactibleReadyToInteract != null) {
                    InteractionHandler.interactibleReadyToInteract.onInteraction();
                }
                break;
            }
            case P: {
                Game.onPause = !Game.onPause;
                break;
            }
            case I: {
                mGUIC.toggleInventoryGUI();
                break;
            }
            case TAB: {
                mGUIC.toggleQuestGUI();
                break;
            }
            case U: {     //Debugging shortcut
                Game.updateQuestGUI = true;
                Game.getInventoryManager().inventoryChanged = true;
                break;
            }
        }

    }
}
