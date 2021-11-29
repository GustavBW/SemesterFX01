package Realtime;

import Realtime.interfaces.Renderable;
import Realtime.interfaces.Tickable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import worldofzuul.Game;

import java.util.Vector;

public class Player implements Renderable, Tickable {

    private double posX;
    private double posY;
    private double orX;
    private double orY;
    private int imW;
    private int imH;  //orX describes the graphical origin. Meaning where the center of any image or rectangle LOOKS to be.
    private double rotationRadian = 0;
    private final Image image;
    private Vector2D velocity;
    private final double drag = 0.99, velFloorVal = 0.01, mvSpeed = 5, toDegree = 57.2957795;
    private boolean upKeyPressed,rightKeyPressed,downKeyPressed,leftKeyPressed = false;

    public Player(int x, int y, Image image){
        this.posX = x;
        this.posY = y;
        this.image = null;
        velocity = new Vector2D(1,1);


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
        gc.rotate(rotationRadian * toDegree);

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

        double prePosX = posX;     //Saving previous location
        double prePosY = posY;

        if (upKeyPressed) {     //Using a Vector2D is necessary for getting the rotation angle
            velocity.addY(-1);
        }
        if (rightKeyPressed) {
            velocity.addX(1);
        }
        if (downKeyPressed) {
            velocity.addY(1);
        }
        if (leftKeyPressed) {
            velocity.addX(-1);
        }

        velocity.normalize();   //Normalizing the vector as that makes EVERYTHING easier
        velocity.multiply(mvSpeed * Game.interpolation); //Well. Yeeting off into space was fun. But interpolation is very much needed

        orX = posX - imW;   //Updating Origins
        orY = posY - imH;

        //Checking if the update in position would place the player out of bounds
        if((orX + velocity.getX() < Game.WIDTH && orX + velocity.getX() > 0) && (orY + velocity.getY() < Game.HEIGHT && orY + velocity.getY() > 0)) {
            posX += velocity.getX();
            posY += velocity.getY();
        }



        rotationRadian = Math.asin((velocity.getY() + 1 / ( Math.sqrt( velocity.getSquareX() + velocity.getSquareY() ) + 1) - 1)); //Adding 1 in both top and bottom to avoid dividing by zero, then subtracting that again
        System.out.println("rotation angle is: " + rotationRadian + " velX is: " + velocity.getX() + " velY is: " + velocity.getY() + " magnitude is: " + velocity.magnitude() + " Game.interpolation is: " + Game.interpolation);


    }

    public Vector2D getVelocity(){return velocity;}
    public double getPosX(){return posX;}
    public double getPosY(){return posY;}
    public double getOrX(){return orX;}
    public double getOrY(){return orY;}

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
