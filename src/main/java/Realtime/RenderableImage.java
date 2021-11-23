package Realtime;

import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class RenderableImage implements Renderable {

    private final Point2D position;
    private final double width, height;
    private final Image image;

    public RenderableImage (Image image){
        this.position = new Point2D(0,0);
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }


    @Override
    public void onInstancedRender() {
        //Do nothing. This is used for the base images.
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(image,position.getX(),position.getY());
    }

    @Override
    public boolean isDead() {
        return false;
    }

    public double getWidth(){
        return width;
    }
    public double getHeight(){
        return height;
    }
    public Image getImage(){
        return image;
    }
}
