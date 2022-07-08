package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SeaBattleGame extends Game {
    ShapeRenderer shapeRenderer;
    @Override
    public void create () {
        shapeRenderer = new ShapeRenderer();
        setScreen(new MainMenu(this,1));
        //setScreen(new PutShipsScreen(this,2));
    }
    @Override
    public void dispose () {
        shapeRenderer.dispose();
    }
}
