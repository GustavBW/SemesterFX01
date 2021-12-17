package Realtime;

import Realtime.debugging.RenderableCircle;
import Realtime.interfaces.Clickable;
import Realtime.interfaces.Interactible;
import Realtime.triggers.DistanceTrigger;
import Realtime.triggers.SquareTrigger;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import worldofzuul.Game;

public class InteractionHandler implements Runnable{

    private Player player;

    public static Interactible interactibleReadyToInteract;
    public static boolean isRunning,isAwaiting = false,awaitBoolean, reInstatitingItem = false;

    private boolean doPrintExitFlagSleepStatus = false;

    public InteractionHandler(Player player){
        this.player = player;
    }

    public void calcDistances(){

        awaitBoolean = true;
        while(!MainGUIController.isReady || MainGUIController.sceneChangeRequested){
            awaiting("InteractionHandler is awaiting MainGuiController");
        }
        onExitFlagSleep();


        while(Game.isRunning && MainGUIController.isRunning){

            if(Game.onPause){continue;}

            awaitBoolean = true;
            while(!MainGUIController.isReady || MainGUIController.sceneChangeRequested){
                awaiting("InteractionHandler is awaiting MainGuiController");
            }
            onExitFlagSleep();
            awaitBoolean = true;

            long timeA = System.nanoTime();

            double pPosX = player.getOrX();        //Using Origin coords here, as it'll give a better result.
            double pPosY = player.getOrY();        //Player.posX & posY would make a scewed distance calculation.

            if(!Interactible.interactibles.isEmpty()) { //An interesting thing happens when you don't check. It simply sends too many updates to MGUIC which messes everything up

                boolean foundSomething = false;

                while(reInstatitingItem){
                    awaiting("InteractionHandler is awaiting boolean reInstatitingItem");
                }
                isAwaiting = false;

                for (Interactible i : Interactible.interactibles) {

                    if(i instanceof SquareTrigger){     //Yeah SquareTriggers don't use math. Just a giant ass if statement.
                        if(((SquareTrigger) i).isInBounds(pPosX,pPosY)){
                            interactibleReadyToInteract = i;
                            i.onInVicinity();
                            foundSomething = true;
                        }
                    }else{
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
                }

                if(!foundSomething){                    //Resets it as the MGUIC listens to whether or not "interactibleReadyToInteract" is null
                    interactibleReadyToInteract = null;
                }
            }

            long timeB = System.nanoTime();

            MainGUIController.logTimeInter = (timeB - timeA);
        }
    }

    public static Clickable findInteractibleOnMouseClick(int mouseX, int mouseY){
        Clickable toReturn = null;

        //Offsetting where you click so it matches what you expect. JavaFX is just wierd
        mouseX += 2;
        mouseY -= 8;

        for(Clickable c : Clickable.clickables){
            int interRadiusSq = c.getInterRadius() * c.getInterRadius();
            double distXSq = (mouseX - c.getX()) * (mouseX - c.getX());
            double distYSq = (mouseY - c.getY()) * (mouseY - c.getY());

            double distanceSq = distXSq + distYSq;

            if (distanceSq < interRadiusSq || c.inBounds(mouseX,mouseY)) {
                toReturn = c;
                break;
            }
        }
        return toReturn;
    }

    private void awaiting(String s){
        isAwaiting = true;
        if(awaitBoolean){
            System.out.println("InteractionHandler was asked to Await. Now awaiting at: " + System.nanoTime() + " amount of interactibles: " + Interactible.interactibles.size());
            System.out.println(s);
            awaitBoolean = !awaitBoolean;
            doPrintExitFlagSleepStatus = true;
        }
        System.out.print("");
    }

    private void onExitFlagSleep(){
        isAwaiting = false;
        if(doPrintExitFlagSleepStatus) {
            System.out.println("InteractionHandler continued from Flag Sleep at SysTimeNs: " + System.nanoTime() + " amount of interactibles: " + Interactible.interactibles.size());
            doPrintExitFlagSleepStatus =  !doPrintExitFlagSleepStatus;
        }
    }
    @Override
    public void run() {
        isRunning = true;
        calcDistances();
    }
}
