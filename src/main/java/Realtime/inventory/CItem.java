package Realtime.inventory;

import BackEnd.PosPicCombo;
import Realtime.InteractionHandler;
import Realtime.MainGUIController;
import Realtime.interfaces.Interactible;
import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import worldofzuul.Game;

import java.util.Random;

public class CItem extends Item implements Interactible, Renderable {

    private Point2D position;
    private Image picture;
    private Item item;
    private PosPicCombo posPic;
    private int interRadius = 50;

    private static Random r = new Random();

    public CItem(Item item, PosPicCombo posPic){
        super(item);
        this.item = item;
        this.posPic = posPic;

        if(posPic.getPos() != null) {
            position = posPic.getPos();
        }else{
            position = new Point2D(Game.WIDTH * r.nextDouble(), Game.HEIGHT * r.nextDouble());
        }
        if(posPic.getPic() != null) {
            picture = posPic.getPic();
        }else{
            picture = new Image(getClass().getResourceAsStream("/Graphics/CItem/1.png"));
        }

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
    public Item getItem(){return item;}
    public PosPicCombo getPosPic(){return posPic;}

    public void reInstate(){
        MainGUIController.getCurrentRoom().getCitems().add(this);
        position = new Point2D(Game.player.getOrX(),Game.player.getOrY());
        Interactible.interactibles.add(this);
        Renderable.renderLayer1.add(this);
    }
    @Override
    public void onInteraction() {
        Game.getInventoryManager().addCItem(this);
        destroy();
        System.out.println("You picked up: " + super.getName());
    }
    @Override
    public void onInVicinity() {
        InteractionHandler.interactibleReadyToInteract = this;
    }
    @Override
    public String getPopUpText() {
        return super.getName() + " " + super.getAmount();
    }

    @Override
    public void onInstancedRender() {
        Renderable.renderLayer1.add(this);
        Interactible.interactibles.add(this);
    }
    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(picture, position.getX(),position.getY());
    }

    public void destroy(){                                  //Removes the item from any static contexts in which a reference is kept
        MainGUIController.getCurrentRoom().getCitems().remove(this);
        Interactible.interactibles.remove(this);
        Renderable.renderLayer1.remove(this);
    }

    @Override
    public String toString(){
        String toReturn = "";
        if(item == null){
            toReturn = "CITEM with no Item";
        }else{
            toReturn = "CITEM: " + item.getName() + " " + item.getAmount();
        }
        return toReturn;
    }
}
