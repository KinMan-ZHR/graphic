package graphicBases.lightsModel;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.Vec3f;
import graphicBases.GameGLEventListener;
import graphicBases.materialPack.Material;
import graphicBases.programmableSupport.ProgrammableLight;

/**
 * created by KinMan谨漫 on 2024/1/5/**
 *
 * @author KinMan谨漫
 * ,
 */
public abstract class DirLight extends ProgrammableLight {
    private static int count=0;
    protected Vec3f direction;
    /**
     * 平行光抽象类
     * 该类相较于ProgrammableLight类，会自动帮你处理光质,如要改变写入的着色器，请重写lightDraw方法
     * 若要定义材质纹理，这里建议您使用继承的方式，实现defineMaterial方法,相应的，如要改变请继承后重写defineMaterial方法
     * 若要定义顶点数据，这里建议您使用继承的方式，实现getVertexData方法,相应的，如要改变请继承后重写getVertexData方法
     * 实现类的命名方式为：光种+形状名+材质名+区别名/用途,例如：Cube_Gold_Pure
     * @param glAutoDrawable
     */

    public DirLight(GLAutoDrawable glAutoDrawable,Vec3f direction) {
        super(glAutoDrawable);
        this.direction=direction;
        count++;
    }
    public DirLight(GLAutoDrawable glAutoDrawable,float directionX,float directionY,float directionZ) {
        super(glAutoDrawable);
        this.direction=new Vec3f(directionX,directionY,directionZ);
        count++;
    }

    /**
     * @param gl4
     */
    @Override
    public void lightDraw(GL4 gl4) {
        for (int i = 0; i < count; i++) {
            GameGLEventListener.objectShaderManager.setUniform("dirLights[" + i + "].direction", direction);
            GameGLEventListener.objectShaderManager.setUniform("dirLights[" + i + "].ambient", material.getAmbient());
            GameGLEventListener.objectShaderManager.setUniform("dirLights[" + i + "].diffuse", material.getDiffuse());
            GameGLEventListener.objectShaderManager.setUniform("dirLights[" + i + "].specular", material.getSpecular());
        }

    }
}
