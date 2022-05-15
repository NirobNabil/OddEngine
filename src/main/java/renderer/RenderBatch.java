package renderer;

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

public class RenderBatch {
    // Vertex
    // ======
    // Pos               Color
    // float, float,     float, float, float, float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 6;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private TriangleRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] elementIndices;
    private int elementIndicesArrayIndex = 0;

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize) {
        shader = new Shader("assets/shaders/default.glsl");
        shader.compile();
        this.sprites = new TriangleRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        // 4 vertices quads
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
        elementIndices = new int[maxBatchSize * 6];

        this.numSprites = 0;
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


//        System.out.println("Elements array:");
//        for( int i=0; i<4; i++ ) {
//            for (int ix = i * 6; ix < (i + 1) * 6; ix++) {
//                System.out.print(elementIndices[ix]);
//                System.out.print(", ");
//            }
//            System.out.println();
//        }

        // Enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    public void addSprite(TriangleRenderer spr) {
        // Get index and add renderObject
        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;

        // Add properties to local vertices array
        loadVertexProperties(index);
        loadElementIndices(index);

        if (numSprites >= this.maxBatchSize) {
            this.hasRoom = false;
        }
    }

    private boolean done = false;
    public void render() {

        if ( !done ) {
            int eboID = glGenBuffers();
//        elementIndices = generateIndices();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementIndices, GL_STATIC_DRAW);
        }


        if(!done) {
            System.out.print(VERTEX_SIZE);
            System.out.println("vertices array: ");
            for(int ix=0; ix<4; ix++) {
                for (int i = 0; i < VERTEX_SIZE; i++) {
                    System.out.print(vertices[ix*VERTEX_SIZE+i]);
                    System.out.print(", ");
                }
                System.out.println(" ;");
            }
            done = true;
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

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    private void loadVertexProperties(int index) {
        TriangleRenderer sprite = this.sprites[index];

        // Find offset within array (4 vertices per sprite)
        int offset = index * 3 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();

        // Add vertices with the appropriate properties
//        float xAdd = 1.0f;
//        float yAdd = 1.0f;
//        for (int i=0; i < 4; i++) {
//            if (i == 1) {
//                yAdd = 0.0f;
//            } else if (i == 2) {
//                xAdd = 0.0f;
//            } else if (i == 3) {
//                yAdd = 1.0f;
//            }
//
//            // Load position
//            vertices[offset] = sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x);
//            vertices[offset + 1] = sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y);
//
//            // Load color
//            vertices[offset + 2] = color.x;
//            vertices[offset + 3] = color.y;
//            vertices[offset + 4] = color.z;
//            vertices[offset + 5] = color.w;
//
//            offset += VERTEX_SIZE;
//        }

        Vector2f[] shapeVertices = sprite.getVertices();
        for( Vector2f vertex : shapeVertices ) {
            System.out.print(vertex);
            vertices[offset] = sprite.gameObject.transform.position.x + vertex.x;
            vertices[offset + 1] = sprite.gameObject.transform.position.y + vertex.y;

            // Load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            offset += VERTEX_SIZE;
        }


    }

//    private int[] generateIndices() {
//        // 6 indices per quad (3 per triangle)
//        int[] elements = new int[3 * maxBatchSize];
//        for (int i=0; i < maxBatchSize; i++) {
//            loadElementIndices(elements, i);
//        }
//
//        return elements;
//    }

    private void loadElementIndices(int index) {
//        int offsetArrayIndex = 3 * index;
//        int offset = 3 * index;
//
//        // 3, 2, 0, 0, 2, 1        7, 6, 4, 4, 6, 5
//        // Triangle 1
//        elements[offsetArrayIndex] = offset + 0;
//        elements[offsetArrayIndex + 1] = offset + 2;
//        elements[offsetArrayIndex + 2] = offset + 1;

        int[] shapeElementIndices = sprites[index].getElementIndices();
        for( int elementIndex : shapeElementIndices ) {
            elementIndices[elementIndicesArrayIndex++] = elementIndex;
        }

    }

    public boolean hasRoom() {
        return this.hasRoom;
    }
}
