package graphicBases.objectModel;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.Vec3f;
import graphicBases.GameGLEventListener;
import graphicBases.materialPack.Material;
import graphicBases.programmableSupport.ProgrammableObject;
import textureBinding.TextureTool;

import static com.jogamp.opengl.GL.GL_FLOAT;

/**
 * created by KinMan谨漫 on 2024/1/4/**
 *
 * @author KinMan谨漫
 * ,
 */
public class Box extends ProgrammableObject {
    /**
     * 可编程管线基础类，想要轻松的画画，继承这个类，实现抽象方法即可
     *
     * @param glAutoDrawable
     */
    public Box(GLAutoDrawable glAutoDrawable) {
        super(glAutoDrawable);
    }
    public Box(GLAutoDrawable glAutoDrawable, Vec3f position) {
        super(glAutoDrawable,position);
    }
    public Box(GLAutoDrawable glAutoDrawable, float positionX, float positionY, float positionZ) {
        super(glAutoDrawable,positionX,positionY,positionZ);
    }

    /**
     * 请定义自己的顶点数据
     *
     * @return 顶点数据
     */
    @Override
    protected float[] getVertexData() {
        return new float[]{
                // positions          // normals           // texture coords
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f,  0.0f,
                0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f,  0.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f,  1.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f,  1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f,  1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f,  0.0f,

                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f,  0.0f,
                0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f,  1.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f,  1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f,  1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f,  0.0f,

                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f,  0.0f,
                -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  1.0f,  1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
                -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  0.0f,  0.0f,
                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f,  0.0f,

                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  1.0f,  0.0f,
                0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  1.0f,  1.0f,
                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
                0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  0.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  1.0f,  0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f,  1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  1.0f,  1.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f,  0.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f,  0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  0.0f,  0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f,  1.0f,

                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f,  1.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  1.0f,  1.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f,  0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  0.0f,  0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f,  1.0f
        };
    }

    /**
     * 抽象方法让子类实现自身索引数据,如果不使用EBO,保持默认函数体即可
     */
    @Override
    protected int[] getIndicesData() {
        return new int[0];
    }

    /**
     * 抽象方法让子类实现自身纹理数据,如果不使用纹理,保持默认函数体即可
     * 若要使用纹理请执行以下句子
     * int[] texture=new int[2];
     * texture[0]= TextureTool.loadTexture(gl4,"./resource/texturePixel/dalishi.jpg",4, GL4.GL_REPEAT, GL4.GL_LINEAR, GL4.GL_LINEAR);
     * texture[1]= TextureTool.bindTexture(gl4,"./resource/texturePixel/awesomeface.png");
     * return texture;
     *
     * @param gl4 gl4
     * @return 纹理数组
     */
    @Override
    protected int[] defineTexture(GL4 gl4) {
          int[] texture=new int[2];
      texture[0]= TextureTool.loadTexture(gl4,"./resource/texturePixel/dalishi.jpg",4, GL4.GL_REPEAT, GL4.GL_LINEAR, GL4.GL_LINEAR);
      texture[1]= TextureTool.bindTexture(gl4,"./resource/texturePixel/awesomeface.png");
      return texture;
    }

    /**
     * 画一般固定的部分
     * 一般是以下内容
     * // 位置属性
     * gl4.glEnableVertexAttribArray(0); // 启用顶点属性数组
     * gl4.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 8*Float.BYTES, 0); // 设置顶点属性数组的格式和位置
     * // 颜色属性
     * gl4.glVertexAttribPointer(1, 3, GL_FLOAT, false, 8*Float.BYTES, 3*Float.BYTES);
     * gl4.glEnableVertexAttribArray(1);
     * // 纹理坐标属性
     * gl4.glVertexAttribPointer(2, 2, GL_FLOAT, false, 8*Float.BYTES, 6*Float.BYTES);
     * gl4.glEnableVertexAttribArray(2);
     *
     * @param gl4
     */
    @Override
    protected void initDraw(GL4 gl4) {
        // 位置属性
        gl4.glEnableVertexAttribArray(0); // 启用顶点属性数组
     gl4.glVertexAttribPointer(0, 3, GL_FLOAT, false, 8*Float.BYTES, 0); // 设置顶点属性数组的格式和位置
     // 法向量属性
     gl4.glVertexAttribPointer(3, 3, GL_FLOAT, false, 8*Float.BYTES, 3*Float.BYTES);
     gl4.glEnableVertexAttribArray(3);
     // 纹理坐标属性
     gl4.glVertexAttribPointer(2, 2, GL_FLOAT, false, 8*Float.BYTES, 6*Float.BYTES);
     gl4.glEnableVertexAttribArray(2);

    }

    /**
     * 愉快的画画，用gl4.glDraw选择画什么即可,最好是随时会变的部分
     * Matrix4f model = new Matrix4f();
     * GameGLEventListener.objectShaderManager.setUniform("model", model);
     * gl4.glDrawArrays(GL_TRIANGLES, 0, 3); // 绘制三角形
     *
     * @param gl4
     */
    @Override
    protected void happyDraw(GL4 gl4) {
        applyTranslation( position,false);
        applyScale(2f,2f,2f,false);
        applyRotation(2,1,1,1,true);
        applyTransformation(gl4, GameGLEventListener.objectShaderManager);
        gl4.glBindVertexArray(vao);
        gl4.glDrawArrays(GL4.GL_TRIANGLES, 0, 36); // 绘制三角形

    }

    /**
     * float[]ambient,float[]diffuse,float[]specular,float shininess
     * 构造Material
     *
     * @return Material 材质
     */
    @Override
    protected Material defineMaterial() {

        return new Material(
                new float[]{ 0.2f,0.2f,0.2f},
                new float[]{1,0.5f,0.31f},
                new float[]{1,1,1},
                32.0f
        );
    }

}
