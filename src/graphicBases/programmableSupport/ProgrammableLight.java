package graphicBases.programmableSupport;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;

import java.util.ArrayList;

/**
 * created by KinMan谨漫 on 2024/1/4/**
 *
 * @author KinMan谨漫
 * ,
 */
public abstract class ProgrammableLight extends ProgrammableBase{
    /**
     * 可编程管线基础类，想要轻松的画画，继承这个类，实现抽象方法即可
     *
     * @param glAutoDrawable
     */
    protected float[] lightPos;
    protected float  constant;
    protected float linear;
    protected float quadratic;
    public static ArrayList<ProgrammableLight> list=new ArrayList<>();
    public ProgrammableLight(GLAutoDrawable glAutoDrawable,float x,float y,float z,float constant,float linear,float quadratic) {
        super(glAutoDrawable);
        lightPos = new float[]{x, y, z};
        this.constant=constant;
        this.linear=linear;
        this.quadratic=quadratic;
        list.add(this);
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
        return new int[0];
    }

    /**
     * @param gl4
     */
    @Override
    protected void useTexture(GL4 gl4) {

    }

    public abstract void lightDraw(GL4 gl4 );
    /**
     * 管理所有继承自ProgrammableLight的类的绘制
     *
     */
    public static void lightDrawAll(GL4 gl4) {
        for (ProgrammableLight programmableLight : list) {
            programmableLight.lightDraw(gl4);
        }
    }


}
