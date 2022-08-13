package odd;

import components.CircleRenderer;
import components.RectangleRenderer;
import imguiLayer.Debug;
import org.joml.Vector2f;
import org.joml.Vector4f;
import physics2d.PhysicsSystem2D;
import physics2d.primitives.AABB;
import physics2d.primitives.Circle;
import physics2d.rigidbody.Rigidbody2D;

public class DemoScene extends Scene {

//    PhysicsSystem2D physics = new PhysicsSystem2D(1.0f / 60.0f, new Vector2f(0, -50000));

    public DemoScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));

        addAABBGameObject( this, "groundleft", 0, 500, 20, 1800, Float.MAX_VALUE, true );
        addAABBGameObject(this, "groundbottom", 900, 100, 1920, 20, Float.MAX_VALUE, true );
        addAABBGameObject(this, "groundtop", 900, 1015, 1920, 20, Float.MAX_VALUE, true );
        addAABBGameObject(this, "groundright", 1855, 500, 10, 2000, Float.MAX_VALUE, true );

        addAABBGameObject(this, "groundright", 500, 500, 300, 50, Float.MAX_VALUE, true );
        addAABBGameObject(this, "groundright", 1200, 300, 300, 50, Float.MAX_VALUE, true );
        addAABBGameObject(this, "groundright", 1050, 700, 300, 50, Float.MAX_VALUE, true );

        addCircleGameObject( this, "objectx", 1000, 200, 20, 20, true, true, false);
    }


    private int counter = 0;
    private boolean to_spawn = true;
    @Override
    public void update(float dt) {
//        System.out.println("FPS: " + (1.0f / dt));

//        Debug.print("##", String.valueOf(MouseListener.get().mouseButtonDown(0)));

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