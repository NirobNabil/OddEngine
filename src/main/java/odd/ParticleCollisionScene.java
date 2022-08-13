package odd;

import com.sun.org.apache.xpath.internal.operations.Bool;
import components.CircleRenderer;
import components.RectangleRenderer;
import imguiLayer.Debug;
import org.joml.Vector2f;
import org.joml.Vector4f;
import physics2d.PhysicsSystem2D;
import physics2d.primitives.AABB;
import physics2d.primitives.Circle;
import physics2d.rigidbody.Rigidbody2D;

public class ParticleCollisionScene extends Scene {

//    PhysicsSystem2D physics = new PhysicsSystem2D(1.0f / 60.0f, new Vector2f(0, -50000));
    Transform obj1, obj2;
    Rigidbody2D rb1, rb2;

    public ParticleCollisionScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));

        int xOffset = 100;
        int yOffset = 100;

        // starts laying out objects from bottom-left
        int objects_in_row = 2, number_of_rows = 2;
        float object_size = 20f;
        float padding = 10f;
        Vector2f starting_pos = new Vector2f(0, 0);

        for (int i = 0; i < number_of_rows; i++) {
            for (int ix = 0; ix < objects_in_row; ix++) {
                float x = starting_pos.x + (object_size + padding) * ix + xOffset;
                float y = starting_pos.y + (object_size + padding) * i + yOffset;
                addCircleGameObject(this, i+"."+ix, x,y,object_size/2.0f, (i+1)*(ix+1)*800, false, false, true);
            }
        }

//        addCircleGameObject("ggg", 00,0,50, 10000000, false, false);

//        addAABBGameObject("groundleft", 100, 500, 20, 2000, Float.MAX_VALUE, true );
//        addAABBGameObject("groundbottom", 900, 100, 1920, 20, Float.MAX_VALUE, true );
//        addAABBGameObject("groundtop", 900, 900, 1920, 20, Float.MAX_VALUE, true );
//        addAABBGameObject("groundright", 1765, 500, 10, 2000, Float.MAX_VALUE, true );

        addAABBGameObject( this, "ground_bottom", 935, 0, 1870, 10, Float.MAX_VALUE, false );
        addAABBGameObject( this, "ground_top", 935, 1020, 1870, 10, Float.MAX_VALUE, false );
        addAABBGameObject( this, "ground_left", 0, 500, 20, 1080, Float.MAX_VALUE, false );
        addAABBGameObject( this, "ground_right", 1870, 500, 20, 1080, Float.MAX_VALUE, false );
//        addAABBGameObject( "lol1", 10, 50, 1, 100, Float.MAX_VALUE, false );


    }


    private int counter = 0;
    private boolean to_spawn = true;
    @Override
    public void update(float dt) {
//        // System.out.println("FPS: " + (1.0f / dt));

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