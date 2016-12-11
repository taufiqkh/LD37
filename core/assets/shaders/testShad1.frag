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

uniform float dark = 0.1;
uniform float ripple = 0.01;
uniform float shadow = 0.072;
uniform float time;

void main()
{
	vec2 iResolution = vec2(800, 600);
	vec2 uv = v_texCoords;
	// uv.y *= -1.0;
	
	vec3 offTexX = texture2D(u_texture, uv).rgb;
	vec3 luma = vec3(0.299, 0.587, 0.114);
	float power = dot (offTexX, luma);
	
	power = sin(3.1415927*2.0 * mod(power + time * dark, 1.0));
	
	gl_FragColor = texture2D(u_texture, uv + vec2(0, power) * ripple);

	// float d = length(uv);
	// vec2 st = uv * dark + ripple * vec2(cos(shadow * time + d), sin(shadow * time - d));

    // vec3 col = texture2D(u_texture, st).xyz;
	// col *= col.x * 2.0;
	// col *= 1.0 - texture2D(u_texture, uv + 0.005 * col.xy).xyy;
	// col *= 1.0 + 2.0 * d;
	
	// gl_FragColor = vec4(col, 1.0);
	// gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
}

