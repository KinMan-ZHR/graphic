package graphicBases.lightsModel.dirLights;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.Matrix4f;
import com.jogamp.opengl.math.Vec3f;
import graphicBases.lightsModel.DirLight;
import graphicBases.materialPack.Material;
import shaderControl.ShaderManager;

import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static graphicBases.GameGLEventListener.SHADOW_HEIGHT;
import static graphicBases.GameGLEventListener.SHADOW_WIDTH;

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
        GL4 gl4 = (GL4) glAutoDrawable.getGL();
        //设置阴影矩阵
        //创建深度贴图
        gl4.glGenFramebuffers(1,depthMapFBO,0);
        gl4.glGenTextures(1,depthMap,0);
        gl4.glBindTexture(GL4.GL_TEXTURE_2D,depthMap[0]);
        gl4.glTexImage2D(GL4.GL_TEXTURE_2D,0,GL4.GL_DEPTH_COMPONENT,SHADOW_WIDTH,SHADOW_HEIGHT,0,GL4.GL_DEPTH_COMPONENT,GL4.GL_FLOAT,null);
        gl4.glTexParameteri(GL4.GL_TEXTURE_2D,GL4.GL_TEXTURE_MIN_FILTER,GL4.GL_NEAREST);
        gl4.glTexParameteri(GL4.GL_TEXTURE_2D,GL4.GL_TEXTURE_MAG_FILTER,GL4.GL_NEAREST);
        gl4.glTexParameteri(GL4.GL_TEXTURE_2D,GL4.GL_TEXTURE_WRAP_S,GL4.GL_CLAMP_TO_BORDER);
        gl4.glTexParameteri(GL4.GL_TEXTURE_2D,GL4.GL_TEXTURE_WRAP_T,GL4.GL_CLAMP_TO_BORDER);
        float[] borderColor = {1.0f,1.0f,1.0f,1.0f};
        gl4.glTexParameterfv(GL4.GL_TEXTURE_2D,GL4.GL_TEXTURE_BORDER_COLOR,borderColor,0);
        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER,depthMapFBO[0]);
        gl4.glFramebufferTexture2D(GL4.GL_FRAMEBUFFER,GL4.GL_DEPTH_ATTACHMENT,GL4.GL_TEXTURE_2D,depthMap[0],0);
        gl4.glDrawBuffer(GL4.GL_NONE);
        gl4.glReadBuffer(GL4.GL_NONE);
        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER,0);
    }
    @Override
    public void shadowDraw(GL4 gl4, ShaderManager shaderManager){
        //阴影
        // 1. Render depth of scene to texture (from light's perspective)
        Matrix4f lightProjection =new Matrix4f();
        float near_plane = 1.0f, far_plane = 7.5f;
        lightProjection.setToOrtho(-10,10,-10,10,near_plane,far_plane);
        Matrix4f lightView =new Matrix4f();
        lightView.setToLookAt(position,direction,new Vec3f(0,1,0),new Matrix4f());

        lightSpaceMatrix.mul(lightProjection,lightView);
        shaderManager.useShaderProgram();
        shaderManager.setUniform("lightSpaceMatrix",lightSpaceMatrix);
        gl4.glViewport(0, 0, SHADOW_WIDTH, SHADOW_HEIGHT);
        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER, depthMapFBO[0]);
        gl4.glClear(GL_DEPTH_BUFFER_BIT); // 清屏

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
     * @param shaderManager
     */
    @Override
    protected void happyDraw(GL4 gl4, ShaderManager shaderManager) {

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
