package com.mygdx.game;

import static com.mygdx.game.LetterPiggy.SCR_WIDTH;

import com.badlogic.gdx.utils.TimeUtils;

public class Ship extends SpaceObject{
    boolean isVisible = true;
    int lives = 3;
    long timeStartInvisible, timeInvisibleInterval = 1000;

    public Ship(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    void move() {
        super.move();
        outOfBounds();
        if(!isVisible) {
            if(timeStartInvisible+timeInvisibleInterval<TimeUtils.millis()){
                isVisible = true;
                x = SCR_WIDTH/2;
            }
        }
    }

    void hit(float tx, float ty) {
        vx = (tx-x)/20;
    }

    @Override
    boolean outOfBounds() {
        if(x < width/2) {
            x = width/2;
            vx = 0;
        }
        if(x > SCR_WIDTH-width/2){
            x = SCR_WIDTH-width/2;
            vx = 0;
        }
        return true;
    }

    void kill(){
        isVisible = false;
        timeStartInvisible = TimeUtils.millis();
        lives--;
    }
}
