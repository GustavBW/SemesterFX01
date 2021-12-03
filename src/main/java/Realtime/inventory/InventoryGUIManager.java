package Realtime.inventory;

import BackEnd.PosPicCombo;
import Realtime.AdvancedRendText;
import Realtime.RenderableButton;
import Realtime.RenderableText;
import Realtime.debugging.TestingCItem;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import worldofzuul.Game;

import java.util.ArrayList;
import java.util.HashMap;

public class InventoryGUIManager {

    private final InventoryManager inventoryManager;
    private ArrayList<CItemButton> CIBs;        //Main buttons for each item in the inventory
    private ArrayList<RenderableText> RTFBs;    //Labels telling what item it is and how many of these you got
    private HashMap<Integer, ArrayList<RenderableButton>> SUBs; //Functional buttons that actually do the Use / Drop thing

    //Specifications for the inventory look.
    private final int mainFrameWidth = 300, mainFrameHeight = 450, buttonSize = 30, subWidth = 40, buttonPadding = 5;
    private final double mainFramePosX = (Game.WIDTH / 2.0) - (mainFrameWidth / 2.0);
    private final double mainFramePosY = (Game.HEIGHT / 2.0) - (mainFrameHeight / 2.0);
    private final AdvancedRendText titleText;
    private final Font fontMainTitle;
    private final Color mainTitleColor;

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

        mainTitleColor = new Color(209 / 255.0,153 / 255.0,0,1);
        fontMainTitle = Font.font( "Impact", 26);
        titleText = new AdvancedRendText("Inventory", new Point2D(mainFramePosX + 100, mainFramePosY - 10), fontMainTitle, mainTitleColor, 100);

        createNew(actuallyTheThing);
    }

    public void createNew(boolean actuallyTheThing){

        //Wiping all the lists of stuff first, then checking if to show a mockup (if actuallyTheThing == false) or the real functional thing (if actuallyTheThing == true)

        GUIisReady = false;

        destroyAll();

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
                newArrayList.add(new useCItemButton(c, "Use", sub1Position, subWidth, buttonSize));

                Point2D sub2Position = new Point2D(sub1Position.getX() + (subWidth * 1.15), sub1Position.getY());
                newArrayList.add(new dropCItemButton(c, "Drop", sub2Position, subWidth, buttonSize));

                SUBs.put(c.getId(), newArrayList);

                i++;
            }
        }

        destroyAll();     //As some of these elements above adds themselves their renderlayers on instantiation, we just make sure to remove them at first.

        GUIisReady = true;
        doDisplay = false;
    }

    public void render(GraphicsContext gc){
        if(doDisplay && GUIisReady) {

            gc.setFill(new Color(1,1,1,0.5));
            gc.fillRoundRect(mainFramePosX - 5, mainFramePosY - 5, mainFrameWidth + 10, mainFrameHeight + 10, 50, 50);

            gc.setFill(new Color(0, 0, 0, 0.5));
            gc.fillRoundRect(mainFramePosX, mainFramePosY, mainFrameWidth, mainFrameHeight, 50, 50);


            for(CItemButton c : CIBs){
                c.render(gc);
            }
            for(RenderableText t : RTFBs){
                t.render(gc);
            }

            if(inspectedElement != null && SUBs.get(inspectedElement.getId()) != null){
                for(RenderableButton rB : SUBs.get(inspectedElement.getId())){
                    rB.render(gc);
                }
            }

            titleText.render(gc);

        }
    }

    public void clearAll(){

        //Screw using clear() this is much more consistent

        CIBs = new ArrayList<>();
        RTFBs = new ArrayList<>();
        SUBs = new HashMap<>();

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
        if(!(RTFBs.isEmpty() || RTFBs == null)){
            for(RenderableText t : RTFBs){
                t.onInstancedRender();
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

        if(inspectedElement != null && SUBs.get(inspectedElement.getId()) != null) {
            for (RenderableButton rB : SUBs.get(inspectedElement.getId())) {
                rB.destroy();
            }
        }

        if(citem != null && SUBs.get(citem.getId()) != null) {
            for (RenderableButton rB : SUBs.get(citem.getId())) {
                rB.enableClickable();
            }
            inspectedElement = citem;
        }
        inspectedElement = citem;
    }

    public boolean getDisplayStatus(){return doDisplay;}
    public CItem getInspectedElement(){return inspectedElement;}
    public InventoryManager getInventoryManager(){return inventoryManager;}
}
