package Realtime.debugging;

import Realtime.TemporaryRenderable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RenderableCircle extends TemporaryRenderable implements Renderable {

    private int diameter, posX, posY;
    private long timeOfDeath;
    private final Color color;
    private final Point2D center;

    public RenderableCircle(Point2D position, int radius, int lifetime, Color color){
        super(lifetime + System.currentTimeMillis());
        this.diameter = radius * 2;
        this.color = color;
        this.center = position;
        this.timeOfDeath = System.currentTimeMillis() + lifetime;

        posX = (int) center.getX() - radius;
        posY = (int) center.getY() - radius;

    }

    @Override
    public void onInstancedRender() {
        Renderable.renderLayer4.add(this);
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.setFill(color);
        gc.fillRoundRect(posX, posY, diameter,diameter,diameter,diameter);

        if(System.currentTimeMillis() > timeOfDeath){
            TemporaryRenderable.tempRends.add(this);
        }
    }

    @Override
    public void destroy() {
        Renderable.renderLayer3.remove(this);
    }


    @Override
    public String toString(){
        return "Renderable Circle | position x : " + posX + " | position y : " + posY + " | radius r : " + diameter / 2;
    }
}
