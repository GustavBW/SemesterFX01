package Realtime;

import Realtime.interfaces.Interactible;
import Realtime.MainGUIController;
import worldofzuul.Game;

public class InteractionHandler implements Runnable{

    private Player player;

    public static Interactible interactibleReadyToInteract = null;
    public static boolean isRunning,isAwaiting = false,awaitBoolean;

    public InteractionHandler(Player player){
        this.player = player;
    }

    public void calcDistances(){
        System.out.println("InteractionHandler was started");
        awaitBoolean = true;
        while(!MainGUIController.isReady){
            awaiting();
        }


        while(Game.isRunning && MainGUIController.isRunning){

            if(Game.onPause){continue;}

            if(!MainGUIController.isReady){
                awaiting();
            }
            onExitFlagSleep();

            long timeA = System.nanoTime();

            int pPosX = player.getOrX();        //Using Origin coords here, as it'll give a better result.
            int pPosY = player.getOrY();        //Player.posX & posY would make a scewed distance calculation.

            if(!Interactible.interactibles.isEmpty()) {

                boolean foundSomething = false;

                for (Interactible i : Interactible.interactibles) {
                    if(i instanceof DistanceTrigger) {
                        int interRadiusSq = i.getInterRadius() * i.getInterRadius();
                        double distXSq = (pPosX - i.getPosX()) * (pPosX - i.getPosX());
                        double distYSq = (pPosY - i.getPosY()) * (pPosY - i.getPosY());

                        double distanceSquared = distXSq + distYSq;

                        if (distanceSquared < interRadiusSq) {
                            interactibleReadyToInteract = i;
                            i.onInVicinity();
                            foundSomething = true;
                        }
                    }
                    if(i instanceof SquareTrigger){     //Yeah SquareTriggers don't use math. Just a giant ass if statement.
                        if(((SquareTrigger) i).isInBounds(pPosX,pPosY)){
                            interactibleReadyToInteract = i;
                            i.onInVicinity();
                            foundSomething = true;
                        }
                    }
                }

                if(!foundSomething){                    //Resets it as the MGUIC listens to whether or not "interactibleReadyToInteract" is null
                    interactibleReadyToInteract = null;
                }

                long timeB = System.nanoTime();

                MainGUIController.logTimeTick = (timeB - timeA);
            }
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
    }
    @Override
    public void run() {
        isRunning = true;
        calcDistances();
    }
}
