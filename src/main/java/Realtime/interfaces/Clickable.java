package Realtime.interfaces;

import java.util.ArrayList;

public interface Clickable {

    ArrayList<Clickable> clickables = new ArrayList<>();

    int getX();
    int getY();
    int getSize();
}
