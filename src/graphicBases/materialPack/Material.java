package graphicBases.materialPack;

/**
 * created by KinMan谨漫 on 2024/1/4/**
 *
 * @author KinMan谨漫
 * ,
 */
public class Material {
    private final float[] ambient;
    private final float[] diffuse;
    private final float[] specular;
    private final float shininess;

    public Material(float[] ambient, float[] diffuse, float[] specular, float shininess) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }
    public Material(float[] ambient, float[] diffuse, float[] specular) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = 0;
    }
    public Material() {
        this.ambient = new float[]{1.0f, 0.5f, 0.31f};
        this.diffuse = new float[]{1.0f, 0.5f, 0.31f};
        this.specular = new float[]{0.5f, 0.5f, 0.5f};
        this.shininess = 32.0f;
    }

    public float[] getAmbient() {
        return ambient;
    }

    public float[] getDiffuse() {
        return diffuse;
    }

    public float[] getSpecular() {
        return specular;
    }

    public float getShininess() {
        return shininess;
    }
}
