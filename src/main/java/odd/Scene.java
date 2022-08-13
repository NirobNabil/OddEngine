package odd;

import components.CircleRenderer;
import components.RectangleRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;
import physics2d.PhysicsSystem2D;
import physics2d.primitives.AABB;
import physics2d.primitives.Circle;
import physics2d.rigidbody.Rigidbody2D;
import renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    public String lockedBy = "none";
    protected Renderer renderer = new Renderer();
    PhysicsSystem2D physics = new PhysicsSystem2D(1.0f / 60.0f, new Vector2f(0, -50000));

    // TODO: Change camera to protected?
    public Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene() {

    }

    public void init() {

    }

    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }

    public abstract void update(float dt);

    public Camera camera() {
        return this.camera;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public void addCircleGameObject(Scene scene, String name, float x, float y, float radius, float mass, Boolean isGravity, boolean is_new, boolean should_throw) {
        GameObject go = new GameObject(
                name,
                new Transform(new Vector2f(x, y), new Vector2f(radius*2.0f, radius*2.0f)),
                this
        );

        Rigidbody2D rb = new Rigidbody2D();
        go.addComponent(rb);
        rb.setMass(mass);
        rb.setRawTransform(go.transform);
        go.addComponent(new CircleRenderer(new Vector4f(1, 1, 1, 1)));

        Circle c = new Circle();
        c.setRadius(radius);
        c.setRigidbody(rb);
        rb.setCollider(c);

        physics.addRigidbody(rb, isGravity, is_new);
        float rand = (float)Math.random();
        float rand2 = (float)Math.random();
        physics.throwAt(go, new Vector2f(((int)rand%2 == 1 ? -1 : 1 ) * rand * 100f, ((int)rand2%2 == 1 ? -1 : 1 ) * rand2 * 100f));

        addGameObjectToScene(go);
    }

    public void addAABBGameObject( Scene scene, String name, float x, float y, float scaleX, float scaleY, float mass, Boolean isGravity ) {
        GameObject go = new GameObject(
                name,
                new Transform(new Vector2f(x, y), new Vector2f(scaleX, scaleY)),
                this
        );

        Rigidbody2D rb = new Rigidbody2D();
        go.addComponent(rb);
        rb.setMass(mass);
        rb.setRawTransform(go.transform);

        Vector2f halfSize = new Vector2f(go.transform.scale).mul(0.5f);
        go.addComponent(new RectangleRenderer(new Vector4f(0, 1, 0, 1), halfSize));
        AABB r = new AABB(new Vector2f(go.transform.position).sub(halfSize), new Vector2f(go.transform.position).add(halfSize));
        r.setRigidbody(rb);
        rb.setMass(mass);
        rb.setCollider(r);
        System.out.println(r.getMax().toString());

        physics.addRigidbody(rb, isGravity, false);

        addGameObjectToScene(go);

    }

}
