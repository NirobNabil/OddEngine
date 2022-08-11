package renderer;

import components.CircleRenderer;
import components.ShapeRenderer;
import odd.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatchCircle extends RenderBatch {
    // Vertex
    // ======
    // Pos                     Color                        Center Pos            Radius
    // float, float, float     float, float, float, float   float, float, float   float
    private final int POS_SIZE = 3;
    private final int COLOR_SIZE = 4;
    private final int CENTER_POS_SIZE = 3;
    private final int RADIUS_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int CENTER_POS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int RADIUS_OFFSET = CENTER_POS_OFFSET + CENTER_POS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + CENTER_POS_SIZE + RADIUS_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private CircleRenderer[] circles;
    private int[] verticesOffset, elementIndicesOffset;
    public int numCircles = 0, elementIndicesArrayIndex = 0, verticesArrayIndex = 0;
    private float[] vertices;
    private int[] elementIndices;

    private int vaoID, vboID, eboID, maxElementIndices;
    private Shader shader;

    public RenderBatchCircle(int maxCircleCount) {
        shader = new Shader("assets/shaders/circleShader.glsl");
        shader.compile();
        this.circles = new CircleRenderer[maxCircleCount];
        this.maxElementIndices = maxCircleCount * 4;   // assuming there are total two adjacent triangles used for a circle

        // TODO: this line can be optimized with variable sized array for vertices
        vertices = new float[maxElementIndices * VERTEX_SIZE];
        elementIndices = new int[maxElementIndices];

        // TODO: This can be optimized too because the array size needs to be equal to maxShapes
        verticesOffset = new int[maxElementIndices];
        elementIndicesOffset = new int[maxElementIndices];
    }

    public void start() {
//        System.out.println(POS_OFFSET + " " + COLOR_OFFSET + " " + CENTER_POS_OFFSET + " " + RADIUS_OFFSET);

        // Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementIndices, GL_STATIC_DRAW);


        // Enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, CENTER_POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, CENTER_POS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, RADIUS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, RADIUS_OFFSET);
        glEnableVertexAttribArray(3);
    }

    @Override
    public void addShape(ShapeRenderer renderer) {
        // Get index and add renderObject
        int index = this.numCircles;
        try {
            this.circles[index] = (CircleRenderer) renderer;
        }catch(Exception e){
            // System.out.println("in addshape");
        }
        this.numCircles++;

        // Add properties to local vertices array
        loadElementIndices(index);
        loadVertexProperties(index);
    }

    private boolean elementIndicesToUpdate = true;
    public void render() {

        glBindVertexArray(vaoID);

        // For now, we will rebuffer all data every frame
        for( int i=0; i<numCircles; i++ ){
            updateVertexProperties(i);
        }

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        if( elementIndicesToUpdate ) {
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementIndices, GL_STATIC_DRAW);
//            elementIndicesToUpdate = false;
        }
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glDrawElements(GL_TRIANGLES, this.elementIndicesArrayIndex, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
//
        glBindVertexArray(0);

        shader.detach();
    }

    private void loadVertexProperties(int index) {
        CircleRenderer circle = this.circles[index];

        Vector4f color = circle.getColor();

        Vector2f[] circleVertices = circle.getVertices();
        this.verticesOffset[index] = this.verticesArrayIndex;

        for( Vector2f vertex : circleVertices ) {

            vertices[this.verticesArrayIndex] = circle.gameObject.transform.position.x + vertex.x;
            vertices[this.verticesArrayIndex + 1] = circle.gameObject.transform.position.y + vertex.y;
            vertices[this.verticesArrayIndex + 2] = 0;
            // no need to set 3rd index because it is zero by default

            // Load color
            vertices[this.verticesArrayIndex + 3] = color.x;
            vertices[this.verticesArrayIndex + 4] = color.y;
            vertices[this.verticesArrayIndex + 5] = color.z;
            vertices[this.verticesArrayIndex + 6] = color.w;

            vertices[this.verticesArrayIndex + 7] = circle.gameObject.transform.position.x + circle.getCenter().x;
            vertices[this.verticesArrayIndex + 8] = circle.gameObject.transform.position.y + circle.getCenter().y;
            vertices[this.verticesArrayIndex + 9] = 0;

            vertices[this.verticesArrayIndex + 10] = circle.getRadius();

//            for( float i : Arrays.copyOfRange(vertices, verticesArrayIndex, verticesArrayIndex+11) ) {
//                System.out.print(i);
//                System.out.print(", ");
//            };
//            System.out.println(";");


            this.verticesArrayIndex += VERTEX_SIZE;
        }

    }

    private void updateVertexProperties(int index) {
        CircleRenderer circle = this.circles[index];

        Vector4f color = circle.getColor();

        Vector2f[] circleVertices = circle.getVertices();
        this.verticesArrayIndex = this.verticesOffset[index];

        for( Vector2f vertex : circleVertices ) {

            vertices[this.verticesArrayIndex] = circle.gameObject.transform.position.x + vertex.x;
            vertices[this.verticesArrayIndex + 1] = circle.gameObject.transform.position.y + vertex.y;
//            vertices[this.verticesArrayIndex + 2] = 0;
            // no need to set 3rd index because it is zero by default

            // Load color
//            vertices[this.verticesArrayIndex + 3] = color.x;
//            vertices[this.verticesArrayIndex + 4] = color.y;
//            vertices[this.verticesArrayIndex + 5] = color.z;
//            vertices[this.verticesArrayIndex + 6] = color.w;

            vertices[this.verticesArrayIndex + 7] = circle.gameObject.transform.position.x + circle.getCenter().x;
            vertices[this.verticesArrayIndex + 8] = circle.gameObject.transform.position.y + circle.getCenter().y;
//            vertices[this.verticesArrayIndex + 9] = 0;

            vertices[this.verticesArrayIndex + 10] = circle.getRadius();

            this.verticesArrayIndex += VERTEX_SIZE;
        }

    }


    private void loadElementIndices(int index) {

        int[] shapeElementIndices = circles[index].getElementIndices();
        this.elementIndicesOffset[index] = this.verticesArrayIndex / VERTEX_SIZE;

        for( int elementIndex : shapeElementIndices ) {
            elementIndices[elementIndicesArrayIndex++] = this.elementIndicesOffset[index] + elementIndex;
        }

    }

    public int roomsLeftForElementIndices() {
        return maxElementIndices - elementIndicesArrayIndex;
    }
}
