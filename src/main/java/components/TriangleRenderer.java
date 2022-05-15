package components;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class TriangleRenderer extends ShapeRenderer {

    private Vector4f color;
    public Vector2f[] localVertices;

    public TriangleRenderer(Vector4f color) {
        this.color = color;
        localVertices = new Vector2f[] {
                new Vector2f( 0, 0),
                new Vector2f( 30, 0),
                new Vector2f( 30, 30)
        };
    }

    public TriangleRenderer(Vector4f color, Vector2f[] vertices) {
        this.color = color;
        localVertices = vertices;
    }

    @Override
    public void start() {
    }

    @Override
    public void update(float dt) {
        rotate(1f);
    }


    public Vector2f[] getVertices() {
        return localVertices;
    }

    @Override
    public int[] getElementIndices() {
        return new int[] { 0, 2, 1 };
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
}
