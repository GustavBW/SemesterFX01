package Realtime;

import java.util.ArrayList;

public interface Tickable {

    public static ArrayList<Tickable> tickables = new ArrayList<Tickable>();

    void onInstancedTick();
    void tick();

}
