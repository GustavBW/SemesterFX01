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
            case W -> {
                Game.player.setUpKeyPressed(true);
            }
            case D -> {
                Game.player.setRightKeyPressed(true);
            }
            case S -> {
                Game.player.setDownKeyPressed(true);
            }
            case A -> {
                Game.player.setLeftKeyPressed(true);
            }
            case SHIFT -> {
                Game.player.setRunning(true);
            }
            case E -> {
                if (InteractionHandler.interactibleReadyToInteract != null) {
                    InteractionHandler.interactibleReadyToInteract.onInteraction();
                }
            }
            case P -> {
                Game.onPause = !Game.onPause;
            }
            case I -> {
                mGUIC.toggleInventoryGUI();
            }
            case TAB -> {
                mGUIC.toggleQuestGUI();
            }
            case U -> {     //Debugging shortcut
                Game.updateQuestGUI = true;
                Game.getInventoryManager().inventoryChanged = true;
            }
            case F3 -> {
                mGUIC.togglePerformanceGUI();
            }
            case ESCAPE -> {
                mGUIC.hideAllGUIs();
            }
        }
    }
}
