package odd;

import org.joml.Vector2f;

public class Transform {

    public Vector2f position;
    public Vector2f scale;

    public Vector2f rotation;

    public Transform() {
        init(new Vector2f(), new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position) {
        init(position, new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale) {
        init(position, scale, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale, Vector2f rotation) {
        init(position, scale, rotation);
    }

    public void init(Vector2f position, Vector2f scale, Vector2f rotation) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
    }
}
