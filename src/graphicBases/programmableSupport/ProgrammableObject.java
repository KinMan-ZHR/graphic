package graphicBases.programmableSupport;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
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
     * 可编程管线基础类，想要轻松的画画，继承这个类，实现抽象方法即可
     *
     * @param glAutoDrawable
     */
    public ProgrammableObject(GLAutoDrawable glAutoDrawable) {
        super(glAutoDrawable);
    }


    /**
     * @param gl4
     */
    @Override
    public void materialDraw(GL4 gl4) {
        if (texture!=null){
            for (int i = 0; i < texture.length; i++) {
                gl4.glActiveTexture(GL_TEXTURE0+i);
                gl4.glBindTexture(GL_TEXTURE_2D, texture[i]);
            }
        }
        GameGLEventListener.objectShaderManager.useShaderProgram();
        GameGLEventListener.objectShaderManager.setUniform("material.ambient",material.getAmbient());
        GameGLEventListener.objectShaderManager.setUniform("material.diffuse",material.getDiffuse());
        GameGLEventListener.objectShaderManager.setUniform("material.specular",material.getSpecular());
        GameGLEventListener.objectShaderManager.setUniform("material.shininess",material.getShininess());

    }
    @Override
    protected void useTexture(GL4 gl4)
    {
        //todo 判断是否使用纹理
        texture= defineTexture(gl4);
        if(texture != null){
            System.out.println("texture.length = " + texture.length);
            GameGLEventListener.objectShaderManager.useShaderProgram();
            GameGLEventListener.objectShaderManager.setUniform("material.textureNum",texture.length);
            if (texture.length!=0){
                for (int i = 0; i < texture.length; i++) {
                    GameGLEventListener.objectShaderManager.setUniform("material.texture"+i,i);
                }
            }
        }
    }




}
