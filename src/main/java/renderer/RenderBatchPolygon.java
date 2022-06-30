package renderer;

import components.ShapeRenderer;
import odd.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatchPolygon extends RenderBatch {
    // Vertex
    // ======
    // Pos                   Color
    // float, float, float   float, float, float, float
    private final int POS_SIZE = 3;
    private final int COLOR_SIZE = 4;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private ShapeRenderer[] shapes;
    private int[] verticesOffset, elementIndicesOffset;
    public int numShapes = 0, elementIndicesArrayIndex = 0, verticesArrayIndex = 0;
    private float[] vertices;
    private int[] elementIndices;

    private int vaoID, vboID, maxElementIndices;
    private Shader shader;

    public RenderBatchPolygon(int maxTriangleCount) {
        shader = new Shader("assets/shaders/default.glsl");
        shader.compile();
        this.shapes = new ShapeRenderer[maxTriangleCount];
        this.maxElementIndices = maxTriangleCount * 3;

        // TODO: this line can be optimized with variable sized array for vertices
        vertices = new float[maxElementIndices * VERTEX_SIZE];
        elementIndices = new int[maxElementIndices];

        // TODO: This can be optimized too because the array size needs to be equal to maxShapes
        verticesOffset = new int[maxElementIndices];
        elementIndicesOffset = new int[maxElementIndices];
    }

    public void start() {
        // Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    public void addShape(ShapeRenderer spr) {
        // Get index and add renderObject
        int index = this.numShapes;
        try {
            this.shapes[index] = spr;
        }catch(Exception e){
            System.out.println("in addshape");
        }
        this.numShapes++;

        // Add properties to local vertices array
        loadVertexProperties(index);
        loadElementIndices(index);
    }

    private boolean elementIndicesToUpdate = true;
    public void render() {

        if ( elementIndicesToUpdate ) {
            int eboID = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementIndices, GL_STATIC_DRAW);
            elementIndicesToUpdate = false;
        }

        // For now, we will rebuffer all data every frame
        for( int i=0; i<numShapes; i++ ){
            updateVertexProperties(i);
        }
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.elementIndicesArrayIndex, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    private void loadVertexProperties(int index) {
        ShapeRenderer shape = this.shapes[index];

        Vector4f color = shape.getColor();

        Vector2f[] shapeVertices = shape.getVertices();
        this.verticesOffset[index] = this.verticesArrayIndex;

        for( Vector2f vertex : shapeVertices ) {

            vertices[this.verticesArrayIndex] = shape.gameObject.transform.position.x + vertex.x;
            vertices[this.verticesArrayIndex + 1] = shape.gameObject.transform.position.y + vertex.y;
            vertices[this.verticesArrayIndex + 2] = 0;

            // Load color
            vertices[this.verticesArrayIndex + 3] = color.x;
            vertices[this.verticesArrayIndex + 4] = color.y;
            vertices[this.verticesArrayIndex + 5] = color.z;
            vertices[this.verticesArrayIndex + 6] = color.w;

            this.verticesArrayIndex += VERTEX_SIZE;
        }

    }

    private void updateVertexProperties(int index) {
        ShapeRenderer shape = this.shapes[index];

        Vector4f color = shape.getColor();

        Vector2f[] shapeVertices = shape.getVertices();
        verticesArrayIndex = this.verticesOffset[index];

        for( Vector2f vertex : shapeVertices ) {

            vertices[this.verticesArrayIndex] = shape.gameObject.transform.position.x + vertex.x;
            vertices[this.verticesArrayIndex + 1] = shape.gameObject.transform.position.y + vertex.y;
            vertices[this.verticesArrayIndex + 2] = 0;

            // Load color
            vertices[this.verticesArrayIndex + 3] = color.x;
            vertices[this.verticesArrayIndex + 4] = color.y;
            vertices[this.verticesArrayIndex + 5] = color.z;
            vertices[this.verticesArrayIndex + 6] = color.w;

            verticesArrayIndex += VERTEX_SIZE;
        }
    }


    private void loadElementIndices(int index) {

        int[] shapeElementIndices = shapes[index].getElementIndices();
        this.elementIndicesOffset[index] = elementIndicesArrayIndex;

        for( int elementIndex : shapeElementIndices ) {
            elementIndices[elementIndicesArrayIndex++] = this.elementIndicesOffset[index] + elementIndex;
        }

    }

    public int roomsLeftForElementIndices() {
        return maxElementIndices - elementIndicesArrayIndex;
    }
}
