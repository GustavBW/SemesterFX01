package Realtime.inventory;

import Realtime.RenderableButton;
import Realtime.interfaces.Clickable;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import worldofzuul.Game;

public class useCItemButton extends RenderableButton implements Clickable, Renderable {

    private CItem citem;
    private String text;
    private Point2D position;

    public useCItemButton(CItem citem, String text, Point2D position, int sizeX, int sizeY) {
        super(text, position, sizeX, sizeY, 2);
        this.citem = citem;
        this.text = text;
        this.position = position;

    }

    @Override
    public void expired(){}

    @Override
    public void onInteraction() {
        Game.getInventoryManager().useCItem(citem);
        System.out.println("You pressed a useCItemButton");
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.setFill(new Color(1,1,1,0.5));
        gc.fillRoundRect(position.getX(),position.getY(),super.getSizes().getX(),super.getSizes().getY(),5,5);

        gc.setFill(Color.BLACK);
        gc.fillText(text,super.getX() + 5,super.getY() + (super.getSizes().getY() / 2) + 4);

    }

    @Override
    public void onInstancedRender() {}
    @Override
    public void onInstancedClick() {}

}
