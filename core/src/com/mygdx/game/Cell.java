package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Cell extends Image {
    private boolean isTaken;
    private boolean shouldBeEmpty;
    private boolean isShot;
    private Boat boat;
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(this.getColor());
        ((TextureRegionDrawable)getDrawable()).draw(batch,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
    }
    public Boat getBoat(){
        return boat;
    }

    public void setBoat(Boat boat) {
        this.boat = boat;
    }

    public void setIsTaken(boolean taken){
        this.isTaken = taken;
    }
    public boolean getIsTaken(){
        return this.isTaken;
    }
    public void setShouldBeEmpty(boolean taken){
        this.shouldBeEmpty = taken;
    }
    public boolean getShouldBeEmpty(){
        return this.shouldBeEmpty;
    }

    public boolean isShot() {
        return isShot;
    }

    public void setShot(boolean shot) {
        isShot = shot;
    }

    public Cell(){
        super(new Texture(Gdx.files.internal("square.jpg")));
        isTaken=false;
        shouldBeEmpty = false;
        setBounds(getX(), getY(),getWidth(),getHeight());
        setTouchable(Touchable.enabled);
    }
    public void changeColor(Color color){
        ColorAction colorAction = new ColorAction();
        colorAction.setEndColor(color);
        Cell.this.addAction(colorAction);
    }
}
