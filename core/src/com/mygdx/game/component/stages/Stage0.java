package com.mygdx.game.component.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.component.Plane;
import com.mygdx.game.controls.PlaneInputProcessor;
import com.mygdx.game.utils.AsteroidsManager;
import com.mygdx.game.utils.FontTextureUtils;
import com.mygdx.game.utils.GameManager;
import com.mygdx.game.utils.UserDataManager;

import static com.mygdx.game.component.GameWorld.frameHeight;
import static com.mygdx.game.component.GameWorld.frameWidth;

public class Stage0 implements Stage{

    private boolean stagePass = false;

    private static Texture backgroundTexture = new Texture("Background_01.png");
    private static Texture starBackgroundTexture = new Texture("stars.png");
    private static Sound bgSound = Gdx.audio.newSound(Gdx.files.internal("stagebg.mp3"));
    private static Sound gameStartSound = Gdx.audio.newSound(Gdx.files.internal("gameStart.wav"));

    float asteroidTimer = 0;
    float asteroidInterval = 2;

    float startTimer = 0;
    float startBlinkTime = 0.5f;
    // 如果当前关通过, 1秒后进入下一关
    float enterNextStageTime = 1f;
    float enterNextStageElapsedTime = 0;

    private World world;
    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    @Override
    public void create() {
        Gdx.graphics.setWindowedMode(frameWidth, frameHeight);
        camera = new OrthographicCamera(frameWidth, frameHeight);
        camera.setToOrtho(false, frameWidth, frameHeight);
        viewport = new ExtendViewport(frameWidth, frameHeight, camera);
        viewport.apply();
        batch = new SpriteBatch();

        world = new World(new Vector2(0, 0), true);
        GameManager.createPlane(world);
        Plane plane = GameManager.getPlane();
        plane.setDoShoot(false);
        plane.body.setTransform(new Vector2(2, 3), 0);

        UserDataManager.setFriendPlaneCount(2);

        Gdx.input.setInputProcessor(new PlaneInputProcessor());
        bgSound.loop();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1); // 设置背景色为黑色
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        if (stagePass) {
            startBlinkTime = 0.1f;
            enterNextStageElapsedTime += Gdx.graphics.getDeltaTime();
        }

        // 更新计时器
        asteroidTimer += Gdx.graphics.getDeltaTime();
        if (asteroidTimer >= asteroidInterval) {
            AsteroidsManager.randomGenerateAsteroids(world, MathUtils.random(1, 3));
            asteroidTimer -= asteroidInterval; // 重置计时器
        }

        GameManager.update();

        batch.begin();

        renderBackground(batch);
        GameManager.render(batch);
        renderSpaceHero(batch);
        renderStart(batch);

        batch.end();

        // 清理超出下边界和左右边界的物体
        GameManager.clearOffScreenBody(world);

        float deltaTime = Gdx.graphics.getDeltaTime();
        world.step(deltaTime, 6, 2);

