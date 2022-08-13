package physics2d.rigidbody;

import imguiLayer.Debug;
import org.joml.Vector2f;
import physics2d.primitives.AABB;
import physics2d.primitives.Circle;
import physics2d.primitives.Collider2D;

public class Collisions {
    public static CollisionManifold findCollisionFeatures(Collider2D c1, Collider2D c2) {
        if (c1 instanceof Circle && c2 instanceof Circle) {
            return findCollisionFeatures((Circle)c1, (Circle)c2);
        } else if( c1 instanceof Circle && c2 instanceof AABB ){
            return findCollisionFeatures((Circle)c1, (AABB)c2 );
        } else if( c1 instanceof AABB && c2 instanceof Circle ){
            return findCollisionFeatures((Circle)c2, (AABB)c1 );
        } else {
            assert false : "Unknown collider '" + c1.getClass() + "' vs '" + c2.getClass() + "'";
        }

        return null;
    }

    public static CollisionManifold findCollisionFeatures(Circle a, Circle b) {
        CollisionManifold result = new CollisionManifold();
        float sumRadii = a.getRadius() + b.getRadius();

//        Debug.print(a.name, a.getCenter().toString() + " " + String.valueOf(a.getRadius()));
//        Debug.print(b.name, b.getCenter().toString() + " " + String.valueOf(b.getRadius()));

        Vector2f distance = new Vector2f(b.getCenter()).sub(a.getCenter());
//        Debug.print(a.name + "-" + b.name, String.valueOf(distance.lengthSquared()));
        if (distance.lengthSquared() - (sumRadii * sumRadii) > 0) {
            return result;
        }

        // Multiply by 0.5 because we want to separate each circle the same
        // amount. Consider updating to factor in the momentum and velocity
        float depth = Math.abs(distance.length() - sumRadii) * 0.5f;
        Vector2f normal = new Vector2f(distance);
        normal.normalize();
        float distanceToPoint = a.getRadius() - depth;
        Vector2f contactPoint = new Vector2f(a.getCenter()).add(
                new Vector2f(normal).mul(distanceToPoint));

        result = new CollisionManifold(normal, depth);
        result.addContactPoint(contactPoint);
        return result;
    }

    public static CollisionManifold findCollisionFeatures( Circle c, AABB r ) {
        CollisionManifold result = new CollisionManifold();
        int circleSide = 0; // 1 - top, 2 - right, 3 - bottom, 4 - left
//        Debug.print("aabb coll", "came");
        if( c.getCenter().x < r.getCenter().x + r.getHalfSize().x && c.getCenter().x > r.getCenter().x - r.getHalfSize().x ) {
            Vector2f distance = new Vector2f(c.getCenter()).sub( new Vector2f(c.getCenter().x, r.getCenter().y) ).mul(-1);
            float sumRadii = c.getRadius() + r.getHalfSize().y;
            if( distance.lengthSquared() - ( sumRadii * sumRadii ) > 0 ) {
                return result;
            }
            float depth = Math.abs(distance.length() - sumRadii) * 0.5f;
            Vector2f normal = new Vector2f(distance);
            normal.normalize();
            float distanceToPoint = r.getHalfSize().y - depth;
            Vector2f contactPoint = new Vector2f(r.getCenter().y).add(
                    new Vector2f(normal).mul(distanceToPoint));
            result = new CollisionManifold(normal, depth);
            result.addContactPoint(contactPoint);
        } else if( c.getCenter().y < r.getCenter().y + r.getHalfSize().y && c.getCenter().y > r.getCenter().y - r.getHalfSize().y ) {
            Vector2f distance = new Vector2f(c.getCenter()).sub( new Vector2f(r.getCenter().x, c.getCenter().y) ).mul(-1);
            float sumRadii = c.getRadius() + r.getHalfSize().x;
            if( distance.lengthSquared() - ( sumRadii * sumRadii ) > 0 ) {
                return result;
            }
            float depth = Math.abs(distance.length() - sumRadii) * 0.5f;
            Vector2f normal = new Vector2f(distance);
            normal.normalize();
            float distanceToPoint = r.getHalfSize().x - depth;
            Vector2f contactPoint = new Vector2f(r.getCenter().x).add(
                    new Vector2f(normal).mul(distanceToPoint));
            result = new CollisionManifold(normal, depth);
            result.addContactPoint(contactPoint);
        }

        return result;

    }

}
