package com.example.paolobonomi.eattheball.Activity.Engine;

import android.opengl.GLES20;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 *
 * Created by HAYDN on 1/7/2015.
 *
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 2.0.
 *
 */

class Square {

    static Square DrawSquare = null;
    static void InitSquare()
    {
        DrawSquare = new Square();
    }

    private FloatBuffer vertexBuffer;

    // number of coordinates per vertex in this array
    private static final int COORDS_PER_VERTEX = 2;
    private static float squareCoords[] = {
            -0.5f, -0.5f,     // bottom left
            0.5f, -0.5f,      // bottom right
            0.5f,  0.5f,      // top right
            -0.5f,  0.5f};    // top left


    private static short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices
    static Vector4f Color = new Vector4f();


    private static final String vertexShaderCode =
            "attribute vec2 vPosition;" +
                    "varying vec2 TexCoord;" +
                    "uniform mat4 vMatrix;" +
                    "void main() {" +
                    "  gl_Position = vMatrix * vec4(vPosition,0,1);" +
                    "  TexCoord = vPosition.st + 0.5;" +
                    "  TexCoord.t = 1.0 - TexCoord.t;" +
                    "}";

    private static final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D vTexture;" +
                    "varying vec2 TexCoord;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D(vTexture,TexCoord.st).rgba * vColor;" +
                    "  gl_FragColor *= gl_FragColor.a;" +
                    "}";

    private int mProgram;
    private int mPositionHandle,mColorHandle,mMatrixHandle;
    private static final int vertexStride = COORDS_PER_VERTEX * 4;
    private static final int vertexCount = 4;

    private Square() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4); // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2); // (# of coordinate values * 2 bytes per short)
        dlb.order(ByteOrder.nativeOrder());
        ShortBuffer drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);


        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // creates OpenGL ES program executables

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // get handle to fragment shader's vMatrix member
        mMatrixHandle = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        // get handle to fragment shader's vTexture member
        GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "vTexture"),0);
    }

    private static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    void draw(Matrix4f matrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        float color[] = { Color.x,Color.y,Color.z,Color.w };
        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // Set mvp matrix
        float f[] = new float[16];
        matrix.get(f,0);
        GLES20.glUniformMatrix4fv(mMatrixHandle,1,false,f,0);

        // Draw the triangle
        //GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_SHORT, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
