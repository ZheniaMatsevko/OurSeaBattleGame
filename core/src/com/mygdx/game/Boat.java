package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Даний клас реалізує корабель, задає всі його поля та властивості, реалізує його основні функції
 */
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

    /**
     * Зчитуєм початкову клітинку корабля
     * @return початкова клітинка корабля
     */
    public Cell getStartCell(){
        return this.startCell;
    }

    /**
     * Задаєм початкову клітинку корабля
     * @param startCell початкова клітинка корабля
     */
    public void setStartCell(Cell startCell) {
        this.startCell = startCell;
    }

    /**
     * Задаєм напрям корабля
     * @param direction напрям корабля
     */
    public void setDirection(int direction){
        this.direction = direction;
    }

    /**
     * Зчитуєм напрям корабля
     * @return напрям корабля
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Зчитуєм розмір корабля
     * @return розмір корабля
     */
    public int getSize(){
        return this.size;
    }

    /**
     * Перевіряєм чи корабель знищений повністю
     * @return чи корабель знищений повністю
     */
    public boolean strike(){
        damaged++;
        if(damaged==size)
            return true;
        return false;
    }

    /**
     * Зчитуєм зображення корабля
     * @param size розмір корабля
     * @return зображення корабля
     */
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
