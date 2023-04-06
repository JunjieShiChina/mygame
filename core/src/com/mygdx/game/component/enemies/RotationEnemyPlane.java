package com.mygdx.game.component.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.ai.EnemyPlaneAI;
import com.mygdx.game.component.bullet.Bullet;
import com.mygdx.game.component.bullet.Bullet2;
import com.mygdx.game.component.pickup.Gold;
import com.mygdx.game.component.pickup.Gold2;
import com.mygdx.game.utils.GameManager;

import java.util.Random;

public class RotationEnemyPlane extends EnemyPlane {

    private static Texture texture = new Texture("rotation_enemy.png");
    private EnemyPlaneAI enemyPlaneAI = new EnemyPlaneAI(this);

    private float revolveSpeed = 0;

    public RotationEnemyPlane(World world, float positionX, float positionY, float xSpeed, float ySpeed, float rotation, float scaleX, float scaleY) {
        super(world, texture, positionX, positionY, xSpeed, ySpeed, rotation, scaleX, scaleY);
        setShootTime(0.1f);
    }

    @Override
    public void update() {
        super.update();
        enemyPlaneAI.rotationEnemyPlaneUpdate(getPlane());
    }

    @Override
    public void shoot() {
        if (isDestroy()) {
            return;
        }
        // 向4个方向发射4颗子弹，会随着怪物的旋转，一起旋转
        int tempRadians = 90;
        for(int i = 0; i < 4; i++) {
            Bullet2 bullet = new Bullet2(super.getBody().getWorld(), -180, tempRadians * i, this);
            // 设置子弹的初始位置为飞机头部往前一点
            float offsetX = (float) Math.cos(Math.toRadians(super.getSprite().getRotation() + tempRadians * i)) * 0.000001f;
            float offsetY = (float) Math.sin(Math.toRadians(super.getSprite().getRotation() + tempRadians * i)) * 0.000001f;
            bullet.body.setTransform(
                    body.getPosition().x + offsetX,
                    body.getPosition().y + offsetY,
                    0
            );

            // 获取敌机的旋转角度，并将其转换为弧度
            float angleRad = (float) Math.toRadians(super.getSprite().getRotation() + tempRadians * i);
            // 使用三角函数计算出X和Y方向上的速度分量
            float bulletSpeedX = (float) Math.cos(angleRad) * 5f;
            float bulletSpeedY = -(float) Math.sin(angleRad) * 5f; // 将 bull

            bullet.body.setLinearVelocity(bulletSpeedX, bulletSpeedY);
            GameManager.addBullet(bullet);
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (isExplosionFinished() && isDestroy()) {
            GameManager.addGold(generateGold());
        }
    }

    @Override
    Bullet loadBullet() {
        return null;
    }


    /**
     * 有一定概率爆银币
     * @return
     */
    public Gold generateGold() {
        Random rand = new Random();
        // 生成一个[0,100)的随机数，如果小于30，生成银币
        if (rand.nextInt(100) < 80) {
            return new Gold2(getBody().getWorld(), getPosition().x + 60, getPosition().y, getVelocity().x/2, getVelocity().y/2);
        } else {
            return null;
        }
    }

    public float getRevolveSpeed() {
        return revolveSpeed;
    }

    public void setRevolveSpeed(float revolveSpeed) {
        this.revolveSpeed = revolveSpeed;
    }
}
