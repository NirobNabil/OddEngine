package renderer;

import components.CircleRenderer;
import components.ShapeRenderer;
import odd.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.awt.*;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public abstract class RenderBatch {

    private boolean elementIndicesToUpdate = true;

    public abstract void start();

    public abstract void addShape(ShapeRenderer renderer);

    public abstract void render();

    public abstract int roomsLeftForElementIndices();
}
