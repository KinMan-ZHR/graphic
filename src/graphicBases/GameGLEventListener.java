package graphicBases;
/**
 * created by KinMan谨漫 on 2023/12/27/**
 *
 * @author KinMan谨漫
 * ,
 */
// 导入相关的类和接口
import createPalace.ForProgrammerCreate;
import com.jogamp.opengl.*;
import graphicBases.cameraModel.Camera;
import graphicBases.programmableSupport.ProgrammableBase;
import graphicBases.programmableSupport.ProgrammableLight;
import graphicBases.programmableSupport.ProgrammableObject;
import main.GameGLCanvas;
import shaderControl.ShaderManager;

import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.util.GLBuffers.sizeof;
// 血泪警告，计算时间请选用高精度的long型，不然会出现很多问题

public class GameGLEventListener implements GLEventListener {
    private static final float[] VERTEX_DATA = { // 顶点数据，一个三角形
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f, 0.5f, 0.0f
    };
    private static  ShaderManager objectShaderManager; // 着色器程序的句柄
    private static ShaderManager lightShaderManager;
    private static ShaderManager shadowShaderManager;
    private static ShaderManager depthShaderManager;

    // 主方法，创建一个窗口并添加一个画布
    public static Camera camera =new Camera(0,0,4,0,0,0);
    public static long frameDeltaTime= System.currentTimeMillis();
    private static long lastFrameTime= System.currentTimeMillis();
    private static long currentFrameTime= System.currentTimeMillis();//当前时间 单位：毫秒

    public static final int SHADOW_WIDTH = 1024, SHADOW_HEIGHT = 1024;
    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL4 gl4 = glAutoDrawable.getGL().getGL4(); // 获取OpenGL的对象
        //todo 着色器程序启动！
        //objectShaderManager =new ShaderManager(gl4,"./src/shaderControl/vertex_shader.glsl","./src/shaderControl/fragment_shader.glsl");
        objectShaderManager =new ShaderManager(gl4,"./src/shaderControl/shadowMapping_vs.glsl","./src/shaderControl/shadowMapping_fs.glsl");
        lightShaderManager =new ShaderManager(gl4,"./src/shaderControl/light_vs.glsl","./src/shaderControl/light_fs.glsl");
       // shadowShaderManager =new ShaderManager(gl4,"./src/shaderControl/depth_vs.glsl","./src/shaderControl/depth_fs.glsl");
       // depthShaderManager =new ShaderManager(gl4,"./src/shaderControl/quad_depth_vs.glsl","./src/shaderControl/quad_depth_fs.glsl");
        //画什么
        new ForProgrammerCreate(glAutoDrawable);
        //计算时间
        lastFrameTime = System.currentTimeMillis();
        //todo 全局设置
//        深度测试
        //gl4.glClearDepth(1.0f);
         gl4.glEnable(GL4.GL_DEPTH_TEST);
//        gl4.glEnable(GL4.GL_BLEND);
//        gl4.glBlendFunc(GL4.GL_SRC_ALPHA, GL4.GL_ONE_MINUS_SRC_ALPHA);
        //定义视口
         gl4.glViewport(0, 0, GameGLCanvas.WINDOW_WIDTH, GameGLCanvas.WINDOW_HEIGHT);
       //定义清屏的颜色
        gl4.glClearColor(0.2f, 0.3f, 0.3f, 1.0f); // 设置清屏时的颜色

        objectShaderManager.useShaderProgram();
        //设置阴影纹理
        objectShaderManager.setUniform("diffuseTexture",0);
        objectShaderManager.setUniform("shadowMap",1);

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        GL4 gl = glAutoDrawable.getGL().getGL4(); // 获取OpenGL的对象
        objectShaderManager.deleteShaderProgram();
        ProgrammableObject.destroyAll(gl);

    }
    /**
     * 开一个线程，每次重绘时调用，用于更新时间
     */
    public static void updateTime(){
        new Thread(()->{
            while (true){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentFrameTime= (long) (System.nanoTime()/1e3);
                frameDeltaTime=currentFrameTime-lastFrameTime;
                lastFrameTime=currentFrameTime;
                System.out.println("frameDeltaTime"+frameDeltaTime);//一会儿frameDeltaTime是0，偶尔是0.125
            }
        }).start();
    }
    /**
     * 这个方法会在每次重绘时被调用，触发重绘的函数是glutPostRedisplay()。
     */
    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        currentFrameTime= System.currentTimeMillis();
        frameDeltaTime=currentFrameTime-lastFrameTime;
        lastFrameTime=currentFrameTime;
        GL4 gl = glAutoDrawable.getGL().getGL4(); // 获取OpenGL的对象
//选光源中第一个来产生阴影
        //todo 画阴影场景
//        ProgrammableLight.shadowDrawAll(gl,shadowShaderManager);
//        // 1. first render to depth map
//        ProgrammableObject.happyDrawAll(gl,shadowShaderManager);
//        //解绑
//        gl.glBindFramebuffer(GL4.GL_FRAMEBUFFER, 0);

        // 2. Render scene as normal using the generated depth/shadow map
        reshape(glAutoDrawable,0, 0, GameGLCanvas.WINDOW_WIDTH, GameGLCanvas.WINDOW_HEIGHT);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT| GL_DEPTH_BUFFER_BIT); // 清屏
        //观察矩阵
        camera.lookAt();
        objectShaderManager.useShaderProgram();
        GameGLEventListener.objectShaderManager.setUniform("view", GameGLEventListener.camera.view);
        GameGLEventListener.objectShaderManager.setUniform("projection", GameGLEventListener.camera.projection);
        GameGLEventListener.objectShaderManager.setUniform("viewPos", GameGLEventListener.camera.cameraPos);
        //todo 画正常场景
        //光源
        ProgrammableLight.lightDrawAll(gl,objectShaderManager);//暂时这个传参无影响
        //物体
        ProgrammableObject.happyDrawAll(gl,objectShaderManager);
        //todo 画光源
        ProgrammableLight.happyDrawAll(gl,lightShaderManager);
        //debug
//        depthShaderManager.useShaderProgram();
//        depthShaderManager.setUniform("near_plane",near_plane);
//        depthShaderManager.setUniform("far_plane",far_plane);
//        gl.glActiveTexture(GL4.GL_TEXTURE0);
//        gl.glBindTexture(GL4.GL_TEXTURE_2D,ProgrammableLight.depthMap[0]);

    }

    private void renderScene(ShaderManager shadowShaderManager) {
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable,int x,  int y, int width, int height) {
        // TODO Auto-generated method stub


    }
    // 从文件中读取着色器代码的函数


}
