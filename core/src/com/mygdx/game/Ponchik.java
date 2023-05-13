package com.mygdx.game;

import static com.mygdx.game.LetterPiggy.SCR_HEIGHT;
import static com.mygdx.game.LetterPiggy.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Ponchik extends SpaceObject{
    char letter;

    public Ponchik() {
        super(0, 0, 100, 100);
        x = MathUtils.random(width/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT+height/2, SCR_HEIGHT*2);
        vy = MathUtils.random(-6f, -3);
        letter = (char) MathUtils.random('A','Z');
    }

    @Override
    boolean outOfBounds() {
        return y<-height/2;
    }
}
