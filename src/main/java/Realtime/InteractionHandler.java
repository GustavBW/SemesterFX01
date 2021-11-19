package Realtime;

import worldofzuul.Game;

public class InteractionHandler implements Runnable{

    private Player player;

    public static Interactible interactibleReadyToInteract = null;
    public static boolean isRunning,isAwaiting = false,awaitBoolean;

    public InteractionHandler(Player player){
        this.player = player;
    }

    public void calcDistances(){

        awaitBoolean = true;
        while(!MainGUIController.isReady){
            awaiting();
        }
        try{Thread.sleep(500);
        }catch (Exception e){e.printStackTrace();}
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
        if(awaitBoolean){
            System.out.println("InteractionHandler was asked to Await. Now awaiting at: " + System.nanoTime());
            awaitBoolean = !awaitBoolean;
        }
        System.out.println(" ");
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
