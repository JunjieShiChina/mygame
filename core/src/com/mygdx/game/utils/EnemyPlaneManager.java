package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.GameWorld;
import com.mygdx.game.component.Plane;
import com.mygdx.game.component.enemies.*;

public class EnemyPlaneManager {

    public static Plane plane;

    private static float elapsedTime = 0;
    private static float spawnIntervalDecreaseInterval = 5;
    private static float spawnIntervalDecreaseAmount = 0.1f;

    private static float timeSinceLastSpawn = 0;
    private static float timeSinceLastSpawn2 = 0;

    private static float spawnInterval = 2; // 敌机生成间隔，单位为秒

    private static boolean firstCreateRotaionEnemy = true;


    public static void createShipEnemy(World world, float positionY, float xSpeed, float ySpeed) {
        // 如果当前世界总飞机超过20架就不创建
        if (GameManager.getEnemyPlanes().size > 15) {
            return;
        }

        boolean validPosition;
        float x;
        do {
            validPosition = true;
            x = MathUtils.random(0, GameWorld.frameWidth - ShipPlane.getTextureWidth());

            // 检查新敌机的位置是否与其他敌机重合
            for (EnemyPlane enemy : GameManager.getEnemyPlanes()) {
                if (MathUtils.isEqual(x, enemy.getPosition().x, 30)) { // 这里使用了误差值10
                    validPosition = false;
                    break;
                }
            }
        } while (!validPosition);

        ShipPlane enemy = new ShipPlane(world, x, positionY, xSpeed, ySpeed, 0, 2, 2);
        enemy.setPlane(plane);
        GameManager.addEnemyPlane(enemy);
    }

    private static void createEnemyPlane2(World world, float positionY, float xSpeed, float ySpeed) {
        // 如果当前世界总飞机超过20架就不创建
        if (GameManager.getEnemyPlanes().size > 20) {
            return;
        }

        boolean validPosition;
        float x;
        do {
            validPosition = true;
            x = MathUtils.random(0, GameWorld.frameWidth - EPlane2.getTextureWidth());

            // 检查新敌机的位置是否与其他敌机重合
            for (EnemyPlane enemy : GameManager.getEnemyPlanes()) {
                if (MathUtils.isEqual(x, enemy.getPosition().x, 30)) { // 这里使用了误差值10
                    validPosition = false;
                    break;
                }
            }
        } while (!validPosition);

        EPlane2 enemy = new EPlane2(world, x, positionY, xSpeed, ySpeed, 0, 2f, 2f);
        enemy.setPlane(plane);
        GameManager.addEnemyPlane(enemy);
    }

    public static void update(World world) {
        elapsedTime += Gdx.graphics.getDeltaTime();

        // 每经过 spawnIntervalDecreaseInterval 秒，就将 spawnInterval 减少 spawnIntervalDecreaseAmount
        if (elapsedTime >= spawnIntervalDecreaseInterval) {
            if (spawnInterval > 0.5) {
                spawnInterval -= spawnIntervalDecreaseAmount;
            }
            elapsedTime = 0;
        }

        timeSinceLastSpawn += Gdx.graphics.getDeltaTime();

        if (timeSinceLastSpawn > spawnInterval && !GameManager.isBossTime()) {
            // 随机生成敌机的概率为80%
            for (int i = 0; i < 2; i++) {
                if (MathUtils.randomBoolean(0.8f)) {
                    // 随机生成初始速度
                    float randomXSpeed = MathUtils.random(-2, 2f);
                    float randomYSpeed = MathUtils.random(1, 3f);
                    createShipEnemy(world, GameWorld.frameHeight, randomXSpeed, -randomYSpeed);
                }
            }
            timeSinceLastSpawn = 0;
        }

        // 游戏10秒后出现新敌人
        if (GameManager.timeSinceStart > 10 && !GameManager.isBossTime()) {
            timeSinceLastSpawn2 += Gdx.graphics.getDeltaTime();
            ;
            if (timeSinceLastSpawn2 > spawnInterval) {
                // 随机生成敌机的概率为80%
                if (MathUtils.randomBoolean(0.8f)) {
                    // 随机生成初始速度
                    float randomXSpeed = MathUtils.random(-2, 2f);
                    float randomYSpeed = MathUtils.random(-2f, -6f);
                    createEnemyPlane2(world, GameWorld.frameHeight, randomXSpeed, randomYSpeed);
                }
                timeSinceLastSpawn2 = 0;
            }
        }

        // 游戏30秒后加入旋转敌人
        if (GameManager.timeSinceStart > 30 && !GameManager.isBossTime() && firstCreateRotaionEnemy) {
            // 生成一个旋转飞机
            RotationEnemyPlane rotationEnemyPlane = new RotationEnemyPlane(world, 50, 500, 0, 0, 0, 2, 2);
            rotationEnemyPlane.setPlane(GameManager.getPlane());
            GameManager.addEnemyPlane(rotationEnemyPlane);

            // 生成一个旋转飞机
            RotationEnemyPlane rotationEnemyPlane2 = new RotationEnemyPlane(world, 1300, 500, 0, 0, 0, 2, 2);
            rotationEnemyPlane2.setPlane(GameManager.getPlane());
            GameManager.addEnemyPlane(rotationEnemyPlane2);

            firstCreateRotaionEnemy = false;
        }

        // 游戏120S后进入BOSS战,BOSS战不刷小兵
        if (GameManager.timeSinceStart > 120 && !GameManager.isBossTime()) {
            GameManager.setBossTime(true);
            loadBoss(world);
        }

        // 加载BOSS
        if (GameManager.isPreCreateBOSS()) {
            // 切换背景音乐为BOSS战音乐
            GameManager.switchBossFightBgSound();
            UserDataManager.resetBoss(1000);
            GameManager.setBoss1(new Boss1(world, 220, GameWorld.frameHeight, 3, -1, 0, 3, 3));
            GameManager.setPreCreateBOSS(false);
        }
    }

    private static void loadBoss(World world) {
        GameManager.showWarning();
    }

}
