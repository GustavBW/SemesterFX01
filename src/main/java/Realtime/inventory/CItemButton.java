package Realtime.inventory;

import Realtime.interfaces.Clickable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class CItemButton implements Clickable, Renderable {

    private Point2D position;
    private int sizeX,sizeY;
    private CItem citem;
    private long timeOfDeath = 0;
    private boolean showSubButtons = false;
    private useCItemButton uCIB;
    private dropCItemButton dCIB;
    private int interactionCount = 0, lifetime;

    private Color color1 = new Color(1,1,1,0.2);
    private Color color2 = new Color(1,1,1,0.5);

    public static ArrayList<CItemButton> deadItemButtons = new ArrayList<>();

    public CItemButton (CItem citem, Point2D position, int sizeX, int sizeY, int lifeTime){
        this.citem = citem;
        this.position = position;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        lifetime = lifeTime;

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
    public Point2D getSizes() {
        return new Point2D(sizeX,sizeY);
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.setFill(color1);
        gc.fillRoundRect(position.getX(), position.getY(), sizeX, sizeY, sizeX, sizeY);

        gc.setFill(color2);

        for(int i = 0; i < 3; i++) {
            gc.fillRect(position.getX() + ((sizeX / 2.0) - 7.5), position.getY() + ((sizeY / 2.0) - 6) + (i * 5), 15, 2);
        }

        if(showSubButtons){
            uCIB.render(gc);
            dCIB.render(gc);
        }

        if(System.currentTimeMillis() > timeOfDeath){
            deadItemButtons.add(this);
        }
    }
    private void createSubButtons(int lifetime){
        Point2D sub1Position = new Point2D(position.getX(), position.getY() + sizeY);
        uCIB = new useCItemButton(citem,"Use",sub1Position, 100,sizeY,lifetime);

        Point2D sub2Position = new Point2D(position.getX(), position.getY() + 2 * sizeY);
        dCIB = new dropCItemButton(citem,"Drop", sub2Position,100,sizeY,lifetime);
    }

    @Override
    public void onInteraction(){
        System.out.println("You pressed a CItemButton");
        interactionCount++;

        if(interactionCount == 1){
            createSubButtons(100);
        }

        showSubButtons = !showSubButtons;
        uCIB.toggleActive();
        dCIB.toggleActive();
    }
    public void destroy(){
        Renderable.renderLayer4.remove(this);
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
