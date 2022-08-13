package components;

import odd.Camera;
import odd.MouseListener;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class TriangleRenderer extends ShapeRenderer {

    private Vector4f color;

    private Vector2f[] vertices_identity = new Vector2f[] {
            new Vector2f( 0, 0),
            new Vector2f( 1, 0),
            new Vector2f( 1, 1)
    };
    public Vector2f[] localVertices;

    public TriangleRenderer(Vector4f color) {
        this.color = color;
        localVertices = new Vector2f[] {
                new Vector2f( 0, 0),
                new Vector2f( 100, 0),
                new Vector2f( 100, 100)
        };;
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
        if( this.gameObject != null ) {
            for( int i=0; i<this.localVertices.length; i++ ) {
                this.localVertices[i].y = this.vertices_identity[i].y * this.gameObject.transform.scale.y;
                this.localVertices[i].x = this.vertices_identity[i].x * this.gameObject.transform.scale.x;
            }
        }
//        rotate(1f);
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
