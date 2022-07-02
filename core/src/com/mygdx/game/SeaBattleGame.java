package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SeaBattleGame extends Game {
    ShapeRenderer shapeRenderer;
    @Override
    public void create () {
        shapeRenderer = new ShapeRenderer();
        setScreen(new MainMenu(this));
    }
    @Override
    public void dispose () {
        shapeRenderer.dispose();
    }
}
