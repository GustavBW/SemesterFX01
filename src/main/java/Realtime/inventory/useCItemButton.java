package Realtime.inventory;

import Realtime.RenderableButton;
import Realtime.interfaces.Clickable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import worldofzuul.Game;

public class useCItemButton extends dropCItemButton implements Clickable, Renderable {

    private CItem citem;

    public useCItemButton(CItem citem, String text, Point2D position, int sizeX, int sizeY) {
        super(citem,  text,  position,  sizeX,  sizeY);
        this.citem = citem;
    }

    @Override
    public void onInteraction() {
        Game.getInventoryManager().useCItem(citem);
        System.out.println("You pressed a useCItemButton for CITEM id: " + citem.getId());
    }
}
