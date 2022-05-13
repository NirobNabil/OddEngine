package renderer;

import components.ShapeRenderer;
import components.SpriteRenderer;
import odd.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatchShape> batches;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void add(GameObject go) {
        ShapeRenderer shape = go.getComponent(ShapeRenderer.class);
        if (shape != null) {
            add(shape);
        }
    }

    private void add(ShapeRenderer shape) {
        boolean added = false;
        for (RenderBatchShape batch : batches) {
            if (batch.hasRoom()) {
                batch.addShape(shape);
                added = true;
                break;
            }
        }

        if (!added) {
            RenderBatchShape newBatch = new RenderBatchShape(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addShape(shape);
        }
    }

    public void render() {
        for (RenderBatchShape batch : batches) {
            batch.render();
        }
    }
}
