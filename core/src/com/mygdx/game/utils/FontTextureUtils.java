package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FontTextureUtils {
    public static final int DIGIT_WIDTH = 9;
    public static final int DIGIT_HEIGHT = 13;

    private static Texture texture = new Texture("font.png");

    public static TextureRegion[][] getDigitsTexture() {
        TextureRegion[][] digits = TextureRegion.split(texture, DIGIT_WIDTH, DIGIT_HEIGHT);
        return digits;
    }


}





