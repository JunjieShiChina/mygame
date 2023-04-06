package com.mygdx.game.component.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.ai.EnemyPlaneAI;
import com.mygdx.game.component.bullet.Bullet;
import com.mygdx.game.component.bullet.Bullet4;
import com.mygdx.game.utils.GameManager;

public class Boss1 extends BOSS {

    private static Texture texture;
    private EnemyPlaneAI enemyPlaneAI = new EnemyPlaneAI(this);

    static {
        texture = new Texture("boss1.png");
    }

    public Boss1(World world, float positionX, float positionY, float xSpeed, float ySpeed, float rotation, float scaleX, float scaleY) {
        super(world, texture, positionX, positionY, xSpeed, ySpeed, rotation, scaleX, scaleY);
        setShootTime(0.5f);
    }

    @Override
    public void shoot() {
        if (isDestroy()) {
            return;
        }

        Bullet bullet1 = loadBullet();
       // 设置子弹的初始位置为飞机头部往前一点
        float offsetX = (float) Math.cos(Math.toRadians(super.getSprite().getRotation())) * 0.0001f - 0.13f;
        float offsetY = (float) Math.sin(Math.toRadians(super.getSprite().getRotation())) * 0.0001f - 0.5f;
        bullet1.body.setTransform(
                body.getPosition().x + offsetX,
                body.getPosition().y + offsetY,
                0
        );

        // 获取敌机的旋转角度，并将其转换为弧度
        float angleRad = (float) Math.toRadians(super.getSprite().getRotation());
        // 使用三角函数计算出X和Y方向上的速度分量
        float bulletSpeedX = (float) Math.cos(angleRad) * 5f;
        float bulletSpeedY = -(float) Math.sin(angleRad) * 5f;

        bullet1.body.setLinearVelocity(bulletSpeedX, bulletSpeedY);

        Bullet bullet0 = new Bullet4(super.getBody().getWorld(), 0, this, 1, 1, 5, 195);
        // 设置子弹的初始位置为飞机头部往前一点
        bullet0.body.setTransform(
                body.getPosition().x + offsetX,
                body.getPosition().y + offsetY,
                0
        );

        bullet0.body.setLinearVelocity(bulletSpeedX, bulletSpeedY);

        Bullet bullet2 = new Bullet4(super.getBody().getWorld(), 0, this, 1, 1, 5, 205);
        // 设置子弹的初始位置为飞机头部往前一点
        bullet2.body.setTransform(
                body.getPosition().x + offsetX,
                body.getPosition().y + offsetY,
                0
        );

        bullet2.body.setLinearVelocity(bulletSpeedX, bulletSpeedY);

        Bullet bullet3 = new Bullet4(super.getBody().getWorld(), 0, this, 1, 1, 5, 165);
        // 设置子弹的初始位置为飞机头部往前一点
        bullet3.body.setTransform(
                body.getPosition().x + offsetX,
                body.getPosition().y + offsetY,
                0
        );

        bullet3.body.setLinearVelocity(bulletSpeedX, bulletSpeedY);

        Bullet bullet4 = new Bullet4(super.getBody().getWorld(), 0, this, 1, 1, 5, 150);
        // 设置子弹的初始位置为飞机头部往前一点
        bullet4.body.setTransform(
                body.getPosition().x + offsetX,
                body.getPosition().y + offsetY,
                0
        );

        bullet4.body.setLinearVelocity(bulletSpeedX, bulletSpeedY);


        GameManager.addBullet(bullet0);
        GameManager.addBullet(bullet1);
        GameManager.addBullet(bullet2);
        GameManager.addBullet(bullet3);
        GameManager.addBullet(bullet4);

        bullet1.getShootSound().play();
    }

    @Override
    Bullet loadBullet() {
        return new Bullet4(super.getBody().getWorld(), 0, this, 1, 1, 5, 180);
    }

    @Override
    public void update() {
        super.update();
        enemyPlaneAI.boss1AIUpdate(getPlane());
    }
}
