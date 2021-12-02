package Realtime.inventory;

import BackEnd.ContentEngine;
import BackEnd.PosPicCombo;
import Realtime.InteractionHandler;
import Realtime.MainGUIController;
import Realtime.interfaces.Clickable;
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
    private int interRadius = 80;
    private boolean showText = false;
    private String text;

    private static Random r = new Random();

    public CItem(Item item, PosPicCombo posPic){
        super(item);
        this.item = item;
        this.posPic = posPic;

        if(posPic.getPos() != null) {
            position = posPic.getPos();
        }else{
            position = new Point2D(100 + (Game.WIDTH * r.nextDouble() - 200), 100 + (Game.HEIGHT * r.nextDouble() - 200));
        }
        if(posPic.getPic() != null) {
            picture = posPic.getPic();
        }else{
            picture = ContentEngine.getDefaultCItemImage();
        }

        this.text = item.getName() + " " + item.getAmount();

    }
    @Override
    public void onInstancedInter(){
        Interactible.interactibles.add(this);
        System.out.println("Added " + this + " to interactibles");
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
        InteractionHandler.reInstatitingItem = true;

        while(!InteractionHandler.isAwaiting){
            System.out.print("");
        }

        MainGUIController.getCurrentRoom().getCitems().add(this);
        position = new Point2D(Game.player.getOrX(),Game.player.getOrY());

        Interactible.interactibles.add(this);
        Renderable.renderLayer1.add(this);

        InteractionHandler.reInstatitingItem = false;
    }
    @Override
    public void onInteraction() {
        boolean destroy = Game.getInventoryManager().addCItem(this);

        if(destroy){destroy();}

        System.out.println("You picked up: " + super.getName());
    }
    @Override
    public void onInVicinity() {
        InteractionHandler.interactibleReadyToInteract = this;
        showText = true;
    }
    @Override
    public String getPopUpText() {
        return super.getName() + " " + super.getAmount();
    }


    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(picture, position.getX(),position.getY());

        if(showText){
            gc.fillText(text, position.getX(), position.getY());
            showText = false;
        }
    }

    public boolean destroy(){                                  //Removes the item from any static contexts in which a reference is kept
        boolean removedFromInteractibles = false;
        boolean removedFromRenderLayer = false;

        InteractionHandler.reInstatitingItem = true;

        while(!InteractionHandler.isAwaiting){
            System.out.print("");
        }

        removedFromInteractibles = Interactible.interactibles.remove(this);
        removedFromRenderLayer = Renderable.renderLayer1.remove(this);

        InteractionHandler.reInstatitingItem = false;

        return removedFromInteractibles && removedFromRenderLayer;
    }

    @Override
    public String toString(){
        String toReturn = "";
        if(item == null){
            toReturn = "CITEM with no Item";
        }else{
            toReturn = "CITEM id: " + item.getId() + " | " + item.getName() + " " + item.getAmount();
        }
        return toReturn;
    }

    @Override
    public void onInstancedRender() {
        Renderable.renderLayer1.add(this);
    }
}
