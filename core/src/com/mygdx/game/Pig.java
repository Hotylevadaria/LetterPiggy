package com.mygdx.game;

import static com.mygdx.game.LetterPiggy.SCR_WIDTH;

import com.badlogic.gdx.utils.TimeUtils;

public class Pig {
    float x, y;
    float width, height;
    float vx, vy;
    boolean isVisible = true;
    int lives = 5;
    long timeStartInvisible, timeInvisibleInterval = 1000;
    long timeStartEat, timeEatInterval = 100;
    int faza, nFaz = 4;
    boolean isEat;

    public Pig(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    void move() {
        if(!isVisible) {
            if(timeStartInvisible+timeInvisibleInterval<TimeUtils.millis()){
                isVisible = true;
                x = SCR_WIDTH/2;
            }
        }
        if(isEat){
            if(timeStartEat+timeEatInterval<TimeUtils.millis()){
                timeStartEat = TimeUtils.millis();
                if(++faza == nFaz){
                    isEat = false;
                    faza = 0;
                }
            }
        }
    }

    void eat(){
        isEat = true;
    }

    public float getX() {
        return x-width/2;
    }

    public float getY() {
        return y-height/2;
    }

    void kill(){
        isVisible = false;
        timeStartInvisible = TimeUtils.millis();
        lives--;
    }
}