        if (enterNextStageElapsedTime > enterNextStageTime) {
            bgSound.stop();
            GameManager.changeStage();
        }
    }

    private void renderStart(SpriteBatch batch) {
        startTimer += Gdx.graphics.getDeltaTime();
        if (startTimer > startBlinkTime) {

            int fontWidth = 60;
            int fontHeight = 60;
            int startX = 300;
            int height = 650;
            int spaceSize = 0;
            // P
            TextureRegion textureRegion = FontTextureUtils.getDigitsTexture()[2][5];
            Sprite sprite = new Sprite(textureRegion);

            sprite.setSize(fontWidth, fontHeight);
            sprite.setPosition(startX, height);
            sprite.draw(batch);

            // R
            textureRegion = FontTextureUtils.getDigitsTexture()[2][7];
            sprite = new Sprite(textureRegion);
            sprite.setSize(fontWidth, fontHeight);
            sprite.setPosition(startX + fontWidth - spaceSize, height);
            sprite.draw(batch);

            // E
            textureRegion = FontTextureUtils.getDigitsTexture()[1][4];
            sprite = new Sprite(textureRegion);
            sprite.setSize(fontWidth, fontHeight);
            sprite.setPosition(startX + fontWidth * 2 - spaceSize * 2, height);
            sprite.draw(batch);

            // S
            textureRegion = FontTextureUtils.getDigitsTexture()[2][8];
            sprite = new Sprite(textureRegion);
            sprite.setSize(fontWidth, fontHeight);
            sprite.setPosition(startX + fontWidth * 3 - spaceSize * 3, height);
            sprite.draw(batch);

            // S
            textureRegion = FontTextureUtils.getDigitsTexture()[2][8];
            sprite = new Sprite(textureRegion);
            sprite.setSize(fontWidth, fontHeight);
            sprite.setPosition(startX + fontWidth * 4 - spaceSize * 4, height);
            sprite.draw(batch);


            // E
            textureRegion = FontTextureUtils.getDigitsTexture()[1][4];
            sprite = new Sprite(textureRegion);
            sprite.setSize(fontWidth, fontHeight);
            sprite.setPosition(startX + fontWidth * 6 - spaceSize * 6, height);
            sprite.draw(batch);

            // N
            textureRegion = FontTextureUtils.getDigitsTexture()[2][3];
            sprite = new Sprite(textureRegion);
            sprite.setSize(fontWidth, fontHeight);
            sprite.setPosition(startX + fontWidth * 7 - spaceSize * 7, height);
            sprite.draw(batch);

            // T
            textureRegion = FontTextureUtils.getDigitsTexture()[2][9];
            sprite = new Sprite(textureRegion);
            sprite.setSize(fontWidth, fontHeight);
            sprite.setPosition(startX + fontWidth * 8 - spaceSize * 8, height);
            sprite.draw(batch);

            // E
            textureRegion = FontTextureUtils.getDigitsTexture()[1][4];
            sprite = new Sprite(textureRegion);
            sprite.setSize(fontWidth, fontHeight);
            sprite.setPosition(startX + fontWidth * 9 - spaceSize * 9, height);
            sprite.draw(batch);

            // R
            textureRegion = FontTextureUtils.getDigitsTexture()[2][7];
            sprite = new Sprite(textureRegion);
            sprite.setSize(fontWidth, fontHeight);
            sprite.setPosition(startX + fontWidth * 10 - spaceSize * 10, height);
            sprite.draw(batch);
        }
        if (startTimer > startBlinkTime*2) {
            startTimer = 0;
        }
    }


    private void renderSpaceHero(SpriteBatch batch) {
        int fontWidth = 100;
        int fontHeight = 250;
        int startX = 150;
        int height = 800;
        int spaceSize = 0;
        // S
        TextureRegion textureRegion = FontTextureUtils.getDigitsTexture()[2][8];
        Sprite sprite = new Sprite(textureRegion);
        sprite.setSize(fontWidth, fontHeight);
        sprite.setPosition(startX, height);
        sprite.draw(batch);

        // P
        textureRegion = FontTextureUtils.getDigitsTexture()[2][5];
        sprite = new Sprite(textureRegion);
        sprite.setSize(fontWidth, fontHeight);
        sprite.setPosition(startX + fontWidth - spaceSize, height);
        sprite.draw(batch);

        // A
        textureRegion = FontTextureUtils.getDigitsTexture()[1][0];
        sprite = new Sprite(textureRegion);
        sprite.setSize(fontWidth, fontHeight);
        sprite.setPosition(startX + fontWidth * 2 - spaceSize * 2, height);
        sprite.draw(batch);

        // C
        textureRegion = FontTextureUtils.getDigitsTexture()[1][2];
        sprite = new Sprite(textureRegion);
        sprite.setSize(fontWidth, fontHeight);
        sprite.setPosition(startX + fontWidth * 3 - spaceSize * 3, height);
        sprite.draw(batch);

        // E
        textureRegion = FontTextureUtils.getDigitsTexture()[1][4];
        sprite = new Sprite(textureRegion);
        sprite.setSize(fontWidth, fontHeight);
        sprite.setPosition(startX + fontWidth * 4 - spaceSize * 4, height);
        sprite.draw(batch);

        // H
        textureRegion = FontTextureUtils.getDigitsTexture()[1][7];
        sprite = new Sprite(textureRegion);
        sprite.setSize(fontWidth, fontHeight);
        sprite.setPosition(startX + fontWidth * 6 - spaceSize * 5, height);
        sprite.draw(batch);

        // E
        textureRegion = FontTextureUtils.getDigitsTexture()[1][4];
        sprite = new Sprite(textureRegion);
        sprite.setSize(fontWidth, fontHeight);
        sprite.setPosition(startX + fontWidth * 7 - spaceSize * 6, height);
        sprite.draw(batch);

        // R
        textureRegion = FontTextureUtils.getDigitsTexture()[2][7];
        sprite = new Sprite(textureRegion);
        sprite.setSize(fontWidth, fontHeight);
        sprite.setPosition(startX + fontWidth * 8 - spaceSize * 6, height);
        sprite.draw(batch);

        // O
        textureRegion = FontTextureUtils.getDigitsTexture()[2][4];
        sprite = new Sprite(textureRegion);
        sprite.setSize(fontWidth, fontHeight);
        sprite.setPosition(startX + fontWidth * 9 - spaceSize * 6, height);
        sprite.draw(batch);

    }

    public void renderBackground(SpriteBatch batch) {
        Sprite sprite = new Sprite(backgroundTexture);
        sprite.setSize(frameWidth, frameHeight);
        sprite.setPosition(0, 0);
        sprite.draw(batch);

        Sprite starSprite = new Sprite(starBackgroundTexture);
        starSprite.setAlpha(MathUtils.random(0.3f, 10f));
        starSprite.setSize(frameWidth, frameHeight);
        starSprite.setPosition(0, 0);
        starSprite.draw(batch);
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean isStagePass() {
        return stagePass;
    }

    @Override
    public void setStagePass(boolean stagePass) {
        gameStartSound.play();
        this.stagePass = stagePass;
    }

    @Override
    public boolean isStageFailed() {
        return false;
    }

    @Override
    public void setStageFailed(boolean stageFailed) {

    }

    @Override
    public boolean isStageOver() {
        return false;
    }

    @Override
    public void setStageOver(boolean stageOver) {

    }

    @Override
    public void clear() {
        UserDataManager.clear();
        GameManager.clear();
    }
}
