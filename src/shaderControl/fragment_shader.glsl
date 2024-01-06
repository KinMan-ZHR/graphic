#version 330 core
struct Material {
       sampler2D texture0;
       sampler2D texture1;
       vec3 ambient;//环境光
       vec3 diffuse;//漫反射
       vec3 specular;//镜面反射
       float shininess;//反光度
       int textureNum;//纹理数量
};
struct PointLight {
       vec3 position;
       vec3 ambient;
       vec3 diffuse;
       vec3 specular;
       float constant;
       float linear;
       float quadratic;
};
struct SpotLight {
    vec3 position;
    vec3 direction;
    float cutOff;
    float outerCutOff;

    float constant;
    float linear;
    float quadratic;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    int spotLightNum;
};
struct DirLight {
    vec3 direction;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};
#define NR_DIR_LIGHTS 1
#define NR_POINT_LIGHTS 2
#define NR_SPOT_LIGHTS 1

out vec4 FragColor;
in vec3 ourColor;
in vec2 TexCoord;
in vec3 FragPos;
in vec3 Normal;
uniform vec3 viewPos;
uniform Material material;
uniform DirLight dirLight[NR_DIR_LIGHTS];
uniform PointLight pointLights[NR_POINT_LIGHTS];
uniform SpotLight spotLight[NR_SPOT_LIGHTS];

//fuction
vec3 CalcPointLight(PointLight light, vec3 norm, vec3 fragPos, vec3 viewDir);
vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir);
vec3 CalcSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDir);
void main()
{
       vec3 baseColor;//物体的底色
       //若使用了纹理，但只分配了一个纹理单元.
       if(material.textureNum == 1)
       baseColor=  texture(material.texture0, TexCoord).rgb;
       else if(material.textureNum == 2)
       baseColor = mix(texture(material.texture0, TexCoord), texture(material.texture1, TexCoord), 0.2).rgb;
       else if(material.textureNum == 0)
       baseColor = ourColor;
    //计算光照
    // properties
    vec3 norm = normalize(Normal);
    vec3 viewDir = normalize(viewPos - FragPos);

    // == =====================================================
    // Our lighting is set up in 3 phases: directional, point lights and an optional flashlight
    // For each phase, a calculate function is defined that calculates the corresponding color
    // per lamp. In the main() function we take all the calculated colors and sum them up for
    // this fragment's final color.
    // == =====================================================

    vec3 result;
    // phase 1: directional lighting
    for(int i = 0; i < NR_DIR_LIGHTS; i++)
    result += CalcDirLight(dirLight[i], norm, viewDir);
    // phase 2: point lights
    for(int i = 0; i < NR_POINT_LIGHTS; i++)
    result += CalcPointLight(pointLights[i], norm, FragPos, viewDir);
    // phase 3: spot light
    for(int i = 0; i < NR_SPOT_LIGHTS; i++)
    result += CalcSpotLight(spotLight[i], norm, FragPos, viewDir);
    result = result * baseColor;
    FragColor = vec4(result, 1.0);

}
// calculates the color when using a point light.
vec3 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir)
{
             //环境光照
             vec3 ambient = light.ambient * material.ambient;
             vec3 lightDir = normalize(light.position - fragPos);
               //TODO：计算光线的衰减强度
             float distance = length(light.position - fragPos);
             float attenuation = min(1.0, 1.0 / (light.constant + light.linear *distance + light.quadratic *distance* distance));
             //TODO：计算得到漫反射光diffuse，入射的漫反射光 * 表面漫反射率 * 物体的底色*衰减强度
             float diff = max(dot(normal, lightDir), 0.0);
             vec3 diffuse = light.diffuse * (diff * material.diffuse)*attenuation;
             //TODO：计算镜面反射光specular，入射的镜面反射光 * 表面镜面反射率 * 物体的底色*衰减强度
//             vec3 reflectDir = reflect(-lightDir, normal);
//             float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
             vec3 halfwayDir = normalize(lightDir + viewDir);
            float spec = pow(max(dot(normal, halfwayDir), 0.0), material.shininess);
             vec3 specular = light.specular * (spec * material.specular)*attenuation;
             //综合光照
             vec3 result = ambient + diffuse + specular;
             return result;
}
// calculates the color when using a directional light.
vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir)
{
    vec3 lightDir = normalize(-light.direction);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
//    vec3 reflectDir = reflect(-lightDir, normal);
//    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 halfwayDir = normalize(lightDir + viewDir);
    float spec = pow(max(dot(normal, halfwayDir), 0.0), material.shininess);
    // combine results
    vec3 ambient = light.ambient * material.ambient;
    vec3 diffuse = light.diffuse * diff * material.diffuse;
    vec3 specular = light.specular * spec * material.specular;
    //综合光照
    vec3 result = ambient + diffuse + specular;
    return result;
}
// calculates the color when using a spot light.
vec3 CalcSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDir)
{
    vec3 lightDir = normalize(light.position - fragPos);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
//    vec3 reflectDir = reflect(-lightDir, normal);
//    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 halfwayDir = normalize(lightDir + viewDir);
    float spec = pow(max(dot(normal, halfwayDir), 0.0), material.shininess);
    // attenuation
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    // spotlight intensity
    float theta = dot(lightDir, normalize(-light.direction));
    float epsilon = light.cutOff - light.outerCutOff;
    float intensity = clamp((theta - light.outerCutOff) / epsilon, 0.0, 1.0);
    // combine results
    vec3 ambient = light.ambient * material.ambient;
    vec3 diffuse = light.diffuse * diff * material.diffuse;
    vec3 specular = light.specular * spec * material.specular;
    ambient *= attenuation * intensity;
    diffuse *= attenuation * intensity;
    specular *= attenuation * intensity;
    //综合光照
    vec3 result = ambient + diffuse + specular;
    return result;
}