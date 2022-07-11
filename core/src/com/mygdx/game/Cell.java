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

/**
 * Даний клас реалізує роботу клітинки поля, задає усі її ключові поля та реалізовує основні методи
 */
public class Cell extends Image {
    private boolean isTaken;
    private boolean shouldBeEmpty;
    private boolean isShot = false;
    private boolean isRadared;
    private Boat boat;
    public Cell(){
        super(new Texture(Gdx.files.internal("square.jpg")));
        isTaken=false;
        shouldBeEmpty = false;
        setBounds(getX(), getY(),getWidth(),getHeight());
        setTouchable(Touchable.enabled);
    }

    /**
     * Малюєм клітинку
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(this.getColor());
        ((TextureRegionDrawable)getDrawable()).draw(batch,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
    }

    /**
     * Зчитуєм човен, який розташований в даній клітинці
     * @return човен
     */
    public Boat getBoat(){
        return boat;
    }

    /**
     * Перевіряєм чи до клітинки був застосований радар
     * @return чи до клітинки був застосований радар
     */
    public boolean isRadared() {
        return isRadared;
    }

    /**
     * Задаєм застосування радару для клітинки
     * @param radared чи до клітинки був застосований радар
     */
    public void setRadared(boolean radared) {
        isRadared = radared;
    }

    /**
     * Задаєм човен, який розташований в даній клітинці
     * @param boat човен
     */
    public void setBoat(Boat boat) {
        this.boat = boat;
    }

    /**
     * Задаєм чи клітинка зайнята
     * @param taken чи клітинка зайнята
     */
    public void setIsTaken(boolean taken){
        this.isTaken = taken;
    }

    /**
     * Зчитуєм чи клітинка зайнята
     * @return чи клітинка зайнята
     */
    public boolean getIsTaken(){
        return this.isTaken;
    }

    /**
     * Задаєм чи клітинка має бути порожньою
     * @param taken чи клітинка має бути порожньою
     */
    public void setShouldBeEmpty(boolean taken){
        this.shouldBeEmpty = taken;
    }

    /**
     * Зчитуєм чи клітинка має бути порожньою
     * @return чи клітинка має бути порожньою
     */
    public boolean getShouldBeEmpty(){
        return this.shouldBeEmpty;
    }

    /**
     * Зчитуєм чи клітинка застрілена
     * @return чи клітинка застрілена
     */
    public boolean isShot() {
        return isShot;
    }

    /**
     * Задаєм чи клітинка застрілена
     * @param shot чи клітинка застрілена
     */
    public void setShot(boolean shot) {
        isShot = shot;
    }

    /**
     * Змінюєм колір клітинки
     * @param color колір клітинки
     */
    public void changeColor(Color color){
        ColorAction colorAction = new ColorAction();
        colorAction.setEndColor(color);
        Cell.this.addAction(colorAction);
    }
}
