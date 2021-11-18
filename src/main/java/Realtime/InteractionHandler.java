package Realtime;

import worldofzuul.Game;

public class InteractionHandler implements Runnable{

    private Player player;

    public InteractionHandler(Player player){
        this.player = player;
    }

    public void calcDistances(){

        int j = 0;
        while(!MainGUIController.isReady){
            System.out.println("IM AWAITING " + j);
            j++;
        }     //Halts the thread until the application class is ready
        System.out.println("InterHandler continued from flag-sleep");

        while(Game.isRunning && MainGUIController.isRunning){
            
            long timeA = System.nanoTime();

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
            long timeB = System.nanoTime();

            MainGUIController.updateLogText(2, timeB - timeA);

        }
    }

    @Override
    public void run() {
        calcDistances();
    }
}
