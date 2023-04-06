package com.mygdx.game.component.pickup;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.bullet.BulletTypeEnum;

public class BulletArrowGoods extends BulletGoods{

    private int price = 60;
    private BulletTypeEnum bulletType = BulletTypeEnum.BARROW;

    public BulletArrowGoods(World world, float positionX, float positionY, float xSpeed, float ySpeed, float rotation, float scaleX, float scaleY) {
        super(world, positionX, positionY, xSpeed, ySpeed, rotation, scaleX, scaleY);
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public BulletTypeEnum getBulletType() {
        return bulletType;
    }


}
