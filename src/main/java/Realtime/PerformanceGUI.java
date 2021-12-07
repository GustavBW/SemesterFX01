package Realtime;

import Realtime.interfaces.Renderable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PerformanceGUI implements Renderable {

    private AdvancedRendText renderFPSText,interFPSText,tickFPSText;
    private boolean isReady = false, doDisplay = false;
    private final Font fontToUse;
    private final Color color;

    public PerformanceGUI(){

        fontToUse = Font.font("Times New Roman", FontWeight.BOLD, 20);
        color = Color.BLACK;

        renderFPSText = new AdvancedRendText("R FPS: Inactive ", new Point2D(10,10), fontToUse, color, 50);
        interFPSText = new AdvancedRendText("I FPS: Inactive ", new Point2D(10,40), fontToUse, color, 50);
        tickFPSText = new AdvancedRendText("T FPS: Inactive ", new Point2D(10,70), fontToUse, color, 50);

    }

    @Override
    public void render(GraphicsContext gc) {
        if(isReady & doDisplay){
            renderFPSText.render(gc);
            interFPSText.render(gc);
            tickFPSText.render(gc);
        }
    }

    @Override
    public void onInstancedRender() {

    }

    public void toggleDisplayStatus(boolean status){
        doDisplay = status;
    }
    public boolean getDisplayStatus(){return doDisplay;}

    public void update(long r, long i, long t){
        renderFPSText.setText("R/s : " + r );
        interFPSText.setText("I/s : " + i );
        tickFPSText.setText("T/s : " + t );
    }

}
