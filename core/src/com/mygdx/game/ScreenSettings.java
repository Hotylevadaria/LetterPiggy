package com.mygdx.game;

import static com.mygdx.game.LetterPiggy.SCR_HEIGHT;
import static com.mygdx.game.LetterPiggy.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenSettings implements Screen {
    LetterPiggy gg;
    Texture imgBackGround;

    TextButton btnName, btnClearRec, btnSound, btnMusic, btnBack;
    String playerName = "Noname";

    InputKeyboard keyboard;
    boolean isEnterName;

    public ScreenSettings(LetterPiggy myGG){
        gg = myGG;
        imgBackGround = new Texture("bg/cosmos02.jpg");

        btnName = new TextButton(gg.fontLarge, "Имя: "+playerName, 20, 1100, true);
        btnClearRec = new TextButton(gg.fontLarge, "Очистка рекордов", 20, 1000, true);
        btnSound = new TextButton(gg.fontLarge, "Звук вкл", 20, 900, true);
        btnMusic = new TextButton(gg.fontLarge, "Музыка вкл", 20, 800, true);
        btnBack = new TextButton(gg.fontLarge, "Назад", 20, 700, true);

        keyboard = new InputKeyboard(SCR_WIDTH, SCR_HEIGHT/1.7f, 8);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // касания экрана и клики мыши
        if(Gdx.input.justTouched()){
            gg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gg.camera.unproject(gg.touch);
            if(isEnterName){
                if(keyboard.endOfEdit(gg.touch.x, gg.touch.y)){
                    playerName = keyboard.getText();
                    btnName.setText("Имя: "+playerName);
                    isEnterName = false;
                }
            } else {
                if (btnName.hit(gg.touch.x, gg.touch.y)) {
                    isEnterName = true;
                }
                if (btnClearRec.hit(gg.touch.x, gg.touch.y)) {
                    btnClearRec.setText("Рекорды очищены");
                }
                if (btnSound.hit(gg.touch.x, gg.touch.y)) {
                    gg.soundOn = !gg.soundOn;
                    btnSound.setText(gg.soundOn ? "Звук вкл" : "Звук выкл");
                }
                if (btnMusic.hit(gg.touch.x, gg.touch.y)) {
                    gg.musicOn = !gg.musicOn;
                    btnMusic.setText(gg.musicOn ? "Музыка вкл" : "Музыка выкл");
                }
                if (btnBack.hit(gg.touch.x, gg.touch.y)) {
                    gg.setScreen(gg.screenIntro);
                }
            }
        }

        gg.camera.update();
        gg.batch.setProjectionMatrix(gg.camera.combined);
        gg.batch.begin();
        gg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnName.font.draw(gg.batch, btnName.text, btnName.x, btnName.y);
        btnClearRec.font.draw(gg.batch, btnClearRec.text, btnClearRec.x, btnClearRec.y);
        btnSound.font.draw(gg.batch, btnSound.text, btnSound.x, btnSound.y);
        btnMusic.font.draw(gg.batch, btnMusic.text, btnMusic.x, btnMusic.y);
        btnBack.font.draw(gg.batch, btnBack.text, btnBack.x, btnBack.y);
        if(isEnterName) keyboard.draw(gg.batch);
        gg.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        btnClearRec.setText("Очистка рекордов");
    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
        keyboard.dispose();
    }
}
