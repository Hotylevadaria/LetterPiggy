package com.mygdx.game;

import static com.mygdx.game.LetterPiggy.SCR_HEIGHT;
import static com.mygdx.game.LetterPiggy.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Food {
    float x, y;
    float width, height;
    float vx, vy;
    float rotation;
    char letter;
    boolean isGoPiggyMouth;
    int type;


    public Food() {
        this.width = 100;
        this.height = 100;
        x = MathUtils.random(SCR_WIDTH/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT+height/2, SCR_HEIGHT*2);
        vy = MathUtils.random(-3f, -0.5f);
        type = MathUtils.random(0, 3);
        letter = (char) MathUtils.random('A','Z');
    }

    void goPiggyMouth() {
        vx = (ScreenGame.MOUTH_X-x)/40;
        vy = (ScreenGame.MOUTH_Y-y)/40;
        isGoPiggyMouth = true;
        rotation = -90;
    }

    void move() {
        x += vx;
        y += vy;
        if(isGoPiggyMouth){
            width -= 1;
            height -= 1;
        }
    }

    public float getX() {
        return x-width/2;
    }

    public float getY() {
        return y-height/2;
    }

    boolean outOfBounds(){
        return y<-height/2;
    }

}
