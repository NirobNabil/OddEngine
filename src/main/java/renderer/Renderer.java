package renderer;

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
        TriangleRenderer spr = go.getComponent(TriangleRenderer.class);
        if (spr != null) {
            add(spr);
        }
    }

    private void add(ShapeRenderer shape) {
        boolean added = false;
        for (RenderBatch batch : batches) {
            if (batch.roomsLeftForElementIndices() >= shape.getElementIndices().length ) {
//                // System.out.println(batch.elementIndicesArrayIndex);
                batch.addShape(shape);
                added = true;
                break;
            }
        }

        if (!added) {
            // System.out.println("came1");
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addShape(shape);
            // System.out.println("came2");
        }
    }

    public void render() {
        for (RenderBatch batch : batches) {
            batch.render();
        }
    }
}
