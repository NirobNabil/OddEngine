package physicsRules.rigidbody;

import odd.Component;
import org.joml.Vector2f;

public abstract class RigidBody2D extends Component {
    private Vector2f position = new Vector2f();
    private float rotation = 0.0f;

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }
}
