package graphicBases.objectModel;

import assimp4j.Mesh;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.Vec3f;
import graphicBases.GameGLEventListener;
import graphicBases.materialPack.Material;
import graphicBases.programmableSupport.ProgrammableMeshObj;
import textureBinding.TextureTool;

import static com.jogamp.opengl.GL.GL_UNSIGNED_INT;

/**
 * created by KinMan谨漫 on 2024/1/6/**
 *
 * @author KinMan谨漫
 * ,
 */
public class Floor extends ProgrammableMeshObj {
    public Floor(GLAutoDrawable glAutoDrawable, Vec3f position) {
        super(glAutoDrawable, position);
    }

    public Floor(GLAutoDrawable glAutoDrawable, float positionX, float positionY, float positionZ) {
        super(glAutoDrawable, positionX, positionY, positionZ);
    }

    /**
     * 导入模型定义mesh
     *
     * @return mesh
     * return Mesh.load("resource/meshModel/floor/floor.fbx", 0);
     */
    @Override
    protected Mesh defineMesh() {
        return Mesh.load("resource/meshModel/floor/floor.fbx", 0);
    }

    /**
     * 抽象方法让子类实现自身纹理数据,如果不使用纹理,保持默认函数体即可
     * 若要使用纹理请执行以下句子
     * int[] texture=new int[1];
     * texture[0]= TextureTool.loadTexture(gl4,"./resource/texturePixel/dalishi.jpg",4, GL4.GL_REPEAT, GL4.GL_LINEAR, GL4.GL_LINEAR);
     * return texture;
     *
     * @param gl4 gl4
     * @return 纹理数组
     */
    @Override
    protected int[] defineTexture(GL4 gl4) {
        int[] texture=new int[1];
        texture[0]= TextureTool.loadTexture(gl4,"./resource/meshModel/floor/wall1_2048.jpg",4, GL4.GL_REPEAT, GL4.GL_LINEAR, GL4.GL_LINEAR);
        return texture;
    }

    /**
     * 愉快的画画，用gl4.glDraw选择画什么即可,最好是随时会变的部分
     * applyTranslation( position,false);
     * applyTransformation(gl4, GameGLEventListener.objectShaderManager);
     * gl4.glBindVertexArray(vao);
     * gl4.glDrawElements(GL4.GL_TRIANGLES, indicesData.length, GL_UNSIGNED_INT, 0);
     *
     * @param gl4
     */
    @Override
    protected void happyDraw(GL4 gl4) {
        applyTranslation( position,false);
        applyTransformation(gl4, GameGLEventListener.objectShaderManager);
        gl4.glBindVertexArray(vao);
        gl4.glDrawElements(GL4.GL_TRIANGLES, indicesData.length, GL_UNSIGNED_INT, 0);

    }

    /**
     * float[]ambient,float[]diffuse,float[]specular,float shininess
     * 构造Material
     *
     * @return Material 材质
     * return new Material(
     * new float[]{ 0.2f,0.2f,0.2f},
     * new float[]{ 0.5f,0.5f,0.5f},
     * new float[]{ 1.0f,1.0f,1.0f},
     * 32.0f
     * );
     */
    @Override
    protected Material defineMaterial() {
        return null;
    }
}
