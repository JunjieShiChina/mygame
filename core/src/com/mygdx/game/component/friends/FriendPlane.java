package com.mygdx.game.component.friends;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.GameObject;
import com.mygdx.game.component.GameWorld;
import com.mygdx.game.component.ai.FriendPlaneAI;
import com.mygdx.game.component.bullet.BulletLaser;
import com.mygdx.game.inter.Shooter;
import com.mygdx.game.utils.BulletShootingFlag;
import com.mygdx.game.utils.GameManager;


public class FriendPlane extends GameObject implements Shooter {

    private BulletShootingFlag bulletShootingFlag = new BulletShootingFlag();

    private boolean shooted = true;
    private float shootElapsedTime = 0;
    private float bulletTimer = 0;
    private static Texture texture = new Texture("friendplane.png");
    private FriendPlaneAI friendPlaneAI;

    public FriendPlane(World world, float positionX, float positionY, float xSpeed, float ySpeed, float rotation) {
        super(world, texture, positionX, positionY, xSpeed, ySpeed, rotation, 1, 1);
        this.getBody().getFixtureList().get(0).setSensor(true);
        friendPlaneAI = new FriendPlaneAI(this, GameManager.getPlane());
    }

    @Override
    public void update() {
        super.update();
        friendPlaneAI.update();
    }

    public static Texture getTexture() {
        return texture;
    }

    @Override
    public void render(SpriteBatch batch) {
        bulletTimer += Gdx.graphics.getDeltaTime();
        if (bulletTimer >= BulletLaser.getShootTime()) {
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
        if (GameManager.getPlane().isDoShoot()) {
            shootElapsedTime += Gdx.graphics.getDeltaTime();
            if (shootElapsedTime > 0.5 && !shooted) {
                this.bulletShootingFlag.setBulletIsShooting(true);
                BulletLaser bullet = new BulletLaser(super.getBody().getWorld(), 0, 0, this);
                bullet.setBulletShootingFlag(this.bulletShootingFlag);
                // 设置子弹的初始位置为飞机头部往前一点
                float offsetX = (float) Math.cos(Math.toRadians(super.getSprite().getRotation())) * 0.0001f;
                // 设置子弹的初始位置为飞机头部
                bullet.body.setTransform(((body.getPosition().x - offsetX) * GameWorld.PPM + getSprite().getWidth() / 2) / GameWorld.PPM,
                        ((body.getPosition().y + 0.7f) * GameWorld.PPM + getSprite().getHeight() / 2) / GameWorld.PPM, 0);

                GameManager.addBullet(bullet);
                bullet.getShootSound().play();
                shooted = true;
            }
            if (shootElapsedTime > 1.5) {
                bulletShootingFlag.setBulletIsShooting(false);
                shootElapsedTime = 0;
                shooted = false;
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        GameManager.checkFriendPlaneBulletAndRemove(this);
    }
}
