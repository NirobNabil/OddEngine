package components;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class RectangleRenderer extends ShapeRenderer {

    private Vector4f color;
    public Vector2f[] localVertices;

    public RectangleRenderer(Vector4f color) {
        this.color = color;
        localVertices = generateIdentity();
    }

    public RectangleRenderer(Vector4f color, Vector2f[] vertices) {
        this.color = color;
        localVertices = vertices;
    }

    public RectangleRenderer(Vector4f color, Vector2f scale) {
        this.color = color;
        this.localVertices = generateIdentity();
        for( int i=0; i<localVertices.length; i++ ) {
            localVertices[i].mul(scale);
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void update(float dt) {

    }


    public Vector2f[] getVertices() {
        return localVertices;
    }

    @Override
    public int[] getElementIndices() {
        return new int[] { 0, 1, 2, 2, 3, 0 };
    }

    // TODO: Optimize this?
    public void rotate(float d) {
        float deg = (float)Math.toRadians(d);
        for( int i=0; i<localVertices.length; i++ ) {
            localVertices[i].x = (float) (localVertices[i].x*Math.cos(deg) - localVertices[i].y*Math.sin(deg));
            localVertices[i].y = (float) (localVertices[i].x*Math.sin(deg) + localVertices[i].y*Math.cos(deg));
        }
    }

    public Vector4f getColor() {
        return this.color;
    }

    private Vector2f[] generateIdentity() {
        return new Vector2f[] {
                new Vector2f( -0.5f, -0.5f),
                new Vector2f( 0.5f, -0.5f),
                new Vector2f( 0.5f, 0.5f),
                new Vector2f( -0.5f, 0.5f)
        };
    }
}
