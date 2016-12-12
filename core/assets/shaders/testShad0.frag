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

uniform float radius = 2000.0;
uniform float angle;
uniform vec2 center = vec2(1500.0, 1500.0);

vec4 PostFX(sampler2D tex, vec2 uv)
{
  vec2 texSize = vec2(3000, 3000);
  vec2 tc = uv * texSize;
  tc -= center;
  float dist = length(tc);
  if (dist < radius) 
  {
    float percent = (radius - dist) / radius;
    float theta = percent * percent * angle * 8.0;
    float s = sin(theta);
    float c = cos(theta);
    tc = vec2(dot(tc, vec2(c, -s)), dot(tc, vec2(s, c)));
  }
  tc += center;
  vec3 color = texture2D(u_texture, tc / texSize).rgb;
  return vec4(color, 1.0);
}

void main()
{
  // gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
  gl_FragColor = PostFX(u_texture, v_texCoords);
}