package worldofzuul;

public class InteractionHandler implements Runnable{

    private Player player;

    public InteractionHandler(Player player){
        this.player = player;
    }

    public void calcDistances(){
        while(Game.isRunning){
            int pPosX = player.getPosX();
            int pPosY = player.getPosY();

            for(Interactible i : Interactible.interactibles){



            }
        }
    }

    @Override
    public void run() {
        calcDistances();
    }
}
