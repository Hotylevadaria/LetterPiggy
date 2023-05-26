package com.mygdx.game;

import static com.mygdx.game.LetterPiggy.SCR_HEIGHT;
import static com.mygdx.game.LetterPiggy.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class ScreenGame implements Screen {
    LetterPiggy gg;
    Texture imgBG;
    Texture imgShip;
    Texture imgEnemy;
    Texture imgFoodAtlas;
    Texture[][] imgPig = new Texture[2][4];
    TextureRegion[] imgFood = new TextureRegion[4];

    static final float MOUTH_X = 406, MOUTH_Y = 480;

    Sound sndOy, sndEat;

    Pig pig;
    ArrayList<Food> foods = new ArrayList<>();

    boolean isGyroscopeAvailable;
    boolean isAccelerometerAvailable;

    long timeEnemyLastSpawn, timeEnemySpawnInterval = 1000;

    boolean pause;
    int letters;

    public ScreenGame(LetterPiggy myGG){
        gg = myGG;
        imgBG = new Texture("kitchen.png");
        imgShip = new Texture("ship.png");
        imgEnemy = new Texture("enemy.png");
        imgFoodAtlas = new Texture("food.png");
        for (int i = 0; i < 2; i++) {
            imgFood[i] = new TextureRegion(imgFoodAtlas, i*250, 0, 250, 250);
            imgFood[i+2] = new TextureRegion(imgFoodAtlas, i*250, 250, 250, 250);
        }

        imgPig[0][0] = new Texture("pig/pig01.png");
        imgPig[0][1] = new Texture("pig/pig12.png");
        imgPig[0][2] = new Texture("pig/pig13.png");
        imgPig[0][3] = new Texture("pig/pig12.png");
        imgPig[1][0] = new Texture("pig/pig01.png");
        imgPig[1][1] = new Texture("pig/pig02.png");
        imgPig[1][2] = new Texture("pig/pig03.png");
        imgPig[1][3] = new Texture("pig/pig02.png");

        sndEat = Gdx.audio.newSound(Gdx.files.internal("hruu.mp3"));
        sndOy = Gdx.audio.newSound(Gdx.files.internal("oy.mp3"));

        isAccelerometerAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        isGyroscopeAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);
    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        pause = false;
        pig = new Pig(330, 385, 600, 600);
        foods.clear();
        letters = 0;
    }

    @Override
    public void render(float delta) {
        // касания экрана и клики мыши
        if(Gdx.input.isTouched()){
            gg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gg.camera.unproject(gg.touch);
            System.out.println(gg.touch.x+" "+gg.touch.y);
        } else if(isAccelerometerAvailable) {
            pig.vx = -Gdx.input.getAccelerometerX()*10;
        } else if(isGyroscopeAvailable) {
            pig.vx = Gdx.input.getGyroscopeY()*10;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            gg.setScreen(gg.screenIntro);
        }
        for(char c='A'; c<='Z'; c++) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.valueOf(""+c))){
                for (int i = 0; i < foods.size(); i++) {
                    if(foods.get(i).letter == c) {
                        foods.get(i).goPiggyMouth();
                        //ponchiks.remove(i);
                        //if(gg.soundOn) sndExplosion.play();
                        letters++;
                    }
                }
            }
        }

        // события
        if(!pause) {
            pig.move();
            spawnEnemy();

            for (int i = foods.size()-1; i >= 0 ; i--) {
                foods.get(i).move();
                if(foods.get(i).x<MOUTH_X+300) {
                    if(!pig.isEat) {
                        if (gg.soundOn) sndEat.play();
                    }
                    pig.eat();
                }
                if(foods.get(i).x<MOUTH_X) {
                    foods.remove(i);
                    continue;
                }
                if (foods.get(i).outOfBounds()) {
                    //if(pig.isVisible) killShip();
                    killPig();
                    if(gg.soundOn) sndOy.play();
                    foods.remove(i);
                }
            }
        }


        // отрисовка всего
        gg.camera.update();
        gg.batch.setProjectionMatrix(gg.camera.combined);
        gg.batch.begin();
        gg.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);

        if(pig.isVisible) gg.batch.draw(imgPig[0][pig.faza], pig.getX(), pig.getY(), pig.width, pig.height,
                0, 0, 500, 500, true, false);

        for(Food food: foods) {
            gg.batch.draw(imgFood[food.type], food.getX(), food.getY(), food.width/2, food.height/2,
                    food.width, food.height, 1, 1, food.rotation);
            if(!food.isGoPiggyMouth) gg.font.draw(gg.batch, ""+food.letter, food.x-12, food.y+100);
        }

        if(pig.isVisible) gg.batch.draw(imgPig[1][pig.faza], pig.getX(), pig.getY(), pig.width, pig.height,
                0, 0, 500, 500, true, false);

        gg.font.draw(gg.batch, "LETTERS: "+ letters, 10, SCR_HEIGHT-10);
        for (int i = 1; i < pig.lives+1; i++) {
            gg.batch.draw(imgShip, SCR_WIDTH-110*i, SCR_HEIGHT-110, 100, 100);
        }
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
        Gdx.input.setCatchKey(Input.Keys.BACK, false);
    }

    @Override
    public void dispose() {
        imgBG.dispose();
        imgShip.dispose();
        for (int i = 0; i < imgPig.length; i++) {
            for (int j = 0; j < imgPig[0].length; j++) {
                imgPig[i][j].dispose();
            }

        }
        sndEat.dispose();
        sndOy.dispose();
    }

    void spawnEnemy(){
        if(TimeUtils.millis() > timeEnemyLastSpawn+timeEnemySpawnInterval) {
            foods.add(new Food());
            timeEnemyLastSpawn = TimeUtils.millis();
        }
    }


    void killPig(){
        if(gg.soundOn) sndOy.play();
        if(--pig.lives == 0) {
            gg.setScreen(gg.screenIntro);
        }
    }
}
