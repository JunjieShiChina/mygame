package com.mygdx.game.component.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.GameObject;
import com.mygdx.game.component.Plane;
import com.mygdx.game.component.bullet.Bullet;
import com.mygdx.game.inter.Shooter;
import com.mygdx.game.utils.GameManager;

public abstract class EnemyPlane extends GameObject implements Shooter {

    private static Sound explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));

    // 主角
    private Plane plane;
    private float bulletTimer = 0;
    private float shootTime = 1;

    public EnemyPlane(World world, Texture texture, float positionX, float positionY, float xSpeed, float ySpeed, float rotation, float scaleX, float scaleY) {
        super(world, texture, positionX, positionY, xSpeed, ySpeed, rotation, scaleX, scaleY);
        this.getBody().getFixtureList().get(0).setSensor(false);
    }

    public EnemyPlane(World world, TextureRegion textureRegion, float positionX, float positionY, float xSpeed, float ySpeed, float rotation, float scaleX, float scaleY) {
        super(world, textureRegion, positionX, positionY, xSpeed, ySpeed, rotation, scaleX, scaleY);
        this.getBody().getFixtureList().get(0).setSensor(false);
    }

    /**
     * 设置主角飞机
     */
    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    @Override
    public void explode(Vector2 explodePosition) {
        super.explode(explodePosition);
        explosionSound.play();
    }

    @Override
    public void render(SpriteBatch batch) {
        bulletTimer += Gdx.graphics.getDeltaTime();
        if (bulletTimer >= shootTime) {
            bulletTimer = 0;
            shoot();
        }

        super.render(batch);
    }

    @Override
    public void shoot() {
        if (isDestroy()) {
            return;
        }

        Bullet bullet = loadBullet();
        // 设置子弹的初始位置为飞机头部往前一点
        float offsetX = (float) Math.cos(Math.toRadians(super.getSprite().getRotation())) * 0.0001f;
        float offsetY = (float) Math.sin(Math.toRadians(super.getSprite().getRotation())) * 0.0001f;
        bullet.body.setTransform(
                body.getPosition().x + offsetX,
                body.getPosition().y + offsetY,
                0
        );

        // 获取敌机的旋转角度，并将其转换为弧度
        float angleRad = (float) Math.toRadians(super.getSprite().getRotation());
        // 使用三角函数计算出X和Y方向上的速度分量
        float bulletSpeedX = (float) Math.cos(angleRad) * 5f;
        float bulletSpeedY = -(float) Math.sin(angleRad) * 5f; // 将 bulletSpeedY 设为负数

        bullet.body.setLinearVelocity(bulletSpeedX, bulletSpeedY);
        GameManager.addBullet(bullet);
    }

    abstract Bullet loadBullet();


    @Override
    public void dispose() {
        super.dispose();
    }

    public Plane getPlane() {
        return plane;
    }

    public float getShootTime() {
        return shootTime;
    }

    public void setShootTime(float shootTime) {
        this.shootTime = shootTime;
    }
}
