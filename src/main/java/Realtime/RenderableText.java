package Realtime;

import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class RenderableText implements Renderable {

    //Simple onscreen text with a lifetime of up to 2.1k seconds (35 minutes)

    private final String text;
    private final Point2D position;
    private int lifetime;
    private long timeOfDeath;
    private boolean isDead = false;

    public static ArrayList<RenderableText> deadRendText = new ArrayList<>();

    public RenderableText(String text, Point2D position, int lifetimeMS){
        this.text = text;
        this.position = position;
        this.lifetime = lifetimeMS;

        timeOfDeath = System.currentTimeMillis() + lifetimeMS;
        onInstancedRender();
        System.out.println("Created RenderableText with timeOfDeath : " + timeOfDeath + " at SysTimeMS : " + System.currentTimeMillis());
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillText(text,position.getX(),position.getY());

        if(System.currentTimeMillis() > timeOfDeath || isDead){
            expired();
        }
    }

    private void expired(){
        isDead = true;
        deadRendText.add(this);
        System.out.println("Destroyed Renderable Text with timeOfDeath : " + timeOfDeath + " at SysTimeMS : " + System.currentTimeMillis());
    }

    @Override
    public boolean isDead() {
        return isDead;
    }

    public void destroy(){
        Renderable.renderLayer4.remove(this);
    }

    @Override
    public void onInstancedRender() {
        Renderable.renderLayer4.add(this);
    }

    public String getText(){return text;}
    public Point2D getPosition(){return position;}
    public void setLifetime(int i){lifetime = i;}
}
