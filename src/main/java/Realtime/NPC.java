package Realtime;

import Realtime.interfaces.Interactible;
import Realtime.interfaces.Renderable;
import Realtime.triggers.DistanceTrigger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class NPC extends DistanceTrigger implements Renderable, Interactible {

    private int interactionRadius = 80;
    private String text;
    private Image image;


    public NPC(double x, double y, String text, Image image) {
        super(x, y, 80);
        this.text = text;
        this.image = image;
    }

    @Override
    public void onInstancedRender() {

    }

    @Override
    public void render(GraphicsContext gc) {

    }
}
