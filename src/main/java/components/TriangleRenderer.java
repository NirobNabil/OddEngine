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
                new Vector2f( 10, 0),
                new Vector2f( 10, 10)
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
    }


    public Vector2f[] getVertices() {
        return localVertices;
    }

    @Override
    public int[] getElementIndices() {
        return new int[] { 0, 2, 1 };
    }



    public Vector4f getColor() {
        return this.color;
    }
}
