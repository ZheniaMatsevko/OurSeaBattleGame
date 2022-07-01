package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SeaBattleGame extends Game {
    ShapeRenderer shapeRenderer;
    MainMenu mainMenu;
    PutShipsScreen putShipsScreen;
    @Override
    public void create () {
        mainMenu = new MainMenu(this);
        putShipsScreen = new PutShipsScreen(this);
        shapeRenderer = new ShapeRenderer();
        setScreen(putShipsScreen);
    }
    public void setPutScreen(){
        setScreen(putShipsScreen);
    }

    @Override
    public void dispose () {
        shapeRenderer.dispose();
    }
}
