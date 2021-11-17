package worldofzuul;

import java.util.ArrayList;

public interface Interactible {

    public static ArrayList<Interactible> interactibles = new ArrayList<>();

    void onInstancedInter();
    void onInteraction();

}
