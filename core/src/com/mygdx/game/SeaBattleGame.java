package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sun.source.tree.EnhancedForLoopTree;

public class SeaBattleGame extends Game {
    ShapeRenderer shapeRenderer;
    @Override
    public void create () {
        shapeRenderer = new ShapeRenderer();
        setScreen(new MainMenu(this,2,5));
       // setScreen(new PutShipsScreen(this,2));
       // setScreen(new EndScreen(this,1,10,2,7));
    }
    @Override
    public void dispose () {
        shapeRenderer.dispose();
    }
}
