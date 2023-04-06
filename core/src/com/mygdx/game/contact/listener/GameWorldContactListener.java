package com.mygdx.game.contact.listener;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.component.Asteroids;
import com.mygdx.game.component.GameWorld;
import com.mygdx.game.component.Plane;
import com.mygdx.game.component.bullet.Bullet;
import com.mygdx.game.component.bullet.BulletLaser;
import com.mygdx.game.component.enemies.BOSS;
import com.mygdx.game.component.enemies.EnemyPlane;
import com.mygdx.game.component.friends.FriendPlane;
import com.mygdx.game.component.pickup.Gold;
import com.mygdx.game.component.pickup.Goods;
import com.mygdx.game.component.pickup.PickUp;

public class GameWorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        /**
         * 陨石不做任何逻辑
         */
        if (bodyA.getUserData() instanceof Asteroids || bodyB.getUserData() instanceof Asteroids) {
            return;
        }

        /**
         * 金币的碰撞
         */
        if (bodyA.getUserData() instanceof PickUp || bodyB.getUserData() instanceof PickUp) {
            if (bodyA.getUserData() instanceof Bullet || bodyB.getUserData() instanceof Bullet) {
                return;
            }
            if (bodyA.getUserData() instanceof Plane) {
                if (bodyB.getUserData() instanceof Gold) {
                    Gold gold = (Gold) bodyB.getUserData();
                    gold.taked();
                }
                if (bodyB.getUserData() instanceof Goods) {
                    Goods goods = (Goods) bodyB.getUserData();
                    goods.taked();
                }

            }
            if (bodyB.getUserData() instanceof Plane) {
                if (bodyA.getUserData() instanceof Gold) {
                    Gold gold = (Gold) bodyA.getUserData();
                    gold.taked();
                }

            }
        }

        /**
         * 敌机和边界的碰撞不受检测
         */
        if (bodyA.getUserData() instanceof GameWorld) {
            if (bodyB.getUserData() instanceof EnemyPlane) {
                return;
            }
        }

        /**
         * 敌机的子弹和敌机碰撞不受检测
         */
        if (bodyA.getUserData() instanceof Bullet) {
            if (bodyB.getUserData() instanceof EnemyPlane) {
                if (((Bullet) bodyA.getUserData()).shooter instanceof EnemyPlane) {
                    return;
                }
            }
        }
        if (bodyB.getUserData() instanceof Bullet) {
            if (bodyA.getUserData() instanceof EnemyPlane) {
                if (((Bullet) bodyB.getUserData()).shooter instanceof EnemyPlane) {
                    return;
                }
            }
        }

        /**
         * 子弹和子弹不受碰撞
         */
        if (bodyB.getUserData() instanceof Bullet) {
            if (bodyA.getUserData() instanceof Bullet) {
               return;
            }
        }

        /**
         * 友军之间子弹不能互相射中
         */
        if (bodyA.getUserData() instanceof Bullet || bodyB.getUserData() instanceof Bullet) {
            if (bodyA.getUserData() instanceof FriendPlane || bodyB.getUserData() instanceof FriendPlane) {
                if (bodyA.getUserData() instanceof Bullet && ((Bullet) bodyA.getUserData()).shooter instanceof Plane) {
                    return;
                }
                if (bodyB.getUserData() instanceof Bullet && ((Bullet) bodyB.getUserData()).shooter instanceof Plane) {
                    return;
                }
            }
            if (bodyA.getUserData() instanceof Plane || bodyB.getUserData() instanceof Plane) {
                if (bodyA.getUserData() instanceof Bullet && ((Bullet) bodyA.getUserData()).shooter instanceof FriendPlane) {
                    return;
                }
                if (bodyB.getUserData() instanceof Bullet && ((Bullet) bodyB.getUserData()).shooter instanceof FriendPlane) {
                    return;
                }
            }
        }

        /**
         * 子弹和BOSS碰撞
         */
        if (bodyA.getUserData() instanceof BOSS || bodyB.getUserData() instanceof BOSS) {
            if (bodyA.getUserData() instanceof Bullet || bodyB.getUserData() instanceof Bullet) {
                if (bodyA.getUserData() instanceof Bullet && ((Bullet) bodyA.getUserData()).shooter instanceof Plane) {
                    Bullet bullet = (Bullet) bodyA.getUserData();
                    if (!bullet.isDestroy) {
                        BOSS boss = (BOSS) bodyB.getUserData();
                        if (boss.isDestroy()) {
                            return;
                        }
                        boss.takeHit(bullet.getAttackPower(), bullet.position.cpy().set(bullet.position.x, bullet.position.y + bullet.getSprite().getHeight()/2));
                        bullet.setDestroy(true);
                        return;
                    }
                }
                if (bodyB.getUserData() instanceof Bullet && ((Bullet) bodyB.getUserData()).shooter instanceof Plane) {
                    Bullet bullet = (Bullet) bodyB.getUserData();
                    if (!bullet.isDestroy) {
                        BOSS boss = (BOSS) bodyA.getUserData();
                        if (boss.isDestroy()) {
                            return;
                        }
                        boss.takeHit(bullet.getAttackPower(), bullet.position.cpy().set(bullet.position.x, bullet.position.y + bullet.getSprite().getHeight()/2));
                        bullet.setDestroy(true);
                        return;
                    }
                }
            }
        }


        /**
         * 子弹碰撞
         */
        if (bodyB.getUserData() instanceof Bullet) {

            /**
             * 子弹碰到的对象非发射方
             */
            if (((Bullet) bodyB.getUserData()).shooter != bodyA.getUserData()) {
                if(bodyA.getUserData() instanceof EnemyPlane) {
                    EnemyPlane enemyPlane = (EnemyPlane) bodyA.getUserData();
                    Bullet bullet = (Bullet) bodyB.getUserData();
                    if (!enemyPlane.isDestroy()) {
                        if(!(bullet instanceof BulletLaser)) {
                            bullet.isDestroy = true;
                            enemyPlane.takeHit(bullet.getAttackPower(), bullet.position.cpy().set(bullet.position.x, bullet.position.y + bullet.getSprite().getHeight()/2));
                        } else {
                            enemyPlane.takeHit(bullet.getAttackPower(), enemyPlane.position.cpy());
                        }
                    }
                    return;
                }
                if (!(bodyA.getUserData() instanceof FriendPlane)) {
                    ((Bullet) bodyB.getUserData()).isDestroy = true;
                }
            }

        }

        /**
         * 子弹碰撞
         */
        if (bodyA.getUserData() instanceof Bullet) {

            /**
             * 子弹碰到的对象非发射方
             */
            if (((Bullet) bodyA.getUserData()).shooter != bodyB.getUserData()) {
                if(bodyB.getUserData() instanceof EnemyPlane) {
                    EnemyPlane enemyPlane = (EnemyPlane) bodyB.getUserData();
                    Bullet bullet = (Bullet) bodyA.getUserData();
                    if (!enemyPlane.isDestroy()) {
                        if(!(bullet instanceof BulletLaser)) {
                            bullet.isDestroy = true;
                            enemyPlane.takeHit(bullet.getAttackPower(), bullet.position.cpy().set(bullet.position.x, bullet.position.y + bullet.getSprite().getHeight() / 2));
                        } else {
                            enemyPlane.takeHit(bullet.getAttackPower(), enemyPlane.position.cpy());
                        }
                    }
                    return;
                }
                ((Bullet) bodyA.getUserData()).isDestroy = true;
            }
        }

        /**
         * 子弹射中主角
         */
        if (bodyA.getUserData() instanceof Bullet || bodyB.getUserData() instanceof Bullet) {
            if (bodyA.getUserData() instanceof Plane || bodyB.getUserData() instanceof Plane) {
                if (bodyA.getUserData() instanceof Bullet) {
                    Plane plane = (Plane) bodyB.getUserData();
                    if (((Bullet) bodyA.getUserData()).shooter != plane && !(((Bullet) bodyA.getUserData()).shooter instanceof FriendPlane)) {
                        plane.takeHit(((Bullet) bodyA.getUserData()).getAttackPower());
                    }
                }
                if (bodyB.getUserData() instanceof Bullet) {
                    Plane plane = (Plane) bodyA.getUserData();
                    if (((Bullet) bodyB.getUserData()).shooter != plane && !(((Bullet) bodyB.getUserData()).shooter instanceof FriendPlane)) {
                        plane.takeHit(((Bullet) bodyB.getUserData()).getAttackPower());
                    }
                }
            }

        }

        /**
         * 敌机碰撞主角
         */
        if (bodyA.getUserData() instanceof EnemyPlane || bodyB.getUserData() instanceof EnemyPlane) {
            if (bodyA.getUserData() instanceof Plane || bodyB.getUserData() instanceof Plane) {
                if (bodyA.getUserData() instanceof EnemyPlane) {
                    EnemyPlane enemyPlane = (EnemyPlane) bodyA.getUserData();
                    Plane plane = (Plane) bodyB.getUserData();
                    plane.takeHit(1);
                    enemyPlane.takeHit(1, plane.position.cpy());
                }
                if (bodyB.getUserData() instanceof EnemyPlane) {
                    Plane plane = (Plane) bodyA.getUserData();
                    EnemyPlane enemyPlane = (EnemyPlane) bodyB.getUserData();
                    plane.takeHit(1);
                    enemyPlane.takeHit(1, plane.position.cpy());
                }
            }
        }

        /**
         * 旋转的小飞机能够拦截敌机
         */
        if (bodyA.getUserData() instanceof FriendPlane || bodyB.getUserData() instanceof FriendPlane) {
            if (bodyA.getUserData() instanceof EnemyPlane || bodyB.getUserData() instanceof EnemyPlane) {
                if (bodyA.getUserData() instanceof EnemyPlane) {
                     FriendPlane friendPlane = (FriendPlane) bodyB.getUserData();
                    ((EnemyPlane) bodyA.getUserData()).takeHit(1, friendPlane.position.cpy());
                }
                if (bodyB.getUserData() instanceof EnemyPlane) {
                    FriendPlane friendPlane = (FriendPlane) bodyA.getUserData();
                    ((EnemyPlane) bodyB.getUserData()).takeHit(1, friendPlane.position.cpy());
                }
            }
        }

    }


    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
