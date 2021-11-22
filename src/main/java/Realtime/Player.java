package Realtime;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import worldofzuul.Game;

public class Player implements Renderable, Tickable {

    private int posX,posY,orX,orY,imW, imH;;  //orX describes the graphical origin. Meaning where the center of any image or rectangle LOOKS to be.
    private final Image image;
    private double velX, velY;
    private final double drag = 0.99, velFloorVal = 0.05, mvSpeed = 3;

    public Player(int x, int y, Image image){
        this.posX = x;
        this.posY = y;
        this.image = image;
        this.velX = 1;
        this.velY = 1;


        if(this.image != null) {                //This if condition wont be necessary when images are working
            this.imW = (int) image.getWidth();
            this.imH = (int) image.getHeight();
            this.orX = imW + posX;
            this.orY = imH + posY;
        }else{
            this.imW = 50;
            this.imH = 50;
            this.orX = posX + imW;
            this.orY = posY + imH;
        }

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
        posX += velX;
        posY += velY;
        orX = imW + posX;
        orY = imH + posY;

        velX *= drag;
        velY *= drag;

        if(posX >= Game.WIDTH || posX <= 0){
            velX = 0;
        }
        if(posX >= Game.HEIGHT || posY <= 0){
            velY = 0;
        }
        if(velX < velFloorVal){
            velX = 0;
        }
        if(velY < velFloorVal){
            velY = 0;
        }
    }

    public void changeVelX(double i){velX += (i * mvSpeed) * (1 / ((velX * velX) + 1));}
    public void changeVelY(double i){velY += (i * mvSpeed) * (1 / ((velX * velX) + 1));}

    public double getVelX(){return velX;}
    public double getVelY(){return velY;}
    public int getPosX(){return posX;}
    public int getPosY(){return posY;}

    public void setPosX(int pos){posX = pos;}
    public void setPosY(int pos){posY = pos;}

    @Override
    public void onInstancedRender() {
        Renderable.renderLayer2.add(this);
    }
    @Override
    public void onInstancedTick() {
        Tickable.tickables.add(this);
    }
}
