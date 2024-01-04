package interaction;

import graphicBases.cameraModel.Camera;
import graphicBases.cameraModel.Camera_Movement;
import graphicBases.GameGLEventListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * created by KinMan谨漫 on 2024/1/1/**
 *
 * @author KinMan谨漫
 * ,
 */
public class GameKeyListener implements KeyListener{
    private final Camera camera=Camera.cameraList.get(0);
    /**
     * 按下w，光源位置向上移动，按下s，光源位置向下移动
     * 按下a，光源位置向左移动，按下d，光源位置向右移动
     * @param e the event to be processed
     */
    //TODO: 1.添加光源类，2.添加光源位置的变量，3.添加光源位置的变化
    //从GameGLCanvas中移动光源位置的变化到这里

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'w' ->
                //相机前进移动
                    camera.ProcessKeyboard(Camera_Movement.FORWARD, GameGLEventListener.frameDeltaTime);
            case 's' ->
                //相机后退移动
                    camera.ProcessKeyboard(Camera_Movement.BACKWARD, GameGLEventListener.frameDeltaTime);
            case 'a' ->
                //相机左移动
                    camera.ProcessKeyboard(Camera_Movement.LEFT, GameGLEventListener.frameDeltaTime);
            case 'd' ->
                //相机右移动
                    camera.ProcessKeyboard(Camera_Movement.RIGHT, GameGLEventListener.frameDeltaTime);
        }
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
