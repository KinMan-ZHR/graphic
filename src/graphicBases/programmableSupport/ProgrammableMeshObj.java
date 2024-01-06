package graphicBases.programmableSupport;

import assimp4j.Mesh;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.Vec3f;
import graphicBases.materialPack.Material;
import textureBinding.TextureTool;

import static com.jogamp.opengl.GL.GL_FLOAT;

/**
 * created by KinMan谨漫 on 2024/1/6/**
 *
 * @author KinMan谨漫
 * ,
 */
public abstract class ProgrammableMeshObj extends ProgrammableObject{
    Mesh mesh;
    protected float[] vertices;
    protected float[] textureCoords;
    protected float[] normals;

    public ProgrammableMeshObj(GLAutoDrawable glAutoDrawable, Vec3f position) {
        super(glAutoDrawable, position);
    }

    public ProgrammableMeshObj(GLAutoDrawable glAutoDrawable, float positionX, float positionY, float positionZ) {
        super(glAutoDrawable, positionX, positionY, positionZ);

    }
    protected void processMesh() {
        mesh=defineMesh();
        this.vertices=mesh.getVertices();
        this.textureCoords=mesh.getTextureCoords();
        this.normals=mesh.getNormals();

    }
    /**
     * 导入模型定义mesh
     * @return mesh
     * return Mesh.load("resource/meshModel/cottage/cottage_fbx.fbx", 0);
     */
    protected abstract Mesh defineMesh();
    /**
     * 抽象方法让子类实现自身纹理数据,如果不使用纹理,保持默认函数体即可
     * 若要使用纹理请执行以下句子
     * int[] texture=new int[2];
     * texture[0]= TextureTool.loadTexture(gl4,"./resource/texturePixel/dalishi.jpg",4, GL4.GL_REPEAT, GL4.GL_LINEAR, GL4.GL_LINEAR);
     * return texture;
     *
     * @param gl4 gl4
     * @return 纹理数组
     */
    @Override
    protected abstract int[] defineTexture(GL4 gl4);
    /**
     * 愉快的画画，用gl4.glDraw选择画什么即可,最好是随时会变的部分
     *    applyTranslation( position,false);
     *    applyTransformation(gl4, GameGLEventListener.objectShaderManager);
     *    gl4.glBindVertexArray(vao);
     *    gl4.glDrawElements(GL4.GL_TRIANGLES, indicesData.length, GL_UNSIGNED_INT, 0);
     *
     * @param gl4
     */
    @Override
    protected abstract void happyDraw(GL4 gl4);
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
    @Override
    protected abstract Material defineMaterial();

    /**
     * 请定义自己的顶点数据
     *
     * @return 顶点数据
     */
    @Override
    protected float[] getVertexData() {

        processMesh();
        float[] vertices=new float[this.vertices.length+this.normals.length+this.textureCoords.length];
        int i=0;
        for (float vertex : this.vertices) {
            vertices[i++] = vertex;
        }
        for (float normal : this.normals) {
            vertices[i++] = normal;
        }
        for (float textureCoord : this.textureCoords) {
            vertices[i++] = textureCoord;
        }
        return vertices;
    }

    /**
     * 抽象方法让子类实现自身索引数据,如果不使用EBO,保持默认函数体即可
     */
    @Override
    protected int[] getIndicesData() {
        return mesh.getIndices();
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
        // 位置属性
        gl4.glEnableVertexAttribArray(0); // 启用顶点属性数组
        gl4.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // 设置顶点属性数组的格式和位置
        // 法向量属性
        gl4.glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, (long) vertices.length *Float.BYTES);
        gl4.glEnableVertexAttribArray(3);
        // 纹理坐标属性
        gl4.glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, (long) (vertices.length + normals.length) *Float.BYTES);
        gl4.glEnableVertexAttribArray(2);

    }


}
