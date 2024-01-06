import assimp4j.Mesh;
import shaderControl.ShaderManager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * created by KinMan谨漫 on 2024/1/5/**
 *
 * @author KinMan谨漫
 * ,
 */
public class TestSomeThing {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        //输出当前工作目录
       // System.out.println(System.getProperty("user.dir"));
       // ShaderManager.readFile("resource/meshModel/cottage/cottage_obj.obj");

        Mesh mesh1=Mesh.load("resource/meshModel/floor/floor.fbx", 0);
        Mesh mesh=mesh1;
        System.out.println("vertices"+ Arrays.toString(mesh.getVertices()));
        System.out.println("vertices.length "+ mesh.getVertices().length);

        System.out.println("textureCoords"+Arrays.toString(mesh.getTextureCoords()));
        System.out.println("normals"+Arrays.toString(mesh.getNormals()));
        System.out.println("indices"+Arrays.toString(mesh.getIndices()));
        System.out.println("furthestPoint"+mesh.getFurthestPoint());
        System.out.println("material"+mesh.getMaterial());

        Mesh mesh2=Mesh.load("resource/meshModel/cottage/cottage_fbx.fbx", 0);
        mesh=mesh2;
        System.out.println("vertices"+ Arrays.toString(mesh.getVertices()));
        System.out.println("textureCoords"+Arrays.toString(mesh.getTextureCoords()));
        System.out.println("normals"+Arrays.toString(mesh.getNormals()));
        System.out.println("indices"+Arrays.toString(mesh.getIndices()));
        System.out.println("furthestPoint"+mesh.getFurthestPoint());
        System.out.println("material"+mesh.getMaterial());

    }
}
