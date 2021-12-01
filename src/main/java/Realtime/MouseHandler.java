package Realtime;

import Realtime.interfaces.Clickable;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MouseHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent mouseEvent) {

        if(mouseEvent.getButton() == MouseButton.PRIMARY){
            Clickable c = InteractionHandler.findInteractibleOnMouseClick( (int) mouseEvent.getX(), (int) mouseEvent.getY());
            if(c != null){
                c.onInteraction();
            }
            System.out.println(c);
        }
    }
}
