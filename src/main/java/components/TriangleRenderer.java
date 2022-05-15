//package components;
//
//import odd.Component;
//import org.joml.Vector2f;
//import org.joml.Vector4f;
//
//import java.awt.*;
//
//public class TriangleRenderer extends ShapeRenderer {
//    private Vector4f color;
//    private Vector2f[] localVertices, globalVertices;
//
//
//    public TriangleRenderer(Vector4f color, Vector2f[] vertices) {
//        this.color = color;
//        this.localVertices = vertices;
//        globalVertices = new Vector2f[localVertices.length];
//    }
//
//    @Override
//    public void start() {
//        generateGlobalVertices();
//    }
//
//    @Override
//    public void update(float dt) {
//        generateGlobalVertices();
//    }
//
//    private void generateGlobalVertices() {
//        float x = this.gameObject.transform.position.x;
//        float y = this.gameObject.transform.position.y;
//        for( int i=0; i<localVertices.length; i++ ) {
//            globalVertices[i] = new Vector2f(localVertices[i].x+x, localVertices[i].y+y);
//        }
//    }
//
//    public Vector4f getColor() {
//        return this.color;
//    }
//
//    public Vector2f[] getVertices() {
//        return globalVertices;
//    }
//
//    public int[] getElementIndices() {
//        return new int[] {0, 1, 2};
//    }
//}
