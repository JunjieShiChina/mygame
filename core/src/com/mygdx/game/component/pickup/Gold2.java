package com.mygdx.game.component.pickup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class Gold2 extends Gold {

    private static Sound genCoinSound = Gdx.audio.newSound(Gdx.files.internal("coinsplash.ogg"));

    private static Texture texture = new Texture("gold2.png");

    public Gold2(World world, float positionX, float positionY, float xSpeed, float ySpeed) {
        super(world, texture, positionX, positionY, xSpeed, ySpeed, 0, 0.3f, 0.3f);
        genCoinSound.play();
    }

    @Override
    public Integer getPrice() {
        return 20;
    }
}

