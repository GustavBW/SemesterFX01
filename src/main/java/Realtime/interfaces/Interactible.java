package Realtime.interfaces;

import java.util.ArrayList;

public interface Interactible {

    double interactionRadius = 0;

    public static ArrayList<Interactible> interactibles = new ArrayList<>();

    void onInstancedInter();
    void onInteraction();
    void onInVicinity();
    String getPopUpText();
    int getInterRadius();
    int getPosX();
    int getPosY();

}
