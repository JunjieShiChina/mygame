package com.mygdx.game.component.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.GameWorld;
import com.mygdx.game.inter.Shooter;
import com.mygdx.game.utils.BulletShootingFlag;

public class BulletLaser extends Bullet {

    private BulletShootingFlag bulletShootingFlag;

    private static Texture texture = new Texture("laser.png");
    private static Sound shootSound =  Gdx.audio.newSound(Gdx.files.internal("laser.mp3"));

    public BulletLaser(World world, float rotation, float bulletAngle, Shooter shooter) {
        super(world, texture, rotation, shooter, 1, 0.3f, 1f, 10f, bulletAngle);
    }

    @Override
    public void updatePosition() {
        // 更新子弹的位置和精灵的位置
        body.setTransform(shooter.getBody().getPosition().x, shooter.getBody().getPosition().y + getSprite().getHeight()/2/GameWorld.PPM, 0);
        position.set(body.getPosition().x * GameWorld.PPM, body.getPosition().y * GameWorld.PPM);
        getSprite().setPosition(position.x - getSprite().getWidth() / 2, position.y - getSprite().getHeight() / 2);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    public static float getShootTime() {
        return 0f;
    }

    public static Texture getTexture() {
        return texture;
    }

    public BulletShootingFlag getBulletShootingFlag() {
        return bulletShootingFlag;
    }

    public void setBulletShootingFlag(BulletShootingFlag bulletShootingFlag) {
        this.bulletShootingFlag = bulletShootingFlag;
    }

    @Override
    public void update() {
        super.update();
        this.setDestroy(!bulletShootingFlag.isBulletIsShooting());
    }

    @Override
    public Sound getShootSound() {
        return shootSound;
    }
}
