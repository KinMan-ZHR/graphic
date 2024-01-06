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
     * 可编程管线基础光源类，想要轻松的画画，继承这个类，实现抽象方法即可
     * 该类相较于ProgrammableBase类，仅仅是帮你屏蔽掉光源无须涉及的部分，但是提供了一个lightDraw方法，让你写入着色器
     * 若要定义一种新的光源，这里建议您使用继承的方式，实现lightDraw方法
     * 若要希望定义的新光源有自己的纹理材质，这里建议您使用继承的方式，重写defineTexture, useTexture方法
     * 建议在定义一个抽象类实现lightDraw方法，然后再定义实现类，实现自己的光源
     * 实现类的命名方式为：光源名+区别名,例如：PointLightPure
     * @param glAutoDrawable
     */

    public static ArrayList<ProgrammableLight> list=new ArrayList<>();
    public ProgrammableLight(GLAutoDrawable glAutoDrawable) {
        super(glAutoDrawable);
        list.add(this);
    }
//    /**
//     * 请定义自己的初始位置数据,如果使用的是方向光,请返回null
//     */
//    protected abstract Vec3f[] defineInitPos();



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
    /**
     * 画材质
     *
     * @param gl4
     */
    @Override
    public void materialDraw(GL4 gl4) {

    }


}
