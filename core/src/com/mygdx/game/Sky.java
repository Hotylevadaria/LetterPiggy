package com.mygdx.game;

import static com.mygdx.game.LetterPiggy.SCR_HEIGHT;
import static com.mygdx.game.LetterPiggy.SCR_WIDTH;

public class Sky extends SpaceObject{
    public Sky(float x, float y) {
        super(x, y, SCR_WIDTH, SCR_HEIGHT);
        vy = 0;
    }

    @Override
    void move() {
        super.move();
        if(outOfBounds()){
            y = SCR_HEIGHT+height/2;
        }
    }
}
