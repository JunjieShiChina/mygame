package com.mygdx.game.component.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.GameWorld;
import com.mygdx.game.inter.Shooter;

public class Bullet3 extends Bullet {

    /**
     * 持续旋转
     */
    private float continuousDegree = 20;

    private static Texture texture = new Texture("bullet3.png");

    private static Sound shootSound = Gdx.audio.newSound(Gdx.files.internal("jian.wav"));

    public Bullet3(World world, float rotation, float bulletAngle, Shooter shooter) {
        super(world, texture, rotation, shooter, 3, 0.2f, 0.2f, 9, bulletAngle);
    }

    @Override
    public void updatePosition() {
        // 更新子弹的旋转角度
        float rotation = this.getSprite().getRotation();
        if (Math.abs(rotation) > 360) {
            rotation = 0;
        }
        this.getSprite().setRotation(rotation + continuousDegree);

        // 更新子弹的位置和精灵的位置
        body.setLinearVelocity(velocity);
        position.set(body.getPosition().x * GameWorld.PPM, body.getPosition().y * GameWorld.PPM);
        this.getSprite().setPosition(position.x - this.getSprite().getWidth() / 2, position.y - this.getSprite().getHeight() / 2);    }

    public static float getShootTime() {
        return 0.15f;
    }

    public static Texture getTexture() {
        return texture;
    }

    @Override
    public Sound getShootSound() {
        return shootSound;
    }
}
