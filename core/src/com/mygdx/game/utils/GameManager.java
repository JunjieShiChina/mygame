package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.component.Asteroids;
import com.mygdx.game.component.GameObject;
import com.mygdx.game.component.GameWorld;
import com.mygdx.game.component.Plane;
import com.mygdx.game.component.bullet.Bullet;
import com.mygdx.game.component.bullet.BulletLaser;
import com.mygdx.game.component.enemies.BOSS;
import com.mygdx.game.component.enemies.EnemyPlane;
import com.mygdx.game.component.friends.FriendPlane;
import com.mygdx.game.component.pickup.Gold;
import com.mygdx.game.component.pickup.Goods;
import com.mygdx.game.component.shader.RedShader;
import com.mygdx.game.component.stages.Stage;
import com.mygdx.game.component.stages.Stage0;
import com.mygdx.game.component.stages.Stage1;
import com.mygdx.game.component.stages.StageOver;

import java.util.Iterator;

public class GameManager {

    public static Stage curStage = new Stage0();

    public static float timeSinceStart = 0;

    private static Plane plane;

    private static float warningElapsedTime = 0;

    private static float passElapsedTime = 0;

    private static Array<EnemyPlane> enemyPlanes = new Array<>();

    private static Array<Bullet> bullets = new Array<>();

    private static Array<Gold> golds = new Array<>();

    private static Array<Asteroids> asteroidsArray = new Array<>();

    private static Array<Goods> goods = new Array<>();

    private static Array<FriendPlane> friendPlanes = new Array<>();

    private static BOSS boss1;

    private static boolean bossTime = false;

    private static boolean warningOver = true;

    private static boolean preCreateBOSS = false;

    private static boolean passStageTriggered = false;

    private static Sound bossFightSound = Gdx.audio.newSound(Gdx.files.internal("bossfight.mp3"));
    private static Sound warningSound = Gdx.audio.newSound(Gdx.files.internal("warning.wav"));
    private static Sound bgSound = Gdx.audio.newSound(Gdx.files.internal("bgsound.mp3"));
    private static Sound passStageSound = Gdx.audio.newSound(Gdx.files.internal("passstage.wav"));
    private static Sound failedSound = Gdx.audio.newSound(Gdx.files.internal("failed.wav"));
    private static Sound shoppingSound = Gdx.audio.newSound(Gdx.files.internal("shopping.mp3"));

    public static void update() {
        timeSinceStart += Gdx.graphics.getDeltaTime();

        plane.update();

        // 更新敌机状态
        for (EnemyPlane enemyPlane : GameManager.getEnemyPlanes()) {
            enemyPlane.update();
        }

        if (boss1 != null && !boss1.isDestroy()) {
            boss1.update();
        }

        // 更新子弹
        for (Bullet bullet : GameManager.getBullets()) {
            bullet.update();
        }

        for (Gold gold : golds) {
            gold.update();
        }

        for (Asteroids asteroids : asteroidsArray) {
            asteroids.update();
        }

        for (Goods goods : goods) {
            goods.update();
        }

        for (FriendPlane friendPlane : friendPlanes) {
            friendPlane.update();
        }

        if (curStage.isStagePass() && !passStageTriggered) {
            passElapsedTime += Gdx.graphics.getDeltaTime();
            if (passElapsedTime > 5) {
                bgSound.stop();
                passStageSound.play();
                plane.passStage();
                passStageTriggered = true;
            }
        }
    }

    /**
     * 刷新小飞机
     * @param friendPlaneCount
     */
    private static void refreshFriendPlanes(int friendPlaneCount) {
        Array.ArrayIterator<FriendPlane> iterator = friendPlanes.iterator();
        while(iterator.hasNext()) {
            FriendPlane friendPlane = iterator.next();
            friendPlane.setDestroy(true);
            Array.ArrayIterator<Bullet> bulletArrayIterator = bullets.iterator();
            while(bulletArrayIterator.hasNext()) {
                Bullet bullet = bulletArrayIterator.next();
                if (bullet.shooter == friendPlane) {
                    bullet.setDestroy(true);
                    bullet.dispose();
                }
                bulletArrayIterator.remove();
            }
            friendPlane.dispose();
            iterator.remove();
        }
        if (friendPlaneCount == 1) {
            FriendPlane friendPlane = new FriendPlane(plane.body.getWorld(), plane.position.x + 100, 0, 0, 0, 0);
            friendPlanes.add(friendPlane);
        }

        if (friendPlaneCount == 2) {
            FriendPlane friendPlane = new FriendPlane(plane.body.getWorld(), plane.position.x + 100, 0, 0, 0, 0);
            friendPlanes.add(friendPlane);
            friendPlane = new FriendPlane(plane.body.getWorld(), plane.position.x - 100, 0, 0, 0, 0);
            friendPlanes.add(friendPlane);
        }

    }

