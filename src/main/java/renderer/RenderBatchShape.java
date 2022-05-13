package renderer;

import components.ShapeRenderer;
import components.TriangleRenderer;
import odd.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatchShape {
    // Vertex
    // ======
    // Pos                     Color                           Attributes
    // float, float, float     float, float, float, float      Int, Int, Int, Int, Int, Int
    private final int POS_ELEM_COUNT = 3;
    private final int POS_SIZE = POS_ELEM_COUNT * Float.BYTES;
    private final int COLOR_ELEM_COUNT = 6;
    private final int COLOR_SIZE = COLOR_ELEM_COUNT * Float.BYTES;
    private final int ATTR_ELEM_COUNT = 6;
    private final int ATTR_SIZE = ATTR_ELEM_COUNT * Float.BYTES;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE;
    private final int ATTR_OFFSET = COLOR_OFFSET + COLOR_SIZE;
    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + ATTR_SIZE;



    private ShapeRenderer[] shapes;
    private int numShapes;
    private boolean hasRoom;
    private float[] vertices;
    private int[] elementsArray;
    private int elementsArrayIndex = 0;

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatchShape(int maxBatchSize) {
        shader = new Shader("assets/shaders/trigshader.glsl");
        shader.compile();
        this.shapes = new TriangleRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        // 4 vertices quads ----- gameswithgabe comment. ignore
        vertices = new float[maxBatchSize * VERTEX_SIZE];

        this.numShapes = 0;
        this.hasRoom = true;
    }

    public void start() {
        // Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_ELEM_COUNT, GL_FLOAT, false, VERTEX_SIZE, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_ELEM_COUNT, GL_FLOAT, false, VERTEX_SIZE, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, ATTR_ELEM_COUNT, GL_INT, false, ATTR_SIZE, COLOR_OFFSET);
        glEnableVertexAttribArray(2);
    }

    // TODO: throw error if hasRoom == false
    public void addShape(ShapeRenderer shape) {
        // Get index and add renderObject
        int index = this.numShapes;
        this.shapes[index] = shape;
        this.numShapes++;

        // Add properties to local vertices array
//        loadVertexProperties(index);

        int offset = index * VERTEX_SIZE;

        Vector4f color = shape.getColor();

//        loadVertexProperties(index);

        int[] elementIndices = shape.getElementIndices();
        for( int elementIndex : elementIndices ) {
            elementsArray[elementsArrayIndex++] = elementIndex;
        }

        if (numShapes >= this.maxBatchSize) {
            this.hasRoom = false;
        }
    }

    public void render() {

        for( int i=0; i<shapes.length; i++ ) {
            loadVertexProperties(i);
        }

        // For now, we will rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);


        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);


        // TODO: check if 3 needs to be replaced with 6 here
        glDrawElements(GL_TRIANGLES, this.numShapes * 3, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    private void loadVertexProperties(int index) {
        ShapeRenderer shape = this.shapes[index];

        // Find offset within array (4 vertices per sprite)
        int offset = index * VERTEX_SIZE;

        Vector4f color = shape.getColor();

        Vector2f[] shapeVertices = shape.getVertices();
        for( Vector2f vertex : shapeVertices ) {
            vertices[offset] = vertex.x;
            vertices[offset+1] = vertex.y;

            vertices[offset+3] = color.x;
            vertices[offset+4] = color.y;
            vertices[offset+5] = color.z;
            vertices[offset+6] = color.w;

            offset += VERTEX_SIZE;
        }
    }

    private int[] generateIndices() {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[POS_SIZE * maxBatchSize];
        for (int i=0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        // 3, 2, 0, 0, 2, 1        7, 6, 4, 4, 6, 5
        // Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean hasRoom() {
        return this.hasRoom;
    }
}
