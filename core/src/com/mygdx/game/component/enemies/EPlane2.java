package com.mygdx.game.component.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.ai.EnemyPlaneAI;
import com.mygdx.game.component.bullet.Bullet;
import com.mygdx.game.component.bullet.Bullet2;
import com.mygdx.game.component.pickup.Gold;
import com.mygdx.game.component.pickup.Gold1;
import com.mygdx.game.component.pickup.Gold2;
import com.mygdx.game.utils.GameManager;

import java.util.Random;

public class EPlane2 extends EnemyPlane {

    private static Texture texture;
    private EnemyPlaneAI enemyPlaneAI = new EnemyPlaneAI(this);

    static {
        texture = new Texture("Spaceship_Enemy - DualShot.png");
    }

    public EPlane2(World world, float positionX, float positionY, float xSpeed, float ySpeed, float rotation, float scaleX, float scaleY) {
        super(world, texture, positionX, positionY, xSpeed, ySpeed, rotation, scaleX, scaleY);
        setShootTime(0.5f);
    }

    @Override
    public void update() {
        super.update();
        enemyPlaneAI.update(getPlane());
    }

    @Override
    Bullet loadBullet() {
        return new Bullet2(super.getBody().getWorld(), -180, 180, this);
    }

    public static int getTextureWidth() {
        return texture.getWidth();
    }

    @Override
    public void shoot() {
        if (isDestroy()) {
            return;
        }

        Bullet bullet = loadBullet();
        // 设置子弹的初始位置为飞机头部往前一点
        float offsetX = (float) Math.cos(Math.toRadians(super.getSprite().getRotation())) * 0.000001f;
        float offsetY = (float) Math.sin(Math.toRadians(super.getSprite().getRotation())) * 0.000001f;
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
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (isExplosionFinished() && isDestroy()) {
            GameManager.addGold(generateGold());
        }
    }

    /**
     * 有一定概率爆银币
     * @return
     */
    public Gold generateGold() {
        Random rand = new Random();
        // 生成一个[0,100)的随机数，如果小于30，生成银币
        if (rand.nextInt(100) < 40) {
            return new Gold2(getBody().getWorld(), getPosition().x + 60, getPosition().y, getVelocity().x/2, getVelocity().y/2);
        } else {
            return null;
        }
    }
}
