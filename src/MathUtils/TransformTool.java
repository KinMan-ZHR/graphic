package MathUtils;

import com.jogamp.opengl.math.Matrix4f;

public class TransformTool {

    public static void main(String[] args) {
        // 创建一个单位矩阵
        Matrix4f matrix = new Matrix4f();
        // 打印矩阵的初始值
        System.out.println(matrix);
        // 创建一个临时矩阵temp,深拷贝原矩阵matrix
        Matrix4f temp = new Matrix4f(matrix);

        // 使用scale方法对矩阵进行缩放变换，参数分别表示在x，y，z轴上的缩放比例
        matrix.scale(2.0f, 3.0f, 1.0f, temp);
        // 打印矩阵的缩放后的值
        System.out.println("放缩后的矩阵"+matrix);
        Matrix4f temp3 = new Matrix4f();
        temp3.translate(1.0f, 2.0f, 0.0f,new Matrix4f());
        // 打印矩阵的平移后的值
        System.out.println("平移后的矩阵"+temp3);
        temp3.mul(matrix);//先放缩后平移
        System.out.println("乘法之后"+temp3);
        System.out.println("乘法之后"+matrix);
        matrix.set(4,5);
        System.out.println("设置之后"+matrix);

    }

    //方法类
    //平移矩阵
    public static void translate(Matrix4f matrix4f,float x, float y, float z) {
        matrix4f.setToTranslation(x, y, z);
    }
    private static void rotateZ(Matrix4f matrix4f, float theta) {
        matrix4f.rotate((float) Math.toRadians(theta), 0.0f, 0.0f, 1.0f,new Matrix4f());
    }
    private static void rotateY(Matrix4f matrix4f, float theta) {
        matrix4f.rotate((float) Math.toRadians(theta), 0.0f, 1.0f, 0.0f,new Matrix4f());
    }
    private static void rotateX(Matrix4f matrix4f, float theta) {
        matrix4f.rotate((float) Math.toRadians(theta), 1.0f, 0.0f, 0.0f,new Matrix4f());
    }
    public static void rotateXYZ(Matrix4f matrix4f, float xTheta, float yTheta, float zTheta) {
        if(xTheta!=0.0f){
            rotateX(matrix4f,xTheta);
        }
        if(zTheta!=0.0f){
            rotateZ(matrix4f,zTheta);
        }
        if(yTheta!=0.0f){
            rotateY(matrix4f,yTheta);
        }
    }
    public static void rotateAxis(Matrix4f matrix4f, float theta,float x, float y, float z) {
        matrix4f.setToRotationAxis((float) Math.toRadians(theta), x, y, z);
    }

    //旋转变换矩阵
    public static Matrix4f rotateMatrixTransform(Matrix4f matrix, float x, float y, float z, float theta) {
        //matrix是原始矩阵，x,y,z是旋转轴的方向向量，theta是旋转角度（弧度制）
        //这里的顺逆时针是根据旋转轴方向向量的方向来判断的，如果旋转轴方向向量是从z轴正方向指向x轴正方向，那么就是顺时针旋转
        //此处的旋转轴方向向量是从z轴正方向指向x轴正方向，所以是顺时针旋转
        Matrix4f result = new Matrix4f();
        float c = (float) Math.cos(theta); //旋转角度的余弦值
        float s = (float) Math.sin(theta); //旋转角度的正弦值
        float k = 1 - c; //旋转角度的余弦值的补数
        //计算旋转变换矩阵的元素
        float r11 = x * x * k + c;
        float r12 = x * y * k - z * s;
        float r13 = x * z * k + y * s;
        float r21 = x * y * k + z * s;
        float r22 = y * y * k + c;
        float r23 = y * z * k - x * s;
        float r31 = x * z * k - y * s;
        float r32 = y * z * k + x * s;
        float r33 = z * z * k + c;
        //用旋转变换矩阵乘以原始矩阵，得到结果矩阵
        for (int i = 0; i < 4; i++) {
            result.set(i,r11 * matrix.get(i) + r12 * matrix.get(i+4) + r13 * matrix.get(i+8));
            result.set(i+4,r21 * matrix.get(i) + r22 * matrix.get(i+4) + r23 * matrix.get(i+8));
            result.set(i+8,r31 * matrix.get(i) + r32 * matrix.get(i+4) + r33 * matrix.get(i+8));
        }
        matrix = result;
        return matrix;
    }
    //缩放矩阵
    public static void scale(Matrix4f matrix4f,float x, float y, float z) {
        matrix4f.setToScale(x, y, z);
    }



}
