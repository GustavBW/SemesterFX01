package Realtime.triggers;

import Realtime.interfaces.Interactible;
import Realtime.interfaces.Renderable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DistanceAutoTrigger extends DistanceTrigger implements Interactible {

    //Yeah so. Basically just a DistanceTrigger BUT with the difference that when you're in range,
    //it automatically activates

    public DistanceAutoTrigger(){
        this(0,0,0);
    }

    public DistanceAutoTrigger(int x, int y, int r){
        super(x,y,r);

        onInstancedInter();
    }

    @Override
    public void onInteraction() {

    }

    @Override
    public void onInVicinity() {
        onInteraction();
    }

    @Override
    public String getPopUpText() {
        return "Whats up my dude!";
    }

}
