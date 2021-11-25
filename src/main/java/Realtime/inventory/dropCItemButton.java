package Realtime.inventory;

import Realtime.RenderableButton;
import Realtime.interfaces.Clickable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import worldofzuul.Game;

public class dropCItemButton extends RenderableButton implements Renderable, Clickable {

    private CItem citem;
    private boolean active = false;
    private String text;


    public dropCItemButton(CItem citem, String text, Point2D position, int sizeX, int sizeY, int lifetime) {
        super(text, position, sizeX, sizeY, lifetime);
        this.citem = citem;
        this.text = text;
    }

    @Override
    public void onInteraction() {
        if(active) {
            Game.getInventoryManager().dropCItem(citem);
            System.out.println("You pressed a dropCItemButton");
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if(active) {
            gc.setFill(new Color(1,1,1,0.5));
            gc.fillRoundRect(super.getX(),super.getY(),super.getSizes().getX(),super.getSizes().getY(),5,5);
            gc.fillText(text,super.getX(),super.getY() + (super.getSizes().getY() / 2));
        }
    }

    public void setActive(boolean active){
        this.active = active;
    }
    public void toggleActive(){
        this.active = !this.active;
    }
}
