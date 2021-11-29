package Realtime;

import Realtime.interfaces.Renderable;
import Realtime.interfaces.Tickable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import worldofzuul.Game;

import java.util.Vector;

public class Player implements Renderable, Tickable {

    private int posX;
    private int posY;
    private int orX;
    private int orY;
    private int imW;
    private int imH;  //orX describes the graphical origin. Meaning where the center of any image or rectangle LOOKS to be.
    private double rotationAngle = 0;
    private final Image image;
    private Vector2D velocity;
    private final double drag = 0.99, velFloorVal = 0.01, mvSpeed = 5;
    private boolean upKeyPressed,rightKeyPressed,downKeyPressed,leftKeyPressed = false;

    public Player(int x, int y, Image image){
        this.posX = x;
        this.posY = y;
        this.image = image;
        velocity = new Vector2D(0,0);


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

        //Gotta save the GraphicsContext as the rotation needs to be removed from the GC using restore()
        gc.save();
        gc.rotate(rotationAngle);

        if(image != null){
            gc.drawImage(image,orX,orY);
        }else{
            gc.setFill(Color.GREEN);
            gc.fillRect(orX,orY,100,100);
        }

        gc.restore();
    }

    @Override
    public void tick() {
        int prePosX = posX;     //Saving previous location
        int prePosY = posY;

        if (upKeyPressed) {     //Using a Vector2D is necessary for getting the rotation angle
            velocity.add(0,-1);
        }
        if (rightKeyPressed) {
            velocity.add(1,0);
        }
        if (downKeyPressed) {
            velocity.add(0,1);
        }
        if (leftKeyPressed) {
            velocity.add(-1, 0);
        }

        velocity.multiply(mvSpeed);

        posX += velocity.getX();
        posY += velocity.getY();

        orX = posX - imW;   //Updating Origins
        orY = posY - imH;

        velocity.normalize();
        rotationAngle = Math.asin(velocity.getY() + 1 / ( Math.sqrt( velocity.getSquareX() + velocity.getSquareY() ) + 1)); //Adding 1 in both top and bottom to avoid dividing by zero
        System.out.println("rotation angle is: " + rotationAngle + " velX is: " + velocity.getX() + " velY is: " + velocity.getY());

        if(posX >= Game.WIDTH || posX <= 0){        //Capping movement at the boarders of the canvas
            posX = prePosX;
        }
        if(posY >= Game.HEIGHT || posY <= 0){
            posY = prePosY;
        }
    }

    public Vector2D getVelocity(){return velocity;}
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
