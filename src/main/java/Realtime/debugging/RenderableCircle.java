package Realtime.debugging;

import Realtime.TemporaryRenderable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RenderableCircle extends TemporaryRenderable implements Renderable {

    private int radius, posX, posY;
    private long timeOfDeath;
    private final Color color;
    private final Point2D center;

    public RenderableCircle(Point2D position, int radius, int lifetime, Color color){
        super(lifetime + System.currentTimeMillis());
        this.radius = radius;
        this.color = color;
        this.center = position;
        this.timeOfDeath = System.currentTimeMillis() + lifetime;

        posX = (int) center.getX() - radius / 2;
        posY = (int) center.getY() - radius / 2;

        onInstancedRender();
    }

    @Override
    public void onInstancedRender() {
        Renderable.renderLayer3.add(this);
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.setFill(color);
        gc.fillRoundRect(posX, posY, radius,radius, radius, radius);

        if(System.currentTimeMillis() > timeOfDeath){
            TemporaryRenderable.tempRends.add(this);
        }
    }

    @Override
    public void destroy() {
        Renderable.renderLayer3.remove(this);
    }
}
