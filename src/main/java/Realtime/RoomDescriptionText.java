package Realtime;

import Realtime.interfaces.Clickable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import worldofzuul.Game;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class RoomDescriptionText extends TemporaryRenderable implements Renderable, Clickable {

    private final Point2D position;
    private final Point2D sizes;
    private final String text;
    private int advance;    //Since you can click this thing multiple times. This tracks how long you've "advanced".
    private final Font fontToUse;
    private final Color color1;
    private final Color color2;
    private final ArrayList<String> textAsLines;
    private int linesMade;
    private final int linesToShowAtATime = 4;
    private final int lineHeight = 10;

    public RoomDescriptionText(String text, int lifetime){
        super(lifetime + System.currentTimeMillis());
        this.text = text;
        sizes = new Point2D(Game.WIDTH / 3.0, Game.HEIGHT / 5.0);   //Don't worry about it. It'll work :D
        position = new Point2D(Game.WIDTH - (sizes.getX() * 2), Game.HEIGHT - (sizes.getY() * 4));

        fontToUse = new Font("Times New Roman", 16D);
        color1 = new Color(1,1,1,0.5);
        color2 = new Color(0,0,0,0.5);

        textAsLines = getTextAsStringArray(text, 50);
    }


    private ArrayList<String> getTextAsStringArray(String text, int symbolsPerLine){    //WHy do I have the strange feeling that this isn't how you do this?
        ArrayList<String> array = new ArrayList<String>();
        linesMade = 0;

        int[] asCharArray = text.chars().toArray();

        int next = 0;
        while (asCharArray[next] != 0){

            StringBuilder toAddtoArray = new StringBuilder();

            for(int i = 0; i < symbolsPerLine; i++){
                toAddtoArray.append((char) asCharArray[next + i]);
            }

            array.add(toAddtoArray.toString());

            linesMade++;
            next += symbolsPerLine;
        }

        return array;
    }


    @Override
    public boolean inBounds(int x, int y) {
        return (position.getX() < x + sizes.getX() && position.getX() > x) && (position.getY() < y + sizes.getY() && position.getY() > y);
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
        gc.fillRoundRect(position.getX() - 5, position.getY() - 5, sizes.getX() + 5, sizes.getY() + 200, 10, 10);   //This will go out of the screen. And that is intentional

        //Inner Rectangle
        gc.setFill(color1);
        gc.fillRoundRect(position.getX(), position.getY(), sizes.getX(), sizes.getY() + 200, 10, 10);   //This will go out of the screen. And that is intentional

        for(int i = 0; i < linesToShowAtATime; i++){

            gc.setFill(Color.BLACK);
            gc.setFont(fontToUse);
            gc.fillText(textAsLines.get(i + (advance * linesToShowAtATime)), position.getX() + 5, position.getY() + (i * lineHeight) + 5, Game.WIDTH / 3.0);

        }
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
