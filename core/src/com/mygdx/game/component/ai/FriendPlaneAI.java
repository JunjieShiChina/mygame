package com.mygdx.game.component.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.component.Plane;
import com.mygdx.game.component.friends.FriendPlane;

public class FriendPlaneAI {
    private FriendPlane friendPlane;
    private Plane plane;
    private float angle = 0.0f;
    private float distance;

    public FriendPlaneAI(FriendPlane friendPlane, Plane plane) {
        this.friendPlane = friendPlane;
        this.plane = plane;
        distance = friendPlane.getPosition().x - plane.position.x;
        if (distance > 0) {
            distance += friendPlane.getSprite().getWidth();
        }
    }

    public void update() {
        angle += 0.04f; // 将角度的变化量调整得更小

        float x = MathUtils.cos(angle + MathUtils.PI / 2) * distance + plane.position.x;
        float y = MathUtils.sin(angle + MathUtils.PI / 2) * distance + plane.position.y;

        friendPlane.setPosition(new Vector2(x, y));
    }
}
