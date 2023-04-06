package com.mygdx.game.component.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.bullet.Bullet;
import com.mygdx.game.component.bullet.Bullet2;
import com.mygdx.game.component.pickup.Gold;
import com.mygdx.game.component.pickup.Gold1;
import com.mygdx.game.utils.GameManager;


import java.util.Random;

public class ShipPlane extends EnemyPlane {

    private static Texture texture = new Texture("Spaceship_Enemy - SingleShot.png");

    public ShipPlane(World world, float positionX, float positionY, float xSpeed, float ySpeed, float rotation, float scaleX, float scaleY) {
        super(world, texture, positionX, positionY, xSpeed, ySpeed, rotation, scaleX, scaleY);
    }

    public static int getTextureWidth() {
        return texture.getWidth();
    }

    @Override
    Bullet loadBullet() {
        return new Bullet2(super.getBody().getWorld(), 0, 180, this);
    }

    @Override
    public void shoot() {
        if (isDestroy()) {
            return;
        }

        Bullet bullet = loadBullet();
        // 设置子弹的初始位置为飞机头部往前一点
        float offsetX = (float) Math.cos(Math.toRadians(super.getSprite().getRotation())) * 0.000001f;
        float offsetY = (float) Math.sin(Math.toRadians(super.getSprite().getRotation())) - 0.5f;
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

    @Override
    public void explode(Vector2 explodePosition) {
        super.explode(explodePosition);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (isExplosionFinished() && isDestroy()) {
            GameManager.addGold(generateGold());
        }
    }

    /**
     * 有一定概率爆铜币
     * @return
     */
    public Gold generateGold() {
        Random rand = new Random();
        // 生成一个[0,100)的随机数，如果小于30，生成金币
        if (rand.nextInt(100) < 50) {
            return new Gold1(getBody().getWorld(), getPosition().x + 60, getPosition().y, getVelocity().x/2, getVelocity().y/2);
        } else {
            return null;
        }
    }
}
