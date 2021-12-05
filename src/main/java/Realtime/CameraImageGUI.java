package Realtime;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import worldofzuul.Game;

public class CameraImageGUI {

    private final Color backgroundColor, forgroundColor;
    private final CIGCloseButton closeButton;
    private final Image image;

    //GUI things
    private final double mainPosX = Game.WIDTH * 0.1, mainPosY = Game.HEIGHT * 0.1, mainWidth = mainPosX + Game.WIDTH * 0.7, mainHeight = mainPosY + Game.HEIGHT * 0.7;
    private final double cPosX = mainPosX + Game.WIDTH * 0.5, cPosY = mainPosY + Game.HEIGHT * 0.7;

    public CameraImageGUI(){

        backgroundColor = new Color(1,1,1,0.5);
        forgroundColor = new Color(0,0,0,0.5);

        closeButton = new CIGCloseButton((int) cPosX, (int) cPosY, 100, 40);
        image = null;

    }



}
