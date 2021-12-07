package Realtime;

import BackEnd.GraphicsProcessor;
import Realtime.interfaces.Renderable;
import Realtime.interfaces.Tickable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import worldofzuul.Game;

import java.util.ArrayList;

public class Player implements Renderable, Tickable {

    private double posX;
    private double posY;
    private double rotation;
    private double orX;
    private double orY;
    private int imW;
    private int imH;  //orX describes the graphical origin. Meaning where the center of any image or rectangle LOOKS to be.
    private final Image image;
    private final double speed = 3;
    private ArrayList<Integer> questsResolved;
    private boolean upKeyPressed,rightKeyPressed,downKeyPressed,leftKeyPressed, running = false;
    private GraphicsProcessor gp = new GraphicsProcessor();

    public Player(int x, int y, Image image){
        this.posX = x;
        this.posY = y;
        this.image = gp.getPlayerGraphics();
        questsResolved = new ArrayList<>();

        this.imW = (int) image.getWidth();
        this.imH = (int) image.getHeight();

        this.orX = posX - imW;
        this.orY = posY - imH;

        onInstancedRender();
        onInstancedTick();
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(image,posX,posY);
    }

    @Override
    public void tick() {
        double prePosX = posX;
        double prePosY = posY;
        double currentSpeed = speed;

        if (running) currentSpeed += 3;

        if (upKeyPressed) {     //Using a Vector2D is necessary for getting the rotation angle
            posY -= currentSpeed;
        }
        if (rightKeyPressed) {
            posX += currentSpeed;
        }
        if (downKeyPressed) {
            posY += currentSpeed;
        }
        if (leftKeyPressed) {
            posX -= currentSpeed;
        }

        orX = posX + (imW / 2.0);   //Updating Origins
        orY = posY + (imH / 2.0);


        if(orX >= Game.WIDTH || orX <= 0){        //Capping movement at the boarders of the canvas
            posX = prePosX;
        }
        if(orY >= Game.HEIGHT || orY <= 0){
            posY = prePosY;
        }
    }

    public double getPosX(){return posX;}
    public double getPosY(){return posY;}
    public double getOrX(){return orX;}
    public double getOrY(){return orY;}

    public void questResolved(int questId){
        questsResolved.add(questId);
        System.out.println("Resolved Quest " + questId);
    }

    public void setPosX(int pos){posX = pos;}
    public void setPosY(int pos){posY = pos;}

    public boolean isUpKeyPressed() {
        return upKeyPressed;
    }

    public void setUpKeyPressed(boolean upKeyPressed) {
        this.upKeyPressed = upKeyPressed;
    }

    public boolean isRightKeyPressed() {
        return rightKeyPressed;
    }

    public void setRightKeyPressed(boolean rightKeyPressed) {
        this.rightKeyPressed = rightKeyPressed;
    }

    public boolean isDownKeyPressed() {
        return downKeyPressed;
    }

    public void setDownKeyPressed(boolean downKeyPressed) {
        this.downKeyPressed = downKeyPressed;
    }

    public boolean isLeftKeyPressed() {
        return leftKeyPressed;
    }

    public void setLeftKeyPressed(boolean leftKeyPressed) {
        this.leftKeyPressed = leftKeyPressed;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean state) {
        this.running = state;
    }

    @Override
    public void onInstancedRender() {
        Renderable.renderLayer2.add(this);
    }
    @Override
    public void onInstancedTick() {
        Tickable.tickables.add(this);
    }
}