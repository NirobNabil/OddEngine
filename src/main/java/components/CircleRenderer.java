package components;

import odd.Camera;
import odd.MouseListener;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class CircleRenderer extends ShapeRenderer {

    private Vector4f color;
    private float radius = 1.0f;
    private Vector2f[] vertices_identity = generateIdentity();
    public Vector2f[] localVertices;

    public CircleRenderer(Vector4f color) {
        this.color = color;
        localVertices = generateIdentity();
        this.radius = 1.0f;
    }

    public CircleRenderer(Vector4f color, Vector2f[] vertices) {
        this.color = color;
        localVertices = vertices;
    }

    @Override
    public void start() {
    }

    @Override
    public void update(float dt) {
        if( this.gameObject != null ) {
            if( this.gameObject.transform.scale.x != this.gameObject.transform.scale.y ) {
                // needs to throw exception here
            }


            this.radius = 1.0f * (this.gameObject.transform.scale.x / 2.0f);
            for( int i=0; i<this.localVertices.length; i++ ) {
                this.localVertices[i].y = this.vertices_identity[i].y * this.gameObject.transform.scale.y;
                this.localVertices[i].x = this.vertices_identity[i].x * this.gameObject.transform.scale.x;
            }
        }
    }


    public Vector2f[] getVertices() {
        return localVertices;
    }

    @Override
    public int[] getElementIndices() {
        return new int[] { 0, 2, 1, 0, 3, 2 };
    }

    public float getRadius() {
        return this.radius;
    }

    public Vector2f getCenter() {
        return new Vector2f(this.radius, this.radius);
    }

    // TODO: Optimize this?
    public void rotate(float d) {
        float deg = d * 0.0174533f;
        for( int i=0; i<localVertices.length; i++ ) {
            localVertices[i].x = (float) (localVertices[i].x*Math.cos(deg) - localVertices[i].y*Math.sin(deg));
            localVertices[i].y = (float) (localVertices[i].x*Math.sin(deg) + localVertices[i].y*Math.cos(deg));
        }
    }

    public Vector4f getColor() {
        return this.color;
    }

    private Vector2f[] generateIdentity() {
        return new Vector2f[]{
            new Vector2f( 0, 0),
            new Vector2f( 1, 0),
            new Vector2f( 1, 1),
            new Vector2f( 0, 1)
        };
    }
}
