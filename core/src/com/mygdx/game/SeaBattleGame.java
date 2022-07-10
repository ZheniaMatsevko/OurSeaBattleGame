package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sun.source.tree.EnhancedForLoopTree;

import java.util.Random;


public class SeaBattleGame extends Game {

    private int totalScore = 0;
    private int radarsUsed = 0;
    private int bombsUsed = 0;
    private int yourShipsKilled = 0;
    public Sound clicksound;
    public Sound click2;
    public Sound shipdestroy;
    public Sound miss1;
    public Sound miss2;
    public Sound damage;
    public Sound wave;
    public Sound radar;
    public Sound start;
    public Music mainMusic;
    public Music lostMusic;
    public Music wonMusic;
    public Music victoryMusic;




    public int getTotalScore() {
        return totalScore;
    }
    public void setTotalScore(int i) {
        totalScore = i;
    }

    public int getRadarsUsed() {
        return radarsUsed;
    }

    public void setRadarsUsed(int i) {
        radarsUsed = i;
    }

    public int getBombsUsed() {
        return bombsUsed;
    }
    public void setBombsUsed(int i) {
        bombsUsed = i;
    }

    public int getYourShipsKilled() {
        return yourShipsKilled;
    }
    public void setYourShipsKilled(int i) {
        yourShipsKilled = i;
    }

    ShapeRenderer shapeRenderer;
    @Override
    public void create () {

       soundOn();
       musicOn();




        shapeRenderer = new ShapeRenderer();
      // setScreen(new MainMenu(this,3,15));

       // setScreen(new PutShipsScreen(this,2));
       setScreen(new EndScreen(this,-1,10,2,7));
        //setScreen(new VictoryScreen(this));
    }
    @Override
    public void dispose () {
        shapeRenderer.dispose();
    }

    public Sound randomMiss() {
        Random random = new Random();
        int c = random.nextInt(2);
        if(c==0) return miss1;
        else return miss2;
    }

    public void soundOn() {
        clicksound = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));
        miss1 = Gdx.audio.newSound(Gdx.files.internal("miss1.mp3"));
        miss2 = Gdx.audio.newSound(Gdx.files.internal("miss2.mp3"));
        shipdestroy = Gdx.audio.newSound(Gdx.files.internal("shipdestroy.mp3"));
        click2 = Gdx.audio.newSound(Gdx.files.internal("click2.mp3"));
        damage = Gdx.audio.newSound(Gdx.files.internal("damage.wav"));
        wave = Gdx.audio.newSound(Gdx.files.internal("wave.mp3"));
        radar = Gdx.audio.newSound(Gdx.files.internal("radar.mp3"));


    }

    public void musicOn() {
        mainMusic = Gdx.audio.newMusic(Gdx.files.internal("soundtrack.mp3"));
        mainMusic.setVolume(0.5f);
        mainMusic.setLooping(true);
        victoryMusic = Gdx.audio.newMusic(Gdx.files.internal("victory.mp3"));
        victoryMusic.setVolume(0.5f);
        lostMusic = Gdx.audio.newMusic(Gdx.files.internal("lost.mp3"));
        lostMusic.setVolume(0.5f);

    }
    public void musicOff() {
        mainMusic = null;
        lostMusic = null;
        victoryMusic = null;
        wonMusic = null;
    }

    public void musicLow() {
        mainMusic.setVolume(0.2f);
        lostMusic.setVolume(0.2f);
        victoryMusic.setVolume(0.2f);
        wonMusic.setVolume(0.2f);
    }

    public void soundOff() {
        clicksound = null;
        miss1 = null;
        miss2 = null;
        shipdestroy = null;
        click2 = null;
        damage = null;
        wave = null;
    }
}
