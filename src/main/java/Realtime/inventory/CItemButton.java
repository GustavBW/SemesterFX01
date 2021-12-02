package Realtime.inventory;

import Realtime.debugging.RenderableCircle;
import Realtime.interfaces.Clickable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class CItemButton implements Clickable, Renderable {

    private final Point2D position;
    private final int sizeX,sizeY, interRadius;
    private final CItem citem;
    private long timeOfDeath = 0;
    private boolean showSubButtons = false;
    private InventoryGUIManager iGUIM;

    private Color color1 = new Color(1,1,1,0.2);
    private Color color2 = new Color(1,1,1,0.5);

    public CItemButton (CItem citem, Point2D position, int sizeX, int sizeY, InventoryGUIManager iGUIM){
        this.citem = citem;
        this.position = position;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.interRadius = ((sizeX + sizeY) / 2) / 2;
        this.iGUIM = iGUIM;
    }

    @Override
    public int getX() {
        return (int) position.getX() + (sizeX / 2);
    }
    @Override
    public int getY() {
        return (int) position.getY() + (sizeY / 2);
    }
    @Override
    public Point2D getSizes() {
        return new Point2D(sizeX,sizeY);
    }

    @Override
    public void render(GraphicsContext gc) {

        if(iGUIM.getInspectedElement() == citem) {

            gc.setFill(new Color(1,1,1,0.8));
            gc.fillRoundRect(position.getX(), position.getY(), sizeX, sizeY, sizeX, sizeY);

            gc.setFill(new Color(0,0,0,0.8));

            for (int i = 0; i < 3; i++) {
                gc.fillRect(position.getX() + ((sizeX / 2.0) - 6) + (i * 5),position.getY() + ((sizeY / 2.0) - 7.5) , 2, 15);

            }
        }else{

            gc.setFill(color1);
            gc.fillRoundRect(position.getX(), position.getY(), sizeX, sizeY, sizeX, sizeY);

            gc.setFill(color2);

            for (int i = 0; i < 3; i++) {
                gc.fillRect(position.getX() + ((sizeX / 2.0) - 7.5), position.getY() + ((sizeY / 2.0) - 6) + (i * 5), 15, 2);
            }
        }
    }

    @Override
    public void onInteraction(){
        iGUIM.setInspectedElement(citem);
    }

    @Override
    public int getInterRadius() {
        return interRadius;
    }

    public void destroy(){
        Clickable.clickables.remove(this);
    }

    @Override
    public void onInstancedRender() {
        Renderable.renderLayer4.add(this);
    }

    @Override
    public void onInstancedClick() {
        Clickable.clickables.add(this);
    }

    @Override
    public boolean inBounds(int x, int y) {
        return false;
    }

}
