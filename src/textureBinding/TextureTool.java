package textureBinding;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Vector;
import javax.imageio.ImageIO;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;

/**
 * created by KinMan谨漫 on 2024/1/3/**
 * 纹理工具类
 * 用于加载纹理图片到内存中，并绑定到纹理对象 loadImage(String imagePath) 返回ByteBuffer
 * 用于释放纹理对象 releaseTexture(GL4 gl, int textureID) 返回void
 * 用于绑定纹理对象 bindTexture(GL4 gl, ByteBuffer buffer, int width, int height) 返回int
 * 会全局控制所有的纹理对象，在加载时会先判断是否已经加载过，如果加载过则直接返回纹理对象的标识符
 */
public class TextureTool {
    // 用于存储所有纹理对象的标识符
    public static HashMap<String, Integer> textureMap = new HashMap<>();

    public static int loadTexture(GL4 gl, String imagePath, int format, int wrapMode, int minFilter, int magFilter) {
        //如果已经加载过这个纹理，直接返回纹理的标识符
        if (textureMap.containsKey(imagePath)) {
            return textureMap.get(imagePath);
        }
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
        //将纹理标识符和纹理路径存入map中
        textureMap.put(imagePath, texture);
        return texture;
    }
    // 1.绑定纹理对象
    public static int bindTexture(GL4 gl, String imagePath) {
       return loadTexture(gl, imagePath, 4, GL4.GL_REPEAT, GL4.GL_LINEAR, GL4.GL_LINEAR);
    }
    
    // 2.释放纹理
    public static void releaseTexture(GL4 gl, int textureID) {
        // 删除纹理对象
        gl.glDeleteTextures(1, new int[]{textureID}, 0);
    }
}
