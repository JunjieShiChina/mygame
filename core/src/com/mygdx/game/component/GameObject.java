package com.mygdx.game.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.component.shader.RedShader;
import com.mygdx.game.utils.GameManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameObject {
    private World world;

    private Texture texture;
    private TextureRegion textureRegion;
    private Sprite sprite;

    public Body body;
    public Vector2 velocity;
    public Vector2 position;

    private boolean blink = false;
    private float blinkElapsedTime = 0f;

    private boolean isDestroy = false;
    private boolean isExplosionFinished = false;

    private static Texture explosionTexture = new Texture("explosion.png");
    ;

    private Map<Sprite, Float> explosions = new HashMap<Sprite, Float>();

    private int singleFrameWidth = 128;
    private int singleFrameHeight = 128;

    public GameObject(World world, Texture texture, float positionX, float positionY, float xSpeed, float ySpeed, float rotation, float scaleX, float scaleY) {
        this.world = world;

        this.texture = texture;
        sprite = new Sprite(texture);
        position = new Vector2(positionX - sprite.getWidth() / 2, positionY);

        // 创建刚体定义
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x / GameWorld.PPM, position.y / GameWorld.PPM);

        // 创建形状
        PolygonShape shape = new PolygonShape();
        float width = sprite.getWidth() / 2f * scaleX;
        float height = sprite.getHeight() / 2f * scaleY;
        shape.setAsBox(width / GameWorld.PPM, height / GameWorld.PPM);

        // 创建刚体夹具定义
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        // 将刚体添加到世界中
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(this);

        // 销毁形状
        shape.dispose();

        velocity = new Vector2(xSpeed, ySpeed);

        sprite.setScale(scaleX, scaleY);
        sprite.setRotation(rotation);

    }

    public GameObject(World world, TextureRegion textureRegion, float positionX, float positionY, float xSpeed, float ySpeed, float rotation, float scaleX, float scaleY) {
        this.world = world;

        this.textureRegion = textureRegion;
        sprite = new Sprite(textureRegion);
        position = new Vector2(positionX - sprite.getWidth() / 2, positionY);

        // 创建刚体定义
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x / GameWorld.PPM, position.y / GameWorld.PPM);

        // 创建形状
        PolygonShape shape = new PolygonShape();
        float width = sprite.getWidth() / 2f * scaleX;
        float height = sprite.getHeight() / 2f * scaleY;
        shape.setAsBox(width / GameWorld.PPM, height / GameWorld.PPM);

        // 创建刚体夹具定义
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        // 将刚体添加到世界中
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(this);

        // 销毁形状
        shape.dispose();

        velocity = new Vector2(xSpeed, ySpeed);

        sprite.setScale(scaleX, scaleY);
        sprite.setRotation(rotation);
    }

    public void takeHit(float damage, Vector2 hitPosition) {
        isDestroy = true;
        explode(hitPosition);
    }

    protected void explode(Vector2 explodePosition) {
        // 创建爆炸效果的精灵
        Sprite explosionSprite = new Sprite(explosionTexture);
        explosionSprite.setBounds(explodePosition.x - singleFrameWidth, explodePosition.y - singleFrameHeight, singleFrameWidth * 2, singleFrameHeight * 2);
        explosionSprite.setPosition(explodePosition.x - singleFrameWidth, explodePosition.y - singleFrameHeight);
        explosionSprite.setSize(singleFrameWidth*2, singleFrameHeight*2);

        // 将爆炸效果的精灵添加到游戏世界中
        explosions.put(explosionSprite, 0f);

    }

    // 更新并绘制所有爆炸效果的精灵
    public void renderExplosions(SpriteBatch batch) {
        Array<TextureRegion> explosionFrames = new Array<TextureRegion>();
        for (int i = 0; i < 10; i++) {
            explosionFrames.add(new TextureRegion(explosionTexture, i * singleFrameWidth, 0, singleFrameWidth, singleFrameHeight));
        }

        float frameDuration = 0.1f;
        Animation<TextureRegion> explosionAnimation = new Animation<TextureRegion>(frameDuration, explosionFrames);

        List<Sprite> finishedExplosions = new ArrayList<Sprite>();

        for (Map.Entry<Sprite, Float> entry : explosions.entrySet()) {
            Sprite explosion = entry.getKey();
            float stateTime = entry.getValue();

            // 更新精灵的帧
            explosion.setRegion(explosionAnimation.getKeyFrame(stateTime, false));
            stateTime += Gdx.graphics.getDeltaTime();

            // 绘制精灵
            explosion.draw(batch);

            // 如果精灵的动画已经播放完毕，则将其添加到已完成爆炸列表中
            if (explosionAnimation.isAnimationFinished(stateTime)) {
                finishedExplosions.add(explosion);
            }
            // 更新explosions中的stateTime值
            explosions.put(explosion, stateTime);
        }

        // 从explosions中移除已完成的爆炸
        for (Sprite finishedExplosion : finishedExplosions) {
            explosions.remove(finishedExplosion);
        }

        if (explosions.isEmpty()) {
            isExplosionFinished = true;
        }
    }

    protected void updatePosition() {
        // 计算飞机的朝向向量
        Vector2 directionVector = velocity.cpy().rotateDeg(sprite.getRotation());

        // 将速度向量和朝向向量相加得到移动向量
        Vector2 moveVector = velocity.cpy().scl(Gdx.graphics.getDeltaTime()).add(directionVector);

        // 更新飞机的位置和精灵的位置
        position.add(moveVector);
        body.setTransform(position.x / GameWorld.PPM, position.y / GameWorld.PPM, sprite.getRotation() * MathUtils.degreesToRadians);
        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
    }

    protected void blink() {
        blink = true;
    }

    public void update() {
        if (!isDestroy) {
            updatePosition();
        }
    }

    public void render(SpriteBatch batch) {
        if (!isDestroy) {
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
            this.getSprite().draw(batch);
            batch.setShader(null);
        }
        if (!explosions.isEmpty()) {
            renderExplosions(batch);
        }
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

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public boolean isDestroy() {
        return isDestroy;
    }

    public void setDestroy(boolean destroy) {
        isDestroy = destroy;
    }

    public boolean isExplosionFinished() {
        return isExplosionFinished;
    }

    public void setExplosionFinished(boolean explosionFinished) {
        isExplosionFinished = explosionFinished;
    }

    public static Texture getExplosionTexture() {
        return explosionTexture;
    }

    public static void setExplosionTexture(Texture explosionTexture) {
        GameObject.explosionTexture = explosionTexture;
    }

    public int getSingleFrameWidth() {
        return singleFrameWidth;
    }

    public int getSingleFrameHeight() {
        return singleFrameHeight;
    }

    public Map<Sprite, Float> getExplosions() {
        return explosions;
    }
}
