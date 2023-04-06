package com.mygdx.game.component.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.contact.listener.GameWorldContactListener;
import com.mygdx.game.utils.*;

import static com.mygdx.game.component.GameWorld.*;

public class Stage1 implements Stage {

    private boolean isStagePass = false;
    private boolean isStageFailed = false;
    private boolean isStageOver = false;

    private World world;
    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Sprite background1;
    private Sprite background2;

    private static final float BACKGROUND_SCROLL_SPEED = 200f;
    private Sprite backgroundStar1;
    private Sprite backgroundStar2;

    private Texture backgroundTexture;
    private Texture backgroundStarTexture;
    private float starAlpha = 1;

    private float asteroidTimer = 0; // 计时器
    private float asteroidInterval = 5f; // 生成陨石的间隔时间

    // 如果当前关通过, 5秒后进入下一关
    float enterNextStageTime = 5f;
    float enterNextStageElapsedTime = 0;

    @Override
    public void create() {
        Gdx.graphics.setWindowedMode(frameWidth, frameHeight);
        camera = new OrthographicCamera(frameWidth, frameHeight);
        camera.setToOrtho(false, frameWidth, frameHeight);
        viewport = new ExtendViewport(frameWidth, frameHeight, camera);
        viewport.apply();
        world = new World(new Vector2(0, 0), true); // 创建一个空的2D世界，重力为0
        batch = new SpriteBatch();

        createScreenBoundaries(world);
        createBackGround();

        GameManager.createPlane(world);
        EnemyPlaneManager.plane = GameManager.getPlane();

        world.setContactListener(new GameWorldContactListener());

        GameManager.playBgSound();
    }

    private void createBackGround() {
        backgroundTexture = new Texture("bkblue.png");
        background1 = new Sprite(backgroundTexture);
        background1.setSize(frameWidth, frameHeight);
        background1.setPosition(0, 0);
        background2 = new Sprite(backgroundTexture);
        background2.setSize(frameWidth, frameHeight);
        background2.setPosition(0, background1.getHeight());

        backgroundStarTexture = new Texture("Stars.png");
        backgroundStar1 = new Sprite(backgroundStarTexture);
        backgroundStar1.setSize(frameWidth, frameHeight);
        backgroundStar1.setPosition(0, 0);
        backgroundStar1.setAlpha(starAlpha);

        backgroundStar2 = new Sprite(backgroundStarTexture);
        backgroundStar2.setSize(frameWidth, frameHeight);
        backgroundStar2.setPosition(0, 0);
        backgroundStar2.setAlpha(starAlpha);

    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1); // 设置背景色为黑色

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        if (isStagePass) {
            if (GameManager.getPlane().position.y > frameHeight) {
                enterNextStageElapsedTime += Gdx.graphics.getDeltaTime();
            }
        }

        // 更新计时器
        asteroidTimer += Gdx.graphics.getDeltaTime();
        if (asteroidTimer >= asteroidInterval) {
            AsteroidsManager.randomGenerateAsteroids(world, MathUtils.random(1, 3));
            asteroidTimer -= asteroidInterval; // 重置计时器
        }

        EnemyPlaneManager.update(world);
        GoodsManager.update(world);
        GameManager.update();

        batch.begin();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        renderBackground(batch);

        // 在 render() 方法中添加以下代码：
        starAlpha = MathUtils.random(0.3f, 10f); // 随机生成一个透明度值
        backgroundStar1.setAlpha(starAlpha); // 将透明度值赋给 Sprite 对象
        backgroundStar2.setAlpha(starAlpha); // 将透明度值赋给 Sprite 对象

        // 在这里添加您要渲染的游戏对象
        GameManager.render(batch);

        UserDataManager.renderUserData(batch);
        batch.end();

        // 清理超出下边界和左右边界的物体
        GameManager.clearOffScreenBody(world);

        float deltaTime = Gdx.graphics.getDeltaTime();
        world.step(deltaTime, 6, 2);

        if (enterNextStageElapsedTime > enterNextStageTime) {
            GameManager.changeStage();
        }
    }

    private void renderBackground(SpriteBatch batch) {
        // 更新背景位置
        float deltaTime = Gdx.graphics.getDeltaTime();
        background1.setY(background1.getY() - BACKGROUND_SCROLL_SPEED * deltaTime);
        background2.setY(background2.getY() - BACKGROUND_SCROLL_SPEED * deltaTime);

        backgroundStar1.setY(backgroundStar1.getY() - BACKGROUND_SCROLL_SPEED * deltaTime);
        backgroundStar2.setY(backgroundStar2.getY() - BACKGROUND_SCROLL_SPEED * deltaTime);

        // 如果背景1移到屏幕底部，将其重置到背景2的顶部
        if (background1.getY() + background1.getHeight() <= 0) {
            background1.setY(background2.getY() + background2.getHeight());
        }

        // 如果背景1移到屏幕底部，将其重置到背景2的顶部
        if (backgroundStar1.getY() + backgroundStar1.getHeight() <= 0) {
            backgroundStar1.setY(backgroundStar2.getY() + backgroundStar2.getHeight());
        }

        // 如果背景2移到屏幕底部，将其重置到背景1的顶部
        if (background2.getY() + background2.getHeight() <= 0) {
            background2.setY(background1.getY() + background1.getHeight());
        }

        // 如果背景2移到屏幕底部，将其重置到背景1的顶部
        if (backgroundStar2.getY() + backgroundStar2.getHeight() <= 0) {
            backgroundStar2.setY(backgroundStar1.getY() + backgroundStar1.getHeight());
        }

        background1.draw(batch);
        background2.draw(batch);
        backgroundStar1.draw(batch);
        backgroundStar2.draw(batch);
    }

    @Override
    public void dispose() {
        batch.dispose();
        GameManager.disposeAll();
        backgroundTexture.dispose();
        UserDataManager.dispose();
    }

    private void createScreenBoundaries(World world) {
        // 左右边界
        for (int i = 0; i < 2; i++) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((i == 0 ? 20 : frameWidth-20) / PPM, frameHeight / 2 / PPM);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(10f / PPM, frameHeight / 2 / PPM);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;

            Body body = world.createBody(bodyDef);
            body.createFixture(fixtureDef);

            body.setUserData(this);

        }

        // 上下边界
        for (int i = 0; i < 2; i++) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(frameWidth / 2 / PPM, (i == 0 ? 0 : frameHeight) / PPM);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(frameWidth / 2 / PPM, 10f / PPM);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;

            Body body = world.createBody(bodyDef);
            body.createFixture(fixtureDef);

            body.setUserData(this);
        }
    }

    @Override
    public boolean isStagePass() {
        return isStagePass;
    }

    @Override
    public void setStagePass(boolean stagePass) {
        isStagePass = stagePass;
    }

    @Override
    public boolean isStageFailed() {
        return isStageFailed;
    }

    @Override
    public void setStageFailed(boolean stageFailed) {
        isStageFailed = stageFailed;
    }

    @Override
    public boolean isStageOver() {
        return isStageOver;
    }

    @Override
    public void setStageOver(boolean stageOver) {
        isStageOver = stageOver;
    }

    @Override
    public void clear() {
        GameManager.clear();
    }
}
