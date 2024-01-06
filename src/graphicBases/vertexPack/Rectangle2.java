package graphicBases.vertexPack;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import graphicBases.GameGLEventListener;
import graphicBases.materialPack.Material;
import graphicBases.programmableSupport.ProgrammableObject;
import shaderControl.ShaderManager;
import textureBinding.TextureTool;

import static com.jogamp.opengl.GL.*;

/**
 * created by KinMan谨漫 on 2024/1/1/**
 *
 * @author KinMan谨漫
 * ,
 */
public class Rectangle2 extends ProgrammableObject {



    public Rectangle2(GLAutoDrawable glAutoDrawable) {
        super(glAutoDrawable);
    }

    @Override
    protected float[] getVertexData() {
        return new float[]{
                //     ---- 位置 ----       ---- 颜色 ----     - 纹理坐标 -
                0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f,   // 右上
                0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f,   // 右下
                -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f,   // 左下
                -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f    // 左上
        };
    }

    @Override
    protected int[] getIndicesData() {
        return new int[]{
                // 注意索引从0开始!
                // 此例的索引(0,1,2,3)就是顶点数组vertices的下标，
                // 这样可以由下标代表顶点组合成矩形
                0, 1, 3, // 第一个三角形
                1, 2, 3  // 第二个三角形;
        };
    }

    /**
     * 若要使用纹理请执行以下句子
     *  int[] texture=new int[2];
     *  texture[0]= TextureTool.loadTexture(gl4,"./resource/texturePixel/dalishi.jpg",4, GL4.GL_REPEAT, GL4.GL_LINEAR, GL4.GL_LINEAR);
     *  texture[1]= TextureTool.bindTexture(gl4,"./resource/texturePixel/awesomeface.png");
     *  return texture;
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

    @Override
    protected void initDraw(GL4 gl4) {
        // 位置属性
        gl4.glEnableVertexAttribArray(0); // 启用顶点属性数组
        gl4.glVertexAttribPointer(0, 3, GL_FLOAT, false, 8*Float.BYTES, 0); // 设置顶点属性数组的格式和位置
        // 颜色属性
        gl4.glVertexAttribPointer(1, 3, GL_FLOAT, false, 8*Float.BYTES, 3*Float.BYTES);
        gl4.glEnableVertexAttribArray(1);
        // 纹理坐标属性
        gl4.glVertexAttribPointer(2, 2, GL_FLOAT, false, 8*Float.BYTES, 6*Float.BYTES);
        gl4.glEnableVertexAttribArray(2);

//        System.out.println(transform);
        //TransformTool.rotateXYZ(transform,0.0f,0.0f,90.0f);//逆时针旋转90度
//        TransformTool.scale(transform,0.5f,0.5f,0.5f);
//        System.out.println(transform);


    }

    @Override
    public void happyDraw(GL4 gl4, ShaderManager shaderManager) {
        //gl4.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);//线框模式
        //gl4.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);//填充模式,默认
        applyTranslation(0.5f,-0.5f,0.0f,false);
        applyRotation(-55.0f,0.0f,0.0f,0.0f,false);
        applyTransformation(gl4,shaderManager);
        shaderManager.setUniform("view", GameGLEventListener.camera.view);
        shaderManager.setUniform("projection", GameGLEventListener.camera.projection);
        gl4.glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
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
}
