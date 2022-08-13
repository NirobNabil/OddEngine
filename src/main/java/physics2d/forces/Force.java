package physics2d.forces;

import org.joml.Vector2f;
import physics2d.rigidbody.Rigidbody2D;

public class Force implements ForceGenerator {

    private Vector2f gravity;

    public Force(Vector2f force) {
        this.gravity = new Vector2f(force);
    }

    @Override
    public void updateForce(Rigidbody2D body, float dt) {
        body.addForce(new Vector2f(gravity));
    }
}