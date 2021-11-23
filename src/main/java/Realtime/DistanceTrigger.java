package Realtime;

import Realtime.interfaces.Interactible;
import Realtime.interfaces.Renderable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DistanceTrigger implements Interactible, Renderable {

    //Remove renderable implimentation when interaction testing is done

    private int posX, posY, interRadius;

    public DistanceTrigger(){
        this(0,0,0);
    }

    public DistanceTrigger(int x, int y, int r){
        this.posX = x;
        this.posY = y;
        this.interRadius = r;

        onInstancedInter();
        onInstancedRender();
    }

    @Override
    public void onInstancedInter() {
        Interactible.interactibles.add(this);
    }

    @Override
    public void onInteraction() {
        System.out.println("You interacted with a distance trigger");
    }

    @Override
    public void onInVicinity() {}

    @Override
    public String getPopUpText() {
        return "Whats up my dude!";
    }

    @Override
    public int getInterRadius() {
        return interRadius;
    }

    @Override
    public int getPosX() {
        return posX;
    }
    @Override
    public int getPosY() {
        return posY;
    }

    public void setPosX(int i){posX = i;}
    public void setPosY(int i){posY = i;}
    public void setInterRadius(int i){interRadius = i;}

    @Override
    public void onInstancedRender() {
        Renderable.renderLayer1.add(this);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.GREY);
        gc.fillOval(posX - (interRadius / 2.0),posY - (interRadius / 2.0),interRadius,interRadius);

        gc.setFill(Color.BLACK);
        gc.fillRect(posX,posY,10,10);

    }

    @Override
    public boolean isDead() {
        return false;
    }
}
