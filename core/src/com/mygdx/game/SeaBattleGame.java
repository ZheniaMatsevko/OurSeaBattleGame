package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.Random;

/**
 * Даний клас запускає гру, здійснює її загальні налаштування та містить статистичні дані
 */
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
    public Music mainMusic;
    public Music lostMusic;
    public Music wonMusic;
    public Music victoryMusic;
    public float delay = 2;
    public int musicStage;
    public boolean soundState;

    /**
     * Зчитуєм загальну кількість очок
     * @return загальна кількість очок
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Задаєм загальну кількість очок
     * @param i загальна кількість очок
     */
    public void setTotalScore(int i) {
        totalScore = i;
    }

    /**
     * Зчитуєм загальну кількість використаних радарів
     * @return загальна кількість використаних радарів
     */
    public int getRadarsUsed() {
        return radarsUsed;
    }

    /**
     * Задаєм загальну кількість використаних радарів
     * @param i загальна кількість використаних радарів
     */
    public void setRadarsUsed(int i) {
        radarsUsed = i;
    }

    /**
     * Задаєм загальну кількість використаних бомб
     * @return загальна кількість використаних бомб
     */
    public int getBombsUsed() {
        return bombsUsed;
    }

    /**
     * Зчитуєм загальну кількість використаних бомб
     * @param i загальна кількість використаних бомб
     */
    public void setBombsUsed(int i) {
        bombsUsed = i;
    }

    /**
     * Зчитуєм загальну кількість ваших вбитих кораблів
     * @return загальна кількість ваших вбитих кораблів
     */
    public int getYourShipsKilled() {
        return yourShipsKilled;
    }

    /**
     * Задаєм загальну кількість ваших вбитих кораблів
     * @param i загальна кількість ваших вбитих кораблів
     */
    public void setYourShipsKilled(int i) {
        yourShipsKilled = i;
    }

    ShapeRenderer shapeRenderer;

    /**
     * Запускаєм початковий екран
     */
    @Override
    public void create () {
        soundOn();
        musicOn();
        shapeRenderer = new ShapeRenderer();
        setScreen(new MainMenu(this,1,0));
    }

    /**
     * Видаляєм shapeRenderer
     */
    @Override
    public void dispose () {
        shapeRenderer.dispose();
    }

    /**
     * Вибираєм звук промаху
     * @return звук промаху
     */
    public Sound randomMiss() {
        Random random = new Random();
        int c = random.nextInt(2);
        if(c==0) return miss1;
        else return miss2;
    }

    /**
     * Добавляєм звуки
     */
    public void soundOn() {
        clicksound = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));
        miss1 = Gdx.audio.newSound(Gdx.files.internal("miss1.mp3"));
        miss2 = Gdx.audio.newSound(Gdx.files.internal("miss2.mp3"));
        shipdestroy = Gdx.audio.newSound(Gdx.files.internal("shipdestroy.mp3"));
        click2 = Gdx.audio.newSound(Gdx.files.internal("click2.mp3"));
        damage = Gdx.audio.newSound(Gdx.files.internal("damage.wav"));
        wave = Gdx.audio.newSound(Gdx.files.internal("wave.mp3"));
        radar = Gdx.audio.newSound(Gdx.files.internal("radar.mp3"));
        soundState = true;
    }

    /**
     * Включаєм музику
     */
    public void musicOn() {
        mainMusic = Gdx.audio.newMusic(Gdx.files.internal("soundtrack.mp3"));
        mainMusic.setVolume(0.5f);
        mainMusic.setLooping(true);
        victoryMusic = Gdx.audio.newMusic(Gdx.files.internal("victory.mp3"));
        victoryMusic.setVolume(0.5f);
        lostMusic = Gdx.audio.newMusic(Gdx.files.internal("lost.mp3"));
        lostMusic.setVolume(0.5f);
        wonMusic = Gdx.audio.newMusic(Gdx.files.internal("win.mp3"));
        wonMusic.setVolume(0.5f);
        musicStage = 2;

    }

    /**
     * Виключаєм музику
     */
    public void musicOff() {
        mainMusic.setVolume(0);
        lostMusic.setVolume(0);
        victoryMusic.setVolume(0);
        wonMusic.setVolume(0);
        musicStage = 0;
    }

    /**
     * Зменшуєм гучність музики
     */
    public void musicLow() {
        mainMusic.setVolume(0.2f);
        lostMusic.setVolume(0.2f);
        victoryMusic.setVolume(0.2f);
        wonMusic.setVolume(0.2f);
        musicStage = 1;
    }

    /**
     * Виключаєм звуки
     */
    public void soundOff() {
        soundState=false;
    }
}
