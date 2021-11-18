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

        onInstancedInter();
        onInstancedRender();
    }

    @Override
    public void onInstancedInter(){
        Interactible.interactibles.add(this);
    }
    @Override
    public int getInterRadius(){return interRadius;}
    @Override
    public int getPosX() {
        return (int) position.getX();
    }
    @Override
    public int getPosY() {
        return (int) position.getY();
    }
    @Override
    public void onInteraction() {
        System.out.println("You're now in radius of Item: " + super.getName());
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
