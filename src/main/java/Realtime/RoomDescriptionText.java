package Realtime;

import Realtime.debugging.RenderableSquare;
import Realtime.interfaces.Clickable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import worldofzuul.Game;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class RoomDescriptionText extends TemporaryRenderable implements Renderable, Clickable {

    //This is able to display a lot of text also scrolling through it by clicking on the box
    //When you click to advance through the last bit of text, the instance is added to the
    //TemporaryRenderable.tempRends arraylist. That arraylist is loop through every frame and
    //called destroy() on any instances in it.

    private final Point2D position;
    private final Point2D sizes;
    private String next = "Next";
    private int advance;    //Since you can click this thing multiple times. This tracks how long you've "advanced".
    private final Font fontToUse;
    private final Font nextTextFont;
    private final Color color1;
    private final Color color2;
    private final Color textColor;
    private final Color nextTextColor;
    private final ArrayList<String> textAsLines;
    private int linesMade = 0;
    private final int linesToShowAtATime = 4;
    private final int lineHeight = 30;
    private int offset = 40;

    public RoomDescriptionText(String text, int lifetime){
        super(lifetime + System.currentTimeMillis());
        sizes = new Point2D(Game.WIDTH / 3.0, Game.HEIGHT / 5.0);   //Don't worry about it. It'll work :D
        position = new Point2D(Game.WIDTH - (sizes.getX() * 2), Game.HEIGHT * 0.8);

        fontToUse = Font.font("Times New Roman", 20D);
        nextTextFont = Font.font("Impact", 26D);

        color2 = new Color(255/255.0,255/255.0,255/255.0,0.5);
        color1 = new Color(0/255.0,0/255.0,0/255.0,0.5);
        textColor = new Color(1,1,1,1);
        nextTextColor = new Color(209 / 255.0,153 / 255.0,0,1);

        textAsLines = ExtendedFunctions.toLines(text,60," ");
        linesMade = textAsLines.size();

        onInstancedClick();
        onInstancedRender();
    }

    @Override
    public boolean inBounds(int x, int y) {

        boolean rangeX = x < position.getX() + sizes.getX() && x > position.getX();
        boolean rangeY = y < position.getY() + sizes.getY() && y > position.getY();

        return (rangeX && rangeY);
    }

    @Override
    public void onInteraction() {

        if(advance * linesToShowAtATime > linesMade){   //If you've gotten shown all the text and press again, it marks this for termination next render pass.
            TemporaryRenderable.tempRends.add(this);
        }

        advance++;
    }

    @Override
    public void render(GraphicsContext gc) {

        //Outer Rectangle
        gc.setFill(color2);
        gc.fillRoundRect(position.getX() - 5, position.getY() - 5, sizes.getX() + 10, sizes.getY() + 200, 10, 10);   //This will go out of the screen. And that is intentional

        //Inner Rectangle
        gc.setFill(color1);
        gc.fillRoundRect(position.getX(), position.getY(), sizes.getX(), sizes.getY() + 200, 10, 10);   //This will go out of the screen. And that is intentional

        gc.setFill(textColor);
        gc.setFont(fontToUse);

        for(int i = 0; i < linesToShowAtATime; i++){
            if(i + (advance * linesToShowAtATime) < textAsLines.size()) {
                gc.fillText(textAsLines.get(i + (advance * linesToShowAtATime)), position.getX() + 5, position.getY() + (i * lineHeight) + 25, sizes.getX());
            }
        }

        gc.setFill(nextTextColor);
        gc.setFont(nextTextFont);

        if(advance * linesToShowAtATime > linesMade){
            next = "Close";
            offset = -10;
        }
        gc.fillText(next, position.getX() + (sizes.getX() / 2.0) - 30, position.getY() + (linesToShowAtATime * lineHeight) + offset, sizes.getX());

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
