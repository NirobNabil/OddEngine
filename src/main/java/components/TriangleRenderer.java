package components;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class TriangleRenderer extends ShapeRenderer {

    private Vector4f color;
    public Vector2f[] localVertices, globalVertices;

    public TriangleRenderer(Vector4f color) {
        this.color = color;
        localVertices = new Vector2f[] {
                new Vector2f( 0, 0),
                new Vector2f( 10, 0),
                new Vector2f( 10, 10)
        };
        globalVertices = new Vector2f[localVertices.length];
    }

    public TriangleRenderer(Vector4f color, Vector2f[] vertices) {
        this.color = color;
        localVertices = vertices;
        globalVertices = new Vector2f[localVertices.length];
    }

    @Override
    public void start() {
    }

    @Override
    public void update(float dt) {
    }

    private void generateGlobalVertices() {
        float x = this.gameObject.transform.position.x;
        float y = this.gameObject.transform.position.y;
        for( int i=0; i<localVertices.length; i++ ) {
            globalVertices[i] = new Vector2f(localVertices[i].x+x, localVertices[i].y+y);
        }
    }

    public Vector2f[] getVertices() {
        generateGlobalVertices();
        return globalVertices;
    }

    @Override
    public int[] getElementIndices() {
        return new int[] { 0, 2, 1 };
    }



    public Vector4f getColor() {
        return this.color;
    }
}
