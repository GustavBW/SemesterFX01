package worldofzuul;

public class DistanceTrigger implements Interactible{

    private int posX, posY, interRadius;

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
        System.out.println("You're in radius of a distance trigger");
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
}
