#version 330 core
layout (location = 0) in vec3 aPos;   // 位置变量的属性位置值为 0
layout (location = 1) in vec3 aColor; // 颜色变量的属性位置值为 1
layout (location = 2) in vec2 aTexCoord; // 纹理坐标属性
layout (location = 3) in vec3 aNormal; // 法向量属性
out vec3 Normal; // 向片段着色器输出一个法向量
out vec3 ourColor; // 向片段着色器输出一个颜色
out vec2 TexCoord; // 向片段着色器输出一个纹理坐标
out vec3 FragPos; // 向片段着色器输出一个片段位置
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
void main()
{
    FragPos = vec3(model * vec4(aPos, 1.0)); // 将片段的世界坐标位置传给片段着色器
    gl_Position = projection * view * vec4(FragPos, 1.0);
    Normal = mat3(transpose(inverse(model))) * aNormal; // 将法向量转换到和世界坐标系一致的空间中
    ourColor = aColor; // 将ourColor设置为我们从顶点数据那里得到的输入颜色
    TexCoord = vec2(aTexCoord.x,1.0-aTexCoord.y); // 将纹理坐标传给片段着色器
}