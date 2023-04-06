package com.mygdx.game.component;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.bullet.*;
import com.mygdx.game.inter.Shooter;

public class BulletFactory {

    public static Bullet createBullet(World world, Shooter shooter, BulletTypeEnum bulletType) {
        switch (bulletType) {
            case B1: return new Bullet1(world, 0, 0, shooter);
            case B3: return new Bullet3(world, 0, 0, shooter);
            case B5: return new Bullet5(world, 0, 0, shooter);
            case B6: return new Bullet6(world, 0, 0, shooter);
            case BARROW: return new BulletArrow(world, 0, 0, shooter);
        }
        return null;
    }

    public static Bullet createBullet2(World world, Shooter shooter, BulletTypeEnum bulletType, float rotation, float bulletAngle) {
        switch (bulletType) {
            case B1: return new Bullet1(world, rotation, bulletAngle, shooter);
            case B3: return new Bullet3(world, rotation, bulletAngle, shooter);
            case B5: return new Bullet5(world, rotation, bulletAngle, shooter);
            case B6: return new Bullet6(world, rotation, bulletAngle, shooter);
            case BARROW: return new BulletArrow(world, rotation, bulletAngle, shooter);
        }
        return null;
    }

}
