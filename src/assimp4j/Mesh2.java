package assimp4j;

/**
 * created by KinMan谨漫 on 2024/1/5/**
 *
 * @author KinMan谨漫
 * ,
 */
import jassimp.AiMaterial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Mesh2 {

    // 顶点坐标数组
    private float[] vertices;
    // 纹理坐标数组
    private float[] textureCoords;
    // 法线向量数组
    private float[] normals;
    // 面的索引数组
    private int[] indices;
    // 材质对象
    private AiMaterial material;

    // 构造方法，根据给定的数组和材质创建一个Mesh对象
    public Mesh2(float[] vertices, float[] textureCoords, float[] normals, int[] indices, AiMaterial material) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;
        this.material = material;
    }
}

