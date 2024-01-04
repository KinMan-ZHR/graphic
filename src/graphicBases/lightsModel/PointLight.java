package graphicBases.lightsModel;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import graphicBases.GameGLEventListener;
import graphicBases.materialPack.Material;
import graphicBases.programmableSupport.ProgrammableLight;

import static com.jogamp.opengl.GL.GL_TRIANGLES;

/**
 * created by KinMan谨漫 on 2024/1/4/**
 *
 * @author KinMan谨漫
 * ,
 */
public class PointLight extends ProgrammableLight {
    private static int count=0;
    public PointLight(GLAutoDrawable glAutoDrawable, float x, float y, float z, float constant, float linear, float quadratic) {
        super(glAutoDrawable, x, y, z, constant, linear, quadratic);
        count++;
    }

    /**
     * @param gl4
     */
    @Override
    public void lightDraw(GL4 gl4) {
        {
            for (int i = 0; i < count; i++) {
                GameGLEventListener.objectShaderManager.setUniform("pointLights[" + i + "].position", lightPos);
                GameGLEventListener.objectShaderManager.setUniform("pointLights[" + i + "].ambient", material.getAmbient());
                GameGLEventListener.objectShaderManager.setUniform("pointLights[" + i + "].diffuse", material.getDiffuse());
                GameGLEventListener.objectShaderManager.setUniform("pointLights[" + i + "].specular", material.getSpecular());
                GameGLEventListener.objectShaderManager.setUniform("pointLights[" + i + "].constant", constant);
                GameGLEventListener.objectShaderManager.setUniform("pointLights[" + i + "].linear", linear);
                GameGLEventListener.objectShaderManager.setUniform("pointLights[" + i + "].quadratic", quadratic);
            }
        }

    }
    /**
     * 可编程管线基础类，想要轻松的画画，继承这个类，实现抽象方法即可
     * 默认cube数据是float[]类型的5个一组，前三个是位置，后两个是纹理坐标
     * @param glAutoDrawable
     */



    /**
     * 请定义自己的顶点数据
     *
     * @return 顶点数据
     */
    @Override
    protected float[] getVertexData() {
        return new float[]{
                // positions          // normals           // texture coords
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,

                -0.5f, -0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                -0.5f, -0.5f,  0.5f,

                -0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,

                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,

                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                -0.5f, -0.5f,  0.5f,
                -0.5f, -0.5f, -0.5f,

                -0.5f,  0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f,
        };
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
           gl4.glEnableVertexAttribArray(0); // 启用顶点属性数组
           gl4.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 3*Float.BYTES, 0); // 设置顶点属性数组的格式和位置
    }

    /**
     * @param gl4
     */
    @Override
    public void materialDraw(GL4 gl4) {

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
        applyTranslation(lightPos[0],lightPos[1],lightPos[2],false);
        //applyRotation(getDeltaTime()*50.0f,0.5f,1.0f,0.0f,false);
        applyScale(0.2f,0.2f,0.2f,false);
        applyTransformation(gl4,GameGLEventListener.lightShaderManager);
        GameGLEventListener.lightShaderManager.setUniform("view", GameGLEventListener.camera.view);
        GameGLEventListener.lightShaderManager.setUniform("projection", GameGLEventListener.camera.projection);
        gl4.glBindVertexArray(vao);
        gl4.glDrawArrays(GL_TRIANGLES, 0, 36);

    }

    /**
     * float[]ambient,float[]diffuse,float[]specular,float shininess
     * 构造Material
     *
     * @return Material 材质
     */

    @Override
    protected Material defineMaterial() {
        return new Material(new float[]{0.2f, 0.2f, 0.2f}, new float[]{0.5f, 0.5f, 0.5f}, new float[]{1.0f, 1.0f, 1.0f});
    }


    /**
     * 可编程管线基础类，想要轻松的画画，继承这个类，实现抽象方法即可
     *
     * @param glAutoDrawable glAutoDrawable
     */

}
