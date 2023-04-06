package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.mygdx.game.component.GameWorld;
import com.mygdx.game.component.bullet.BulletTypeEnum;
import com.mygdx.game.component.pickup.FriendGoods;
import com.mygdx.game.component.pickup.Gold;
import com.mygdx.game.component.shader.RedShader;
import com.mygdx.game.component.shader.YellowShader;

import java.util.Collection;
import java.util.HashMap;

public class UserDataManager {
    /**
     * 主角信息
     */
    private static float blood = 5;
    private static Integer coinCount = 1000;
    private static float fontSize = 40;
    private static float spaceSize = 15;
    // 主角可以有3种特殊子弹
    private static BulletTypeEnum[] bulletTypes = new BulletTypeEnum[3];
    private static int curBulletIndex = 0;
    private static int lastTakeBulletIndex = 2;

    // 主角可以有2个小飞机
    private static int friendPlaneCount = 0;

    private static boolean coinBlink = false;
    private static float coinBlinkElapsedTime = 0f;

    /**
     * BOSS信息
     */
    private static float totalBossBlood = 0;
    private static float curBossBlood = 0;
    private static Texture bossBloodTexture = new Texture("bossblood.png");
    private static Texture bulletItemTexture = new Texture("bulletitem.png");
    private static Texture borderTexture = new Texture("border.png");


    private static ShaderProgram yellowShader = YellowShader.createShader();

    private static HashMap<String, Texture> textureMap = new HashMap<>();

    public static void addCoin(Gold gold) {
        coinCount += gold.getPrice();
    }

    public static void renderUserData(SpriteBatch batch) {
        renderBlood(batch);
        renderBulletItem(batch);
        renderCoinCount(batch);
    }

    private static void renderBulletItem(SpriteBatch batch) {
        int startX = 500;
        int spaceX = 1;
        int spaceY = 1;
        for(int i = 0; i < 3; i++) {
            Sprite sprite = new Sprite(bulletItemTexture);
            sprite.setSize(sprite.getWidth()/10, sprite.getHeight()/5);
            sprite.setPosition(startX + i * sprite.getWidth(), 5);
            sprite.draw(batch);
            if (bulletTypes[i] != null) {
                Sprite bulletTypeSprite = new Sprite(bulletTypes[i].getTexture());
                bulletTypeSprite.setSize(sprite.getWidth() - 5, sprite.getHeight() - 5);
                bulletTypeSprite.setPosition(startX + i * sprite.getWidth() + spaceX, 5 + spaceY);
                bulletTypeSprite.draw(batch);
            }
            if (curBulletIndex == i) {
                Sprite borderSprite = new Sprite(borderTexture);
                borderSprite.setSize(sprite.getWidth()+2, sprite.getHeight());
                borderSprite.setPosition(startX + i * sprite.getWidth()-1, spaceY + 4);
                borderSprite.draw(batch);
            }
        }
    }

    private static void renderBlood(SpriteBatch batch) {
        if (blood < 0) {
            blood = 0;
        }
        StringBuilder imgName = new StringBuilder("5HP Bar - ").append((int)Math.floor(blood)).append(".png");
        Texture texture = textureMap.get(imgName.toString());
        if (texture == null) {
            texture = new Texture(imgName.toString());
            textureMap.put(imgName.toString(), texture);
        }
        Sprite sprite = new Sprite(texture);
        sprite.setSize(sprite.getWidth() * 2, sprite.getHeight() * 2);
        sprite.setPosition(0, - 30);
        sprite.draw(batch);
    }

