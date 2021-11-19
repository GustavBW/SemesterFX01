package Realtime;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public interface Renderable {

    ArrayList<Renderable> renderLayer0 = new ArrayList<>();
    ArrayList<Renderable> renderLayer1 = new ArrayList<>();
    ArrayList<Renderable> renderLayer2 = new ArrayList<>();
    ArrayList<Renderable> renderLayer3 = new ArrayList<>();
    ArrayList<Renderable> renderLayer4 = new ArrayList<>();

    void onInstancedRender();
    void render(GraphicsContext gc);
}
