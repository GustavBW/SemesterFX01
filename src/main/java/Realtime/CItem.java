package Realtime;

import BackEnd.PosPicCombo;
import Realtime.Interactible;
import Realtime.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import worldofzuul.Item;

public class CItem extends Item implements Interactible, Renderable {

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
    public void onInVicinity() {

    }
    @Override
    public void onInstancedRender() {
        //Since CItems are de-rendered when the rooms change, it can't add itself when instantied. MGUIC takes care of it.
    }
    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(picture, position.getX(),position.getY());
    }
}
