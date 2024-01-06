#version 330 core
out vec4 FragColor;

in VS_OUT {
    vec3 FragPos;
    vec3 Normal;
    vec2 TexCoords;
    vec4 FragPosLightSpace;
} fs_in;
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
uniform sampler2D diffuseTexture;
uniform sampler2D shadowMap;
#define NR_DIR_LIGHTS 1
#define NR_POINT_LIGHTS 2
#define NR_SPOT_LIGHTS 1

uniform vec3 lightPos;
uniform vec3 viewPos;
uniform Material material;
uniform DirLight dirLight[NR_DIR_LIGHTS];
uniform PointLight pointLights[NR_POINT_LIGHTS];
uniform SpotLight spotLight[NR_SPOT_LIGHTS];

uniform bool shadows=true;

float ShadowCalculation(vec4 fragPosLightSpace);
//fuction
vec3 CalcPointLight(PointLight light, vec3 norm, vec3 fragPos, vec3 viewDir);
vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir);
vec3 CalcSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDir);

void main()
{
    vec3 baseColor;//物体的底色
    if(material.textureNum == 0)
    baseColor = texture(diffuseTexture, fs_in.TexCoords).rgb;
    //若使用了纹理，但只分配了一个纹理单元.
    else if(material.textureNum == 1)
    baseColor=  texture(material.texture0, fs_in.TexCoords).rgb;
    else if(material.textureNum == 2)
    baseColor = mix(texture(material.texture0, fs_in.TexCoords), texture(material.texture1, fs_in.TexCoords), 0.2).rgb;

    vec3 normal = normalize(fs_in.Normal);
    vec3 viewDir =  normalize(viewPos - fs_in.FragPos);
    vec3 result;
    // phase 1: directional lighting
    for(int i = 0; i < NR_DIR_LIGHTS; i++)
    result += CalcDirLight(dirLight[i], normal, viewDir);
    // phase 2: point lights
    for(int i = 0; i < NR_POINT_LIGHTS; i++)
    result += CalcPointLight(pointLights[i], normal, fs_in.FragPos, viewDir);
    // phase 3: spot light
    for(int i = 0; i < NR_SPOT_LIGHTS; i++)
    result += CalcSpotLight(spotLight[i], normal, fs_in.FragPos, viewDir);
    result = result * baseColor;

//    // Calculate shadow
//    float shadow = shadows ? ShadowCalculation(fs_in.FragPosLightSpace) : 0.0;
//    shadow = min(shadow, 0.75); // reduce shadow strength a little: allow some diffuse/specular light in shadowed regions
//    vec3 lighting = (ambient + (1.0 - shadow) * (diffuse + specular)) * baseColor;

    FragColor = vec4(result, 1.0f);
}
float ShadowCalculation(vec4 fragPosLightSpace,vec3 lightPos)
{
    // perform perspective divide
    vec3 projCoords = fragPosLightSpace.xyz / fragPosLightSpace.w;
    // Transform to [0,1] range
    projCoords = projCoords * 0.5 + 0.5;
    // Get closest depth value from light's perspective (using [0,1] range fragPosLight as coords)
    float closestDepth = texture(shadowMap, projCoords.xy).r;
    // Get depth of current fragment from light's perspective
    float currentDepth = projCoords.z;
    // Calculate bias (based on depth map resolution and slope)
    vec3 normal = normalize(fs_in.Normal);
    vec3 lightDir = normalize(lightPos - fs_in.FragPos);
    float bias = max(0.05 * (1.0 - dot(normal, lightDir)), 0.005);
    // Check whether current frag pos is in shadow
    // float shadow = currentDepth - bias > closestDepth  ? 1.0 : 0.0;
    // PCF
    float shadow = 0.0;
    vec2 texelSize = 1.0 / textureSize(shadowMap, 0);
    for(int x = -1; x <= 1; ++x)
    {
        for(int y = -1; y <= 1; ++y)
        {
            float pcfDepth = texture(shadowMap, projCoords.xy + vec2(x, y) * texelSize).r;
            shadow += currentDepth - bias > pcfDepth  ? 1.0 : 0.0;
        }
    }
    shadow /= 9.0;

    // Keep the shadow at 0.0 when outside the far_plane region of the light's frustum.
    if(projCoords.z > 1.0)
    shadow = 0.0;

    return shadow;
}
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