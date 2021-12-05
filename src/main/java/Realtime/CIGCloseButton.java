package Realtime;

import Realtime.interfaces.Clickable;
import Realtime.interfaces.Renderable;
import Realtime.triggers.SquareTrigger;
import javafx.scene.canvas.GraphicsContext;

public class CIGCloseButton extends SquareTrigger implements Clickable, Renderable {


    public CIGCloseButton(int x, int y, int sizeX, int sizeY){
        super(x,y,sizeX,sizeY);




    }

    @Override
    public void onInstancedClick() {

    }

    @Override
    public boolean inBounds(int x, int y) {
        return false;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void onInstancedRender() {

    }

    @Override
    public void render(GraphicsContext gc) {

    }
}
