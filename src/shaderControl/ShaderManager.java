package shaderControl;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.math.Matrix4f;
import com.jogamp.opengl.math.Vec3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * created by KinMan谨漫 on 2024/1/2/**
 *
 * @author KinMan谨漫
 * ,
 */
public class ShaderManager {
    public int shaderProgram; // 着色器程序的句柄
    public GL4 gl;
    public ShaderManager(GL4 gl,String vertexShaderPath, String fragmentShaderPath) {
        this.gl=gl;
        // 从文件中读取顶点着色器代码
        String VERTEX_SHADER = readFile(vertexShaderPath);
        // 从文件中读取片元着色器代码
        String FRAGMENT_SHADER = readFile(fragmentShaderPath);
        //todo 着色器程序启动！
         shaderProgram = createShaderProgram(gl, VERTEX_SHADER, FRAGMENT_SHADER); // 创建着色器程序
    }
    public static String readFile(String fileName) {
        //String curDir = System.getProperty("user.dir");
        //System.out.println("当前工作目录为: " + curDir);
        // 创建一个 StringBuilder 对象，用于存储读取的内容
        StringBuilder shaderCode = new StringBuilder();
        try {
            // 创建一个 BufferedReader 对象，用于读取文件
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            // 逐行读取文件内容，并追加到 StringBuilder 对象中
            String line;
            while ((line = reader.readLine()) != null) {
                shaderCode.append(line).append("\n");
            }
            // 关闭 BufferedReader 对象
            reader.close();
        } catch (IOException e) {
            // 如果发生异常，打印错误信息
            e.printStackTrace();
        }
        // 返回 StringBuilder 对象转换的字符串
       // System.out.println(shaderCode.toString());
        return shaderCode.toString();
    }
    // 编译和链接着色器程序的方法
    private int createShaderProgram(GL4 gl, String vertexShader, String fragmentShader) {
        // 创建一个顶点着色器对象
        int vertexShaderId = gl.glCreateShader(GL4.GL_VERTEX_SHADER); // 创建一个顶点着色器对象
        gl.glShaderSource(vertexShaderId, 1, new String[]{vertexShader}, null,0); // 设置顶点着色器的源代码
        gl.glCompileShader(vertexShaderId); // 编译顶点着色器

         // 检查编译是否成功
        int[] success = new int[1];
        gl.glGetShaderiv(vertexShaderId, GL4.GL_COMPILE_STATUS, success, 0);
        if (success[0] == GL4.GL_FALSE) {
            // 如果编译失败，打印错误信息
            int[] logLength = new int[1];
            gl.glGetShaderiv(vertexShaderId, GL4.GL_INFO_LOG_LENGTH, logLength, 0);
            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(vertexShaderId, logLength[0], null, 0, log, 0);
            System.out.println("Vertex shader compilation failed:\n" + new String(log));
        }
        // 创建一个片元着色器对象
        int fragmentShaderId = gl.glCreateShader(GL4.GL_FRAGMENT_SHADER); // 创建一个片元着色器对象
        gl.glShaderSource(fragmentShaderId, 1, new String[]{fragmentShader}, null); // 设置片元着色器的源代码
        gl.glCompileShader(fragmentShaderId); // 编译片元着色器
        // 检查编译是否成功
        gl.glGetShaderiv(fragmentShaderId, GL4.GL_COMPILE_STATUS, success, 0);
        if (success[0] == GL4.GL_FALSE) {
            // 如果编译失败，打印错误信息
            int[] logLength = new int[1];
            gl.glGetShaderiv(fragmentShaderId, GL4.GL_INFO_LOG_LENGTH, logLength, 0);
            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(fragmentShaderId, logLength[0], null, 0, log, 0);
            System.out.println("Fragment shader compilation failed:\n" + new String(log));
        }

        int shaderProgramId = gl.glCreateProgram(); // 创建一个着色器程序对象
        gl.glAttachShader(shaderProgramId, vertexShaderId); // 将顶点着色器附加到着色器程序
        gl.glAttachShader(shaderProgramId, fragmentShaderId); // 将片元着色器附加到着色器程序
        gl.glLinkProgram(shaderProgramId); // 链接着色器程序
        gl.glDeleteShader(vertexShaderId); // 删除顶点着色器对象
        gl.glDeleteShader(fragmentShaderId); // 删除片元着色器对象

        // 检查链接是否成功
        gl.glGetProgramiv(shaderProgramId, GL4.GL_LINK_STATUS, success, 0);
        if (success[0] == GL4.GL_FALSE) {
            // 如果链接失败，打印错误信息
            int[] logLength = new int[1];
            gl.glGetProgramiv(shaderProgramId, GL4.GL_INFO_LOG_LENGTH, logLength, 0);
            byte[] log = new byte[logLength[0]];
            gl.glGetProgramInfoLog(shaderProgramId, logLength[0], null, 0, log, 0);
            System.out.println("Shader program linking failed:\n" + new String(log));
        }
        return shaderProgramId; // 返回着色器程序的句柄
    }
    public void useShaderProgram(){
        gl.glUseProgram(shaderProgram); // 使用着色器程序
    }
    public void unUseShaderProgram(){
        gl.glUseProgram(0); // 使用着色器程序
    }
    public void deleteShaderProgram(){
        gl.glDeleteProgram(shaderProgram); // 删除着色器程序
    }
    /**
     * 设置uniform变量
     * @param name uniform变量名
     * @param value uniform变量值
     */
    public <T> void setUniform(String name, T value) {
        int vertexColorLocation = gl.glGetUniformLocation(shaderProgram, name);
        if (value instanceof Integer) {
            gl.glUniform1i(vertexColorLocation, (Integer) value);
        } else if (value instanceof Float) {
            gl.glUniform1f(vertexColorLocation, (Float) value);
        } else if (value instanceof float[] array) {
            if (array.length == 3) {
                gl.glUniform3f(vertexColorLocation, array[0], array[1], array[2]);
            } else if (array.length == 4) {
                gl.glUniform4f(vertexColorLocation, array[0], array[1], array[2], array[3]);
            } else  if (array.length == 16) {
                gl.glUniformMatrix4fv(vertexColorLocation, 1, false, array, 0);
            } else{
                throw new IllegalArgumentException("Invalid array length");
            }
        }else if(value instanceof Matrix4f matrix4f) {
            gl.glUniformMatrix4fv(vertexColorLocation, 1, false, matrix4f.get(new float[16]), 0);
        }else if(value instanceof Vec3f vec3f) {
            gl.glUniform3f(vertexColorLocation, vec3f.get(0), vec3f.get(1), vec3f.get(2));
        }
        else {
            throw new IllegalArgumentException("Invalid value type");
        }
    }

    /**
     * 设置uniform变量
     * @param name uniform变量名
     * @param value uniform变量值
     */
    public void setUniformMatrix4fv(String name,float[] value) {
        int vertexColorLocation = gl.glGetUniformLocation(shaderProgram, name);
        gl.glUniformMatrix4fv(vertexColorLocation, 1, false, value, 0);
    }
}
