package com.mygdx.game.component.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class RedShader {

    private static ShaderProgram redShader = null;

    private static final String VERTEX_SHADER =
            "attribute vec4 a_position; \n" +
                    "attribute vec4 a_color;\n" +
                    "attribute vec2 a_texCoord0;\n" +
                    "uniform mat4 u_projTrans;\n" +
                    "varying vec4 v_color;\n" +
                    "varying vec2 v_texCoords;\n" +
                    "void main() {\n" +
                    "   v_color = vec4(1, 0, 0, 1); \n" +
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
                    "   float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));\n" +
                    "   vec4 red = vec4(1.0, 0.0, 0.0, texColor.a);\n" +
                    "   if (gray == 0.0) {\n" +
                    "       gl_FragColor = texColor;\n" +
                    "   } else {\n" +
                    "       gl_FragColor = mix(texColor, red, gray);\n" +
                    "   }\n" +
                    "}\n";

    private static ShaderProgram createShader() {
        ShaderProgram shader = new ShaderProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        if (!shader.isCompiled()) {
            throw new RuntimeException("Could not create red shader: " + shader.getLog());
        }
        return shader;
    }

    public static ShaderProgram getShader() {
        if (redShader == null) {
            redShader = createShader();
        }
        return redShader;
    }

}
