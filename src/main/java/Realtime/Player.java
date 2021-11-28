package Realtime;

import Realtime.interfaces.Renderable;
import Realtime.interfaces.Tickable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import worldofzuul.Game;

public class Player implements Renderable, Tickable {

    private int posX;
    private int posY;
    private int orX;
    private int orY;
    private int imW;
    private int imH;  //orX describes the graphical origin. Meaning where the center of any image or rectangle LOOKS to be.
    private final Image image;
    private double velX, velY;
    private final double drag = 0.99, velFloorVal = 0.01, mvSpeed = 5;
    private boolean upKeyPressed,rightKeyPressed,downKeyPressed,leftKeyPressed = false;

    public Player(int x, int y, Image image){
        this.posX = x;
        this.posY = y;
        this.image = image;
        this.velX = 1;
        this.velY = 1;


        if(this.image != null) {                //This if condition wont be necessary when images are working
            this.imW = (int) image.getWidth();
            this.imH = (int) image.getHeight();
        }else{
            this.imW = 50;
            this.imH = 50;
        }

        this.orX = posX - imW;
        this.orY = posY - imH;

        onInstancedRender();
        onInstancedTick();
    }

    @Override
    public void render(GraphicsContext gc) {
        if(image != null){
            gc.drawImage(image,orX,orY);
        }else{
            gc.setFill(Color.GREEN);
            gc.fillRect(orX,orY,100,100);
        }
    }

    @Override
    public void tick() {
        int prePosX = posX;     //Saving previous location
        int prePosY = posY;

        if (upKeyPressed) {
            posY -= 5;
        }
        if (rightKeyPressed) {
            posX += 5;
        }
        if (downKeyPressed) {
            posY += 5;
        }
        if (leftKeyPressed) {
            posX -= 5;
        }

        orX = posX - imW;   //Updating Origins
        orY = posY - imH;

        if(posX >= Game.WIDTH || posX <= 0){        //Capping movement at the boarders of the canvas
            posX = prePosX;
        }
        if(posY >= Game.HEIGHT || posY <= 0){
            posY = prePosY;
        }
    }

    public void changeVelX(double i){velX += (i * mvSpeed) * (1 / ((velX * velX) + 1));}
    public void changeVelY(double i){velY += (i * mvSpeed) * (1 / ((velX * velX) + 1));}

    public double getVelX(){return velX;}
    public double getVelY(){return velY;}
    public int getPosX(){return posX;}
    public int getPosY(){return posY;}
    public int getOrX(){return orX;}
    public int getOrY(){return orY;}

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

    @Override
    public void onInstancedRender() {
        Renderable.renderLayer2.add(this);
    }
    @Override
    public void onInstancedTick() {
        Tickable.tickables.add(this);
    }
}
