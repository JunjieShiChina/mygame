package com.mygdx.game.component.bullet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.BulletFactory;
import com.mygdx.game.component.GameWorld;
import com.mygdx.game.inter.Shooter;
import com.mygdx.game.utils.GameManager;

public enum BulletTypeEnum {
    B1(Bullet1.getTexture()),B3(Bullet3.getTexture()),B5(Bullet5.getTexture()),B6(Bullet6.getTexture()), BARROW(BulletArrow.getTexture());

    private Texture texture;

    BulletTypeEnum(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void shoot(World world, Shooter shooter, Body shooterBody, Sprite shootSprite) {
        if (this == B1) {
            Bullet bullet = BulletFactory.createBullet(world, shooter, B1);

            float offSetX = 0.12f;
            if (shooterBody.getPosition().x <= 0.41f) {
                offSetX -= 0.1f;
            }
            if (shooterBody.getPosition().y >= 12.58f) {
                offSetX += 0.1f;
            }

            // 设置子弹的初始位置为飞机头部
            bullet.body.setTransform(((shooterBody.getPosition().x - offSetX) * GameWorld.PPM + shootSprite.getWidth() / 2) / GameWorld.PPM,
                    ((shooterBody.getPosition().y + 0.7f) * GameWorld.PPM + shootSprite.getHeight() / 2) / GameWorld.PPM, 0);

            GameManager.addBullet(bullet);
            bullet.getShootSound().play();
        }

        if (this == B3) {
            Bullet bullet = BulletFactory.createBullet(world, shooter, B3);

            float offSetX = 0.12f;
            if (shooterBody.getPosition().x <= 0.41f) {
                offSetX -= 0.1f;
            }
            if (shooterBody.getPosition().y >= 12.58f) {
                offSetX += 0.1f;
            }

            // 设置子弹的初始位置为飞机头部
            bullet.body.setTransform(((shooterBody.getPosition().x - offSetX) * GameWorld.PPM + shootSprite.getWidth() / 2) / GameWorld.PPM,
                    ((shooterBody.getPosition().y + 0.7f) * GameWorld.PPM + shootSprite.getHeight() / 2) / GameWorld.PPM, 0);
            bullet.position.set(bullet.body.getPosition());

            GameManager.addBullet(bullet);
            bullet.getShootSound().play();
        }

        if (this == B5) {
            Bullet bullet1 = BulletFactory.createBullet(world, shooter, B5);
            Bullet bullet2 = BulletFactory.createBullet(world, shooter, B5);

            float offSetX = 0.12f;
            if (shooterBody.getPosition().x <= 0.41f) {
                offSetX -= 0.1f;
            }
            if (shooterBody.getPosition().y >= 12.58f) {
                offSetX += 0.1f;
            }

            // 设置子弹的初始位置为飞机头部
            bullet1.body.setTransform(((shooterBody.getPosition().x + 0.2f - offSetX) * GameWorld.PPM + shootSprite.getWidth() / 2) / GameWorld.PPM,
                    ((shooterBody.getPosition().y + 0.7f) * GameWorld.PPM + shootSprite.getHeight() / 2) / GameWorld.PPM, 0);

            bullet2.body.setTransform(((shooterBody.getPosition().x - 0.2f - offSetX) * GameWorld.PPM + shootSprite.getWidth() / 2) / GameWorld.PPM,
                    ((shooterBody.getPosition().y + 0.7f) * GameWorld.PPM + shootSprite.getHeight() / 2) / GameWorld.PPM, 0);

            GameManager.addBullet(bullet1);
            GameManager.addBullet(bullet2);


            bullet1.getShootSound().play();
        }

        if (this == B6) {
            Bullet bullet1 = BulletFactory.createBullet(world, shooter, B6);
            Bullet bullet2 = BulletFactory.createBullet(world, shooter, B6);
            Bullet bullet3 = BulletFactory.createBullet(world, shooter, B6);
            Bullet bullet4 = BulletFactory.createBullet(world, shooter, B6);
            Bullet bullet5 = BulletFactory.createBullet(world, shooter, B6);

            float offSetX = 0.12f;
            if (shooterBody.getPosition().x <= 0.41f) {
                offSetX -= 0.1f;
            }
            if (shooterBody.getPosition().y >= 12.58f) {
                offSetX += 0.1f;
            }

            // 设置子弹的初始位置为飞机头部
            bullet1.body.setTransform(((shooterBody.getPosition().x + 0.2f - offSetX) * GameWorld.PPM + shootSprite.getWidth() / 2) / GameWorld.PPM,
                    ((shooterBody.getPosition().y + 0.7f) * GameWorld.PPM + shootSprite.getHeight() / 2) / GameWorld.PPM, 0);
            bullet2.body.setTransform(((shooterBody.getPosition().x + 0.1f - offSetX) * GameWorld.PPM + shootSprite.getWidth() / 2) / GameWorld.PPM,
                    ((shooterBody.getPosition().y + 0.7f) * GameWorld.PPM + shootSprite.getHeight() / 2) / GameWorld.PPM, 0);
            bullet3.body.setTransform(((shooterBody.getPosition().x - offSetX) * GameWorld.PPM + shootSprite.getWidth() / 2) / GameWorld.PPM,
                    ((shooterBody.getPosition().y + 0.7f) * GameWorld.PPM + shootSprite.getHeight() / 2) / GameWorld.PPM, 0);
            bullet4.body.setTransform(((shooterBody.getPosition().x - 0.1f - offSetX) * GameWorld.PPM + shootSprite.getWidth() / 2) / GameWorld.PPM,
                    ((shooterBody.getPosition().y + 0.7f) * GameWorld.PPM + shootSprite.getHeight() / 2) / GameWorld.PPM, 0);
            bullet5.body.setTransform(((shooterBody.getPosition().x - 0.2f - offSetX) * GameWorld.PPM + shootSprite.getWidth() / 2) / GameWorld.PPM,
                    ((shooterBody.getPosition().y + 0.7f) * GameWorld.PPM + shootSprite.getHeight() / 2) / GameWorld.PPM, 0);

            GameManager.addBullet(bullet1);
            GameManager.addBullet(bullet2);
            GameManager.addBullet(bullet3);
            GameManager.addBullet(bullet4);
            GameManager.addBullet(bullet5);

            bullet1.getShootSound().play();
        }
        if (this == BARROW) {
            Bullet bullet1 = BulletFactory.createBullet2(world, shooter, BARROW, 90, 45);
            Bullet bullet2 = BulletFactory.createBullet2(world, shooter, BARROW, 0, -45);
            Bullet bullet3 = BulletFactory.createBullet2(world, shooter, BARROW, 45, 0);

            float offSetX = 0.12f;
            if (shooterBody.getPosition().x <= 0.41f) {
                offSetX -= 0.1f;
            }
            if (shooterBody.getPosition().y >= 12.58f) {
                offSetX += 0.1f;
            }

            // 设置子弹的初始位置为飞机头部
            bullet1.body.setTransform(((shooterBody.getPosition().x + 0.1f - offSetX) * GameWorld.PPM + shootSprite.getWidth() / 2) / GameWorld.PPM,
                    ((shooterBody.getPosition().y + 0.7f) * GameWorld.PPM + shootSprite.getHeight() / 2) / GameWorld.PPM, 0);
            bullet2.body.setTransform(((shooterBody.getPosition().x - 0.1f - offSetX) * GameWorld.PPM + shootSprite.getWidth() / 2) / GameWorld.PPM,
                    ((shooterBody.getPosition().y + 0.7f) * GameWorld.PPM + shootSprite.getHeight() / 2) / GameWorld.PPM, 0);
            bullet3.body.setTransform(((shooterBody.getPosition().x - offSetX) * GameWorld.PPM + shootSprite.getWidth() / 2) / GameWorld.PPM,
                    ((shooterBody.getPosition().y + 0.7f) * GameWorld.PPM + shootSprite.getHeight() / 2) / GameWorld.PPM, 0);


            GameManager.addBullet(bullet1);
            GameManager.addBullet(bullet2);
            GameManager.addBullet(bullet3);

            bullet1.getShootSound().play();
        }
    }

    public float getShootTime() {
        if (this == B1) {
            return Bullet1.getShootTime();
        }
        if (this == B3) {
            return Bullet3.getShootTime();
        }
        if (this == B5) {
            return Bullet5.getShootTime();
        }
        if (this == B6) {
            return Bullet6.getShootTime();
        }
        if (this == BARROW) {
            return BulletArrow.getShootTime();
        }
        return 0.1f;
    }
}
