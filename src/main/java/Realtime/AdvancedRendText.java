package Realtime;

import Realtime.interfaces.Renderable;
import javafx.beans.NamedArg;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class AdvancedRendText implements Renderable {

    private final Font fontToUse;
    private final Point2D position;
    private final String text;
    private final Color color;
    private final int symbolsPerLine;
    private final ArrayList<String> lines;
    private final double lineHeight;
    private final double textLineOffset = 1;

    public AdvancedRendText(String text, Point2D position, Font font, Color color, int symbolsPerLine) {
        this.text = text;
        this.position = position;
        this.fontToUse = font;
        this.color = color;
        this.symbolsPerLine = symbolsPerLine;

        lineHeight = font.getSize() * textLineOffset;
        lines = ExtendedFunctions.toLines(text, symbolsPerLine, " ");
    }

    @Override
    public void render(GraphicsContext gc) {
        int counter = 0;

        gc.setFill(color);
        gc.setFont(fontToUse);

        for(String s : lines) {
            gc.fillText(s, position.getX(), position.getY() + (lineHeight * counter));
            counter++;
        }
    }

    @Override
    public void onInstancedRender() {    }

    public double getTotalHeight(){
        return lines.size() * textLineOffset * fontToUse.getSize();
    }

    public double getY(){return position.getY();}
    public double getX(){return position.getX();}

    public AdvancedRendText getDropShadow(double pixelOffset){
        return new AdvancedRendText(text, new Point2D(position.getX() + pixelOffset, position.getY() + pixelOffset), fontToUse, Color.BLACK, symbolsPerLine);
    }
}
