package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;

public class Fragment extends SpaceObject{
    int type;
    float a, v;
    float angle, speedRotation;

    public Fragment(float x, float y) {
        super(x, y, MathUtils.random(20, 50), MathUtils.random(20, 50));
        a = MathUtils.random(0f, 360);
        v = MathUtils.random(1f, 8);
        vx = v*MathUtils.sin(a);
        vy = v*MathUtils.cos(a);
        type = MathUtils.random(0, 3);
        speedRotation = MathUtils.random(-5f, 5);
    }

    @Override
    void move() {
        super.move();
        angle += speedRotation;
    }
}
