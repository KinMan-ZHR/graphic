import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import graphicBases.GameGLEventListener;
import interaction.GameKeyListener;
import interaction.GameMouseListener;

import javax.swing.*;
import java.awt.*;

// 单例模式
public class GameGLCanvas {
    // 定义一些常量和变量
    private static final int FPS = 60; // 动画的帧率
    private static final int WINDOW_WIDTH = 800; // 窗口的宽度
    private static final int WINDOW_HEIGHT = 600; // 窗口的高度
    private static final String WINDOW_TITLE = "Basic Frame"; // 窗口的标题

    private static volatile GameGLCanvas gameGLCanvas;
    // 单例模式私有构造函数懒汉式
    private GameGLCanvas() {
        // 创建一个OpenGL的绘图窗口
        final GLCanvas glcanvas = createGLCanvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        //want to do: 将事件监听器添加到画布中 glCanvas.addGLEventListener(new graphicBases.GameGLEventListener());
        glcanvas.addGLEventListener(new GameGLEventListener());//必须在主函数中添加事件监听器。
        final Frame frame =createFrame(WINDOW_TITLE);// 创建一个窗口
        FPSAnimator fpsAnimator = new FPSAnimator(glcanvas, FPS, true); // 创建一个动画器
        fpsAnimator.start();// 启动动画器
        //want to do: 添加键盘监听器 glCanvas.addKeyListener(new interaction.GameKeyListener());
        GameKeyListener gameKeyListener=new GameKeyListener();
        GameMouseListener gameMouseListener=new GameMouseListener();
        SwingUtilities.invokeLater(() -> {
            //在事件分派线程EDT中添加画布
            frame.add(glcanvas);// 将画布添加到窗口
            frame.setVisible(true);// 显示窗口
            //在事件分派线程EDT中添加监听器
            glcanvas.addKeyListener(gameKeyListener);
            glcanvas.addMouseListener(gameMouseListener);
            glcanvas.addMouseMotionListener(gameMouseListener);
            glcanvas.addMouseWheelListener(gameMouseListener);
            frame.addWindowFocusListener(gameMouseListener);
        });

    }
    public static void main(String[] args) {

        gameGLCanvas=GameGLCanvas.getGameGLCanvas();

    }
    public static Frame createFrame(String WINDOW_TITLE){
        final Frame frame = new Frame(WINDOW_TITLE);
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(WINDOW_WIDTH,  WINDOW_HEIGHT);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                //want to do: 释放窗口资源 frame.dispose();
                frame.dispose(); // 释放窗口资源
                System.exit(0);
            }
        });
        return frame;
    }
    public static GLCanvas createGLCanvas(int width, int height){
        final GLProfile glProfile = GLProfile.get(GLProfile.GL4);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        final GLCanvas glcanvas = new GLCanvas(glCapabilities);
        glcanvas.setSize(width, height);
        return glcanvas;
    }
    // 单例模式获取实例的方法懒汉式
    public static GameGLCanvas getGameGLCanvas(){
        // 单例模式双重检查锁
        if(gameGLCanvas==null){
            synchronized (GameGLCanvas.class){
                if(gameGLCanvas==null){
                    gameGLCanvas=new GameGLCanvas();
                }
            }
        }
        return gameGLCanvas;


    }




}