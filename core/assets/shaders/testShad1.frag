#version 120

#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

uniform float speed = 0.1;
uniform float distance = 0.01;
uniform float time;

void main()
{
	vec2 iResolution = vec2(800, 600);
	vec2 uv = v_texCoords;
	// uv.y *= -1.0;
	
	vec3 offTexX = texture2D(u_texture, uv).rgb;
	vec3 luma = vec3(0.299, 0.587, 0.114);
	float power = dot (offTexX, luma);
	
	power = sin(3.1415927*2.0 * mod(power + time * speed, 1.0));
	
	gl_FragColor = texture2D(u_texture, uv + vec2(0, power) * distance);
}

