package com.mygdx.game.component.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.inter.Shooter;

public class BulletArrow extends Bullet{

    private static Texture texture = new Texture("arrow.png");
    private static Sound shootSound = Gdx.audio.newSound(Gdx.files.internal("arrow.mp3"));

    public BulletArrow(World world, float rotation, float bulletAngle, Shooter shooter) {
        super(world, texture, rotation, shooter, 3, 0.5f, 0.5f, 8, bulletAngle);
    }

    public static float getShootTime() {
        return 0.35f;
    }

    public static Texture getTexture() {
        return texture;
    }

    @Override
    public Sound getShootSound() {
        return shootSound;
    }
}
