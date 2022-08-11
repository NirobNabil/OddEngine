#type vertex
#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec3 centerPos;
layout (location=3) in float radius;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec3 fcenterPos;
out float fradius;

void main() {
    fColor = aColor;
    fcenterPos = centerPos;
    fradius = radius;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

    #type fragment
    #version 330 core

in vec4 fColor;
in vec3 fcenterPos;
in float fradius;

out vec4 color;

void main()
{
//    if( distance( fcenterPos, gl_FragCoord.xyz ) > fradius ) color = vec4(0.0f);
//    color = smoothstep( 0.0, 0.05f,  vec4(vec3(1.0f) - distance( fcenterPos, gl_FragCoord.xyz )/vec3(fradius), 1.0));
//    color = color * fColor;
//    if( lessThan(color.xyz, vec3(1.0f)) == bvec3(true) ) color = vec4( color.xyz, max( max(color.x, color.y), color.z ) );
//    color = vec4(distance( fcenterPos, gl_FragCoord.xyz ), 0.0f, 0.0f, 1.0f);
//    color = smoothstep(fradius,fradius+1,color);
    color = vec4(1.0f, 0,0, 1.0f);
}


//#type vertex
//#version 330 core
//layout (location=0) in vec3 aPos;
//layout (location=1) in vec4 aColor;
//
//uniform mat4 uProjection;
//uniform mat4 uView;
//
//out vec4 fColor;
//
//void main()
//{
//    fColor = aColor;
//    gl_Position = uProjection * uView * vec4(aPos, 1.0);
//}
//
//    #type fragment
//    #version 330 core
//
//in vec4 fColor;
//
//out vec4 color;
//
//void main()
//{
//    //    color = fColor;
//    color = vec4(0.0f, 0.0f, 1.0f, 1.0f);
//}
