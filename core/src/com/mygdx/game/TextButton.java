package com.mygdx.game;

import static com.mygdx.game.LetterPiggy.SCR_WIDTH;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class TextButton {
    float x, y;
    float width, height;
    String text;
    BitmapFont font;
    boolean isScrCenter;

    public TextButton(BitmapFont font, String text, float x, float y, boolean isScrCenter) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.font = font;
        GlyphLayout gl = new GlyphLayout(font, text);
        width = gl.width;
        height = gl.height;
        this.isScrCenter = isScrCenter;
        textToCenter();
    }

    public void setText(String text) {
        this.text = text;
        textToCenter();
    }

    public TextButton(BitmapFont font, String text, float x, float y) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.font = font;
        GlyphLayout gl = new GlyphLayout(font, text);
        width = gl.width;
        height = gl.height;
    }


    private void textToCenter(){
        if(isScrCenter) {
            GlyphLayout gl = new GlyphLayout(font, text);
            width = gl.width;
            this.x = SCR_WIDTH/2 - width/2;
        }
    }

    boolean hit(float tx, float ty){
        return x < tx && tx < x+width && y-height < ty && ty < y;
    }
}
