package createPalace;

import com.jogamp.opengl.GLAutoDrawable;
import graphicBases.GameGLEventListener;
import graphicBases.lightsModel.dirLights.DirLightsBase;
import graphicBases.lightsModel.pointLights.PointLightBase;
import graphicBases.lightsModel.spotLights.SpotLightBase;
import graphicBases.objectModel.Box;
import graphicBases.objectModel.Cottage;
import graphicBases.objectModel.Floor;

/**
 * created by KinMan谨漫 on 2024/1/5/**
 *
 * @author KinMan谨漫
 * ,
 */
public class ForProgrammerCreate {
    /**
     * 可编程管线创建类
     * 把你想画的都放在这里
     * @param glAutoDrawable
     */
    public ForProgrammerCreate(GLAutoDrawable glAutoDrawable) {
        new Box(glAutoDrawable,0,1,-5);
        new DirLightsBase(glAutoDrawable, -0.2f, 10.0f, -0.3f);
        new PointLightBase(glAutoDrawable,1.2f,1.0f,-2.0f);
        new PointLightBase(glAutoDrawable,-1.2f,1.0f,2.0f);
        new SpotLightBase(glAutoDrawable, GameGLEventListener.camera.cameraPos,GameGLEventListener.camera.cameraFront);

        new Cottage(glAutoDrawable,4,1,-9);
        new Floor(glAutoDrawable,0,-1,0);
    }
}
