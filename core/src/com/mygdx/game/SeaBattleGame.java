package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SeaBattleGame extends Game {
    ShapeRenderer shapeRenderer;
    MainMenu mainMenu;
    PutShipsScreen putShipsScreen;
    @Override
    public void create () {
        shapeRenderer = new ShapeRenderer();
        //InputMultiplexer multiplexer = new InputMultiplexer();
        //multiplexer.addProcessor(mainMenu.getStage()); // set stage as first input processor
        //multiplexer.addProcessor(putShipsScreen);  // set your game input precessor as second
        //Gdx.input.setInputProcessor(multiplexer);
        setScreen(new MainMenu(this));
    }
    public void setPutScreen(){

        setScreen(putShipsScreen);
    }

    @Override
    public void dispose () {
        shapeRenderer.dispose();
    }
}
