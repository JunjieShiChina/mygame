package com.mygdx.game.controls;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.component.stages.Stage0;
import com.mygdx.game.component.stages.Stage1;
import com.mygdx.game.utils.GameManager;
import com.mygdx.game.utils.UserDataManager;

public class PlaneInputProcessor extends InputAdapter {


    public PlaneInputProcessor() {
    }

    @Override
    public boolean keyDown(int keycode) {
        if (GameManager.curStage instanceof Stage0) {
            if (keycode == Input.Keys.ENTER) {
                GameManager.curStage.setStagePass(true);
            }
        }

        if (GameManager.curStage instanceof Stage1) {

            if (keycode == Input.Keys.LEFT) {
                GameManager.getPlane().velocity.x -= 10;
            }
            if (keycode == Input.Keys.RIGHT) {
                GameManager.getPlane().velocity.x += 10;
            }
            if (keycode == Input.Keys.DOWN) {
                GameManager.getPlane().velocity.y -= 10;
            }
            if (keycode == Input.Keys.UP) {
                GameManager.getPlane().velocity.y += 10;
            }

            if (keycode == Input.Keys.TAB) {
                UserDataManager.setCurBulletTypeIndex();
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (GameManager.curStage instanceof Stage1) {
            if (keycode == Input.Keys.LEFT) {
                GameManager.getPlane().velocity.x += 10;
            }
            if (keycode == Input.Keys.RIGHT) {
                GameManager.getPlane().velocity.x -= 10;
            }
            if (keycode == Input.Keys.DOWN) {
                GameManager.getPlane().velocity.y += 10;
            }
            if (keycode == Input.Keys.UP) {
                GameManager.getPlane().velocity.y -= 10;
            }
            GameManager.getPlane().body.setLinearVelocity(GameManager.getPlane().velocity);
        }
        return true;
    }
}

