package Realtime;

import Realtime.interfaces.Interactible;

public class DistanceTrigger implements Interactible {

    private int posX, posY, interRadius;

    public DistanceTrigger(){
        this(0,0,0);
    }

    public DistanceTrigger(int x, int y, int r){
        this.posX = x;
        this.posY = y;
        this.interRadius = r;

        onInstancedInter();
    }


    @Override
    public void onInstancedInter() {
        Interactible.interactibles.add(this);
    }

    @Override
    public void onInteraction() {
        System.out.println("You interacted with a distance trigger");
    }

    @Override
    public void onInVicinity() {System.out.println("You're in radius of a distance trigger");}

    @Override
    public String getPopUpText() {
        return " ";
    }

    @Override
    public int getInterRadius() {
        return interRadius;
    }

    @Override
    public int getPosX() {
        return posX;
    }
    @Override
    public int getPosY() {
        return posX;
    }

    public void setPosX(int i){posX = i;}
    public void setPosY(int i){posY = i;}
    public void setInterRadius(int i){interRadius = i;}
}
