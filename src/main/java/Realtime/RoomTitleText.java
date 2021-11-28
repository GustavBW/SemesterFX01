package Realtime;

import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import worldofzuul.Game;

public class RoomTitleText implements Renderable {

    //This text will automaticle align right in the middle of the screen at the top.

    private final Point2D position;
    private final Font fontToUse;
    private final String text;
    private Color color1, color2;
    private int dropShadowOffset = 3;


    public RoomTitleText(String text, int fontSize) {
        this.text = text;
        fontToUse = new Font("Impact", fontSize);
        this.position = new Point2D(Game.WIDTH / 2.0 - ((text.getBytes().length * (fontSize * 0.75)) / 2),0);
            //So. Font size is measured in "points" Pt, not pixels (Px), which means above is to take the amount of letters in the text, at a given font size, converted to pixels.
            //That is how I get the size of the text.

        color2 = new Color(1,1,1,1);
        color1 = new Color(0,0,0,1);
    }

    public void destroy(){
        Renderable.renderLayer4.remove(this);
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.setFill(color1);
        gc.setFont(fontToUse);
        gc.fillText(text, position.getX(), position.getY());

        gc.setFill(color2);
        gc.fillText(text, position.getX() + dropShadowOffset,position.getY() + dropShadowOffset);

    }
    @Override
    public void onInstancedRender() {

    }

}
