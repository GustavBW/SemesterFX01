package Realtime.inventory;

import Realtime.RenderableButton;
import Realtime.debugging.RenderableSquare;
import Realtime.interfaces.Clickable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import worldofzuul.Game;

public class dropCItemButton extends RenderableButton implements Renderable, Clickable {

    private CItem citem;
    private String text;
    private Point2D position;
    private int sizeX, sizeY, interRadius = 10, x, y;

    public dropCItemButton(CItem citem, String text, Point2D position, int sizeX, int sizeY) {
        super(text, position, sizeX, sizeY, 2);
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        this.citem = citem;
        this.text = text;
        this.position = position;
    }

    @Override
    public int getInterRadius() {
        return interRadius;
    }

    @Override
    public boolean isInBounds(int pOrX,int pOrY){
        //If each of the coordinates given (x & y) is within the boundaries of the square trigger. Based on its position and its size.
        return super.isInBounds(pOrX, pOrY);
    }

    @Override
    public void expired(){}

    @Override
    public void onInteraction() {
        Game.getInventoryManager().useCItem(citem);
        System.out.println("You pressed a dropCItemButton for CITEM id: " + citem.getId());
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.setFill(new Color(1,1,1,0.5));
        gc.fillRoundRect(position.getX(),position.getY(),sizeX,sizeY,5,5);

        gc.setFill(Color.BLACK);
        gc.fillText(text,position.getX() + 5,position.getY() + (sizeY / 2.0) + 4);

    }

    @Override
    public void onInstancedRender() {    }

    @Override
    public void onInstancedClick() {    }
}