    public static void render(SpriteBatch batch) {
        Iterator<Asteroids> asteroidsIterator = asteroidsArray.iterator();
        while (asteroidsIterator.hasNext()) {
            Asteroids asteroids = asteroidsIterator.next();
            asteroids.render(batch);
            if (asteroids.isDestroy()) {
                asteroidsIterator.remove();
            }
        }

        UserDataManager.renderBossBlood(batch);

        Iterator<Bullet> bulletIterator = GameManager.getBullets().iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.render(batch);
            if (bullet.isDestroy) {
                bulletIterator.remove();
            }
        }

        plane.render(batch);

        // 更新小飞机
        if (friendPlanes.size != UserDataManager.getFriendPlaneCount()) {
            refreshFriendPlanes(UserDataManager.getFriendPlaneCount());
        }
        Iterator<FriendPlane> friendPlaneIterator = friendPlanes.iterator();
        while (friendPlaneIterator.hasNext()) {
            FriendPlane friendPlane = friendPlaneIterator.next();
            if (!friendPlane.isExplosionFinished() || !friendPlane.isDestroy()) {
                friendPlane.render(batch);
                continue;
            }
            friendPlane.dispose();
            friendPlaneIterator.remove();
        }

        Iterator<EnemyPlane> iterator = GameManager.getEnemyPlanes().iterator();
        while (iterator.hasNext()) {
            EnemyPlane enemyPlane = iterator.next();
            // 在这里执行你需要对每个 enemyPlane 进行的操作
            if (!enemyPlane.isExplosionFinished() || !enemyPlane.isDestroy()) {
                enemyPlane.render(batch);
                continue;
            }
            enemyPlane.dispose();
            iterator.remove();
        }

        if (boss1 != null && (!boss1.isDestroy() || !boss1.isBossExplodeFinished())) {
            boss1.render(batch);
        } else if (boss1 != null) {
            boss1.dispose();
            boss1 = null;
            bossFightSound.stop();
            curStage.setStagePass(true);
        }

        Iterator<Gold> goldIterator = golds.iterator();
        while (goldIterator.hasNext()) {
            Gold gold = goldIterator.next();
            gold.render(batch);
            if (gold.isDestroy()) {
                goldIterator.remove();
            }
        }

        Array.ArrayIterator<Goods> goodsIterator = goods.iterator();
        while (goodsIterator.hasNext()) {
            Goods goods = goodsIterator.next();
            goods.render(batch);
            if (goods.isDestroy()) {
                goodsIterator.remove();
            }
        }


