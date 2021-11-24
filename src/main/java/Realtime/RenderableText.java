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
    private long timeOfDeath;
    private boolean isDead = false;

    public static ArrayList<RenderableText> deadRendText = new ArrayList<>();

    public RenderableText(String text, Point2D position, int lifetimeMS){
        this.text = text;
        this.position = position;

        timeOfDeath = System.currentTimeMillis() + lifetimeMS;
        onInstancedRender();
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillText(text, position.getX(), position.getY());

        if(System.currentTimeMillis() > timeOfDeath || isDead){
            expired();
        }
    }

    private void expired(){
        isDead = true;
        deadRendText.add(this);
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
    public void extendLifetime(int i){
        timeOfDeath += i;
    }
}
