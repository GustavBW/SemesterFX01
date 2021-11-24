package Realtime.interfaces;

import java.util.ArrayList;

public interface Clickable {

    ArrayList<Clickable> clickables = new ArrayList<>();

    void onInstancedClick();
    int getX();
    int getY();
    int getSize();
}