        if (!warningOver) {
            warningElapsedTime += Gdx.graphics.getDeltaTime();
            if (warningElapsedTime < 10) {
                renderWarning(batch);
            } else {
                warningSound.stop();
                warningOver = true;
                warningElapsedTime = 0;
                preCreateBOSS = true;
            }
        }

    }

    /**
     * 渲染警告
     */
    private static void renderWarning(SpriteBatch batch) {
        int fontSize = 150;
        int spaceSize = 10;
        int startX = 100;
        int height = GameWorld.frameHeight / 2;

        batch.setShader(RedShader.getShader());

        boolean shouldDraw = (int) timeSinceStart % 2 == 0;
        if (shouldDraw) {
            // W
            TextureRegion textureRegion = FontTextureUtils.getDigitsTexture()[3][2];
            Sprite sprite = new Sprite(textureRegion);
            sprite.setSize(fontSize, fontSize);
            sprite.setPosition(startX, height);
            sprite.draw(batch);

            // A
            textureRegion = FontTextureUtils.getDigitsTexture()[1][0];
            sprite = new Sprite(textureRegion);
            sprite.setSize(fontSize, fontSize);
            sprite.setPosition(startX + fontSize - spaceSize, height);
            sprite.draw(batch);

            // R
            textureRegion = FontTextureUtils.getDigitsTexture()[2][7];
            sprite = new Sprite(textureRegion);
            sprite.setSize(fontSize, fontSize);
            sprite.setPosition(startX + fontSize * 2 - spaceSize * 2, height);
            sprite.draw(batch);

            // N
            textureRegion = FontTextureUtils.getDigitsTexture()[2][3];
            sprite = new Sprite(textureRegion);
            sprite.setSize(fontSize, fontSize);
            sprite.setPosition(startX + fontSize * 3 - spaceSize * 3, height);
            sprite.draw(batch);

            // I
            textureRegion = FontTextureUtils.getDigitsTexture()[1][8];
            sprite = new Sprite(textureRegion);
            sprite.setSize(fontSize, fontSize);
            sprite.setPosition(startX + fontSize * 4 - spaceSize * 4, height);
            sprite.draw(batch);

            // N
            textureRegion = FontTextureUtils.getDigitsTexture()[2][3];
            sprite = new Sprite(textureRegion);
            sprite.setSize(fontSize, fontSize);
            sprite.setPosition(startX + fontSize * 5 - spaceSize * 5, height);
            sprite.draw(batch);

            // G
            textureRegion = FontTextureUtils.getDigitsTexture()[1][6];
            sprite = new Sprite(textureRegion);
            sprite.setSize(fontSize, fontSize);
            sprite.setPosition(startX + fontSize * 6 - spaceSize * 6, height);
            sprite.draw(batch);
        }
        batch.setShader(null);
    }

    public static void addEnemyPlane(EnemyPlane enemyPlane) {
        enemyPlanes.add(enemyPlane);
    }

    public static void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public static Array<EnemyPlane> getEnemyPlanes() {
        return enemyPlanes;
    }

    public static Array<Bullet> getBullets() {
        return bullets;
    }

    public static void addGold(Gold gold) {
        if (gold == null) {
            return;
        }
        golds.add(gold);
    }

    public static void addAsteroids(Asteroids asteroids) {
        asteroidsArray.add(asteroids);
    }

    public static void clearOffScreenBody(World world) {
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);

        for (Body body : bodies) {
            Object userData = body.getUserData();
            if (userData instanceof GameWorld) {
                continue;
            }
            Vector2 currentPosition = body.getPosition();
            if (currentPosition.x < 0 || // 超出左边界
                    currentPosition.x > GameWorld.frameWidth / GameWorld.PPM || // 超出右边界
                    currentPosition.y < 0 || // 超出下边界
                    currentPosition.y > GameWorld.frameHeight / GameWorld.PPM + 5) { // 超出上边界5的
                if (userData instanceof GameObject && !(userData instanceof FriendPlane)) {
                    GameObject gameObject = (GameObject) userData;
                    gameObject.setExplosionFinished(true);
                    gameObject.setDestroy(true);
                }
                if (userData instanceof Bullet && !(userData instanceof BulletLaser)) {
                    Bullet bullet = (Bullet) userData;
                    bullet.setDestroy(true);
                }
            }
        }
    }

    public static void disposeAll() {
        for (EnemyPlane enemyPlane : GameManager.getEnemyPlanes()) {
            enemyPlane.dispose();
        }

        for (Bullet bullet : GameManager.getBullets()) {
            bullet.dispose();
        }

        for (Gold gold : golds) {
            gold.dispose();
        }

        for (Asteroids asteroids : asteroidsArray) {
            asteroids.dispose();
        }

        for (Goods goodsItem : goods) {
            goodsItem.dispose();
        }

        plane.dispose();
    }

    public static void setBoss1(BOSS boss1) {
        GameManager.boss1 = boss1;
    }

    public static EnemyPlane getBoss1() {
        return boss1;
    }

    public static boolean isBossTime() {
        return bossTime;
    }

    public static void setBossTime(boolean bossTime) {
        GameManager.bossTime = bossTime;
    }

    public static void showWarning() {
        warningOver = false;
        warningSound.loop();
    }

    public static boolean isWarningOver() {
        return warningOver;
    }

    public static void setWarningOver(boolean warningOver) {
        GameManager.warningOver = warningOver;
    }

    public static boolean isPreCreateBOSS() {
        return preCreateBOSS;
    }

    public static void setPreCreateBOSS(boolean preCreateBOSS) {
        GameManager.preCreateBOSS = preCreateBOSS;
    }

    public static void switchBossFightBgSound() {
        bgSound.stop();
        bossFightSound.loop();
    }

    public static void playBgSound() {
        bgSound.loop();
    }

    public static void createPlane(World world) {
        plane = new Plane(world);
    }

    public static Plane getPlane() {
        return plane;
    }

    public static float getTimeSinceStart() {
        return timeSinceStart;
    }

    public static void setTimeSinceStart(float timeSinceStart) {
        GameManager.timeSinceStart = timeSinceStart;
    }

    public static void addGoods(Goods addGoods) {
        goods.add(addGoods);
    }

    public static Sound getFailedSound() {
        return failedSound;
    }

    public static Sound getShoppingSound() {
        return shoppingSound;
    }

    /**
     * 切换关卡
     */
    public static void changeStage() {

        if (curStage instanceof Stage0) {
            curStage.clear();
            Stage1 stage1 = new Stage1();
            stage1.create();
            curStage = stage1;
            GameManager.curStage.setStagePass(false);
        } else if (curStage instanceof Stage1) {
            curStage.clear();
            StageOver stageOver = new StageOver();
            stageOver.create();
            curStage = stageOver;
        }


    }

    public static void clear() {
        plane.setDestroy(true);
    }

    public static void checkFriendPlaneBulletAndRemove(FriendPlane friendPlane) {
        Array.ArrayIterator<Bullet> bulletArrayIterator = bullets.iterator();
        while(bulletArrayIterator.hasNext()) {
            Bullet bullet = bulletArrayIterator.next();
            if (bullet.shooter == friendPlane) {
                bullet.setDestroy(true);
                bullet.dispose();
            }
            bulletArrayIterator.remove();
        }
    }
}
