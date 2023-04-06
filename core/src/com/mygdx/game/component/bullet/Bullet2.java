package com.mygdx.game.component.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.inter.Shooter;

public class Bullet2 extends Bullet{

    private static Texture totalBulletTexture;
    private static Texture texture;
    private static Animation<TextureRegion> bulletAnimation;
    private static float elapsedTime = 0;
    private static Sound shootSound = Gdx.audio.newSound(Gdx.files.internal("buqiang.mp3"));

    static {
        totalBulletTexture = new Texture("pixelbullets.png");
        int bulletWidth = 16;
        int bulletHeight = 16;
        int bulletsPerRow = 4;
        int bulletsPerColumn = 4;

        TextureRegion[][] textureRegions = TextureRegion.split(totalBulletTexture, bulletWidth, bulletHeight);

        // 提取第二排的4个子弹并创建新的纹理，将其大小设置为原来的三倍
        TextureRegion[] bulletFrames = new TextureRegion[bulletsPerRow];
        for (int i = 0; i < bulletsPerRow; i++) {
            TextureRegion textureRegion = textureRegions[1][i];
            totalBulletTexture.getTextureData().prepare();
            Pixmap originalPixmap = totalBulletTexture.getTextureData().consumePixmap();
            Pixmap enlargedPixmap = new Pixmap(bulletWidth * 2, bulletHeight * 2, originalPixmap.getFormat());
            enlargedPixmap.drawPixmap(originalPixmap, textureRegion.getRegionX(), textureRegion.getRegionY(), bulletWidth, bulletHeight, 0, 0, bulletWidth * 2, bulletHeight * 2);
            Texture enlargedTexture = new Texture(enlargedPixmap);
            enlargedPixmap.dispose();
            originalPixmap.dispose();
            bulletFrames[i] = new TextureRegion(enlargedTexture, 0, 0, bulletWidth * 2, bulletHeight * 2);
        }

        bulletAnimation = new Animation<TextureRegion>(0.3f, bulletFrames);
        bulletAnimation.setPlayMode(Animation.PlayMode.LOOP);

        texture = bulletAnimation.getKeyFrame(0).getTexture();
    }


    public Bullet2(World world, float rotation, float bulletAngle, Shooter shooter) {
        super(world, texture, rotation, shooter, 0.5f, 2, 2, 5, bulletAngle);
    }

    @Override
    public void update() {
        super.update();
        texture = bulletAnimation.getKeyFrame(elapsedTime, true).getTexture();
        elapsedTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void render(SpriteBatch batch) {
        if (isDestroy) {
            dispose();
            return;
        }
        batch.draw(bulletAnimation.getKeyFrame(elapsedTime), this.getSprite().getX(), this.getSprite().getY());
    }

    public static float getShootTime() {
        return 1;
    }

    @Override
    public  Sound getShootSound() {
        return shootSound;
    }
}
