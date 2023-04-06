package com.mygdx.game.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.component.bullet.BulletTypeEnum;
import com.mygdx.game.component.shader.RedShader;
import com.mygdx.game.controls.PlaneInputProcessor;
import com.mygdx.game.inter.Shooter;
import com.mygdx.game.utils.GameManager;
import com.mygdx.game.utils.UserDataManager;

public class Plane implements Shooter {
    private boolean destroy = false;

    private World world;

    private Sprite sprite;

    private static Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.wav"));

    private boolean blink = false;
    private float blinkElapsedTime = 0f;
    private boolean doShoot = true;

    public Body body;
    public Vector2 velocity;
    public Vector2 position;

    private float bulletTimer = 0;

    public Plane(World world) {
        this.world = world;

        Texture texture = new Texture("Spaceship_Protagonist - P1.png"); // 请确保您的项目中有一个名为"plane.png"的飞机图片文件
        sprite = new Sprite(texture);
        sprite.setScale(3, 3);
        position = new Vector2(800 - sprite.getWidth() / 2, 50);

        // 创建飞机的刚体定义
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x/ GameWorld.PPM, (position.y)/ GameWorld.PPM);

        // 创建飞机的形状
        PolygonShape shape = new PolygonShape();
        float width = sprite.getWidth()/3;
        float height = sprite.getHeight();
        shape.setAsBox(width/ GameWorld.PPM, height/ GameWorld.PPM);

        // 创建刚体夹具定义
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1.0f;
        fixtureDef.isSensor = false;
        fixtureDef.shape = shape;

        // 将刚体添加到世界中
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(this);

        // 销毁形状
        shape.dispose();

        velocity = new Vector2(0, 0);

        Gdx.input.setInputProcessor(new PlaneInputProcessor());

    }

    public void render(SpriteBatch batch) {
        if (blink) {
            if (Math.round(GameManager.timeSinceStart * 10) % 2 == 0) {
                batch.setShader(RedShader.getShader());
            }
            blinkElapsedTime += Gdx.graphics.getDeltaTime();
            if (blinkElapsedTime > 1) {
                blink = false;
                blinkElapsedTime = 0;
            }
        }
        sprite.draw(batch);
        batch.setShader(null);

        bulletTimer += Gdx.graphics.getDeltaTime();
        if (bulletTimer >= getShootTime()) {
            bulletTimer = 0;
            shoot();
        }

    }

    public void update() {
        // 将飞机移动到新位置
        updatePosition();
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }

    public void updatePosition() {
        // 更新飞机的位置和精灵的位置
        body.setLinearVelocity(velocity);
        position.set(body.getPosition().x * GameWorld.PPM, body.getPosition().y * GameWorld.PPM);
        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
    }

    @Override
    public void shoot() {
        if (doShoot) {
            getBulletType().shoot(world, this, body, sprite);
        }
    }

    public BulletTypeEnum getBulletType() {
        BulletTypeEnum curBulletType = UserDataManager.getCurBulletType();
        if (curBulletType == null) {
            curBulletType = BulletTypeEnum.B1;
        }
        return curBulletType;
    }

    public float getShootTime() {
        return getBulletType().getShootTime();
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public Vector2 getPosition() {
        return this.position;
    }

    @Override
    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public Body getBody() {
        return body;
    }


    /**
     * 闪烁的时候不受伤害
     * @param damage
     */
    public void takeHit(float damage) {
        if (!blink) {
            UserDataManager.reduceBlood(damage);
            hurtSound.play();
            blink();
        }
    }

    private void blink() {
        blink = true;
    }


    /**
     * 通关关卡
     */
    public void passStage() {
        Gdx.input.setInputProcessor(null);
        doShoot = false;
        velocity.set(0, 5);
        this.body.getFixtureList().get(0).setSensor(true);
    }

    public boolean isDoShoot() {
        return doShoot;
    }

    public void setDoShoot(boolean doShoot) {
        this.doShoot = doShoot;
    }

    public boolean isDestroy() {
        return destroy;
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }
}
