package com.mygdx.game.component.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class LightRedShader {

    private static final String VERTEX_SHADER =
            "attribute vec4 a_position; \n" +
                    "attribute vec4 a_color;\n" +
                    "attribute vec2 a_texCoord0;\n" +
                    "uniform mat4 u_projTrans;\n" +
                    "varying vec4 v_color;\n" +
                    "varying vec2 v_texCoords;\n" +
                    "void main() {\n" +
                    "   v_color = vec4(1.0, 0.8, 0.8, 1.0); \n" +
                    "   v_texCoords = a_texCoord0;\n" +
                    "   gl_Position =  u_projTrans * a_position;\n" +
                    "}\n";

    private static final String FRAGMENT_SHADER =
            "#ifdef GL_ES\n" +
                    "    precision mediump float;\n" +
                    "#endif\n" +
                    "varying vec4 v_color;\n" +
                    "varying vec2 v_texCoords;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "void main() {\n" +
                    "   vec4 texColor = texture2D(u_texture, v_texCoords);\n" +
                    "   vec3 color = texColor.rgb * v_color.rgb;\n" +
                    "   float intensity = max(color.r, max(color.g, color.b));\n" +
                    "   color *= (1.0 / intensity);\n" +
                    "   gl_FragColor = vec4(color.rgb, texColor.a);\n" +
                    "}\n";

    public static ShaderProgram createShader() {
        ShaderProgram shader = new ShaderProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        if (!shader.isCompiled()) {
            throw new RuntimeException("Could not create light red shader: " + shader.getLog());
        }
        return shader;
    }
}
