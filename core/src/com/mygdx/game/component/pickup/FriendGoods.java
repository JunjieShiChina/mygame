package com.mygdx.game.component.pickup;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.component.friends.FriendPlane;
import com.mygdx.game.utils.UserDataManager;

public class FriendGoods extends Goods {

    private int price = 300;

    public FriendGoods(World world, float positionX, float positionY, float xSpeed, float ySpeed, float rotation, float scaleX, float scaleY) {
        super(world, positionX, positionY, xSpeed, ySpeed, rotation, scaleX, scaleY);
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public Texture getGoodsTexture() {
        return FriendPlane.getTexture();
    }

    @Override
    public void taked() {
        if (!isDestroy()) {
            if (UserDataManager.getCoinCount() > getPrice()) {
                UserDataManager.addFriendPlane(this);
                UserDataManager.consume(getPrice());
                setDestroy(true);
            } else {
                UserDataManager.coinBlink();
            }
        }
    }

}
