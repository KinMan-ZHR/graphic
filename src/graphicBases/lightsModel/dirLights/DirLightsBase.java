package graphicBases.lightsModel.dirLights;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.Vec3f;
import graphicBases.lightsModel.DirLight;
import graphicBases.materialPack.Material;

/**
 * created by KinMan谨漫 on 2024/1/5/**
 *
 * @author KinMan谨漫
 * ,
 */
public class DirLightsBase extends DirLight {
    /**
     * 平行光抽象类
     * 该类相较于ProgrammableLight类，会自动帮你处理光质,如要改变写入的着色器，请重写lightDraw方法
     * 若要定义材质纹理，这里建议您使用继承的方式，实现defineMaterial方法,相应的，如要改变请继承后重写defineMaterial方法
     * 若要定义顶点数据，这里建议您使用继承的方式，实现getVertexData方法,相应的，如要改变请继承后重写getVertexData方法
     * 实现类的命名方式为：光种+形状名+材质名+区别名/用途,例如：Cube_Gold_Pure
     *
     * @param glAutoDrawable
     * @param direction
     */
    public DirLightsBase(GLAutoDrawable glAutoDrawable, Vec3f direction) {
        super(glAutoDrawable, direction);
    }
    public DirLightsBase(GLAutoDrawable glAutoDrawable, float directionX, float directionY, float directionZ) {
        super(glAutoDrawable, directionX, directionY, directionZ);
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
     * todo 通知gpu如何解析顶点数据来画画
     *  0表示顶点属性的索引，这个索引对应于顶点着色器中layout(location = 0) in vec3 position;中的0
     *  3表示顶点属性的大小，这个大小对应于顶点着色器中layout(location = 0) in vec3 position;中的vec3
     *  GL.GL_FLOAT表示顶点属性的类型，这个类型对应于顶点着色器中layout(location = 0) in vec3 position;中的vec3
     *  false表示是否希望数据被标准化，这个参数对应于顶点着色器中layout(location = 0) in vec3 position;中的false
     *  第五个参数叫做步长(Stride)，它告诉我们在连续的顶点属性组之间的间隔。由于下个组位置数据在3个float之后，我们把步长设置为3 * sizeof(float)。要注意的是由于我们知道这个数组是紧密排列的（在两个顶点属性之间没有空隙）我们也可以设置为0来让OpenGL决定具体步长是多少（只有当数值是紧密排列时才可用）。
     *  0表示顶点属性数组的偏移量，这个参数对应于顶点着色器中layout(location = 0) in vec3 position;中的0
     *  一般是以下内容
     *      // 位置属性
     *         gl4.glEnableVertexAttribArray(0); // 启用顶点属性数组
     *         gl4.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 8*Float.BYTES, 0); // 设置顶点属性数组的格式和位置
     *      // 颜色属性
     *         gl4.glVertexAttribPointer(1, 3, GL_FLOAT, false, 8*Float.BYTES, 3*Float.BYTES);
     *         gl4.glEnableVertexAttribArray(1);
     *      // 纹理坐标属性
     *         gl4.glVertexAttribPointer(2, 2, GL_FLOAT, false, 8*Float.BYTES, 6*Float.BYTES);
     *         gl4.glEnableVertexAttribArray(2);
     *
     * @param gl4
     */
    @Override
    protected void initDraw(GL4 gl4) {

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
                new float[]{0.05f, 0.05f, 0.05f},
                new float[]{0.4f, 0.4f, 0.4f},
                new float[]{0.5f, 0.5f, 0.5f},
                32.0f
        );
    }
}
