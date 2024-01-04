package textureBinding;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;

/**
 * created by KinMan谨漫 on 2024/1/3/**
 * 纹理工具类
 * 用于加载纹理图片到内存中，并绑定到纹理对象 loadImage(String imagePath) 返回ByteBuffer
 * 用于释放纹理对象 releaseTexture(GL4 gl, int textureID) 返回void
 * 用于绑定纹理对象 bindTexture(GL4 gl, ByteBuffer buffer, int width, int height) 返回int
 */
public class TextureTool {

    public static int loadTexture(GL4 gl, String imagePath, int format, int wrapMode, int minFilter, int magFilter) {
        //生成一个纹理
        int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        int texture = textures[0];
        //绑定这个纹理
        gl.glBindTexture(GL4.GL_TEXTURE_2D, texture);
        //设置纹理的环绕和过滤方式
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, wrapMode);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, wrapMode);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, minFilter);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, magFilter);
        //加载图像，创建纹理和生成mipmap
        // 使用ImageIO读取图片文件
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imagePath));
            // 获取图片的宽度和高度
            int width = image.getWidth();
            int height = image.getHeight();
            //使用ByteBuffer来存储图像数据
            ByteBuffer data = ByteBuffer.allocate(width * height * format);
            //循环遍历图像的像素并存入缓冲区
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int rgb = image.getRGB(j, i); //获取像素颜色
                    data.put((byte) ((rgb >> 16) & 0xFF)); //放入红色分量
                    data.put((byte) ((rgb >> 8) & 0xFF)); //放入绿色分量
                    data.put((byte) (rgb & 0xFF)); //放入蓝色分量
                    if (format == 4) { //如果格式是RGBA，还要放入透明度分量
                        data.put((byte) ((rgb >> 24) & 0xFF));
                    }
                }
            }
            data.flip(); //翻转缓冲区，准备传递给OpenGL
            //使用glTexImage2D来创建纹理
            gl.glTexImage2D(GL4.GL_TEXTURE_2D, 0, format == 3 ? GL4.GL_RGB : GL4.GL_RGBA, width, height, 0, format == 3 ? GL4.GL_RGB : GL4.GL_RGBA, GL4.GL_UNSIGNED_BYTE, data);
            //使用glGenerateMipmap来生成mipmap
            gl.glGenerateMipmap(GL4.GL_TEXTURE_2D);
            //返回纹理的标识符
        } catch (IOException e) {
            String curDir = System.getProperty("user.dir");
            System.out.println("当前工作目录为: " + curDir);
            throw new RuntimeException("Unable to load texture: " + imagePath);
        }
        return texture;
    }
    // 1.绑定纹理对象
    public static int bindTexture(GL4 gl, String imagePath) {
        // 使用ImageIO读取图片文件
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            String curDir = System.getProperty("user.dir");
            System.out.println("当前工作目录为: " + curDir);
            throw new RuntimeException("Unable to load texture: " + imagePath);
        }
        // 获取图片的宽度和高度
        int width = image.getWidth();
        int height = image.getHeight();
        // 创建一个字节缓冲区，用于存储图片的像素数据
        ByteBuffer buffer = GLBuffers.newDirectByteBuffer(width * height * 4);
        // 遍历图片的每个像素
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // 获取像素的ARGB值
                int pixel = image.getRGB(x, y);
                // 将ARGB值分解为四个字节，并存入缓冲区
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // R
                buffer.put((byte) ((pixel >> 8) & 0xFF));  // G
                buffer.put((byte) (pixel & 0xFF));         // B
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // A
            }
        }
        // 反转缓冲区，准备读取
        buffer.flip();
        int[] textureID = new int[1]; // 创建一个数组，用于存储纹理对象的句柄
        //第一个参数表示生成纹理的数量，第二个参数表示存储纹理对象的int数组，第三个参数表示存储纹理对象的int数组的起始位置
        gl.glGenTextures(1, textureID, 0);
        // 绑定纹理对象
        gl.glBindTexture(GL4.GL_TEXTURE_2D, textureID[0]);
        // 设置纹理参数
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, GL4.GL_REPEAT);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, GL4.GL_REPEAT);
        // 生成2D纹理
        gl.glTexImage2D(GL4.GL_TEXTURE_2D, 0, GL4.GL_RGBA, width, height, 0, GL4.GL_RGBA, GL4.GL_UNSIGNED_BYTE, buffer);
        // 生成多级渐远纹理
        gl.glGenerateMipmap(GL4.GL_TEXTURE_2D);
        // 返回纹理对象的ID
        return textureID[0];
    }
    
    // 2.释放纹理
    public static void releaseTexture(GL4 gl, int textureID) {
        // 删除纹理对象
        gl.glDeleteTextures(1, new int[]{textureID}, 0);
    }
}
