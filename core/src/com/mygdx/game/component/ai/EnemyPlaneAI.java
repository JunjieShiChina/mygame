package com.mygdx.game.component.ai;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.component.GameWorld;
import com.mygdx.game.component.Plane;
import com.mygdx.game.component.enemies.EnemyPlane;
import com.mygdx.game.component.enemies.RotationEnemyPlane;

public class EnemyPlaneAI {
    private EnemyPlane enemyPlane;

    public EnemyPlaneAI(EnemyPlane enemyPlane) {
        this.enemyPlane = enemyPlane;
    }

    private boolean isInRange(Plane plane) {
        float distance = enemyPlane.getPosition().dst(plane.position);
        return distance < 300;
    }

    public void update(Plane plane) {
        if (isInRange(plane)) {
            Vector2 direction = new Vector2(plane.position.x - enemyPlane.getPosition().x, plane.position.y - enemyPlane.getPosition().y);
            float newRotation = direction.angleDeg();
            enemyPlane.getSprite().setRotation(newRotation+90);
        }
    }

    public void boss1AIUpdate(Plane plane) {
        float posX = enemyPlane.getPosition().x; // 敌机的x坐标
        float posY = enemyPlane.getPosition().y; // 敌机的y坐标
        if (posY <= GameWorld.frameHeight - 150) {
            enemyPlane.getVelocity().set(enemyPlane.getVelocity().x, 0);
        }

        // 检查是否到达窗口左右边缘
        if (posX + enemyPlane.getSprite().getWidth() * enemyPlane.getSprite().getScaleX() / 2 >= GameWorld.frameWidth || posX - enemyPlane.getSprite().getWidth() * enemyPlane.getSprite().getScaleX() / 2 <= 0) {
            enemyPlane.getPosition().set(enemyPlane.getPosition().x, enemyPlane.getPosition().y);
            enemyPlane.getVelocity().set(enemyPlane.getVelocity().x * -1, enemyPlane.getVelocity().y);
        }
    }


    public void rotationEnemyPlaneUpdate(Plane plane) {
        RotationEnemyPlane rotationEnemyPlane = (RotationEnemyPlane) enemyPlane;
        // 慢慢从两边飞入,并慢慢开始旋转起来
        float posX = enemyPlane.getPosition().x; // 敌机的x坐标
        enemyPlane.getPosition().set(enemyPlane.getPosition().x, enemyPlane.getPosition().y);
        if (posX < 400) {
            enemyPlane.getVelocity().set(1f, enemyPlane.getVelocity().y);
        }
        if (posX > 900) {
            enemyPlane.getVelocity().set(- 1f, enemyPlane.getVelocity().y);
        }

        // 到指定位置后开始旋转
        if (posX >= 400 && posX <= 900 && rotationEnemyPlane.getRevolveSpeed() < 10) {
            enemyPlane.getVelocity().set(0, 0);
            rotationEnemyPlane.setRevolveSpeed(rotationEnemyPlane.getRevolveSpeed() + 0.1f);
        }

        rotationEnemyPlane.getSprite().setRotation(rotationEnemyPlane.getSprite().getRotation() + rotationEnemyPlane.getRevolveSpeed());
    }
}
