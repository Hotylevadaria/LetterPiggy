package com.mygdx.game;

public class Shot extends SpaceObject{
    public Shot(float x, float y) {
        super(x, y, 100, 100);
        vy = 10;
    }

    boolean overlap(Ponchik enemy){
        return Math.abs(x-enemy.x) < width/2 + enemy.width/2 & Math.abs(y-enemy.y) < height/2 + enemy.height/2;
    }
}
