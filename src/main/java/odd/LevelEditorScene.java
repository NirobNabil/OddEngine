package odd;

import components.CircleRenderer;
import components.RectangleRenderer;
import components.TriangleRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import physics2d.PhysicsSystem2D;
import physics2d.primitives.Circle;
import physics2d.rigidbody.Rigidbody2D;
import renderer.DebugDraw;

public class LevelEditorScene extends Scene {

    PhysicsSystem2D physics = new PhysicsSystem2D(1.0f / 60.0f, new Vector2f(0, -1000));
    Transform obj1, obj2;
    Rigidbody2D rb1, rb2;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));

//        obj1 = new Transform(new Vector2f(100, 500));
//        obj2 = new Transform(new Vector2f(200, 500));
//        rb1 = new Rigidbody2D();
//        rb2 = new Rigidbody2D();
//        rb1.setRawTransform(obj1);
//        rb2.setRawTransform(obj2);
//        rb1.setMass(100.0f);
//        rb2.setMass(200.0f);
//
//        physics.addRigidbody(rb1);
//        physics.addRigidbody(rb2);

        int xOffset = 400;
        int yOffset = 500;

        // starts laying out objects from bottom-left
        int objects_in_row = 3, number_of_rows = 3;
        float object_size = 100f;
        float padding = 20f;
        Vector2f starting_pos = new Vector2f(0, 0);

        for (int i = 0; i < number_of_rows; i++) {
            for (int ix = 0; ix < objects_in_row; ix++) {
                float x = starting_pos.x + (object_size + padding) * ix;
                float y = starting_pos.y + (object_size + padding) * i;
                GameObject go = new GameObject("Obj" + x + "" + y, new Transform(new Vector2f(xOffset + x, yOffset + y), new Vector2f(object_size, object_size)), this);
                go.addComponent(new CircleRenderer(new Vector4f(0, 1, 0, 1)));

                Rigidbody2D rb = new Rigidbody2D();
                rb.setMass(300*(objects_in_row-ix+1)*(number_of_rows-i+1));
                rb.setRawTransform(go.transform);
                Circle c = new Circle();
                c.setRadius(1.0f);
                c.setRigidbody(rb);
                physics.addRigidbody(rb, true);

                go.addComponent(rb);

                //                GameObject go2 = new GameObject("Obj" + x + object_size*5 + "" + y, new Transform(new Vector2f(x+object_size*5, y), new Vector2f(object_size, object_size)), this);
//                go2.addComponent(new TriangleRenderer(new Vector4f(1, 0, 0, 1)));

                this.addGameObjectToScene(go);
//                this.addGameObjectToScene(go2);
            }
        }
    }


    @Override
    public void update(float dt) {
//        // System.out.println("FPS: " + (1.0f / dt));

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

//        DebugDraw.addBox2D(obj1.position, new Vector2f(32, 32), 0.0f, new Vector3f(1, 0, 0));
//        DebugDraw.addBox2D(obj2.position, new Vector2f(32, 32), 0.0f, new Vector3f(0.2f, 0.8f, 0.1f));
//        this.gameObjects.get(0).transform.position =
        physics.update(dt);

        this.renderer.render();
    }
}
