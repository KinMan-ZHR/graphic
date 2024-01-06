package graphicBases.lightsModel.pointLights;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.Vec3f;
import graphicBases.GameGLEventListener;
import graphicBases.lightsModel.PointLight;
import graphicBases.materialPack.Material;
import shaderControl.ShaderManager;

import static com.jogamp.opengl.GL.GL_TRIANGLES;

/**
 * created by KinMan谨漫 on 2024/1/5/**
 *
 * @author KinMan谨漫
 * ,
 */
public class PointLightBase extends PointLight {
    /**
     * 点光源抽象类
     * 该类相较于ProgrammableLight类，会自动帮你处理光质,如要改变写入的着色器，请重写lightDraw方法
     * 若要定义材质纹理，这里建议您使用继承的方式，实现defineMaterial方法,相应的，如要改变请继承后重写defineMaterial方法
     * 若要定义顶点数据，这里建议您使用继承的方式，实现getVertexData方法,相应的，如要改变请继承后重写getVertexData方法
     * 实现类的命名方式为：形状名+材质名+区别名,例如：Cube_Gold_Pure
     *
     * @param glAutoDrawable
     * @param x
     * @param y
     * @param z
     */
    public PointLightBase(GLAutoDrawable glAutoDrawable, float x, float y, float z) {
        super(glAutoDrawable, x, y, z);
    }

    public PointLightBase(GLAutoDrawable glAutoDrawable, Vec3f position) {
        super(glAutoDrawable, position);
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
     * 愉快的画画，用gl4.glDraw选择画什么即可,最好是随时会变的部分
     * Matrix4f model = new Matrix4f();
     * GameGLEventListener.objectShaderManager.setUniform("model", model);
     * gl4.glDrawArrays(GL_TRIANGLES, 0, 3); // 绘制三角形
     *
     * @param gl4
     * @param shaderManager
     */
    @Override
    protected void happyDraw(GL4 gl4, ShaderManager shaderManager) {
        applyTranslation(position,false);
        //applyRotation(getDeltaTime()*50.0f,0.5f,1.0f,0.0f,false);
        applyScale(0.2f,0.2f,0.2f,false);
        applyTransformation(gl4, shaderManager);
        shaderManager.setUniform("view", GameGLEventListener.camera.view);
        shaderManager.setUniform("projection", GameGLEventListener.camera.projection);
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
     * return new float[]{constant,linear,quadratic};
     *
     * @return
     */
    @Override
    protected float[] defineAttenuation() {
        return new float[]{1.0f,0.2f,0.2f};
    }
}
