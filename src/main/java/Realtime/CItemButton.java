package Realtime;

import Realtime.interfaces.Clickable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import worldofzuul.InventoryManager;

import java.util.ArrayList;

public class CItemButton implements Clickable, Renderable {

    private Point2D position;
    private int sizeX,sizeY;
    private CItem citem;
    private InventoryManager inventoryManager;
    private long timeOfDeath = 0;
    public static ArrayList<CItemButton> deadItemButtons = new ArrayList<>();


    public CItemButton (CItem citem, Point2D position, InventoryManager inventoryManager, int sizeX, int sizeY, int lifeTime){
        this.citem = citem;
        this.position = position;
        this.inventoryManager = inventoryManager;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        timeOfDeath = System.currentTimeMillis() + lifeTime;

        onInstancedClick();
        onInstancedRender();
    }




    @Override
    public int getX() {
        return (int) position.getX();
    }
    @Override
    public int getY() {
        return (int) position.getY();
    }
    @Override
    public int getSize() {
        return (sizeX + sizeY) / 2;
    }


    @Override
    public void render(GraphicsContext gc) {

        gc.setFill(new Color(1,1,1,0.2));
        gc.fillRoundRect(position.getX(), position.getY(), sizeX, sizeY, sizeX, sizeY);

        gc.setFill(new Color(1,1,1,1));

        for(int i = 0; i < 3; i++) {
            gc.fillRect(position.getX() + ((sizeX / 2.0) - 7.5), position.getY() + ((sizeY / 2.0) - 6) + (i * 5), 15, 2);
        }


        if(System.currentTimeMillis() > timeOfDeath){
            deadItemButtons.add(this);
        }
    }

    public void destroy(){
        Renderable.renderLayer4.remove(this);
    }

    @Override
    public void onInstancedRender() {
        Renderable.renderLayer4.add(this);
    }
    @Override
    public void onInstancedClick() {
        Clickable.clickables.add(this);
    }

}
