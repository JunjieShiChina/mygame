package com.mygdx.game.component.pickup;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.GameObject;

public class PickUp extends GameObject {
    public PickUp(World world, Texture texture, float positionX, float positionY, float xSpeed, float ySpeed, float rotation, float scaleX, float scaleY) {
        super(world, texture, positionX, positionY, xSpeed, ySpeed, rotation, scaleX, scaleY);
        this.getBody().getFixtureList().get(0).setSensor(true);
    }
}
