package Realtime.debugging;

import Realtime.TemporaryRenderable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RenderableSquare extends TemporaryRenderable implements Renderable {

    private double sizeX;
    private double sizeY;
    private int posX;
    private int posY;
    private long timeOfDeath;
    private final Color color;
    private final Point2D position;

    public RenderableSquare(Point2D position, double sizeX, double sizeY, int lifetime, Color color){
        super(lifetime + System.currentTimeMillis());
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.color = color;
        this.position = position;
        this.timeOfDeath = System.currentTimeMillis() + lifetime;

        onInstancedRender();
    }

    @Override
    public void onInstancedRender() {
        Renderable.renderLayer3.add(this);
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.setFill(color);
        gc.fillRect(position.getX(),position.getY(), sizeX, sizeY);

        if(System.currentTimeMillis() > timeOfDeath){
            TemporaryRenderable.tempRends.add(this);
        }
    }

    @Override
    public void destroy() {
        Renderable.renderLayer3.remove(this);
    }
}

