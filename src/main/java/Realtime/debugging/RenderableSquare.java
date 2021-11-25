package Realtime.debugging;

import Realtime.TemporaryRenderable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RenderableSquare extends TemporaryRenderable implements Renderable {

    private int sizeX, sizeY, posX, posY;
    private long timeOfDeath;
    private final Color color;
    private final Point2D position;

    public RenderableSquare(Point2D position, int sizeX, int sizeY, int lifetime, Color color){
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
        gc.fillRect(posX, posY, sizeX, sizeY);

        if(System.currentTimeMillis() > timeOfDeath){
            TemporaryRenderable.tempRends.add(this);
        }
    }

    @Override
    public void destroy() {
        Renderable.renderLayer3.remove(this);
    }
}

