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

public class dropCItemButton extends RenderableButton implements Renderable, Clickable {

    private CItem citem;
    private String text;
    private Point2D position;
    private int sizeX, sizeY, interRadius = 10, x, y;
    private Color color1;
    private Font fontToUse;

    public dropCItemButton(CItem citem, String text, Point2D position, int sizeX, int sizeY) {
        super(text, position, sizeX, sizeY, 2);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.color1 = new Color(1,1,1,0.5);
        this.citem = citem;
        this.text = text;
        this.position = position;

        fontToUse = new Font("Helvetica", 16D);
    }

    @Override
    public int getInterRadius() {
        return interRadius;
    }

    @Override
    public boolean isInBounds(double pOrX,double pOrY){
        //If each of the coordinates given (x & y) is within the boundaries of the square trigger. Based on its position and its size.
        return super.isInBounds(pOrX, pOrY);
    }

    @Override
    public void expired(){}

    @Override
    public void onInteraction() {
        color1 = new Color(1,1,1,0.8);
        Game.getiGUIM().setInspectedElement(null);
        Game.getInventoryManager().inventoryChanged = true;
        Game.getInventoryManager().dropCItem(citem);
        System.out.println("You pressed a dropCItemButton for CITEM id: " + citem.getId());
    }

    public void destroy(){
        Clickable.clickables.remove(this);
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.setFill(color1);
        gc.fillRoundRect(position.getX(),position.getY(),sizeX,sizeY,10,10);

        gc.setFill(Color.BLACK);
        gc.setFont(fontToUse);
        gc.fillText(text,position.getX() + 2,position.getY() + (sizeY / 2.0) + 6);

    }

    @Override
    public void onInstancedRender() {    }

    @Override
    public void onInstancedClick() {Clickable.clickables.add(this);}
}
