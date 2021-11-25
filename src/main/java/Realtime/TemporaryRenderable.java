package Realtime;

import java.util.ArrayList;

public abstract class TemporaryRenderable {

    private long timeOfDeath;

    public static ArrayList<TemporaryRenderable> tempRends = new ArrayList<>();

    public TemporaryRenderable(long timeOfDeath){}

    public abstract void destroy();

}
