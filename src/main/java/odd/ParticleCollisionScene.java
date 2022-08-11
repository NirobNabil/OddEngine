package odd;

import components.CircleRenderer;
import components.RectangleRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;
import physics2d.PhysicsSystem2D;
import physics2d.primitives.AABB;
import physics2d.primitives.Circle;
import physics2d.rigidbody.Rigidbody2D;

public class ParticleCollisionScene extends Scene {

    PhysicsSystem2D physics = new PhysicsSystem2D(1.0f / 60.0f, new Vector2f(0, -50000));
    Transform obj1, obj2;
    Rigidbody2D rb1, rb2;

    public ParticleCollisionScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));

        int xOffset = 10;
        int yOffset = 10;

        // starts laying out objects from bottom-left
        int objects_in_row = 50, number_of_rows = 50;
        float object_size = 7f;
        float padding = 10f;
        Vector2f starting_pos = new Vector2f(0, 0);

        for (int i = 0; i < number_of_rows; i++) {
            for (int ix = 0; ix < objects_in_row; ix++) {
                float x = starting_pos.x + (object_size + padding) * ix + xOffset;
                float y = starting_pos.y + (object_size + padding) * i + yOffset;
                addCircleGameObject(i+"."+ix, x,y,object_size/2.0f, (i+1)*(ix+1)*800, false);
            }
        }

        addAABBGameObject("groundleft", 0, 500, 20, 2000, Float.MAX_VALUE, true );
        addAABBGameObject("groundbottom", 900, 0, 1920, 20, Float.MAX_VALUE, true );
        addAABBGameObject("groundtop", 900, 1015, 1920, 20, Float.MAX_VALUE, true );
        addAABBGameObject("groundright", 1865, 500, 10, 2000, Float.MAX_VALUE, true );

    }


    private void addCircleGameObject( String name, float x, float y, float radius, float mass, Boolean isGravity ) {
        GameObject go = new GameObject(
                name,
                new Transform(new Vector2f(x, y), new Vector2f(radius*2.0f, radius*2.0f)),
                this
        );

        Rigidbody2D rb = new Rigidbody2D();
        go.addComponent(rb);
        rb.setMass(mass);
        rb.setRawTransform(go.transform);
        go.addComponent(new CircleRenderer(new Vector4f(0, 1, 0, 1)));

        Circle c = new Circle();
        c.setRadius(radius);
        c.setRigidbody(rb);
        rb.setCollider(c);

        physics.addRigidbody(rb, isGravity);
        float rand = (float)Math.random();
        float rand2 = (float)Math.random();
        physics.throwAt(go, new Vector2f(((int)rand%2 == 1 ? -1 : 1 ) * rand *100f, (rand2%2 == 1 ? -1 : 1 ) * rand2 *100f));

        this.addGameObjectToScene(go);
    }

    private void addAABBGameObject( String name, float x, float y, float scaleX, float scaleY, float mass, Boolean isGravity ) {
        GameObject go = new GameObject(
                name,
                new Transform(new Vector2f(x, y), new Vector2f(scaleX, scaleY)),
                this
        );

        Rigidbody2D rb = new Rigidbody2D();
        go.addComponent(rb);
        rb.setMass(mass);
        rb.setRawTransform(go.transform);

        go.addComponent(new RectangleRenderer(new Vector4f(0, 1, 0, 1), new Vector2f(scaleX, scaleY)));
        Vector2f halfSize = new Vector2f(go.transform.scale).mul(0.5f);
        AABB r = new AABB(new Vector2f(go.transform.position).sub(halfSize), new Vector2f(go.transform.position).add(halfSize));
        r.setRigidbody(rb);
        rb.setMass(mass);
        rb.setCollider(r);

        physics.addRigidbody(rb, isGravity);

        this.addGameObjectToScene(go);

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
