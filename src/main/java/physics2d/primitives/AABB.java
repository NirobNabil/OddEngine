package physics2d.primitives;

import org.joml.Vector2f;
import physics2d.rigidbody.Rigidbody2D;

public class AABB extends Collider2D {

    public String name = "";
    public Vector2f size = new Vector2f();
    public Vector2f halfSize = new Vector2f();
    public Rigidbody2D rigidbody = null;

    public AABB( Vector2f halfSize ) {
        this.halfSize = halfSize;
    }

    public AABB( Vector2f min, Vector2f max ){
        this.size = new Vector2f(max).sub(min);
        this.halfSize = new Vector2f(this.size).mul(0.5f);
    }


    public Vector2f getMin() {
        return new Vector2f(this.rigidbody.getPosition()).sub(this.halfSize);
    }

    public Vector2f getMax() {
        return new Vector2f(this.rigidbody.getPosition()).add(this.halfSize);
    }

    public Vector2f getHalfSize() { return this.halfSize; }

    public Vector2f getCenter() {
        return new Vector2f(this.rigidbody.getPosition());
    }


    public void setRigidbody(Rigidbody2D rb) {
        this.rigidbody = rb;
//        this.name = rb.gameObject.name;
    }

}
