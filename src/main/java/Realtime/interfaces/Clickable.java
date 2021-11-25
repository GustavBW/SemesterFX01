package Realtime.interfaces;

import javafx.geometry.Point2D;

import java.util.ArrayList;

public interface Clickable {

    ArrayList<Clickable> clickables = new ArrayList<>();

    void onInstancedClick();
    boolean inBounds(int x, int y);
    int getX();
    int getY();
    Point2D getSizes();
    void onInteraction();

    int getInterRadius();
}
