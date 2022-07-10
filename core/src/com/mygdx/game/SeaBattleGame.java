package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sun.source.tree.EnhancedForLoopTree;


public class SeaBattleGame extends Game {

    private int totalScore = 0;
    private int radarsUsed = 0;
    private int bombsUsed = 0;
    private int yourShipsKilled = 0;

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
        shapeRenderer = new ShapeRenderer();
       setScreen(new MainMenu(this,1,0));
       // setScreen(new PutShipsScreen(this,2));
       // setScreen(new EndScreen(this,1,10,2,7));
       // setScreen(new VictoryScreen(this));
    }
    @Override
    public void dispose () {
        shapeRenderer.dispose();
    }
}
