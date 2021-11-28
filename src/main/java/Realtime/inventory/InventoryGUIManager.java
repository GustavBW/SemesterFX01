package Realtime.inventory;

import BackEnd.PosPicCombo;
import Realtime.RenderableButton;
import Realtime.RenderableText;
import Realtime.debugging.TestingCItem;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import worldofzuul.Game;

import java.util.ArrayList;
import java.util.HashMap;

public class InventoryGUIManager {

    private final InventoryManager inventoryManager;
    private final ArrayList<CItemButton> CIBs;        //Main buttons for each item in the inventory
    private final ArrayList<RenderableText> RTFBs;    //Labels telling what item it is and how many of these you got
    private final HashMap<Integer, ArrayList<RenderableButton>> SUBs; //Functional buttons that actually do the Use / Drop thing

    //Me checking if updating the inventory GUI actually works as expected.
    private int howManyTimesHaveIDoneThis = 0;

    //Specifications for the inventory look.
    private final int mainFrameWidth = 300, mainFrameHeight = 400, buttonSize = 30, subWidth = 40, buttonPadding = 5;
    private final double mainFramePosX = (Game.WIDTH / 2.0) - (mainFrameWidth / 2.0);
    private final double mainFramePosY = (Game.HEIGHT / 2.0) - (mainFrameHeight / 2.0);

    //The Inspected Element is set by the CItemButton that is pressed. THis is how the iGUIM determines which Sub-buttons to show
    private CItem inspectedElement;

    //These are public as for easy reference. Their function is super basic, but very important
    public boolean doDisplay = false;
    public static boolean GUIisReady = false; //Makes sure not to try and render the GUI whilest a new one is being created.

    public InventoryGUIManager(InventoryManager inventoryManager, boolean actuallyTheThing){
        this.inventoryManager = inventoryManager;
        this.CIBs = new ArrayList<>();
        this.RTFBs = new ArrayList<>();
        this.SUBs = new HashMap<>();

        howManyTimesHaveIDoneThis = 0;
        createNew(actuallyTheThing);
    }

    public void createNew(boolean actuallyTheThing){

        //Wiping all the lists of stuff first, then checking if to show a mockup (if actuallyTheThing == false) or the real functional thing (if actuallyTheThing == true)

        howManyTimesHaveIDoneThis++;

        GUIisReady = false;
        clearAll();

        if(actuallyTheThing) {

            int i = 0;

            for (CItem c : inventoryManager.getCInventory()) {

                //CIBS
                Point2D CIBposition = new Point2D(mainFramePosX + buttonSize, ((mainFramePosY + (mainFrameHeight * 0.11)) + (i * (buttonSize + buttonPadding))) - (buttonSize / 2.0));   //Disable this when there's some Citems
                CIBs.add(new CItemButton(c, CIBposition, buttonSize, buttonSize, this));

                Point2D RTFBposition = new Point2D(CIBposition.getX() + (buttonSize * 1.5), CIBposition.getY() + 4 + (buttonSize / 2.0));
                RTFBs.add(new RenderableText(c.getPopUpText(), RTFBposition, 100));

                //SUBS
                ArrayList<RenderableButton> newArrayList = new ArrayList<>();

                Point2D sub1Position = new Point2D(CIBposition.getX() + 175, CIBposition.getY());
                newArrayList.add(new useCItemButton(c,"Use", sub1Position, subWidth, buttonSize));
                System.out.println("Made SUB at y = " + CIBposition.getY());

                Point2D sub2Position = new Point2D(sub1Position.getX() + (subWidth * 1.15), sub1Position.getY());
                newArrayList.add(new dropCItemButton(c,"Drop", sub2Position,subWidth, buttonSize));

                SUBs.put(c.getId(), newArrayList);

                i++;
            }

        }else{      //This is purely just to be able to test the system without having CItems in the game yet

            for (int j = 0; j < 10; j++) {

                TestingCItem tempCitem = new TestingCItem(new Item(j,"tempCItem",69),new PosPicCombo(null, new Point2D(1,1)));

                //CIBS
                Point2D CIBposition = new Point2D(mainFramePosX + (buttonSize * 0.75), ((mainFramePosY + (mainFrameHeight * 0.11)) + (j * (buttonSize + buttonPadding))) - (buttonSize / 2.0));
                CIBs.add(new CItemButton(tempCitem, CIBposition, buttonSize, buttonSize, this));

                Point2D RTFBposition = new Point2D(CIBposition.getX() + (buttonSize * 1.5), CIBposition.getY() + 4 + (buttonSize / 2.0));
                RTFBs.add(new RenderableText("Stuff & Amount " + howManyTimesHaveIDoneThis, RTFBposition, 100));

                //SUBS
                ArrayList<RenderableButton> newArrayList = new ArrayList<>();

                Point2D sub1Position = new Point2D(CIBposition.getX() + 175, CIBposition.getY());
                newArrayList.add(new useCItemButton(tempCitem,"Use", sub1Position, subWidth, buttonSize));

                Point2D sub2Position = new Point2D(sub1Position.getX() + (subWidth * 1.15), sub1Position.getY());
                newArrayList.add(new dropCItemButton(tempCitem,"Drop", sub2Position,subWidth, buttonSize));

                SUBs.put(j, newArrayList);
            }
        }
        destroyAll();     //As some of these elements above adds themselves their renderlayers on instantiation, we just make sure to remove them at first.

        GUIisReady = true;
    }

