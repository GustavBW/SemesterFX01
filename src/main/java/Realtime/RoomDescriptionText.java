package Realtime;

import Realtime.interfaces.Clickable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import worldofzuul.Game;

public class RoomDescriptionText extends TemporaryRenderable implements Renderable, Clickable {

    private Point2D position;
    private Point2D sizes;
    private String text;
    private int advance;    //Since you can click this thing multiple times. This tracks how long you've "advanced".

    public RoomDescriptionText(String text, int lifetime){
        super(lifetime + System.currentTimeMillis());
        this.text = text;
        sizes = new Point2D(Game.WIDTH / 3.0, Game.HEIGHT / 5.0);   //Don't worry about it. It'll work :D
        position = new Point2D(Game.WIDTH - (sizes.getX() * 2), Game.HEIGHT - (sizes.getY() * 4));
    }



    @Override
    public boolean inBounds(int x, int y) {
        return (position.getX() < x + sizes.getX() && position.getX() > x) && (position.getY() < y + sizes.getY() && position.getY() > y);
    }


    @Override
    public void onInteraction() {
        advance++;
    }



    @Override
    public void render(GraphicsContext gc) {

    }

    @Override
    public void destroy() {
        Clickable.clickables.remove(this);
        Renderable.renderLayer4.remove(this);
    }

    @Override
    public Point2D getSizes() {
        return sizes;
    }
    @Override
    public int getX() {
        return (int) position.getX();
    }
    @Override
    public int getY() {
        return (int) position.getY();
    }
    @Override
    public int getInterRadius() {
        return 0;
    }


    @Override
    public void onInstancedRender() {
        Renderable.renderLayer4.add(this);
    }
    @Override
    public void onInstancedClick() {
        Clickable.clickables.add(this);
    }
}
