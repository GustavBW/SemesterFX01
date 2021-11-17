package worldofzuul;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public interface Renderable {

    public static ArrayList<Renderable> renderables = new ArrayList<>();

    void onInstancedRender();
    void render(GraphicsContext gc);
}
