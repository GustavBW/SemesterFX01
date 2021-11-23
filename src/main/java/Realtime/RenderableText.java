package Realtime;

import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class RenderableText implements Renderable {

    //Simple onscreen text with a lifetime of up to 2.1k seconds (35 minutes)

    private final String text;
    private final Point2D position;
    private int lifetime;
    private final long timeOfDeath;

    public RenderableText(String text, Point2D position, int lifetimeMS){
        this.text = text;
        this.position = position;
        this.lifetime = lifetimeMS;

        timeOfDeath = System.currentTimeMillis() + lifetime;
        onInstancedRender();
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.fillText(text,position.getX(),position.getY());

        if(System.currentTimeMillis() >= timeOfDeath){
            destroy();
        }
    }

    private void destroy(){
        Renderable.renderLayer1.remove(this);
    }

    @Override
    public void onInstancedRender() {
        Renderable.renderLayer1.add(this);
    }

    public String getText(){return text;}
    public Point2D getPosition(){return position;}

    public void setLifetime(int i){lifetime = i;}
}
