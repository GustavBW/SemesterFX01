package Realtime;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class MouseMoveHandler implements EventHandler<MouseEvent> {
    private MainGUIController mGUIC;
    public static double mouseX, mouseY;

    public MouseMoveHandler(MainGUIController mguic) {
        this.mGUIC = mguic;
        mouseX = 0;
        mouseY = 0;
    }
    public void handle(MouseEvent mouseEvent) {
        this.mouseX = mouseEvent.getX();
        this.mouseY = mouseEvent.getY();
    }
}
