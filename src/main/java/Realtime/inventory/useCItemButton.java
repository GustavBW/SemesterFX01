package Realtime.inventory;

import Realtime.RenderableButton;
import Realtime.interfaces.Clickable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;

public class useCItemButton extends RenderableButton implements Clickable, Renderable {

    private CItem citem;


    public useCItemButton(CItem citem, String text, Point2D position, int sizeX, int sizeY, int lifetime) {
        super(text, position, sizeX, sizeY, lifetime);
        this.citem = citem;
    }


}
