package Realtime.inventory;

import Realtime.RenderableButton;
import Realtime.debugging.RenderableSquare;
import Realtime.interfaces.Clickable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import worldofzuul.Game;

public class useCItemButton extends dropCItemButton implements Clickable, Renderable {

    private CItem citem;
    private Point2D position;
    private int sizeX, sizeY;
    private Color color1;
    private String text;
    private Font fontToUse;

    public useCItemButton(CItem citem, String text, Point2D position, int sizeX, int sizeY) {
        super(citem,  text,  position,  sizeX,  sizeY);
        this.citem = citem;
        this.position = position;
        this.color1 = new Color(1,1,1,0.5);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.text = text;

        fontToUse = new Font("Helvetica", 16D);
    }

    @Override
    public void onInteraction() {
        color1 = new Color(1,1,1,0.8);
        Game.getInventoryManager().inventoryChanged = true;
        Game.getInventoryManager().useCItem(citem);
        System.out.println("You pressed a useCItemButton for CITEM id: " + citem.getId());
    }

    @Override
    public void render(GraphicsContext gc){
        gc.setFill(color1);
        gc.fillRoundRect(position.getX(),position.getY(),sizeX,sizeY,10,10);

        gc.setFill(Color.BLACK);
        gc.setFont(fontToUse);
        gc.fillText(text,position.getX() + 7,position.getY() + (sizeY / 2.0) + 6);
    }

    @Override
    public void onInstancedRender() {    }

    @Override
    public void onInstancedClick() { Clickable.clickables.add(this); }

    @Override
    public void destroy(){
        Clickable.clickables.remove(this);
    }
}
