package components;

import jade.Component;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color;
    public Vector2f[] localVertices, globalVertices;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
//        this.localVertices = vertices;
        localVertices = new Vector2f[] {
                new Vector2f( 0, 0),
                new Vector2f( 10, 0),
                new Vector2f( 10, 10)
        };
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



    public Vector4f getColor() {
        return this.color;
    }
}
