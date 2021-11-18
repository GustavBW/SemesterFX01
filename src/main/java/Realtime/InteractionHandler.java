package Realtime;

import worldofzuul.Game;

public class InteractionHandler implements Runnable{

    private Player player;

    public InteractionHandler(Player player){
        this.player = player;
    }

    public void calcDistances(){
        while(Game.isRunning && MainGUIController.isRunning){
            int pPosX = player.getPosX();
            int pPosY = player.getPosY();

            for(Interactible i : Interactible.interactibles){

                int interRadiusSq = i.getInterRadius() * i.getInterRadius();
                double distXSq = (pPosX - i.getPosX()) * (pPosX - i.getPosX());
                double distYSq = (pPosY - i.getPosY()) * (pPosY - i.getPosY());

                double distanceSquared = distXSq + distYSq;

                if(distanceSquared < interRadiusSq){
                    i.onInteraction();
                }
            }
        }
    }

    @Override
    public void run() {
        calcDistances();
    }
}
