package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.GameWorld;
import com.mygdx.game.component.enemies.ShipPlane;
import com.mygdx.game.component.pickup.*;

import java.util.Random;

public class GoodsManager {

    // 每10S掉落商品
    private static float bulletGoodsInterval = 10f;
    private static float BulletGoodsTimer = 0;
    private static Random random = new Random();


    public static void update(World world) {
        // 更新计时器
        BulletGoodsTimer += Gdx.graphics.getDeltaTime();
        // 每20S随机生成商品
        if (BulletGoodsTimer >= bulletGoodsInterval) {
            GameManager.addGoods(randomGenGoods(world));
            BulletGoodsTimer = 0;
        }
    }

    private static Goods randomGenGoods(World world) {
        float scale = 2;
        Goods goods = null;
        int number = MathUtils.random(1, 5); // 生成1到5的随机整数
        float x = MathUtils.random(0, GameWorld.frameWidth - ShipPlane.getTextureWidth());
        if (number == 1) {
            goods = new Bullet3Goods(world, x, GameWorld.frameHeight, 0, -3, 0, scale, scale);
        }
        if (number == 2) {
            goods = new BulletArrowGoods(world, x, GameWorld.frameHeight, 0, -3, 0, scale, scale);
        }
        if (number == 3) {
            goods = new Bullet5Goods(world, x, GameWorld.frameHeight, 0, -3, 0, scale, scale);
        }
        if (number == 4) {
            goods = new Bullet6Goods(world, x, GameWorld.frameHeight, 0, -3, 0, scale, scale);
        }
        if (number == 5) {
            goods = new FriendGoods(world, x, GameWorld.frameHeight, 0, -3, 0, scale, scale);
        }

        return goods;
    }

}
