package com.mygdx.game.inter;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public interface Shooter {

    void shoot();

    Sprite getSprite();

    Vector2 getPosition();

    Vector2 getVelocity();

    Body getBody();
}
