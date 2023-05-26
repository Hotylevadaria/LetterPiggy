package com.mygdx.game;

import static com.mygdx.game.LetterPiggy.SCR_HEIGHT;
import static com.mygdx.game.LetterPiggy.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenIntro implements Screen {
    LetterPiggy gg;
    Texture imgBackGround;
    Texture imgShip;

    TextButton btnPlay, btnSettings, btnAbout, btnExit;

    public ScreenIntro(LetterPiggy myGG){
        gg = myGG;
        imgBackGround = new Texture("bg/cosmos01.jpg");
        imgShip = new Texture("ship.png");
        btnPlay = new TextButton(gg.fontLarge, "ИГРАТЬ", 100, 1000);
        btnSettings = new TextButton(gg.fontLarge, "НАСТРОЙКИ", 100, 900);
        btnAbout = new TextButton(gg.fontLarge, "ОБ ИГРЕ", 100, 800);
        btnExit = new TextButton(gg.fontLarge, "ВЫХОД", 100, 700);
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
            if(btnPlay.hit(gg.touch.x, gg.touch.y)) {
                gg.setScreen(gg.screenGame);
            }
            if(btnSettings.hit(gg.touch.x, gg.touch.y)) {
                gg.setScreen(gg.screenSettings);
            }
            if(btnAbout.hit(gg.touch.x, gg.touch.y)) {
                gg.setScreen(gg.screenAbout);
            }
            if(btnExit.hit(gg.touch.x, gg.touch.y)) {
                Gdx.app.exit();
            }
        }

        gg.camera.update();
        gg.batch.setProjectionMatrix(gg.camera.combined);
        gg.batch.begin();
        gg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        gg.batch.draw(imgShip, 520, 1080);
        btnPlay.font.draw(gg.batch, btnPlay.text, btnPlay.x, btnPlay.y);
        btnSettings.font.draw(gg.batch, btnSettings.text, btnSettings.x, btnSettings.y);
        btnAbout.font.draw(gg.batch, btnAbout.text, btnAbout.x, btnAbout.y);
        btnExit.font.draw(gg.batch, btnExit.text, btnExit.x, btnExit.y);
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

    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
    }
}
