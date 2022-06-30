package odd;

import components.RectangleRenderer;
import components.TriangleRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {


    public LevelEditorScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));

        int xOffset = 0;
        int yOffset = 0;

        // starts laying out objects from bottom-left
        int objects_in_row = 3;
        float object_size = 100f;
        float padding = 5f;
        Vector2f starting_pos = new Vector2f(0, 0);

        for (int i = 0; i < objects_in_row; i++) {
            for (int ix = 0; ix < objects_in_row; ix++) {
                float x = starting_pos.x + (object_size + padding) * ix;
                float y = starting_pos.y + (object_size + padding) * i;
                GameObject go = new GameObject("Obj" + x + "" + y, new Transform(new Vector2f(x, y), new Vector2f(object_size, object_size)), this);
                go.addComponent(new TriangleRenderer(new Vector4f(1, 1, 1, 1)));
                this.addGameObjectToScene(go);
            }
        }
    }


    @Override
    public void update(float dt) {
//        // System.out.println("FPS: " + (1.0f / dt));

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}
