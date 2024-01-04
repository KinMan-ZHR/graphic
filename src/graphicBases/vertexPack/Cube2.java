package graphicBases.vertexPack;

import MathUtils.TransformTool;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import graphicBases.GameGLEventListener;
import graphicBases.materialPack.Material;
import textureBinding.TextureTool;

import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import com.jogamp.opengl.math.Matrix4f;

/**
 * created by KinMan谨漫 on 2024/1/3/**
 *
 * @author KinMan谨漫
 * ,
 */
public class Cube2 extends Cube{
    private final Matrix4f[] cubePositions;
    /**
     * 可编程管线基础类，想要轻松的画画，继承这个类，实现抽象方法即可
     *
     * @param glAutoDrawable glAutoDrawable
     */
    public Cube2(GLAutoDrawable glAutoDrawable) {
        super(glAutoDrawable);
        cubePositions=createInitPos();
        //打印cubePositions
//        for (Matrix4f cubePosition : cubePositions) {
//            System.out.println(cubePosition.toString());
//        }
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
         gl4.glVertexAttribPointer(0, 3, GL_FLOAT, false, 5*Float.BYTES, 0); // 设置顶点属性数组的格式和位置
         // 纹理坐标属性
         gl4.glVertexAttribPointer(2, 2, GL_FLOAT, false, 5*Float.BYTES, 3*Float.BYTES);
         gl4.glEnableVertexAttribArray(2);

    }

    /**
     * 愉快的画画，用gl4.glDraw选择画什么即可,最好是随时会变的部分
     *
     * @param gl4
     */
    @Override
    protected void happyDraw(GL4 gl4) {
        //System.out.println("texture"+texture.length);
        for (int i = 0; i < 10; i++) {
            applyTranslation(cubePositions[i]);
            applyRotation(getDeltaTime()*50.0f,0.5f,1.0f,0.0f,false);
            applyTransformation(gl4,GameGLEventListener.objectShaderManager);
            gl4.glBindVertexArray(vao);
            gl4.glDrawArrays(GL_TRIANGLES, 0, 36);
        }
//        applyTranslation(-0.5f,0.5f,0.0f,false);
//        applyRotation(getDeltaTime()*50.0f,0.5f,1.0f,0.0f,false);
//        applyTransformation(gl4);
//        gl4.glDrawArrays(GL_TRIANGLES, 0, 36);
    }

    /**
     * float[]ambient,float[]diffuse,float[]specular,float shininess
     * 构造Material
     *
     * @return Material 材质
     */
    @Override
    protected Material defineMaterial() {
        return null;
    }


    private Matrix4f[] createInitPos(){
        Matrix4f[] modelMatrices = new Matrix4f[10];
        for (int i = 0; i < 10; i++) {
            modelMatrices[i] = new Matrix4f();
        }
        modelMatrices[0].setToTranslation(0.0f,  0.0f,  0.0f);
        modelMatrices[1].setToTranslation(2.0f,  5.0f, -15.0f);
        modelMatrices[2].setToTranslation(-1.5f, -2.2f, -2.5f);
        modelMatrices[3].setToTranslation(-3.8f, -2.0f, -12.3f);
        modelMatrices[4].setToTranslation(2.4f, -0.4f, -3.5f);
        modelMatrices[5].setToTranslation(-1.7f,  3.0f, -7.5f);
        modelMatrices[6].setToTranslation(1.3f, -2.0f, -2.5f);
        modelMatrices[7].setToTranslation(1.5f,  2.0f, -2.5f);
        modelMatrices[8].setToTranslation(1.5f,  0.2f, -1.5f);
        modelMatrices[9].setToTranslation(-1.3f,  1.0f, -1.5f);
        return modelMatrices;
    }
}
