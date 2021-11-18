package worldofzuul;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class CItem extends Item implements Interactible,Renderable{

    private Point2D position;
    private Image picture;
    private int interRadius = 50;

    public CItem(Item item, PosPicCombo posPic){
        super(item);
        position = posPic.getPos();
        picture = posPic.getPic();
    }



    @Override
    public void onInstancedInter(){
        Interactible.interactibles.add(this);
    }

    @Override
    public int getInterRadius(){return interRadius;}

    @Override
    public void onInteraction() {

    }

    @Override
    public void onInstancedRender() {
        Renderable.renderables.add(this);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(picture, position.getX(),position.getY());
    }
}
