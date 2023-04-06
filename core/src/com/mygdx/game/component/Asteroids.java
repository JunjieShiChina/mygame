package com.mygdx.game.component;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

public class Asteroids extends GameObject {
    public Asteroids(World world, Texture texture, float positionX, float positionY, float xSpeed, float ySpeed, float rotation, float scaleX, float scaleY) {
        super(world, texture, positionX, positionY, xSpeed, ySpeed, rotation, scaleX, scaleY);
        this.getBody().getFixtureList().get(0).setSensor(true);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (isDestroy()) {
            dispose();
            return;
        }
        getSprite().draw(batch);
    }
}
