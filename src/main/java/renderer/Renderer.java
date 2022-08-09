package renderer;

import components.CircleRenderer;
import components.ShapeRenderer;
import components.TriangleRenderer;
import odd.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 10000;
    private List<RenderBatch> batches;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void add(GameObject go) {
        ShapeRenderer shape = go.getComponent(ShapeRenderer.class);
        if (shape != null) {
            if( shape instanceof CircleRenderer ) {
//                System.out.println("Added Circle");
                addToCircleRendererBatch( shape );
            }else if( shape instanceof TriangleRenderer ) {
                addToTriangleRendererBatch( shape );
            }
        }
    }

    private void addToTriangleRendererBatch (ShapeRenderer shape) {
        boolean added = false;
        for (RenderBatch batch : batches) {
            if ( batch instanceof RenderBatchPolygon && batch.roomsLeftForElementIndices() >= shape.getElementIndices().length ) {
                batch.addShape(shape);
                added = true;
                break;
            }
        }

        if (!added) {
            RenderBatchPolygon newBatch = new RenderBatchPolygon(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addShape(shape);
        }
    }

    private void addToCircleRendererBatch ( ShapeRenderer circle ) {
        boolean added = false;
        for (RenderBatch batch : batches) {
            if ( batch instanceof RenderBatchCircle && batch.roomsLeftForElementIndices() >= circle.getElementIndices().length ) {
                batch.addShape(circle);
                added = true;
                break;
            }
        }

        if (!added) {
            RenderBatchCircle newBatch = new RenderBatchCircle(100);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addShape(circle);
        }
    }

    public void render() {
        for (RenderBatch batch : batches) {
            batch.render();
        }
    }
}
