package com.mygdx.game.component.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.inter.Shooter;

public class Bullet4 extends Bullet {

    private static Texture texture = new Texture("bullet4.png");
    private static Sound shootSound = Gdx.audio.newSound(Gdx.files.internal("buqiang.mp3"));


    public Bullet4(World world, float rotation, Shooter shooter, float scaleX, float scaleY, float bulletSpeed, float bulletAngle) {
        super(world, texture, rotation, shooter, 0.5f, scaleX, scaleY, bulletSpeed, bulletAngle);
    }

    @Override
    public void updatePosition() {
        super.updatePosition();
    }

    public static float getShootTime() {
        return 0;
    }

    @Override
    public  Sound getShootSound() {
        return shootSound;
    }
}
