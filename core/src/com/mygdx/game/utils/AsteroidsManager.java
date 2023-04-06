package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.Asteroids;
import com.mygdx.game.component.GameWorld;

import java.util.Random;

public class AsteroidsManager {

    /**
     * 随机生成陨石
     * @param world
     */
    public static void randomGenerateAsteroids(World world, Integer createCount) {
        Random random = new Random();
        for (int i = 0 ; i<createCount; i++) {
            int number = random.nextInt(16) + 1; // 生成1到16的随机整数
            String result = String.format("%02d", number);
            StringBuilder imgPath = new StringBuilder("Asteroids ").append(result).append(".png");
            Texture texture = new Texture(imgPath.toString());
            GameManager.addAsteroids(new Asteroids(world, texture, MathUtils.random(1, GameWorld.frameWidth), GameWorld.frameHeight, 0, -MathUtils.random(0.1f, 2), 0, 1, 1));
        }
    }
}
