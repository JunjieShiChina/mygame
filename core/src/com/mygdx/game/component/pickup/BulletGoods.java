package com.mygdx.game.component.pickup;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.bullet.BulletTypeEnum;
import com.mygdx.game.utils.UserDataManager;

public abstract class BulletGoods extends Goods {

    public BulletGoods(World world, float positionX, float positionY, float xSpeed, float ySpeed, float rotation, float scaleX, float scaleY) {
        super(world, positionX, positionY, xSpeed, ySpeed, rotation, scaleX, scaleY);
        getBody().getFixtureList().get(0).setSensor(true);
    }

    @Override
    public void taked() {
        if (!isDestroy()) {
            if (UserDataManager.getCoinCount() > getPrice()) {
                UserDataManager.addBulletType(getBulletType());
                UserDataManager.consume(getPrice());
                setDestroy(true);
            } else {
                UserDataManager.coinBlink();
            }
        }
    }

    public abstract BulletTypeEnum getBulletType();

    @Override
    public Texture getGoodsTexture() {
        return getBulletType().getTexture();
    }

}
