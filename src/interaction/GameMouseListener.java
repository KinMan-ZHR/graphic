package interaction;

import graphicBases.cameraModel.Camera;

import java.awt.*;
import java.awt.event.*;

/**
 * created by KinMan谨漫 on 2024/1/3/**
 *
 * @author KinMan谨漫
 * ,
 */
public class GameMouseListener implements MouseListener, MouseWheelListener, MouseMotionListener,WindowFocusListener {
    //获取对象
    private final Camera camera=Camera.cameraList.get(0);
    // 定义两个变量，用来存储鼠标按下时的位置和拖拽后的位置
    private final Point pressedPoint = new Point();
    private final Point draggedPoint = new Point();
    private boolean focus=false;
    public GameMouseListener() {
    }
    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        focus=true;

    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        focus=true;
        // 获取鼠标按下时的位置，并赋值给第一个变量
        pressedPoint.x = e.getX();
        pressedPoint.y = e.getY();
        //System.out.println("按下了x=" + pressedPoint.x + ", y=" + pressedPoint.y);
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Invoked when the mouse wheel is rotated.
     *
     * @param e the event to be processed
     * @see MouseWheelEvent
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //根据鼠标滚轮的滚动，改变camera的fov
        int notches = e.getWheelRotation();
        if (notches < 0) {
            camera.ProcessMouseScroll(1);
        } else {
            camera.ProcessMouseScroll(-1);
        }

    }

    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  {@code MOUSE_DRAGGED} events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&amp;Drop implementations,
     * {@code MOUSE_DRAGGED} events may not be delivered during a native
     * Drag&amp;Drop operation.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (!focus){
            return;
        }
        //根据鼠标的移动，改变camera的yaw和pitch
        draggedPoint.x = e.getX();
        draggedPoint.y = e.getY();
        // 计算拖拽的x,y的值
        int dx = draggedPoint.x - pressedPoint.x;
        int dy = draggedPoint.y - pressedPoint.y;
        // 在控制台输出拖拽的x,y的值
        //System.out.println("拖拽了x=" + dx + ", y=" + dy);
        camera.ProcessMouseMovement(dx,dy,true);
    }

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {

    }

    /**
     * Invoked when the Window is set to be the focused Window, which means
     * that the Window, or one of its subcomponents, will receive keyboard
     * events.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowGainedFocus(WindowEvent e) {
           focus=true;
    }

    /**
     * Invoked when the Window is no longer the focused Window, which means
     * that keyboard events will no longer be delivered to the Window or any of
     * its subcomponents.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowLostFocus(WindowEvent e) {
        focus=false;

    }
}
