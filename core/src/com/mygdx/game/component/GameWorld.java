package com.mygdx.game.component;

import com.badlogic.gdx.ApplicationAdapter;
import com.mygdx.game.utils.GameManager;

public class GameWorld extends ApplicationAdapter {

    public static final float PPM = 100;

    public static final int frameWidth = 1300;
    public static final int frameHeight = 1300;


    @Override
    public void create() {
        GameManager.curStage.create();
    }

    @Override
    public void render() {
        GameManager.curStage.render();
    }

    @Override
    public void dispose() {
        GameManager.curStage.dispose();
    }
}
