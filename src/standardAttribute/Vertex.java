package standardAttribute;

import com.jogamp.opengl.math.Vec2f;
import com.jogamp.opengl.math.Vec3f;

/**
 * created by KinMan谨漫 on 2024/1/5/**
 *
 * @author KinMan谨漫
 * ,
 */
// Define a constant

public class Vertex {
    // position
    Vec3f Position;
    // normal
    Vec3f Normal;
    // texCoords
    Vec2f TexCoords;
    // tangent
    Vec3f Tangent;
    // bitangent
    Vec3f Bitangent;
    // bone indexes which will influence this vertex
    final int MAX_BONE_INFLUENCE = 4;
    int[] m_BoneIDs = new int[MAX_BONE_INFLUENCE];
    // weights from each bone
    float[] m_Weights = new float[MAX_BONE_INFLUENCE];
    public static int getLength(){
        return 14*4+4*4+4*4;
    }

}

