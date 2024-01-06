package graphicBases.programmableSupport;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.Vec3f;
import graphicBases.GameGLEventListener;

import static com.jogamp.opengl.GL.GL_TEXTURE0;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;

/**
 * created by KinMan谨漫 on 2024/1/4/**
 *
 * @author KinMan谨漫
 * ,
 */
public abstract class ProgrammableObject extends ProgrammableBase{


    /**
     * 可编程管线基础物体类，想要轻松的画画，继承这个类，实现抽象方法即可
     * 该类相较于ProgrammableBase类，会自动帮你处理材质和纹理,如要改变写入的着色器，请重写materialDraw方法
     * 若要定义材质纹理，这里建议您使用继承的方式，实现defineMaterial,defineTexture方法
     * 若要定义顶点数据，这里建议您使用继承的方式，实现getVertexData方法
     * 若要让形状不变仅改变材质纹理，这里建议您使用继承的方式，重写defineMaterial,defineTexture方法
     * 实现类的命名方式为：形状名+材质名+区别名,例如：BoxGoldPure
     * @param glAutoDrawable
     */
    public ProgrammableObject(GLAutoDrawable glAutoDrawable) {
        super(glAutoDrawable);
    }
    public ProgrammableObject(GLAutoDrawable glAutoDrawable, Vec3f position) {
        super(glAutoDrawable);
        this.position=position;
    }
    public ProgrammableObject(GLAutoDrawable glAutoDrawable, float positionX, float positionY, float positionZ) {
        super(glAutoDrawable);
        this.position.set(positionX,positionY,positionZ);
    }


    /**
     * 一般而言画材质是物体该做的事情，但是如果你想要改变材质，这里建议您使用继承的方式，重写materialDraw方法
     GameGLEventListener.objectShaderManager.useShaderProgram();
     GameGLEventListener.objectShaderManager.setUniform("material.textureNum",texture.length);
     for (int i = 0; i < texture.length; i++) {
     gl4.glActiveTexture(GL_TEXTURE0+i);
     gl4.glBindTexture(GL_TEXTURE_2D, texture[i]);
     GameGLEventListener.objectShaderManager.setUniform("material.texture"+i,i);
     }
     GameGLEventListener.objectShaderManager.setUniform("material.ambient",material.getAmbient());
     GameGLEventListener.objectShaderManager.setUniform("material.diffuse",material.getDiffuse());
     GameGLEventListener.objectShaderManager.setUniform("material.specular",material.getSpecular());
     GameGLEventListener.objectShaderManager.setUniform("material.shininess",material.getShininess());
     * @param gl4
     */
    @Override
    public void materialDraw(GL4 gl4) {
        GameGLEventListener.objectShaderManager.useShaderProgram();
        GameGLEventListener.objectShaderManager.setUniform("material.textureNum",texture.length);
        for (int i = 0; i < texture.length; i++) {
            gl4.glActiveTexture(GL_TEXTURE0+i);
            gl4.glBindTexture(GL_TEXTURE_2D, texture[i]);
            GameGLEventListener.objectShaderManager.setUniform("material.texture"+i,i);
        }

        GameGLEventListener.objectShaderManager.setUniform("material.ambient",material.getAmbient());
        GameGLEventListener.objectShaderManager.setUniform("material.diffuse",material.getDiffuse());
        GameGLEventListener.objectShaderManager.setUniform("material.specular",material.getSpecular());
        GameGLEventListener.objectShaderManager.setUniform("material.shininess",material.getShininess());

    }
    @Override
    protected void useTexture(GL4 gl4)
    {
        //System.out.println("texture.length = " + texture.length);

    }




}
