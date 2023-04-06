package com.mygdx.game.component.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.component.GameWorld;
import com.mygdx.game.inter.Shooter;

public abstract class Bullet {
    private Sprite sprite;
    private Texture texture;
    private World world;

    public Body body;
    public Vector2 velocity;
    public Vector2 position;
    public boolean isDestroy = false;

    /**
     * 伤害力
     */
    private float attackPower;
    public Shooter shooter;

    private static Sound shootSound = Gdx.audio.newSound(Gdx.files.internal("buqiang.mp3"));

    public Bullet(World world, Texture texture, float rotation, Shooter shooter, float attackPower, float scaleX, float scaleY, float bulletSpeed, float bulletAngle) {
        this.world = world;
        this.shooter = shooter;

        this.texture = texture;
        this.attackPower = attackPower;
        sprite = new Sprite(texture);
        sprite.setRotation(rotation);
        sprite.setScale(scaleX, scaleY);
        position = new Vector2(800 - sprite.getWidth() / 2, 10);

        // 创建子弹的刚体定义
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x / GameWorld.PPM, position.y / GameWorld.PPM);

        // 创建子弹的形状
        PolygonShape shape = new PolygonShape();
        float width = sprite.getWidth() / 2f * scaleX;
        float height = sprite.getHeight() / 2f * scaleY;
        shape.setAsBox(width / GameWorld.PPM, height / GameWorld.PPM);

        // 创建刚体夹具定义
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        // 将刚体添加到世界中
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(this);
        body.setBullet(true);

        // 销毁形状
        shape.dispose();

        // 获取射击者的机头朝向向量
        float shooterRotation = shooter.getSprite().getRotation();
        float angleRad = (float) Math.toRadians(shooterRotation + bulletAngle);
        Vector2 headingVector = new Vector2((float) Math.cos(angleRad), (float) Math.sin(angleRad));

        // 将子弹速度向量设置为指定的速度
        velocity = new Vector2(0, bulletSpeed);

        // 将子弹速度向量旋转机头朝向的角度
        velocity.rotateDeg(headingVector.angleDeg());
    }

    public Sound getShootSound() {
        return shootSound;
    }


    public void update() {
        updatePosition();
    }

    public void render(SpriteBatch batch) {
        if (isDestroy) {
            dispose();
            return;
        }
        sprite.draw(batch);
    }

    public void updatePosition() {
        // 更新子弹的位置和精灵的位置
        body.setLinearVelocity(velocity);
        position.set(body.getPosition().x * GameWorld.PPM, body.getPosition().y * GameWorld.PPM);
        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
    }

    public void dispose() {
        world.destroyBody(this.body);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setDestroy(boolean destroy) {
        isDestroy = destroy;
    }

    public float getAttackPower() {
        return attackPower;
    }

    public Body getBody() {
        return body;
    }
}
