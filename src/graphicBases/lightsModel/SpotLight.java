package graphicBases.lightsModel;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.Vec3f;
import graphicBases.GameGLEventListener;
import graphicBases.materialPack.Material;
import graphicBases.programmableSupport.ProgrammableLight;

/**
 * created by KinMan谨漫 on 2024/1/4/**
 *
 * @author KinMan谨漫
 * ,
 */
public abstract class SpotLight extends ProgrammableLight {

    protected float cutOff;
    protected float outerCutOff;
    protected float  constant;
    protected float linear;
    protected float quadratic;
    private static int count=0;
    protected Vec3f direction;
    /**
     * 聚光灯抽象类
     * 该类相较于ProgrammableLight类，会自动帮你处理光质,如要改变写入的着色器，请重写lightDraw方法
     * 若要定义材质纹理，这里建议您使用继承的方式，实现defineMaterial方法,相应的，如要改变请继承后重写defineMaterial方法
     * 若要定义顶点数据，这里建议您使用继承的方式，实现getVertexData方法,相应的，如要改变请继承后重写getVertexData方法
     * 实现类的命名方式为：形状名+材质名+区别名/或者用途,例如：Cube_Gold_Pure
     * @param glAutoDrawable
     */

    public SpotLight(GLAutoDrawable glAutoDrawable, float x, float y, float z,Vec3f direction) {
        super(glAutoDrawable);
        processCutoff();
        processAttenuation();
        position.set(x,y,z);
        this.direction=direction;
        count++;
    }
    public SpotLight(GLAutoDrawable glAutoDrawable, Vec3f position,Vec3f direction) {
        super(glAutoDrawable);
        processCutoff();
        processAttenuation();
        this.position=position;
        this.direction=direction;
        count++;
    }

    /**
     * @param gl4
     */
    @Override
    public void lightDraw(GL4 gl4) {
        for (int i = 0; i < count; i++) {
            GameGLEventListener.objectShaderManager.setUniform("spotLight["+i+"].position",position);
            GameGLEventListener.objectShaderManager.setUniform("spotLight["+i+"].direction",direction);
            GameGLEventListener.objectShaderManager.setUniform("spotLight["+i+"].ambient",material.getAmbient());
            GameGLEventListener.objectShaderManager.setUniform("spotLight["+i+"].diffuse",material.getDiffuse());
            GameGLEventListener.objectShaderManager.setUniform("spotLight["+i+"].specular",material.getSpecular());
            GameGLEventListener.objectShaderManager.setUniform("spotLight["+i+"].constant",constant);
            GameGLEventListener.objectShaderManager.setUniform("spotLight["+i+"].linear",linear);
            GameGLEventListener.objectShaderManager.setUniform("spotLight["+i+"].quadratic",quadratic);
            GameGLEventListener.objectShaderManager.setUniform("spotLight["+i+"].cutOff",cutOff);
            GameGLEventListener.objectShaderManager.setUniform("spotLight["+i+"].outerCutOff",outerCutOff);
        }




    }
    private void processAttenuation(){
        float[] attenuation=defineAttenuation();
        constant=attenuation[0];
        linear=attenuation[1];
        quadratic=attenuation[2];
    }
    /**
     * return new float[]{constant,linear,quadratic};
     * @return
     */
    protected abstract float[] defineAttenuation();
    private void processCutoff(){
        float[] temp=defineCutOff();
        cutOff=(float) Math.cos( Math.toRadians(temp[0]));
        outerCutOff=(float) Math.cos( Math.toRadians(temp[1]));
    }
    /**
     *
     * return new float[]{cutOffAngle,outerCutOffAngle};
     * @return
     */
    protected abstract float[] defineCutOff();


}
