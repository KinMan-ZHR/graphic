package graphicBases.programmableSupport;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.Matrix4f;
import com.jogamp.opengl.math.Vec3f;
import graphicBases.materialPack.Material;
import shaderControl.ShaderManager;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import static com.jogamp.opengl.GL.*;
// Define a class to represent a vertex


/**
 * created by KinMan谨漫 on 2024/1/1/**
 *
 * @author KinMan谨漫
 * ,
 */

public abstract class ProgrammableBase {
    // 定义一个静态的哈希表来存储子类对象的vbo
    private static final HashMap<Class<?>, Integer> vboBuffer = new HashMap<>();
    private static final HashMap<Class<?>, Integer> eboBuffer = new HashMap<>();
    protected final int vao; // 顶点数组对象的句柄
    //修改三剑客
    protected int[] indicesData;//索引数据
    protected float[] VERTEX_DATA;//顶点数据
    protected int[] texture;//纹理数组
    protected Material material=new Material();//材质
    protected Vec3f position=new Vec3f();//位置,默认为0,0,0

    // 获取当前的纳秒时间
    private final static long start=System.nanoTime();;
    private final Matrix4f rotation=new Matrix4f();
    private final Matrix4f translation=new Matrix4f();
    private final Matrix4f scale=new Matrix4f();
    private Matrix4f tempRotation=new Matrix4f();
    private Matrix4f tempTranslation=new Matrix4f();
    private Matrix4f tempScale=new Matrix4f();

    public static final ArrayList<ProgrammableBase> list=new ArrayList<>();
    /**
     * 可编程管线基础类，想要轻松的画画，继承这个类，实现抽象方法即可
     */
    public ProgrammableBase(GLAutoDrawable glAutoDrawable) {
        GL4 gl4 = glAutoDrawable.getGL().getGL4(); // 获取OpenGL的对象
        list.add(this);//将自己加入到list中

        //顶点数组对象vao
        int[] vaoIds = new int[1]; // 创建一个数组，用于存储顶点数组对象的句柄
        gl4.glGenVertexArrays(1, vaoIds, 0); // 生成一个顶点数组对象
        vao = vaoIds[0]; // 获取顶点数组对象的句柄
        gl4.glBindVertexArray(vao); // 绑定顶点数组对象
        //顶点缓冲对象启动vbo
        useVBO(gl4);
        //索引缓冲对象启动ebo
        useEBO(gl4);
        texture= defineTexture(gl4);
        // 判断是否使用材质
        if (defineMaterial()!=null){
            material=defineMaterial();
        }
        //初始化顶点指针属性
        initDraw(gl4);
    }