    public void render(GraphicsContext gc){
        if(doDisplay && GUIisReady) {
            gc.setFill(new Color(0, 0, 0, 0.3));
            gc.fillRoundRect(mainFramePosX, mainFramePosY, mainFrameWidth, mainFrameHeight, 50, 50);


            for(CItemButton c : CIBs){
                c.render(gc);
            }
            for(RenderableText t : RTFBs){
                t.render(gc);
            }

            if(inspectedElement != null){
                for(RenderableButton rB : SUBs.get(inspectedElement.getId())){
                    rB.render(gc);
                }
            }

        }
    }

    public void clearAll(){
        if(!CIBs.isEmpty() && CIBs != null) {
            CIBs.clear();
        }

        if(!RTFBs.isEmpty() && RTFBs != null) {
            RTFBs.clear();
        }

        if(!SUBs.isEmpty() && SUBs != null) {
            SUBs.clear();
        }
    }
    public void destroyAll(){
        if(!(CIBs.isEmpty() || CIBs == null)) {
            for(CItemButton c : CIBs){
                c.destroy();
            }
        }

        if(!(RTFBs.isEmpty() || RTFBs == null)) {
            for(RenderableText t : RTFBs){
                t.destroy();
            }
        }

        if(!(SUBs.isEmpty() || SUBs == null)) {
            for(ArrayList<RenderableButton> ARB : SUBs.values()){
                for(RenderableButton rB : ARB){
                    rB.destroy();
                }
            }
        }
    }
    public void reInstateAll(){
        if(!(CIBs.isEmpty() || CIBs == null)) {
            for(CItemButton c : CIBs){
                c.onInstancedClick();
            }
        }
            //No RTFB's here as they're not interactible / clickable. They're but renderable.

        if(!(SUBs.isEmpty() || SUBs == null)) {
            for(ArrayList<RenderableButton> ARB : SUBs.values()){
                for(RenderableButton rB : ARB){
                    rB.onInstancedClick();
                }
            }
        }
    }

    public void setDoDisplay(boolean active){
        if(active){
            doDisplay = true;
            reInstateAll();
        }else{
            doDisplay = false;
            inspectedElement = null;
            destroyAll();
        }
    }

    public void setInspectedElement(CItem citem){

        if(inspectedElement != null) {
            for (RenderableButton rB : SUBs.get(inspectedElement.getId())) {
                rB.destroy();
            }
        }

        for(RenderableButton rB : SUBs.get(citem.getId())){
            rB.enableClickable();
        }

        inspectedElement = citem;

    }

    public boolean getDisplayStatus(){return doDisplay;}
    public CItem getInspectedElement(){return inspectedElement;}
    public InventoryManager getInventoryManager(){return inventoryManager;}
}
