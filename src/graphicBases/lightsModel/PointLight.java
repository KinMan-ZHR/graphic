package graphicBases.lightsModel;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.math.Vec3f;
import graphicBases.GameGLEventListener;
import graphicBases.programmableSupport.ProgrammableLight;
import shaderControl.ShaderManager;

/**
 * created by KinMan谨漫 on 2024/1/4/**
 *
 * @author KinMan谨漫
 * ,
 */
public abstract class PointLight extends ProgrammableLight {
    private static int count=0;
    protected float  constant;
    protected float linear;
    protected float quadratic;
    /**
     * 点光源抽象类
     * 该类相较于ProgrammableLight类，会自动帮你处理光质,如要改变写入的着色器，请重写lightDraw方法
     * 若要定义材质纹理，这里建议您使用继承的方式，实现defineMaterial方法,相应的，如要改变请继承后重写defineMaterial方法
     * 若要定义顶点数据，这里建议您使用继承的方式，实现getVertexData方法,相应的，如要改变请继承后重写getVertexData方法
     * 实现类的命名方式为：形状名+材质名+区别名,例如：Cube_Gold_Pure
     * @param glAutoDrawable
     */

    public PointLight(GLAutoDrawable glAutoDrawable, float x, float y, float z) {
        super(glAutoDrawable);
        processAttenuation();
        position.set(x,y,z);
        count++;
    }
    public PointLight(GLAutoDrawable glAutoDrawable, Vec3f position) {
        super(glAutoDrawable);
        processAttenuation();
        this.position=position;
        count++;
    }

    /**
     * @param gl4
     * @param shaderManager
     */
    @Override
    public void lightDraw(GL4 gl4, ShaderManager shaderManager) {
        {
            for (int i = 0; i < count; i++) {
                shaderManager.setUniform("pointLights[" + i + "].position", position);
                shaderManager.setUniform("pointLights[" + i + "].ambient", material.getAmbient());
                shaderManager.setUniform("pointLights[" + i + "].diffuse", material.getDiffuse());
                shaderManager.setUniform("pointLights[" + i + "].specular", material.getSpecular());
                shaderManager.setUniform("pointLights[" + i + "].constant", constant);
                shaderManager.setUniform("pointLights[" + i + "].linear", linear);
                shaderManager.setUniform("pointLights[" + i + "].quadratic", quadratic);
            }
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

}
