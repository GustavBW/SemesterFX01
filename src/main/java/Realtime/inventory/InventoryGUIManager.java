package Realtime.inventory;

import BackEnd.PosPicCombo;
import Realtime.RenderableButton;
import Realtime.RenderableText;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import worldofzuul.Game;

import java.util.ArrayList;
import java.util.HashMap;

public class InventoryGUIManager {

    private InventoryManager inventoryManager;
    private ArrayList<CItemButton> CIBs;
    private ArrayList<RenderableText> RTFBs;
    private HashMap<Integer, ArrayList<RenderableButton>> SUBs;

    private final int mainFrameWidth = 300, mainFrameHeight = 400, buttonSize = 30, buttonPadding = 5;
    private final double mainFramePosX = (Game.WIDTH / 2.0) - (mainFrameWidth / 2.0);
    private final double mainFramePosY = (Game.HEIGHT / 2.0) - (mainFrameHeight / 2.0);

    private CItem inspectedElement;

    public boolean doDisplay = false;

    public InventoryGUIManager(InventoryManager inventoryManager, boolean actuallyTheThing){
        this.inventoryManager = inventoryManager;
        this.CIBs = new ArrayList<>();
        this.RTFBs = new ArrayList<>();
        this.SUBs = new HashMap<>();

        createNew(actuallyTheThing);
    }

    public void createNew(boolean actuallyTheThing){

        int i = 0;

        if(actuallyTheThing) {
            for (CItem c : inventoryManager.getCInventory()) {

                //CIBS
                Point2D CIBposition = new Point2D(mainFramePosX + buttonSize, ((mainFramePosY + (mainFrameHeight * 0.11)) + (i * (buttonSize + buttonPadding))) - (buttonSize / 2.0));   //Disable this when there's some Citems
                CIBs.add(new CItemButton(c, CIBposition, buttonSize, buttonSize, this));

                Point2D RTFBposition = new Point2D(mainFramePosX + buttonSize + (buttonSize * 2), (mainFramePosY + (mainFrameHeight * 0.11)) + (i * (buttonSize + buttonPadding)) + 4);
                RTFBs.add(new RenderableText(c.getName() + " " + c.getAmount(), RTFBposition, 100));

                //SUBS
                ArrayList<RenderableButton> newArrayList = new ArrayList<>();

                Point2D sub1Position = new Point2D(CIBposition.getX() + 150, CIBposition.getY());
                newArrayList.add(new useCItemButton(c,"Use",sub1Position, 40,buttonSize));

                Point2D sub2Position = new Point2D(CIBposition.getX() + 150 + 40 + 5, CIBposition.getY());
                newArrayList.add(new dropCItemButton(c,"Drop", sub2Position,40,buttonSize));

                SUBs.put(c.getId(), newArrayList);

                i++;
            }

        }else{      //This is purely just to be able to test the system without having CItems in the game yet

            System.out.println("InventoryGUIManager is creating new InventoryGUI - iGUIM 68");
            for (int j = 0; j < 10; j++) {

                CItem tempCitem = new CItem(new Item(j,"tempCItem",69),new PosPicCombo(null, new Point2D(1,1)));

                //CIBS
                Point2D CIBposition = new Point2D(mainFramePosX + buttonSize, ((mainFramePosY + (mainFrameHeight * 0.11)) + (j * (buttonSize + buttonPadding))) - (buttonSize / 2.0));   //Disable this when there's some Citems
                CIBs.add(new CItemButton(tempCitem, CIBposition, buttonSize, buttonSize, this));

                Point2D RTFBposition = new Point2D(mainFramePosX + buttonSize + (buttonSize * 2), (mainFramePosY + (mainFrameHeight * 0.11)) + (j * (buttonSize + buttonPadding)) + 4);
                RTFBs.add(new RenderableText("Stuff & Amount", RTFBposition, 100));

                //SUBS
                ArrayList<RenderableButton> newArrayList = new ArrayList<>();

                Point2D sub1Position = new Point2D(CIBposition.getX() + 150, CIBposition.getY());
                newArrayList.add(new useCItemButton(tempCitem,"Use",sub1Position, 40,buttonSize));

                Point2D sub2Position = new Point2D(CIBposition.getX() + 150 + 40 + 5, CIBposition.getY());
                newArrayList.add(new dropCItemButton(tempCitem,"Drop", sub2Position,40,buttonSize));

                SUBs.put(j, newArrayList);
            }
        }

        destroyAll();     //As some of these elements above adds themselves their renderlayers on instantiation, we just make sure to remove them at first.
    }

    public void render(GraphicsContext gc){
        if(doDisplay) {
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
        if(!(CIBs.isEmpty() || CIBs == null)) {
            CIBs.clear();
        }

        if(!(RTFBs.isEmpty() || RTFBs == null)) {
            RTFBs.clear();
        }

        if(!(SUBs.isEmpty() || SUBs == null)) {
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
        System.out.println("InventoryGUIManager destroyed all - iGUIM 153");
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

        System.out.println("InventoryGUIManager reinstated all - iGUIM 171");
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


}
