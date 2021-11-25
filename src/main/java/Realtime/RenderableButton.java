package Realtime;

import Realtime.interfaces.Clickable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class RenderableButton extends SquareTrigger implements Clickable, Renderable {

    private String text;
    private int lifetime;
    private long timeOfDeath;

    public static ArrayList<RenderableButton> deadRendButton = new ArrayList<>();

    public RenderableButton(String text, Point2D position, int sizeX, int sizeY, int lifetime){
        super((int) position.getX(), (int) position.getY(), sizeX, sizeY);
        this.text = text;
        this.lifetime = lifetime;
        this.timeOfDeath = System.currentTimeMillis() + lifetime;

        onInstancedRender();
        onInstancedClick();
    }

    @Override
    public void onInstancedInter() {    }

    @Override
    public void onInstancedClick() {
        Clickable.clickables.add(this);
    }

    @Override
    public boolean inBounds(int x, int y) {
        return super.isInBounds(x,y);
    }

    @Override
    public int getX() {
        return super.getPosX();
    }

    @Override
    public int getY() {
        return super.getPosY();
    }

    @Override
    public Point2D getSizes() {
        return super.getSizes();
    }

    @Override
    public void onInstancedRender() {
        Renderable.renderLayer4.add(this);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.GREY);
        gc.fillRect(super.getPosX(),super.getPosY(),super.getSizes().getX(),super.getSizes().getY());

        if(System.currentTimeMillis() > timeOfDeath){
            expired();
        }
    }
    public void expired(){
        deadRendButton.add(this);
    }
    public void destroy(){
        Renderable.renderLayer4.remove(this);
        Clickable.clickables.remove(this);
    }
}
