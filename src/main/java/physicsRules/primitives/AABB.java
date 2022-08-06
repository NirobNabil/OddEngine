package physicsRules.primitives;

import org.joml.Vector2f;
import org.lwjgl.system.CallbackI;
import physicsRules.rigidbody.RigidBody2D;

public class AABB {
    //private Vector2f center = new Vector2f();
    private Vector2f size = new Vector2f();
    private RigidBody2D rigidbody = null;

    public AABB() {

    }

    public AABB(Vector2f min, Vector2f max) {
        this.size = new Vector2f(max).sub(min);
        //this.center = new Vector2f(min).add(new Vector2f(size).mul(.5f));
    }

    public Vector2f getMin() {
        return new Vector2f();
    }
}
