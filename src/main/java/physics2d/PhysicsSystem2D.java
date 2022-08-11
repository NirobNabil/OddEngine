package physics2d;

import components.RectangleRenderer;
import imguiLayer.Debug;
import odd.GameObject;
import odd.Transform;
import org.joml.Vector2f;
import physics2d.forces.ForceRegistry;
import physics2d.forces.Gravity2D;
import physics2d.primitives.AABB;
import physics2d.primitives.Circle;
import physics2d.primitives.Collider2D;
import physics2d.rigidbody.CollisionManifold;
import physics2d.rigidbody.Collisions;
import physics2d.rigidbody.IntersectionDetector2D;
import physics2d.rigidbody.Rigidbody2D;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PhysicsSystem2D {
    private ForceRegistry forceRegistry;
    private Gravity2D gravity;

    private List<Rigidbody2D> rigidbodies;
    private List<Rigidbody2D> bodies1;
    private List<Rigidbody2D> bodies2;
    private List<CollisionManifold> collisions;

    private int cols = 2, rows = 3;
    private Vector2f windowSize = new Vector2f( 2100, 1100 );
    private Vector2f cellSize = new Vector2f( windowSize ).mul(new Vector2f( 1.0f/(float)cols, 1.0f/(float)rows ));
    private AABB[] gridBoxes = new AABB[cols*rows];
    private List<Integer>[] gridIndexes = new List[rows*cols];

    private float fixedUpdate;
    private int impulseIterations = 4;

    public PhysicsSystem2D(float fixedUpdateDt, Vector2f gravity) {
        this.forceRegistry = new ForceRegistry();
        this.gravity = new Gravity2D(gravity);

        this.rigidbodies = new ArrayList<>();
        this.bodies1 = new ArrayList<>();
        this.bodies2 = new ArrayList<>();
        this.collisions = new ArrayList<>();

        for( int i = 0; i < rows; i++ ) {
            for( int ix = 0; ix < cols; ix++ ) {
                Rigidbody2D r = new Rigidbody2D();
                r.setRawTransform( new Transform( new Vector2f( i, ix ).mul( new Vector2f(cellSize) ).add(new Vector2f(cellSize).mul(0.5f) ) ) );
                AABB a = new AABB( new Vector2f(cellSize).mul( 0.5f ) );
                r.name = new Vector2f( i, ix ).mul( new Vector2f(cellSize) ).add(new Vector2f(cellSize).mul(0.5f) ).toString();
                a.setRigidbody( r );

                System.out.println( i +","+ix+ " " + windowSize.mul(0.5f) + " | " + a.getMin().toString() + " - " + a.getMax().toString() );
                gridBoxes[i*cols + ix] = a;
                gridIndexes[i*cols + ix] = new ArrayList<Integer>();
            }
        }

        this.fixedUpdate = fixedUpdateDt;
    }

    public void update(float dt) {
        fixedUpdate();
    }

    public void fixedUpdate() {
        bodies1.clear();
        bodies2.clear();
        collisions.clear();

        int size = rigidbodies.size();

        for( int i = 0; i<rows*cols; i++ ) {
            gridIndexes[i] = new ArrayList<Integer>();
        }

        Rigidbody2D r;
        Collider2D c;
        for( int i=0; i<size; i++ ) {
            r = rigidbodies.get(i);
            c = r.getCollider();
            for( int ix = 0; ix < rows*cols; ix++ ) {
//                System.out.println( gridBoxes[ix].getMin().toString() );
                if( c instanceof AABB ) {
                    if( IntersectionDetector2D.AABBAndAABB( (AABB) c, gridBoxes[ix] ) ) {
                        gridIndexes[ix].add(i);
                    }
                }  else if( c instanceof Circle ) {
                    if( IntersectionDetector2D.circleAndAABB( (Circle) c, gridBoxes[ix] ) ) {
                        gridIndexes[ix].add(i);
                    }
                }
            }
        }


        for( int i = 0; i < rows*cols; i++ ) {
            if( gridIndexes[i] == null ) continue;
            String out = "";
            for( int ix : gridIndexes[i] ) {
                if( rigidbodies.get(ix).gameObject != null ) out += rigidbodies.get(ix).gameObject.name + ", ";
                else if( rigidbodies.get(ix).name != "" ) out += rigidbodies.get(ix).name + ", ";
                else out += ix + ", ";
            }
            Debug.print( gridBoxes[i].rigidbody.name, out );
        }

        // Find any collisions
//        for( int x = 0; x < rows*cols; x++ ) {
//            for (int i=0; i < gridIndexes[x].size(); i++) {
//                for (int j=i; j < gridIndexes[x].size(); j++) {
            for (int i=0; i < size; i++) {
                for (int j=i; j < size; j++) {

                    if (i == j) continue;

                    CollisionManifold result = new CollisionManifold();
//                    Rigidbody2D r1 = rigidbodies.get(gridIndexes[x].get(i));
//                    Rigidbody2D r2 = rigidbodies.get(gridIndexes[x].get(j));
                    Rigidbody2D r1 = rigidbodies.get(i);
                    Rigidbody2D r2 = rigidbodies.get(j);
                    Collider2D c1 = r1.getCollider();
                    Collider2D c2 = r2.getCollider();

                    if( c1 instanceof Circle && c2 instanceof Circle ) {
                        if( !IntersectionDetector2D.circleAndCircle((Circle) c1,(Circle) c2) ) continue;
                    } else if( c1 instanceof Circle ) {
                        if( !IntersectionDetector2D.circleAndAABB((Circle) c1, (AABB) c2) ) continue;
                    } else if( c2 instanceof Circle ) {
                        if( !IntersectionDetector2D.circleAndAABB((Circle) c2, (AABB) c1) ) continue;
                    }



                    if (c1 != null && c2 != null && !(r1.hasInfiniteMass() && r2.hasInfiniteMass())) {
                        result = Collisions.findCollisionFeatures(c1, c2);
                    }

                    if (result != null && result.isColliding()) {
                        bodies1.add(r1);
                        bodies2.add(r2);
                        collisions.add(result);
                    }
                }
            }
//        }


        // Update the forces
        forceRegistry.updateForces(fixedUpdate);

        // Resolve collisions via iterative impulse resolution
        // iterate a certain amount of times to get an approximate solution
        for (int k=0; k < impulseIterations; k++) {
            for (int i=0; i < collisions.size(); i++) {
                int jSize = collisions.get(i).getContactPoints().size();
                for (int j=0; j < jSize; j++) {
                    Rigidbody2D r1 = bodies1.get(i);
                    Rigidbody2D r2 = bodies2.get(i);
                    applyImpulse(r1, r2, collisions.get(i));
                }
            }
        }

        // Update the velocities of all rigidbodies
        for (int i=0; i < rigidbodies.size(); i++) {
            rigidbodies.get(i).physicsUpdate(fixedUpdate);
        }
//
//        // Apply linear projection
    }

    public void throwAt( GameObject go, Vector2f velocity ) {
        Rigidbody2D rb = go.getComponent(Rigidbody2D.class);
        rb.setLinearVelocity(velocity);
    }

    private void applyImpulse(Rigidbody2D a, Rigidbody2D b, CollisionManifold m) {
        // Linear velocity
        float invMass1 = a.getInverseMass();
        float invMass2 = b.getInverseMass();
        float invMassSum = invMass1 + invMass2;
        if (invMassSum == 0f) {
            return;
        }

        // Relative velocity
        Vector2f relativeVel = new Vector2f(b.getVelocity()).sub(a.getVelocity());
        Vector2f relativeNormal = new Vector2f(m.getNormal()).normalize();
//        Debug.print("w", a.gameObject.name + "-" + b.gameObject.name + " = " + relativeVel.dot(relativeNormal));
        // Moving away from each other? Do nothing
        if (relativeVel.dot(relativeNormal) > 0.0f) {
            return;
        }



        float e = Math.min(a.getCor(), b.getCor());
        float numerator = (-(1.0f + e) * relativeVel.dot(relativeNormal));
        float j = numerator / invMassSum;
        if (m.getContactPoints().size() > 0 && j != 0.0f) {
            j /= (float)m.getContactPoints().size();
        }

        Vector2f impulse = new Vector2f(relativeNormal).mul(j);
        a.setVelocity(
                new Vector2f(a.getVelocity()).add(new Vector2f(impulse).mul(invMass1).mul(-1f)));
        b.setVelocity(
                new Vector2f(b.getVelocity()).add(new Vector2f(impulse).mul(invMass2).mul(1f)));
    }

    public void addRigidbody(Rigidbody2D body, boolean addGravity) {
        this.rigidbodies.add(body);
        if (addGravity)
            this.forceRegistry.add(body, gravity);
    }
}
