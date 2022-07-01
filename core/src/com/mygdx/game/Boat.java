package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Boat extends Image {
    private int size;
    private int damaged;
    private int direction;
    private Cell startCell;

    public Boat(int size, Texture image){
        super(image);
        this.size = size;
        this.damaged = 0;
        setBounds(getX(), getY(),getWidth(),getHeight());
        setTouchable(Touchable.enabled);
    }
    public Cell getStartCell(){
        return this.startCell;
    }

    public void setStartCell(Cell startCell) {
        this.startCell = startCell;
    }

    public void setDirection(int direction){
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public int getSize(){
        return this.size;
    }
    public boolean strike(){
        damaged++;
        if(damaged==size)
            return true;
        return false;
    }
    public int getIsDamaged(){
        return damaged;
    }

    public static Texture getBoatImage(int size){
        switch (size){
            case 1:
                return new Texture(Gdx.files.internal("boat1.png"));
            case 2:
                return new Texture(Gdx.files.internal("boat2.png"));
            case 3:
                return new Texture(Gdx.files.internal("boat3.png"));
            case 4:
                return new Texture(Gdx.files.internal("boat4.png"));
        }
        return null;
    }
}
