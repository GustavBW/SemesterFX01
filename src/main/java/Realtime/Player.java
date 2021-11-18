package Realtime;

import Realtime.Renderable;
import Realtime.Tickable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Player implements Renderable, Tickable {

    private int posX,posY;
    private Image image;
    private double velX, velY, drag = 0.99, velFloorVal = 0.05, mvSpeed = 3;

    public Player(int x, int y, Image image){
        this.posX = x;
        this.posY = y;
        this.image = image;
        this.velX = 1;
        this.velY = 1;

        onInstancedRender();
        onInstancedTick();
    }

    @Override
    public void render(GraphicsContext gc) {
        if(image != null){
            gc.drawImage(image,posX,posY);
        }else{
            gc.setFill(Color.WHITE);
            gc.fillRect(posX,posY,100,100);
        }
    }

    @Override
    public void tick() {
        posX += velX;
        posY += velY;

        velX *= drag;
        velY *= drag;

        if(velX < velFloorVal){
            velX = 0;
        }
        if(velY < velFloorVal){
            velY = 0;
        }
    }

    public void changeVelX(double i){velX += i * mvSpeed;}
    public void changeVelY(double i){velY += i * mvSpeed;}

    public double getVelX(){return velX;}
    public double getVelY(){return velY;}
    public int getPosX(){return posX;}
    public int getPosY(){return posY;}

    public void setPosX(int pos){posX = pos;}
    public void setPosY(int pos){posY = pos;}

    @Override
    public void onInstancedRender() {
        Renderable.renderables.add(this);
    }
    @Override
    public void onInstancedTick() {
        Tickable.tickables.add(this);
    }
}