    private void useVBO(GL4 gl4) {
        //尚未创建vbo
        if (vboBuffer.get(this.getClass()) == null) {
            //todo 顶点缓冲对象启动！
            int[] vboIds = new int[1]; // 创建一个数组，用于存储顶点缓冲对象的句柄
            gl4.glGenBuffers(1, vboIds, 0); // 生成一个顶点缓冲对象,将索引存放在vboIds[0]中
            // 顶点缓冲对象的句柄
            int vbo = vboIds[0]; // 获取顶点缓冲对象的句柄
            VERTEX_DATA = getVertexData();
            //GL.GL_ARRAY_BUFFER表示该VBO用于存储顶点属性，如位置、颜色、纹理坐标等。
            gl4.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo); // 绑定顶点缓冲对象到GL_ARRAY_BUFFER目标
            //todo 将cpu中的数据复制到gpu中
            gl4.glBufferData(GL.GL_ARRAY_BUFFER, (long) VERTEX_DATA.length * Float.BYTES, FloatBuffer.wrap(VERTEX_DATA), GL_STATIC_DRAW); // 将顶点数据存储到显存中
            //vboBuffer.getOrDefault(this.getClass(), 0);
            vboBuffer.put(this.getClass(), vbo); // 将顶点缓冲对象的句柄存储到哈希表中
//            System.out.println("Class = " + this.getClass().getName() + "创建vbo");
//            System.out.println("vboBuffer = " + vboBuffer.get(this.getClass()));
        }else {
//            System.out.println("Class = " + this.getClass().getName() + "使用以前vbo");
//            System.out.println("vboBuffer = " + vboBuffer.get(this.getClass()));
            //已经创建vbo
            gl4.glBindBuffer(GL.GL_ARRAY_BUFFER, vboBuffer.get(this.getClass())); // 绑定顶点缓冲对象到GL_ARRAY_BUFFER目标
        }
    }

    /**
     * 请定义自己的顶点数据
     * @return 顶点数据
     */
    protected abstract float[] getVertexData();
    /**
     * 抽象方法让子类实现自身索引数据,如果不使用EBO,保持默认函数体即可
     */
    protected abstract int[] getIndicesData();
    /**
     * 抽象方法让子类实现自身纹理数据,如果不使用纹理,保持默认函数体即可
     * 若要使用纹理请执行以下句子
     *  int[] texture=new int[2];
     *  texture[0]= TextureTool.loadTexture(gl4,"./resource/texturePixel/dalishi.jpg",4, GL4.GL_REPEAT, GL4.GL_LINEAR, GL4.GL_LINEAR);
     *  texture[1]= TextureTool.bindTexture(gl4,"./resource/texturePixel/awesomeface.png");
     *  return texture;
     *  @param gl4 gl4
     *  @return 纹理数组
     */
    protected abstract int[] defineTexture(GL4 gl4);
    /**
     * 时代变了，该方法被废弃了,没有作用，请重写materialDraw方法
     * @param gl4 gl4
     */
    @Deprecated
    protected abstract void useTexture(GL4 gl4);
    /**
     * 使用EBO来解决重复顶点的问题
     */
    protected void useEBO(GL4 gl4){
        indicesData=getIndicesData();
        if (indicesData.length==0)
            return;
        //todo 索引缓冲对象启动！
        if (eboBuffer.get(this.getClass()) == null) {
            int[] eboIds = new int[1]; // 创建一个数组，用于存储顶点缓冲对象的句柄
            gl4.glGenBuffers(1, eboIds, 0); // 生成一个顶点缓冲对象,将索引存放在vboIds[0]中
            // 顶点缓冲对象的句柄
            int ebo = eboIds[0]; // 获取顶点缓冲对象的句柄
            gl4.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo); // 绑定顶点缓冲对象到GL_ARRAY_BUFFER目标
            //todo 将cpu中的数据复制到gpu中
            gl4.glBufferData(GL_ELEMENT_ARRAY_BUFFER, (long) indicesData.length * Integer.BYTES, IntBuffer.wrap(indicesData), GL_STATIC_DRAW); // 将顶点数据存储到显存中
            //vboBuffer.getOrDefault(this.getClass(), 0);
            eboBuffer.put(this.getClass(), ebo); // 将顶点缓冲对象的句柄存储到哈希表中
        }else {
            gl4.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboBuffer.get(this.getClass())); // 绑定顶点缓冲对象到GL_ARRAY_BUFFER目标
        }
    }
    /**
     *  todo 通知gpu如何解析顶点数据来画画
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
     */
    protected abstract void initDraw(GL4 gl4);

    /**
     * 最重要的类，用来画画
     *
     * @param gl4
     * @param shaderManager
     */
    protected void allReadyDraw(GL4 gl4, ShaderManager shaderManager){
        //画材质
        materialDraw(gl4,shaderManager);
        //清空所有的临时矩阵，以保证为帧内可累加，帧与帧之间不累加
        tempRotation=new Matrix4f();
        tempTranslation=new Matrix4f();
        tempScale=new Matrix4f();
        happyDraw(gl4,shaderManager);
    }
    /**
     * 画材质
     *
     * @param gl4
     * @param shaderManager
     */
    public abstract void materialDraw(GL4 gl4, ShaderManager shaderManager);
    /**
     *  愉快的画画，用gl4.glDraw选择画什么即可,最好是随时会变的部分
     *      Matrix4f model = new Matrix4f();
     *      GameGLEventListener.objectShaderManager.setUniform("model", model);
     *      gl4.glDrawArrays(GL_TRIANGLES, 0, 3); // 绘制三角形
     */
    protected abstract void happyDraw(GL4 gl4, ShaderManager shaderManager);
    protected void deleteBind(GL4 gl4){
        gl4.glBindVertexArray(0); // 解绑顶点数组对象
        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER, 0); // 解绑顶点缓冲对象
        gl4.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0); // 解绑索引缓冲对象
    }
    public static float getDeltaTime(){
        // 计算时间间隔，单位为秒
        return (float) ((System.nanoTime() - start) / 1e9);
    }
    protected void applyRotation(float theta, float x, float y, float z,boolean accumulable) {
        if (accumulable){
            Matrix4f temp=new Matrix4f(rotation);
            rotation.rotate((float) Math.toRadians(theta), x, y, z, temp);
        }
        else {
            Matrix4f temp=new Matrix4f(tempRotation);
            tempRotation.rotate((float) Math.toRadians(theta), x, y, z, temp);

        }
    }
    protected void applyRotation(float theta,Vec3f vec3f,boolean accumulable) {
        if (accumulable){
            Matrix4f temp=new Matrix4f(rotation);
            rotation.rotate((float) Math.toRadians(theta), vec3f, temp);
        }
        else {
            Matrix4f temp=new Matrix4f(tempRotation);
            tempRotation.rotate((float) Math.toRadians(theta), vec3f, temp);
        }
    }
    protected void applyTranslation(float x, float y, float z,boolean accumulable) {
        if (accumulable){
            Matrix4f temp=new Matrix4f(translation);
            translation.translate(x, y, z, temp);
        }
        else {
            Matrix4f temp=new Matrix4f(tempTranslation);
            tempTranslation.translate(x, y, z, temp);
        }

    }
    protected void applyTranslation(Vec3f vec3f,boolean accumulable) {
        if (accumulable){
            Matrix4f temp=new Matrix4f(translation);
            translation.translate(vec3f, temp);
        }
        else {
            Matrix4f temp=new Matrix4f(tempTranslation);
            tempTranslation.translate(vec3f, temp);
        }

    }
    /**
     * 请使用这个方法来初始平移
     * @param matrix4f matrix4f
     */
    protected void applyTranslation(Matrix4f matrix4f) {
        tempTranslation.mul(matrix4f);
    }
    protected void applyScale(float x, float y, float z,boolean accumulable) {
        if (accumulable){
            Matrix4f temp=new Matrix4f(scale);
            scale.scale(x, y, z, temp);
        }
        else {
            Matrix4f temp=new Matrix4f(tempScale);
            tempScale.scale(x, y, z, temp);
        }
    }
    protected void applyScale(Vec3f vec3f,boolean accumulable) {
        applyScale(vec3f.get(0),vec3f.get(1),vec3f.get(2),accumulable);
    }

    /**
     * 执行变换
     * @param gl4
     */
    protected void applyTransformation(GL4 gl4, ShaderManager shaderManager) {
        Matrix4f model = new Matrix4f();
        model.mul(translation);
        model.mul(tempTranslation);
        model.mul(scale);
        model.mul(tempScale);
        model.mul(rotation);
        model.mul(tempRotation);
        shaderManager.useShaderProgram();
        shaderManager.setUniform("model", model);
    }
    /**
     * float[]ambient,float[]diffuse,float[]specular,float shininess
     * 构造Material
     * @return Material 材质
     * return new Material(
     *                new float[]{ 0.2f,0.2f,0.2f},
     *                new float[]{ 0.5f,0.5f,0.5f},
     *                new float[]{ 1.0f,1.0f,1.0f},
     *                32.0f
     *                );
     */
    protected abstract Material defineMaterial();
    /**
     * 管理所有继承自ProgrammableBase的类的绘制
     *
     */
    public static void happyDrawAll(GL4 gl4, ShaderManager shaderManager){
        for (ProgrammableBase programmableBase : list) {
            programmableBase.allReadyDraw(gl4,shaderManager);
        }
    }
    /**
     * 管理所有继承自ProgrammableBase的类的销毁
     */
    public static void destroyAll(GL4 gl4){
        for (ProgrammableBase programmableBase : list) {
            programmableBase.deleteBind(gl4);
        }
    }

}
