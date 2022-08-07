package physics2d.rigidbody;

import odd.Component;
import org.joml.Vector2f;

public class Rigidbody2D extends Component {
    private Vector2f position;
    private float rotation;

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void update(float dt) {

    }
}
