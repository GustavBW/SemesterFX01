package Realtime;

import worldofzuul.Game;

public class InteractionHandler implements Runnable{

    private Player player;

    public static Interactible interactibleReadyToInteract = null;
    public static boolean isRunning,isAwaiting;

    public InteractionHandler(Player player){
        this.player = player;
    }

    public void calcDistances(){

        int j = 0;
        while(!MainGUIController.isReady){
            awaiting();
        }
        onExitFlagSleep();

        while(Game.isRunning && MainGUIController.isRunning){

            if(Game.onPause){continue;}

            if(!MainGUIController.isReady){
                awaiting();
            }
            isAwaiting = false;

            long timeA = System.nanoTime();

            int pPosX = player.getPosX();
            int pPosY = player.getPosY();

            for(Interactible i : Interactible.interactibles){

                int interRadiusSq = i.getInterRadius() * i.getInterRadius();
                double distXSq = (pPosX - i.getPosX()) * (pPosX - i.getPosX());
                double distYSq = (pPosY - i.getPosY()) * (pPosY - i.getPosY());

                double distanceSquared = distXSq + distYSq;

                if(distanceSquared < interRadiusSq){
                    i.onInVicinity();
                    interactibleReadyToInteract = i;
                }
            }

            long timeB = System.nanoTime();
            MainGUIController.updateLogText(2, timeB - timeA);
        }
    }

    private void awaiting(){
        isAwaiting = true;
    }
    private void onExitFlagSleep(){
        isAwaiting = false;
        System.out.println("InterHandler continued from flag-sleep at SysTimeNS: " + System.nanoTime());
    }

    @Override
    public void run() {
        isRunning = true;
        calcDistances();
    }
}
