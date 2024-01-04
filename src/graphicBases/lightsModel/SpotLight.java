package graphicBases.lightsModel;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.Vec3f;
import graphicBases.GameGLEventListener;
import graphicBases.materialPack.Material;
import graphicBases.programmableSupport.ProgrammableLight;

/**
 * created by KinMan谨漫 on 2024/1/4/**
 *
 * @author KinMan谨漫
 * ,
 */
public class SpotLight extends ProgrammableLight {

    float cutOff= (float) Math.cos( Math.toRadians(12.5f));
    float outerCutOff= (float) Math.cos(Math.toRadians(15f));

    public SpotLight(GLAutoDrawable glAutoDrawable) {
        super(glAutoDrawable, 0, 0, 0, 1.0f, 0.09f, 0.032f);
    }

    /**
     * 请定义自己的顶点数据
     *
     * @return 顶点数据
     */
    @Override
    protected float[] getVertexData() {
        return new float[0];
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

    }

    /**
     * 画材质
     *
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
                new float[]{0.0f, 0.0f, 0.0f},
                new float[]{1.0f, 1.0f, 1.0f},
                new float[]{1.0f, 1.0f, 1.0f}
        );
    }

    /**
     * @param gl4
     */
    @Override
    public void lightDraw(GL4 gl4) {
        System.out.println("cameraFront:"+GameGLEventListener.camera.cameraFront);
        GameGLEventListener.objectShaderManager.useShaderProgram();
        GameGLEventListener.objectShaderManager.setUniform("spotLight.position",GameGLEventListener.camera.cameraPos);
        GameGLEventListener.objectShaderManager.setUniform("spotLight.direction",GameGLEventListener.camera.cameraFront);
        GameGLEventListener.objectShaderManager.setUniform("spotLight.ambient",material.getAmbient());
        GameGLEventListener.objectShaderManager.setUniform("spotLight.diffuse",material.getDiffuse());
        GameGLEventListener.objectShaderManager.setUniform("spotLight.specular",material.getSpecular());
        GameGLEventListener.objectShaderManager.setUniform("spotLight.constant",constant);
        GameGLEventListener.objectShaderManager.setUniform("spotLight.linear",linear);
        GameGLEventListener.objectShaderManager.setUniform("spotLight.quadratic",quadratic);
        GameGLEventListener.objectShaderManager.setUniform("spotLight.cutOff",cutOff);
        GameGLEventListener.objectShaderManager.setUniform("spotLight.outerCutOff",outerCutOff);


    }
}
