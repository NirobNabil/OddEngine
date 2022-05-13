package components;

import odd.Component;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.awt.*;

public class TriangleRenderer extends ShapeRenderer {
    private Vector4f color;
    private Vector2f[] localVertices, globalVertices;


    public TriangleRenderer(Vector4f color, Vector2f[] vertices) {
        this.color = color;
        this.localVertices = vertices;
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {
        float x = this.gameObject.transform.position.x;
        float y = this.gameObject.transform.position.y;
        for( Vector2f vertex : localVertices ) {
            globalVertices[globalVertices.length] = new Vector2f(vertex.x+x, vertex.y+y);
        }
    }

    public Vector4f getColor() {
        return this.color;
    }

    public Vector2f[] getVertices() {
        return globalVertices;
    }

    public int[] getElementIndices() {
        return new int[] {0, 1, 2};
    }
}
