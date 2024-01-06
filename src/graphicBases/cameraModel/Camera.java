package graphicBases.cameraModel;

import com.jogamp.opengl.math.Matrix4f;
import com.jogamp.opengl.math.Vec3f;

import java.util.ArrayList;

import static graphicBases.cameraModel.Camera_Movement.*;

;

// 摄像头类
public class Camera {
    // 摄像机位置
    public float eyeX;
    public float eyeY;
    public float eyeZ;
    // 目标位置
    float centerX;
    float centerY;
    float centerZ;
    float yaw=-90.0f;//偏航角
    float pitch=0.0f;//俯仰角
    // 摄像头的旋转速度
    private final float Mousesensitivity=0.01f;//鼠标灵敏度
    private final float MovementSpeed=0.01f;//移动速度;
    private final float LowerY=0.0f;//最低高度
    // 摄像头的缩放大小
    private float zoom=45.0f;
     public Vec3f cameraPos=new Vec3f();
     public Vec3f cameraFront=new Vec3f();
     private Vec3f cameraUp=new Vec3f();
     private Vec3f cameraRight=new Vec3f();
     private Vec3f worldUp=new Vec3f(0,1,0);
     public Matrix4f view=new Matrix4f();
     public Matrix4f projection=new Matrix4f();
     //管理所有摄像机对象
    public static ArrayList<Camera>cameraList=new ArrayList<>();
    // 构造方法，初始化摄像头的属性
    public Camera(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ){
        this.eyeX = eyeX;
        this.eyeY = eyeY;
        this.eyeZ = eyeZ;
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
        cameraPos=new Vec3f(eyeX,eyeY,eyeZ);
        worldUp=new Vec3f(0,1,0);
        updateCameraVectors();
        cameraList.add(this);

    }
    public Camera(float eyeX, float eyeY, float eyeZ){
        this.eyeX = eyeX;
        this.eyeY = eyeY;
        this.eyeZ = eyeZ;
        cameraPos=new Vec3f(eyeX,eyeY,eyeZ);
        worldUp=new Vec3f(0,1,0);
        updateCameraVectors();
        cameraList.add(this);
    }
    public void updateCameraVectors(){
        cameraFront.setX ((float) (Math.cos(Math.toRadians(yaw))*Math.cos(Math.toRadians(pitch))));
        cameraFront.setY ((float) (Math.sin(Math.toRadians(pitch))));
        cameraFront.setZ ((float) (Math.sin(Math.toRadians(yaw))*Math.cos(Math.toRadians(pitch))));
        cameraFront.normalize();
        cameraRight.cross(cameraFront,worldUp);
        cameraRight.normalize();
        cameraUp.cross(cameraRight,cameraFront);
        cameraUp.normalize();
//        System.out.println("cameraFront"+cameraFront);
//        System.out.println("cameraRight"+cameraRight);
//        System.out.println("cameraUp"+cameraUp);

    }

    // position: 摄像机的位置
// target: 摄像机的目标
// worldUp: 世界坐标系的上方向
    public void lookAt()
    {
        //Camera Direction是cameraFront的负数
        Vec3f cameraDirection =new Vec3f();
        cameraDirection.sub(cameraFront);
        cameraDirection.normalize();
        Vec3f cameraRight =new Vec3f();
        cameraRight.cross(this.cameraUp.normalize(), cameraDirection);
        cameraRight.normalize();
        Vec3f cameraUp =new Vec3f();
        cameraUp.cross(cameraDirection, cameraRight);
        //System.out.println("cameraPos"+cameraPos);

        Matrix4f translation=new Matrix4f();
        //第四列第一行3*4+0
        translation.set(12,-cameraPos.get(0));
        //第四列第二行
        translation.set(13,-cameraPos.get(1));
        //第四列第三行
        translation.set(14,-cameraPos.get(2));
        Matrix4f rotation=new Matrix4f();
        //第一列第一行
        rotation.set(0, cameraRight.get(0));
        //第二列第一行1*4+0
        rotation.set(4, cameraRight.get(1));
        //第三列第一行2*4+0
        rotation.set(8, cameraRight.get(2));
        //第一列第二行0*4+1
        rotation.set(1, cameraUp.get(0));
        //第二列第二行1*4+1
        rotation.set(5, cameraUp.get(1));
        //第三列第二行2*4+1
        rotation.set(9, cameraUp.get(2));
        //第一列第三行
        rotation.set(2, cameraDirection.get(0));
        //第二列第三行
        rotation.set(6, cameraDirection.get(1));
        //第三列第三行
        rotation.set(10, cameraDirection.get(2));
        //投影矩阵
        projection.setToPerspective((float) Math.toRadians(zoom), 800.0f / 600.0f, 0.1f, 100.0f);
        view=rotation.mul(translation);
    }
    // processes input received from any keyboard-like input system. Accepts input parameter in the form of camera defined ENUM (to abstract it from windowing systems)
    public void ProcessKeyboard(Camera_Movement direction, float deltaTime)
    {
        float velocity = MovementSpeed * deltaTime;
        if (direction == FORWARD){
             cameraPos.add(cameraFront.mul(velocity));
        }
        if (direction == BACKWARD)
             cameraPos.sub(cameraFront.mul(velocity));
         if (direction == LEFT)
                cameraPos.sub(cameraRight.mul(velocity));
            if (direction == RIGHT)
                cameraPos.add(cameraRight.mul(velocity));
            //保持人物视线水平
           // cameraPos.setY(0);
        if(cameraPos.get(1)<LowerY){
            cameraPos.setY(LowerY);
        }
    }


    // processes input received from a mouse scroll-wheel event. Only requires input on the vertical wheel-axis
    public void ProcessMouseScroll(float yoffset)
    {
        zoom -= yoffset;
        if (zoom < 1.0f)
            zoom = 1.0f;
        if (zoom > 45.0f)
            zoom = 45.0f;
    }
    public void ProcessMouseMovement(float xoffset, float yoffset, boolean constrainPitch)
    {
        xoffset *= Mousesensitivity;
        yoffset *= Mousesensitivity;

        yaw   += xoffset;
        pitch -= yoffset;

        // make sure that when pitch is out of bounds, screen doesn't get flipped
        if (constrainPitch)
        {
            if (pitch > 89.0f)
                pitch = 89.0f;
            if (pitch < -89.0f)
                pitch = -89.0f;
        }

        // update Front, Right and Up Vectors using the updated Euler angles
        updateCameraVectors();
    }


}
