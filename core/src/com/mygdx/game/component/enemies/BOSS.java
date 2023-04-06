package com.mygdx.game.component.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.component.bullet.Bullet;
import com.mygdx.game.utils.GameManager;
import com.mygdx.game.utils.UserDataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BOSS extends EnemyPlane {

    private boolean bossExplodeFinished = false;
    private float bossExplodeElapsedTime = 0f;

    private static Sound bossExplodeSound = Gdx.audio.newSound(Gdx.files.internal("bossexplode.mp3"));

    private Map<Sprite, Float> bossExplosions = new HashMap<Sprite, Float>();

    public BOSS(World world, Texture texture, float positionX, float positionY, float xSpeed, float ySpeed, float rotation, float scaleX, float scaleY) {
        super(world, texture, positionX, positionY, xSpeed, ySpeed, rotation, scaleX, scaleY);
    }

    @Override
    Bullet loadBullet() {
        return null;
    }

    @Override
    public void takeHit(float damage, Vector2 hitPosition) {
        if(UserDataManager.reduceBossBlood(damage)) {
            blink();
            explode(hitPosition);
        } else {
            // BOSS被击败
            this.setDestroy(true);
            bossExplode();
        }

    }

    /**
     * boss爆炸特效
     */
    private void bossExplode() {
        bossExplodeSound.play();
        // 创建爆炸效果的精灵
        Sprite explosionSprite = new Sprite(getExplosionTexture());
        explosionSprite.setBounds(position.x - getSingleFrameWidth(), position.y - getSingleFrameHeight(), getSingleFrameWidth() * 2, getSingleFrameHeight() * 2);
        explosionSprite.setPosition(position.x - getSingleFrameWidth(), position.y - getSingleFrameHeight());
        explosionSprite.setSize(getSingleFrameWidth()*2, getSingleFrameHeight()*2);

        // 将爆炸效果的精灵添加到游戏世界中
        bossExplosions.put(explosionSprite, 0f);

        explosionSprite = new Sprite(getExplosionTexture());
        explosionSprite.setBounds(position.x - getSingleFrameWidth()*2, position.y - getSingleFrameHeight(), getSingleFrameWidth() * 2, getSingleFrameHeight() * 2);
        explosionSprite.setPosition(position.x - getSingleFrameWidth()*2, position.y - getSingleFrameHeight());
        explosionSprite.setSize(getSingleFrameWidth()*2, getSingleFrameHeight()*2);

        // 将爆炸效果的精灵添加到游戏世界中
        bossExplosions.put(explosionSprite, 0f);

        explosionSprite = new Sprite(getExplosionTexture());
        explosionSprite.setBounds(position.x, position.y - getSingleFrameHeight(), getSingleFrameWidth() * 2, getSingleFrameHeight() * 2);
        explosionSprite.setPosition(position.x, position.y - getSingleFrameHeight());
        explosionSprite.setSize(getSingleFrameWidth()*2, getSingleFrameHeight()*2);

        // 将爆炸效果的精灵添加到游戏世界中
        bossExplosions.put(explosionSprite, 0f);

        explosionSprite = new Sprite(getExplosionTexture());
        explosionSprite.setBounds(position.x - getSingleFrameWidth(), position.y - getSingleFrameHeight()*2, getSingleFrameWidth() * 2, getSingleFrameHeight() * 2);
        explosionSprite.setPosition(position.x - getSingleFrameWidth(), position.y - getSingleFrameHeight()*2);
        explosionSprite.setSize(getSingleFrameWidth()*2, getSingleFrameHeight()*2);

        // 将爆炸效果的精灵添加到游戏世界中
        bossExplosions.put(explosionSprite, 0f);

    }

    public boolean isBossExplodeFinished() {
        return bossExplodeFinished;
    }

    public void setBossExplodeFinished(boolean bossExplodeFinished) {
        this.bossExplodeFinished = bossExplodeFinished;
    }

    // 更新并绘制所有爆炸效果的精灵(炸3s)
    public void renderBossExplosions(SpriteBatch batch) {
        bossExplodeElapsedTime += Gdx.graphics.getDeltaTime();
        Array<TextureRegion> explosionFrames = new Array<TextureRegion>();
        for (int i = 0; i < 10; i++) {
            explosionFrames.add(new TextureRegion(getExplosionTexture(), i * getSingleFrameWidth(), 0, getSingleFrameWidth(), getSingleFrameHeight()));
        }

        float frameDuration = 0.1f;
        Animation<TextureRegion> explosionAnimation = new Animation<TextureRegion>(frameDuration, explosionFrames);

        List<Sprite> finishedExplosions = new ArrayList<Sprite>();

        for (Map.Entry<Sprite, Float> entry : bossExplosions.entrySet()) {
            Sprite explosion = entry.getKey();
            float stateTime = entry.getValue();

            // 更新精灵的帧
            explosion.setRegion(explosionAnimation.getKeyFrame(stateTime, true));
            stateTime += Gdx.graphics.getDeltaTime();

            // 绘制精灵
            explosion.draw(batch);

            // 如果精灵的动画已经播放完毕，则将其添加到已完成爆炸列表中
            if (explosionAnimation.isAnimationFinished(stateTime)) {
                finishedExplosions.add(explosion);
            }
            // 更新explosions中的stateTime值
            bossExplosions.put(explosion, stateTime);
        }

        if (bossExplodeElapsedTime > 3) {
            // 从explosions中移除已完成的爆炸
            for (Sprite finishedExplosion : finishedExplosions) {
                bossExplosions.remove(finishedExplosion);
            }
            bossExplodeElapsedTime = 0;
        }

        if (bossExplosions.isEmpty()) {
            bossExplodeFinished = true;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        if (!bossExplosions.isEmpty()) {
            renderBossExplosions(batch);
        }
    }


}
