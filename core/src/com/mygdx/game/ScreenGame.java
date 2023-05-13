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
    Texture imgStars;
    Texture imgShip;
    Texture imgEnemy;
    Texture imgShot;
    Texture imgAtlasFragment;
    TextureRegion[] imgFragment = new TextureRegion[4];

    Sound sndShot, sndExplosion;

    Sky[] skies = new Sky[2];
    Ship ship;
    ArrayList<Ponchik> ponchiks = new ArrayList<>();
    ArrayList<Shot> shots = new ArrayList<>();
    ArrayList<Fragment> fragments = new ArrayList<>();

    boolean isGyroscopeAvailable;
    boolean isAccelerometerAvailable;

    long timeEnemyLastSpawn, timeEnemySpawnInterval = 1000;
    long timeShotLastSpawn, timeShotSpawnInterval = 500;

    boolean pause;
    int frags;

    public ScreenGame(LetterPiggy myGG){
        gg = myGG;
        imgStars = new Texture("stars.png");
        imgShip = new Texture("ship.png");
        imgEnemy = new Texture("enemy.png");
        imgShot = new Texture("shot.png");
        imgAtlasFragment = new Texture("fragment.png");
        imgFragment[0] = new TextureRegion(imgAtlasFragment, 0, 0, 200, 200);
        imgFragment[1] = new TextureRegion(imgAtlasFragment, 200, 0, 200, 200);
        imgFragment[2] = new TextureRegion(imgAtlasFragment, 0, 200, 200, 200);
        imgFragment[3] = new TextureRegion(imgAtlasFragment, 200, 200, 200, 200);

        sndShot = Gdx.audio.newSound(Gdx.files.internal("blaster.wav"));
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));

        skies[0] = new Sky(SCR_WIDTH/2, SCR_HEIGHT/2);
        skies[1] = new Sky(SCR_WIDTH/2, SCR_HEIGHT+SCR_HEIGHT/2);

        isAccelerometerAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        isGyroscopeAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);
        ship = new Ship(SCR_WIDTH/2, 150, 200, 200);
    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        pause = false;
    }

    @Override
    public void render(float delta) {
        // касания экрана и клики мыши
        if(Gdx.input.isTouched()){
            gg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gg.camera.unproject(gg.touch);
            ship.hit(gg.touch.x, gg.touch.y);
        } else if(isAccelerometerAvailable) {
            ship.vx = -Gdx.input.getAccelerometerX()*10;
        } else if(isGyroscopeAvailable) {
            ship.vx = Gdx.input.getGyroscopeY()*10;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            gg.setScreen(gg.screenIntro);
        }
        for(char c='A'; c<='Z'; c++) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.valueOf(""+c))){
                for (int i = 0; i < ponchiks.size(); i++) {
                    if(ponchiks.get(i).letter == c) {
                        spawnFragments(ponchiks.get(i).x, ponchiks.get(i).y);
                        ponchiks.remove(i);
                        if(gg.soundOn) sndExplosion.play();
                        frags++;
                    }
                }
            }
        }

        // события
        if(!pause) {
            for (Sky sky : skies) sky.move();
            ship.move();
            if(ship.isVisible) {
                spawnShot();
            }
            spawnEnemy();

            for (int i = ponchiks.size()-1; i >= 0 ; i--) {
                ponchiks.get(i).move();
                if (ponchiks.get(i).outOfBounds()) {
                    if(ship.isVisible) killShip();
                    ponchiks.remove(i);
                }
            }
            for (int i = shots.size()-1; i >= 0; i--) {
                shots.get(i).move();
                if (shots.get(i).outOfBounds()) {
                    shots.remove(i);
                    break;
                }
                for (int j = ponchiks.size()-1; j >= 0; j--) {
                    if(shots.get(i).overlap(ponchiks.get(j))){
                        spawnFragments(ponchiks.get(j).x, ponchiks.get(j).y);
                        shots.remove(i);
                        ponchiks.remove(j);
                        if(gg.soundOn) sndExplosion.play();
                        frags++;
                        break;
                    }
                }
            }
        }
        for (int i = fragments.size()-1; i >= 0 ; i--) {
            fragments.get(i).move();
            if (fragments.get(i).outOfBounds()) {
                fragments.remove(i);
            }
        }

        // отрисовка всего
        gg.camera.update();
        gg.batch.setProjectionMatrix(gg.camera.combined);
        gg.batch.begin();
        for(Sky sky: skies) gg.batch.draw(imgStars, sky.getX(), sky.getY(), sky.width, sky.height);
        for(Fragment frag: fragments)
            gg.batch.draw(imgFragment[frag.type], frag.getX(), frag.getY(), frag.width/2, frag.height/2,
                    frag.width, frag.height, 1, 1, frag.angle);
        for(Ponchik ponchik: ponchiks) {
            gg.batch.draw(imgEnemy, ponchik.getX(), ponchik.getY(), ponchik.width, ponchik.height);
            gg.font.draw(gg.batch, ""+ponchik.letter, ponchik.x-12, ponchik.y+100);
        }
        for(Shot shot: shots) gg.batch.draw(imgShot, shot.getX(), shot.getY(), shot.width, shot.height);
        if(ship.isVisible) gg.batch.draw(imgShip, ship.getX(), ship.getY(), ship.width, ship.height);
        gg.font.draw(gg.batch, "FRAGS: "+frags, 10, SCR_HEIGHT-10);
        for (int i = 1; i < ship.lives+1; i++) {
            gg.batch.draw(imgShip, SCR_WIDTH-60*i, SCR_HEIGHT-60, 50, 50);
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
        pause = true;
    }

    @Override
    public void dispose() {
        imgStars.dispose();
        imgShip.dispose();
        imgAtlasFragment.dispose();
        sndExplosion.dispose();
        sndShot.dispose();
    }

    void spawnEnemy(){
        if(TimeUtils.millis() > timeEnemyLastSpawn+timeEnemySpawnInterval) {
            ponchiks.add(new Ponchik());
            timeEnemyLastSpawn = TimeUtils.millis();
        }
    }

    void spawnShot(){
        if(TimeUtils.millis() > timeShotLastSpawn+timeShotSpawnInterval) {
            shots.add(new Shot(ship.x, ship.y));
            timeShotLastSpawn = TimeUtils.millis();
            if(gg.soundOn) sndShot.play();
        }
    }

    void spawnFragments(float x, float y){
        for (int i = 0; i < 30; i++) {
            fragments.add(new Fragment(x, y));
        }
    }

    void killShip(){
        spawnFragments(ship.x, ship.y);
        ship.kill();
        if(gg.soundOn) sndExplosion.play();
    }
}
