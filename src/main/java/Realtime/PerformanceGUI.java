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
    private final Color color1, color2;

    public PerformanceGUI(){

        fontToUse = Font.font("Times New Roman", FontWeight.BOLD, 16);
        color1 = Color.BLACK;
        color2 = new Color(1,1,1,0.5);

        int yPos = 20;
        int xPos = 10;
        int offset = 30;

        renderFPSText = new AdvancedRendText("R FPS: Inactive ", new Point2D(xPos,yPos), fontToUse, color1, 50);
        yPos += offset;
        interFPSText = new AdvancedRendText("I FPS: Inactive ", new Point2D(xPos,yPos), fontToUse, color1, 50);
        yPos += offset;
        tickFPSText = new AdvancedRendText("T FPS: Inactive ", new Point2D(xPos,yPos), fontToUse, color1, 50);

        isReady = true;
    }

    @Override
    public void render(GraphicsContext gc) {
        if(isReady & doDisplay){

            gc.setFill(color2);
            gc.fillRoundRect(-50,-50,200,150,10,10);

            renderFPSText.render(gc);
            interFPSText.render(gc);
            tickFPSText.render(gc);
        }
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
    @Override
    public void onInstancedRender() {

    }

}