    private static void renderCoinCount(SpriteBatch batch) {
        // C
        TextureRegion textureRegion = FontTextureUtils.getDigitsTexture()[1][2];
        Sprite sprite = new Sprite(textureRegion);
        sprite.setSize(fontSize, fontSize);
        sprite.setPosition(1000, 0);
        sprite.draw(batch);

        // O
        textureRegion = FontTextureUtils.getDigitsTexture()[2][4];
        sprite = new Sprite(textureRegion);
        sprite.setSize(fontSize, fontSize);
        sprite.setPosition(1000 + fontSize - spaceSize, 0);
        sprite.draw(batch);

        // I
        textureRegion = FontTextureUtils.getDigitsTexture()[1][8];
        sprite = new Sprite(textureRegion);
        sprite.setSize(fontSize, fontSize);
        sprite.setPosition(1000 + fontSize*2 - spaceSize*2, 0);
        sprite.draw(batch);

        // N
        textureRegion = FontTextureUtils.getDigitsTexture()[2][3];
        sprite = new Sprite(textureRegion);
        sprite.setSize(fontSize, fontSize);
        sprite.setPosition(1000 + fontSize*3 - spaceSize*3, 0);
        sprite.draw(batch);

        // 数字
        batch.setShader(yellowShader);
        if (coinBlink) {
            if (Math.round(GameManager.timeSinceStart * 10) % 2 == 0) {
                batch.setShader(RedShader.getShader());
            }
            coinBlinkElapsedTime += Gdx.graphics.getDeltaTime();
            if (coinBlinkElapsedTime > 1) {
                coinBlink = false;
                coinBlinkElapsedTime = 0;
            }
        }
        char[] chars = String.valueOf(coinCount).toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int index = Integer.parseInt(Character.toString(chars[i])) - 1;
            if (index == -1) {
                index = 9;
            }
            textureRegion = FontTextureUtils.getDigitsTexture()[0][index];
            sprite = new Sprite(textureRegion);
            sprite.setSize(fontSize, fontSize);
            sprite.setPosition(1000 + fontSize*(3 + i) - spaceSize * (0.6f*i), 0);
            sprite.draw(batch);
        }
        batch.setShader(null);

    }

    public static void dispose() {
        Collection<Texture> textures = textureMap.values();
        for (Texture texture : textures) {
            texture.dispose();
        }
        bossBloodTexture.dispose();
    }

    public static boolean reduceBlood(float damage) {
        if (blood <= 0) {
            return false;
        }
        blood -= damage;
        return true;
    }

    public static void resetBoss(Integer bossBlood) {
        UserDataManager.curBossBlood = bossBlood;
        UserDataManager.totalBossBlood = bossBlood;
    }

    public static boolean reduceBossBlood(float damage) {
        if (curBossBlood <= 0) {
            return false;
        }
        curBossBlood -= damage;
        return true;
    }

    public static void renderBossBlood(SpriteBatch batch) {
        if (curBossBlood > 0) {
            Sprite sprite = new Sprite(bossBloodTexture);

            // 计算width
            float initialWidth = sprite.getWidth()*2 + 250;
            float curWidth = initialWidth * (curBossBlood/totalBossBlood);

            sprite.setSize(curWidth, sprite.getHeight()/8);
            sprite.setPosition(50, GameWorld.frameHeight - 30);
            sprite.draw(batch);
        }

    }

    public static BulletTypeEnum getCurBulletType() {
        return bulletTypes[curBulletIndex];
    }

    public static void setCurBulletTypeIndex() {
        if (curBulletIndex == bulletTypes.length - 1) {
            curBulletIndex = 0;
            return;
        }
       curBulletIndex += 1;
    }

    public static void addBulletType(BulletTypeEnum bulletType) {
        if (lastTakeBulletIndex == bulletTypes.length - 1) {
            lastTakeBulletIndex = 0;
        } else {
            lastTakeBulletIndex += 1;
        }
        bulletTypes[lastTakeBulletIndex] = bulletType;
        curBulletIndex = lastTakeBulletIndex;
    }

    public static Integer getCoinCount() {
        return coinCount;
    }

    public static void consume(int price) {
        GameManager.getShoppingSound().play();
        coinCount -= price;
    }

    /**
     * 金币数闪烁
     */
    public static void coinBlink() {
        GameManager.getFailedSound().play();
        coinBlink = true;
    }

    public static void addFriendPlane(FriendGoods goods) {
        if (friendPlaneCount == 2) {
            coinCount += goods.getPrice();
            return;
        }
        friendPlaneCount += 1;
    }

    public static int getFriendPlaneCount() {
        return friendPlaneCount;
    }

    public static void setFriendPlaneCount(int friendPlaneCount) {
        UserDataManager.friendPlaneCount = friendPlaneCount;
    }

    public static void clear() {
        friendPlaneCount = 0;
    }
}
