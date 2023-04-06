package com.mygdx.game.component.pickup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utils.UserDataManager;

public abstract class Gold extends PickUp {

    // 金币只存活5S
    private float livingTime = 0f;
    private float elapsedTime = 0f;
    private boolean isBlinking = false;

    private static Sound getCoinSound = Gdx.audio.newSound(Gdx.files.internal("getcoin.wav"));

    public Gold(World world, Texture texture, float positionX, float positionY, float xSpeed, float ySpeed, float rotation, float scaleX, float scaleY) {
        super(world, texture, positionX, positionY, xSpeed, ySpeed, rotation, scaleX, scaleY);
        this.getBody().getFixtureList().get(0).setSensor(true);
    }

    @Override
    public void update() {
        if (!isDestroy()) {
            livingTime += Gdx.graphics.getDeltaTime();
        }
        super.update();
        if (livingTime > 5 && !isDestroy()) {
            this.setDestroy(true);
            livingTime = 0;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (isDestroy()) {
            dispose();
            return;
        }
        elapsedTime += Gdx.graphics.getDeltaTime();
        if (elapsedTime >= 0.2f) { // 控制闪烁频率
            elapsedTime = 0f;
            isBlinking = !isBlinking; // 切换是否显示
        }

        if (isBlinking) {
            batch.setColor(Color.RED); // 设置绘制颜色为红色
            super.render(batch); // 显示金币
            batch.setColor(Color.WHITE); // 恢复默认颜色
        }
    }

    public void playGetSound() {
        getCoinSound.play();
    }

    public void taked() {
        if (!isDestroy()) {
            playGetSound();
            setDestroy(true);
            UserDataManager.addCoin(this);
        }

    }

    public abstract Integer getPrice();
}

