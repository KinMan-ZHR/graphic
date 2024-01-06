//package graphicBases.support;
//
//import assimp4j.Mesh;
//import com.jogamp.opengl.GL;
//import com.jogamp.opengl.GL4;
//import com.jogamp.opengl.GLAutoDrawable;
//import com.jogamp.opengl.math.Matrix4f;
//import graphicBases.programmableSupport.ProgrammableBase;
//import standardAttribute.Texture;
//import standardAttribute.Vertex;
//
//import java.nio.FloatBuffer;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Vector;
//
//import static com.jogamp.opengl.GL.GL_ELEMENT_ARRAY_BUFFER;
//import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
//
///**
// * created by KinMan谨漫 on 2024/1/5/**
// *
// * @author KinMan谨漫
// * ,
// */
//public abstract class Model {
//    private static final HashMap<Class<?>, Integer> vboBuffer = new HashMap<>();
//    private static final HashMap<Class<?>, Integer> eboBuffer = new HashMap<>();
//    protected final int vao; // 顶点数组对象的句柄
//    //public Vector<Texture> textures_loaded = new Vector<>();
//    public Vector<Mesh> meshes = new Vector<>();
//    public Vector<Vertex> vertices = new Vector<>();
//    public Vector<Integer> indices = new Vector<>();
//    public Vector<Texture> textures = new Vector<>();
//    // 获取当前的纳秒时间
//    private final static long start=System.nanoTime();;
//    private final Matrix4f rotation=new Matrix4f();
//    private final Matrix4f translation=new Matrix4f();
//    private final Matrix4f scale=new Matrix4f();
//    private Matrix4f tempRotation=new Matrix4f();
//    private Matrix4f tempTranslation=new Matrix4f();
//    private Matrix4f tempScale=new Matrix4f();
//    public static final ArrayList<Model> list=new ArrayList<>();
//    public Model(GLAutoDrawable glAutoDrawable) {
//        vertices=defineVertices();
//        indices=defineIndices();
//        textures=defineTextures();
//        GL4 gl4 = glAutoDrawable.getGL().getGL4(); // 获取OpenGL的对象
//        list.add(this);//将自己加入到list中
//        //尚未创建vbo
//        if (vboBuffer.get(this.getClass()) == null) {
//            //todo 顶点缓冲对象启动！
//            int[] vboIds = new int[1]; // 创建一个数组，用于存储顶点缓冲对象的句柄
//            gl4.glGenBuffers(1, vboIds, 0); // 生成一个顶点缓冲对象,将索引存放在vboIds[0]中
//            // 顶点缓冲对象的句柄
//            int vbo = vboIds[0]; // 获取顶点缓冲对象的句柄
//            float[] VERTEX_DATA = getVertexData();
//            //GL.GL_ARRAY_BUFFER表示该VBO用于存储顶点属性，如位置、颜色、纹理坐标等。
//            gl4.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo); // 绑定顶点缓冲对象到GL_ARRAY_BUFFER目标
//            //todo 将cpu中的数据复制到gpu中
//            gl4.glBufferData(GL.GL_ARRAY_BUFFER, (long) VERTEX_DATA.length * Float.BYTES, FloatBuffer.wrap(VERTEX_DATA), GL_STATIC_DRAW); // 将顶点数据存储到显存中
//            //vboBuffer.getOrDefault(this.getClass(), 0);
//            vboBuffer.put(this.getClass(), vbo); // 将顶点缓冲对象的句柄存储到哈希表中
////            System.out.println("Class = " + this.getClass().getName() + "创建vbo");
////            System.out.println("vboBuffer = " + vboBuffer.get(this.getClass()));
//        }else {
////            System.out.println("Class = " + this.getClass().getName() + "使用以前vbo");
////            System.out.println("vboBuffer = " + vboBuffer.get(this.getClass()));
//            //已经创建vbo
//            gl4.glBindBuffer(GL.GL_ARRAY_BUFFER, vboBuffer.get(this.getClass())); // 绑定顶点缓冲对象到GL_ARRAY_BUFFER目标
//        }
//
//        //System.out.println("vbo = " + vbo);
//        //todo 顶点数组对象启动！
//        int[] vaoIds = new int[1]; // 创建一个数组，用于存储顶点数组对象的句柄
//        gl4.glGenVertexArrays(1, vaoIds, 0); // 生成一个顶点数组对象
//        vao = vaoIds[0]; // 获取顶点数组对象的句柄
//        gl4.glBindVertexArray(vao); // 绑定顶点数组对象
//        //todo 判断是否使用EBO
//        indicesData=getIndicesData();
//        if (indicesData.length!=0){
//            useEBO(gl4);
//        }
//        //判断是否使用纹理
//        useTexture(gl4);
//        //使用材质
//        if (defineMaterial()!=null){
//            material=defineMaterial();
//        }
//        initDraw(gl4);
//    }
//
//    private Vector<Texture> defineTextures() {
//    }
//
//    private Vector<Integer> defineIndices() {
//    }
//
//    private Vector<Vertex> defineVertices() {
//    }
//
//    /**
//     * 管理所有继承自ProgrammableBase的类的绘制
//     *
//     */
//    public static void happyDrawAll(GL4 gl4){
//        for (Model model : list) {
//            model.allReadyDraw(gl4);
//        }
//    }
//    /**
//     * 管理所有继承自ProgrammableBase的类的销毁
//     */
//    public static void destroyAll(GL4 gl4){
//        for (Model model  : list) {
//            model.deleteBind(gl4);
//        }
//    }
//    protected abstract void initDraw(GL4 gl4);
//    protected void allReadyDraw(GL4 gl4){
//        materialDraw(gl4);
//        //清空所有的临时矩阵，以保证为帧内可累加，帧与帧之间不累加
//        tempRotation=new Matrix4f();
//        tempTranslation=new Matrix4f();
//        tempScale=new Matrix4f();
//        happyDraw(gl4);
//    }
//    /**
//     * 画材质
//     * @param gl4
//     */
//    public abstract void materialDraw(GL4 gl4);
//    /**
//     *  愉快的画画，用gl4.glDraw选择画什么即可,最好是随时会变的部分
//     *      Matrix4f model = new Matrix4f();
//     *      GameGLEventListener.objectShaderManager.setUniform("model", model);
//     *      gl4.glDrawArrays(GL_TRIANGLES, 0, 3); // 绘制三角形
//     */
//    protected abstract void happyDraw(GL4 gl4);
//    protected void deleteBind(GL4 gl4){
//        gl4.glBindVertexArray(0); // 解绑顶点数组对象
//        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER, 0); // 解绑顶点缓冲对象
//        gl4.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0); // 解绑索引缓冲对象
//    }
//}
