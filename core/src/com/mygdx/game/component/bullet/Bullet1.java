package com.mygdx.game.component.bullet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.inter.Shooter;

public class Bullet1 extends Bullet {

    private static Texture texture = new Texture("bullet.png");

    public Bullet1(World world, float rotation, float bulletAngle, Shooter shooter) {
        super(world, texture, rotation, shooter, 1, 1, 1, 6, bulletAngle);
    }

    public static Texture getTexture() {
        return texture;
    }

    public static float getShootTime() {
        return 0.25f;
    }
}
