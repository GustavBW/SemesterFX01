package worldofzuul;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class PosPicCombo {

    private Image image;
    private Point2D position;

    public PosPicCombo(Image image, Point2D position){
        this.image = image;
        this.position = position;
    }

    public Point2D getPos(){
        return position;
    }
    public Image getPic(){
        return image;
    }

}
