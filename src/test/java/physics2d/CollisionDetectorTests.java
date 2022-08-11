package physics2d;

import odd.Transform;
import org.joml.Vector2f;
import org.junit.Test;
import physics2d.primitives.AABB;
import physics2d.primitives.Circle;
import physics2d.rigidbody.IntersectionDetector2D;
import physics2d.primitives.Line2D;
import physics2d.rigidbody.Rigidbody2D;
import sun.jvm.hotspot.types.basic.BasicOopField;
import sun.util.resources.cldr.ext.TimeZoneNames_uz_Arab;

import static junit.framework.TestCase.assertTrue;

public class CollisionDetectorTests {
    private final float EPSILON = 0.000001f;

    @Test
    public void pointOnLine2DShouldReturnTrueTest() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(0, 0);

        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
    }

    @Test
    public void pointOnLine2DShouldReturnTrueTestTwo() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(12, 4);

        assertTrue(IntersectionDetector2D.pointOnLine(point, line));
    }

    @Test
    public void pointOnVerticalLineShouldReturnTrue() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(0, 10));
        Vector2f point = new Vector2f(0, 5);

        boolean result = IntersectionDetector2D.pointOnLine(point, line);
        assertTrue(result);
    }

    @Test
    public void circleInAABBShouldReturnTrue() {
        Rigidbody2D r = new Rigidbody2D( );
        r.setRawTransform( new Transform( new Vector2f(200, 200) ));
        Circle c = new Circle();
        c.setRigidbody(r);

        r = new Rigidbody2D();
        r.setRawTransform( new Transform(new Vector2f(0,0).add(new Vector2f(200,200))) );
        AABB a = new AABB( new Vector2f(100,100) );
        a.setRigidbody(r);

        Boolean result = IntersectionDetector2D.circleAndAABB( c, a );

        assertTrue(result);
    }
}
