package com.mygdx.game.component.pickup;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utils.FontTextureUtils;

public abstract class Goods extends PickUp{

    private static Texture texture = new Texture("goods.png");

    public Goods(World world, float positionX, float positionY, float xSpeed, float ySpeed, float rotation, float scaleX, float scaleY) {
        super(world, texture, positionX, positionY, xSpeed, ySpeed, rotation, scaleX, scaleY);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (isDestroy()) {
            dispose();
            return;
        }
        super.render(batch);
        int space = 12;

        Sprite sprite = new Sprite(getGoodsTexture());
        sprite.setSize(this.getSprite().getWidth(), this.getSprite().getHeight());
        sprite.setPosition(this.getSprite().getX(), this.getSprite().getY());
        sprite.draw(batch);

        char[] chars = String.valueOf(getPrice()).toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int index = Integer.parseInt(Character.toString(chars[i])) - 1;
            if (index == -1) {
                index = 9;
            }
            TextureRegion textureRegion = FontTextureUtils.getDigitsTexture()[0][index];
            sprite = new Sprite(textureRegion);
            sprite.setSize(15, 15);
            sprite.setPosition(this.getSprite().getX() + this.getSprite().getWidth()/2 + i*space, this.getSprite().getY());
            sprite.draw(batch);
        }
    }

    public abstract int getPrice();

    public abstract Texture getGoodsTexture();

    public abstract void taked();
}
