package Realtime;

import javafx.geometry.Point2D;

public class SquareTrigger implements Interactible{

    private boolean inBounds;
    private final int x,y,sizeX,sizeY,orX,orY;

    public SquareTrigger(int x, int y, int size){
        this(x,y,size,size);
    }

    public SquareTrigger(int x, int y, int sizeX, int sizeY){
        this.x = x;
        this.y = y;
        this.sizeX = Math.abs(sizeX);   //No negative sizes
        this.sizeY = Math.abs(sizeY);
        this.orX = x - sizeX / 2;
        this.orY = y - sizeY / 2;

        onInstancedInter();
    }

    @Override
    public void onInstancedInter() {
        Interactible.interactibles.add(this);
    }

    @Override
    public void onInteraction() {

    }

    @Override
    public void onInVicinity() {

    }

    public boolean isInBounds(int pOrX,int pOrY){
        inBounds = (pOrX < orX + sizeX / 2 && pOrX > orX - sizeX / 2) && (pOrY < orY + sizeY / 2 && pOrY > orY - sizeY / 2);

        return inBounds;
    }
    public Point2D getInvApproachVector(int pOrX, int pOrY){
        Point2D apprVec = new Point2D(pOrX - orX,pOrY - orY);
        return apprVec;
    }
    public Point2D getSizes(){
        return new Point2D(sizeX,sizeY);
    }

    @Override
    public int getInterRadius() {
        return (sizeX + sizeY) / 2;
    }

    @Override
    public int getPosX() {
        return x;
    }

    @Override
    public int getPosY() {
        return y;
    }
}
