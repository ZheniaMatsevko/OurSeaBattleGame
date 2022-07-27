package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Даний клас реалізовує графічний інтерфейс та функції поля для гри
 */
public class PlayGround extends Actor  {
    private int numberOfCellsInRow;
    private Group group;
    private Stage stage;
    private Cell[][] cellsGround;
    private Boat[] boats;
    private Boat changedBoat;
    private int isBoatChanged;
    private float previousX;
    private float previousY;
    private boolean isCellDragged;
    private boolean isBoatsVisible;
    private Cell paintedCell;
    private int score;
    private String message;
    private Cell[] radaredCells;
    private LinkedList<Cell> bombedCells;

    public PlayGround(int numberOfCellsInRow, int numberOfBoats, int xC, int yC){
        message=null;
        paintedCell = null;
        isBoatsVisible=true;
        radaredCells = null;
        isBoatChanged = 0;
        score = 0;
        bombedCells = null;
        this.numberOfCellsInRow = numberOfCellsInRow;
        setBounds(0,Gdx.graphics.getHeight(),Gdx.graphics.getWidth(),Gdx.graphics.getHeight()-50);
        setTouchable(Touchable.enabled);
        group = new Group();
        stage = new Stage();
        cellsGround = new Cell[numberOfCellsInRow][numberOfCellsInRow];
        float x = xC, y=yC;
        int count=0;
        for(int i=0;i<numberOfCellsInRow;i++){
            for(int j=0;j<numberOfCellsInRow;j++){
                cellsGround[i][j] = new Cell();
                if(i==0 && j==0){
                    y-=cellsGround[i][j].getHeight();
                    cellsGround[i][j].setPosition(x,y);
                }else if(j==0){
                    cellsGround[i][j].setPosition(x,y);
                }else{
                    x=cellsGround[i][j-1].getX()+cellsGround[i][j-1].getWidth();
                    cellsGround[i][j].setPosition(x,y);
                }
                if(j==numberOfCellsInRow-1){
                    x=xC;
                    y=cellsGround[i][j-1].getY()-cellsGround[i][j-1].getHeight();
                }
                cellsGround[i][j].setName(String.valueOf(count++));
                group.addActor(cellsGround[i][j]);
            }
        }
        stage.addActor(group);
        boats = new Boat[numberOfBoats];
        for(int i=0;i<numberOfBoats;i++){
            if(i==0){
                boats[i] = new Boat(4,Boat.getBoatImage(4));
            } else if(i>0 && i<3) {
                boats[i] = new Boat(3, Boat.getBoatImage(3));
            }
            else if(i>2 && i<6) {
                boats[i] = new Boat(2, Boat.getBoatImage(2));
            }
            else {
                boats[i] = new Boat(1, Boat.getBoatImage(1));
            }
            stage.addActor(boats[i]);
        }
        putRandomBoats();
        isCellDragged = false;
    }

    /**
     * Задаєм клітинки для бомбардування
     * @param bombedCells клітинки для бомбардування
     */
    public void setBombedCells(LinkedList<Cell> bombedCells) {
        this.bombedCells = bombedCells;
    }

    /**
     * Зчитуєм клітинки для бомбардування
     * @return клітинки для бомбардування
     */
    public LinkedList<Cell> getBombedCells(){
        return this.bombedCells;
    }

    /**
     * Зчитуєм основну клітинку для бомбардування
     * @return основна клітинка для бомбардування
     */
    public Cell getMainBombedCell(){
        return bombedCells.getFirst();
    }

    /**
     * Задаєм клітинки для радару
     * @param cells клітинки для радару
     */
    public void setRadaredCells(Cell[] cells){
        if(cells==null)
            radaredCells=null;
        else{
            this.radaredCells = new Cell[cells.length];
            for(int i=0;i<cells.length;i++){
                radaredCells[i] = cells[i];
            }
        }
    }

    /**
     * Зчитуєм основну клітинку для радару
     * @return основна клітинка для радару
     */
    public Cell getMainRadaredCell(){
        return radaredCells[0];
    }

    /**
     * Зчитуєм клітинки для радару
     * @return клітинки для радару
     */
    public Cell[] getRadaredCells() {
        return radaredCells;
    }

    /**
     * Розставляєм кораблі на свої місця
     */
    public void putShipsOnItsPlaces(){
        for(int i=0;i<numberOfCellsInRow;i++){
            if(boats[i].getDirection()==0){
                if(boats[i].getX()!=boats[i].getStartCell().getX() || boats[i].getY()!=boats[i].getStartCell().getY())
                    boats[i].setPosition(boats[i].getStartCell().getX(),boats[i].getStartCell().getY());
            }
            else if(boats[i].getDirection()==1){
                if(boats[i].getX()!=boats[i].getStartCell().getX()+getCellWidth() || boats[i].getY()!=boats[i].getStartCell().getY())
                    boats[i].setPosition(boats[i].getStartCell().getX()+getCellWidth(),boats[i].getStartCell().getY());
            }
        }
    }

    /**
     * Задаєм нові координати для клітинок
     * @param xC новий X
     * @param yC новий Y
     */
    public void setNewPositions(int xC, int yC){
        float x = xC, y=yC;
        for(int i=0;i<numberOfCellsInRow;i++){
            for(int j=0;j<numberOfCellsInRow;j++){
                if(i==0 && j==0){
                    y-=cellsGround[i][j].getHeight();
                    cellsGround[i][j].setPosition(x,y);
                }else if(j==0){
                    cellsGround[i][j].setPosition(x,y);
                }else{
                    x=cellsGround[i][j-1].getX()+cellsGround[i][j-1].getWidth();
                    cellsGround[i][j].setPosition(x,y);
                }
                if(j==numberOfCellsInRow-1){
                    x=xC;
                    y=cellsGround[i][j-1].getY()-cellsGround[i][j-1].getHeight();
                }
            }
        }
        for(int i=0;i<numberOfCellsInRow;i++){
            if(boats[i].getDirection()==0)
                boats[i].setPosition(boats[i].getStartCell().getX(),boats[i].getStartCell().getY());
            if(boats[i].getDirection()==1)
                boats[i].setPosition(boats[i].getStartCell().getX()+getCellWidth(),boats[i].getStartCell().getY());
        }
    }

    /**
     * Задаєм чи кораблі видно
     * @param boatsVisible чи кораблі видно
     */
    public void setBoatsVisible(boolean boatsVisible) {
        isBoatsVisible = boatsVisible;
        for(int i=0;i<numberOfCellsInRow;i++){
            boats[i].setVisible(boatsVisible);
        }
    }

    /**
     * Перевіряєм чи клітинка зайнята
     * @param cell клітинка
     * @return чи клітинка зайнята
     */
    public boolean checkShotCell(Cell cell){
        if(!cell.getIsTaken())
            return false;
        return true;
    }

    /**
     * Зчитуєм клітинку, на яку наведений курсор мишки
     * @return клітинка, на яку наведений курсор мишки
     */
    public Cell getPaintedCell() {
        return paintedCell;
    }

    /**
     * Задаєм клітинку, на яку наведений курсор мишки
     * @param paintedCell клітинка, на яку наведений курсор мишки
     */
    public void setPaintedCell(Cell paintedCell) {
        this.paintedCell = paintedCell;
    }

    /**
     * Зчитуєм назву клітинки
     * @param cell клітинка
     * @return назва клітинки
     */
    public String getCellName(Cell cell){
        String name="";
        String letters = "ABCDEFGHIJ";
        name+=letters.charAt(getJ(cell)) + String.valueOf(getI(cell)+1);
        return name;
    }

    /**
     * Стріляєм в клітинку
     * @param cell клітинка
     * @return чи було вбито корабель
     */
    public boolean killCell(Cell cell){
        cell.setShot(true);
        if(cell.getBoat().strike()){
            score++;
            cell.getBoat().setVisible(true);
            Image cross = new Image(new Texture(Gdx.files.internal("x.png")));
            cross.setPosition(cell.getX(),cell.getY());
            stage.addActor(cross);
            int x=0,y=0;
            for(int i=0;i<numberOfCellsInRow;i++){
                for(int j=0;j<numberOfCellsInRow;j++){
                    if(cellsGround[i][j].equals(cell.getBoat().getStartCell())) {
                        x = i;
                        y = j;
                        break;
                    }
                }
            }
            if(cell.getBoat().getDirection()==0)
                shotSurroundingHorizontal(x,y,cell.getBoat().getSize());
            if(cell.getBoat().getDirection()==1)
                shotSurroundingVertical(x,y,cell.getBoat().getSize());
            return true;
        }else{
            Image cross = new Image(new Texture(Gdx.files.internal("x.png")));
            cross.setPosition(cell.getX(),cell.getY());
            stage.addActor(cross);
            return false;
        }

    }

    /**
     * Зчитуєм рахунок гри
     * @return рахунок гри
     */
    public int getScore() {
        return score;
    }

    /**
     * Задаєм чи зміщено клітинку
     * @param cellDragged чи зміщено клітинку
     */
    public void setCellDragged(boolean cellDragged) {
        isCellDragged = cellDragged;
    }

    /**
     * Зчитуєм чи зміщено клітинку
     * @return чи зміщено клітинку
     */
    public boolean isCellDragged() {
        return isCellDragged;
    }

    /**
     * Задаєм попередню координату X клітинки
     * @param previousX попередня координата X клітинки
     */
    public void setPreviousX(float previousX) {
        this.previousX = previousX;
    }
    /**
     * Задаєм попередню координату Y клітинки
     * @param previousY попередня координата Y клітинки
     */
    public void setPreviousY(float previousY) {
        this.previousY = previousY;
    }

    /**
     * Зчитуєм чи було розташування корабля зміщене
     * @return чи було розташування корабля зміщене
     */
    public int isBoatChanged() {
        return isBoatChanged;
    }

    /**
     * Задаєм чи було розташування корабля зміщене
     * @param val чи було розташування корабля зміщене
     */
    public void setIsBoatChanged(int val){
        this.isBoatChanged = val;
    }

    /**
     * Зчитуєм зміщений корабель
     * @return зміщений корабель
     */
    public Boat getChangedBoat(){
        return this.changedBoat;
    }

    /**
     * Задаєм зміщений корабель
     * @param changedBoat зміщений корабель
     */
    public void setChangedBoat(Boat changedBoat) {
        if(previousX==0 && previousY==0){
            previousX = changedBoat.getX();
            previousY = changedBoat.getY();
        }
        this.changedBoat = changedBoat;
    }

    /**
     * Обстрілюєм суміжні клітинки біля вбитого корабля по горизонталі
     * @param originX рядок в масиві клітинок
     * @param originY стовпець в масиві клітинок
     * @param boatSize розмір корабля
     */
    private void shotSurroundingHorizontal(int originX, int originY, int boatSize){
        LinkedList<Cell> emptyCells = new LinkedList<>();
        if(originY-1>=0) {
            emptyCells.add(cellsGround[originX][originY - 1]);
        }
        if (originY - 1 >= 0 && originX - 1 >= 0)
            emptyCells.add(cellsGround[originX-1][originY - 1]);
        if (originY - 1 >= 0 && originX + 1 < numberOfCellsInRow)
            emptyCells.add(cellsGround[originX+1][originY - 1]);
        for (int i = 0; i < boatSize; i++) {
            if (originX + 1 < numberOfCellsInRow)
                emptyCells.add(cellsGround[originX+1][originY + i]);
            if (originX - 1 >= 0)
                emptyCells.add(cellsGround[originX-1][originY +i]);
        }
        if (originY + boatSize < numberOfCellsInRow && originX - 1 >= 0)
            emptyCells.add(cellsGround[originX-1][originY + boatSize]);
        if (originY + boatSize < numberOfCellsInRow && originX + 1 < numberOfCellsInRow)
            emptyCells.add(cellsGround[originX+1][originY + boatSize]);
        if (originY + boatSize < numberOfCellsInRow)
            emptyCells.add(cellsGround[originX][originY + boatSize]);
        for(Cell cell: emptyCells){
            cell.setShot(true);
            cell.changeColor(Color.GRAY);
        }
    }
    /**
     * Обстрілюєм суміжні клітинки біля вбитого корабля по вертикалі
     * @param originX рядок в масиві клітинок
     * @param originY стовпець в масиві клітинок
     * @param boatSize розмір корабля
     */
    private void shotSurroundingVertical(int originX, int originY, int boatSize) {
        LinkedList<Cell> emptyCells = new LinkedList<>();
        if (originX + 1 <numberOfCellsInRow) {
            emptyCells.add(cellsGround[originX+1][originY]);
        }
        if (originY - 1 >= 0 && originX + 1 <numberOfCellsInRow)
            emptyCells.add(cellsGround[originX+1][originY - 1]);
        if (originX + 1 <numberOfCellsInRow && originY + 1 < numberOfCellsInRow)
            emptyCells.add(cellsGround[originX+1][originY +1]);
        for (int i = 0; i < boatSize; i++) {
            if (originY + 1 < numberOfCellsInRow && originX-i>=0)
                emptyCells.add(cellsGround[originX-i][originY + 1]);
            if (originY - 1 >= 0 && originX-i>=0)
                emptyCells.add(cellsGround[originX-i][originY - 1]);
        }
        if (originX - boatSize >= 0 && originY - 1 >= 0)
            emptyCells.add(cellsGround[originX - boatSize][originY - 1]);
        if (originX - boatSize >= 0 && originY + 1 < numberOfCellsInRow)
            emptyCells.add(cellsGround[originX - boatSize][originY + 1]);
        if (originX - boatSize >= 0)
            emptyCells.add(cellsGround[originX - boatSize][originY]);
        for(Cell cell: emptyCells){
            cell.setShot(true);
            cell.changeColor(Color.GRAY);
        }
    }
    /**
     * Перевіряєм чи можливо розташувати корабель по горизонталі
     * @param originX рядок в масиві клітинок
     * @param originY стовпець в масиві клітинок
     * @param boatSize розмір корабля
     * @param boat корабель
     */
    private boolean checkSurroundingHorizontal(int originX, int originY, int boatSize, Boat boat){
        if(originY-1>=0 && cellsGround[originX][originY-1].getIsTaken()){
            return false;
        }else if(originY-1>=0){
            cellsGround[originX][originY-1].setShouldBeEmpty(true);
        }
        if(originY-1>=0 && originX-1>=0 && cellsGround[originX-1][originY-1].getIsTaken()) {
            return false;
        }
        else if(originY-1>=0 && originX-1>=0)
            cellsGround[originX-1][originY-1].setShouldBeEmpty(true);
        if(originY-1>=0 && originX+1<numberOfCellsInRow && cellsGround[originX+1][originY-1].getIsTaken()) {
            return false;
        }
        else if(originY-1>=0 && originX+1<numberOfCellsInRow)
            cellsGround[originX+1][originY-1].setShouldBeEmpty(true);;
        for(int i=0;i<boatSize;i++){
            if(originX+1<numberOfCellsInRow && cellsGround[originX+1][originY+i].getIsTaken()) {
                return false;
            }
            else if(originX+1<numberOfCellsInRow)
                (cellsGround[originX+1][originY+i]).setShouldBeEmpty(true);
            if(originX-1>=0 && cellsGround[originX-1][originY+i].getIsTaken()) {
                return false;
            }
            else if(originX-1>=0)
                cellsGround[originX-1][originY+i].setShouldBeEmpty(true);
        }
        if(originY+boatSize<numberOfCellsInRow && originX-1>=0 && cellsGround[originX-1][originY+boatSize].getIsTaken()) {
            return false;
        }
        else if(originY+boatSize<numberOfCellsInRow && originX-1>=0)
            cellsGround[originX-1][originY+boatSize].setShouldBeEmpty(true);
        if(originY+boatSize<numberOfCellsInRow && originX+1<numberOfCellsInRow && cellsGround[originX+1][originY+boatSize].getIsTaken()) {
            return false;
        }
        else if(originY+boatSize<numberOfCellsInRow && originX+1<numberOfCellsInRow)
            cellsGround[originX+1][originY+boatSize].setShouldBeEmpty(true);
        if(originY+boatSize<numberOfCellsInRow && cellsGround[originX][originY+boatSize].getIsTaken()) {
            return false;
        }
        else if(originY+boatSize<numberOfCellsInRow)
            cellsGround[originX][originY+boatSize].setShouldBeEmpty(true);
        for(int i=0;i<boatSize;i++){
            cellsGround[originX][originY+i].setIsTaken(true);
            cellsGround[originX][originY+i].setBoat(boat);
        }
        return true;
    }
    /**
     * Перевіряєм чи можливо розташувати корабель по вертикалі
     * @param originX рядок в масиві клітинок
     * @param originY стовпець в масиві клітинок
     * @param boatSize розмір корабля
     * @param boat корабель
     */
    private boolean checkSurroundingVertical(int originX, int originY, int boatSize, Boat boat){
        if(originX+1<numberOfCellsInRow && cellsGround[originX+1][originY].getIsTaken()){
            return false;
        }else if(originX+1<numberOfCellsInRow){
            cellsGround[originX+1][originY].setShouldBeEmpty(true);
        }
        if(originY-1>=0 && originX+1<numberOfCellsInRow && cellsGround[originX+1][originY-1].getIsTaken())
            return false;
        else if(originY-1>=0 && originX+1<numberOfCellsInRow)
            cellsGround[originX+1][originY-1].setShouldBeEmpty(true);
        if(originX+1<numberOfCellsInRow && originY+1<numberOfCellsInRow && cellsGround[originX+1][originY+1].getIsTaken())
            return false;
        else if(originX+1<numberOfCellsInRow && originY+1<numberOfCellsInRow)
            cellsGround[originX+1][originY+1].setShouldBeEmpty(true);
        for(int i=0;i<boatSize;i++){
            if(originY+1<numberOfCellsInRow && cellsGround[originX-i][originY+1].getIsTaken())
                return false;
            else if(originY+1<numberOfCellsInRow)
                cellsGround[originX-i][originY+1].setShouldBeEmpty(true);
            if(originY-1>=0 && cellsGround[originX-i][originY-1].getIsTaken())
                return false;
            else if(originY-1>=0)
                cellsGround[originX-i][originY-1].setShouldBeEmpty(true);
        }
        if(originX-boatSize>=0 && originY-1>=0 && cellsGround[originX-boatSize][originY-1].getIsTaken())
            return false;
        else if(originX-boatSize>=0 && originY-1>=0)
            cellsGround[originX-boatSize][originY-1].setShouldBeEmpty(true);
        if(originX-boatSize>=0 && originY+1<numberOfCellsInRow && cellsGround[originX-boatSize][originY+1].getIsTaken())
            return false;
        else if(originX-boatSize>=0 && originY+1<numberOfCellsInRow)
            cellsGround[originX-boatSize][originY+1].setShouldBeEmpty(true);
        if(originX-boatSize>=0 && cellsGround[originX-boatSize][originY].getIsTaken())
            return false;
        else if(originX-boatSize>=0)
            cellsGround[originX-boatSize][originY].setShouldBeEmpty(true);
        for(int i=0;i<boatSize;i++){
            cellsGround[originX-i][originY].setIsTaken(true);
            cellsGround[originX-i][originY].setBoat(boat);
        }
        return true;
    }

    /**
     * Зчитуєм ширину клітинки
     * @return ширина клітинки
     */
    public float getCellWidth(){
        return cellsGround[0][0].getWidth();
    }

    /**
     * Зчитуєм висоту клітинки
     * @return висота клітинки
     */
    public float getCellHeight(){
        return cellsGround[0][0].getHeight();
    }

    /**
     * Очищуєм поле
     */
    public void cleanField(){
        for(int i=0;i<numberOfCellsInRow;i++){
            for(int j=0;j<numberOfCellsInRow;j++){
                cellsGround[i][j].setIsTaken(false);
                cellsGround[i][j].setBoat(null);
                cellsGround[i][j].setShouldBeEmpty(false);
            }
            boats[i].setRotation(0);
        }
    }

    /**
     * Зчитуєм рядок клітинки на полі
     * @param cell клітинка
     * @return рядок клітинки на полі
     */
    public int getI(Cell cell){
        for(int i=0;i<numberOfCellsInRow;i++){
            for(int j=0;j<numberOfCellsInRow;j++){
                if(cellsGround[i][j].equals(cell))
                    return i;
            }
        }
        return -1;
    }
    /**
     * Зчитуєм рядок стовпець на полі
     * @param cell клітинка
     * @return стовпець клітинки на полі
     */
    public int getJ(Cell cell){
        for(int i=0;i<numberOfCellsInRow;i++){
            for(int j=0;j<numberOfCellsInRow;j++){
                if(cellsGround[i][j].equals(cell))
                    return j;
            }
        }
        return -1;
    }

    /**
     * Зчитуєм клітинку
     * @param i рядок
     * @param j стовпець
     * @return клітинка
     */
    public Cell getCell(int i, int j){
        return cellsGround[i][j];
    }

    /**
     * Зчитуєм кількість клітинок в рядку
     * @return кількість клітинок в рядку
     */
    public int getNumberOfCellsInRow(){
        return numberOfCellsInRow;
    }

    /**
     * Розставляєм кораблі на рандомні локації
     */
    public void putRandomBoats(){
        int originX,originY;
        int direction=0;
        boolean canLocate = false;
        for(int b=0;b<numberOfCellsInRow;b++){
            canLocate = false;
            do{
                originX = ThreadLocalRandom.current().nextInt(0, numberOfCellsInRow);
                originY = ThreadLocalRandom.current().nextInt(0, numberOfCellsInRow);
                while(!cellsGround[originX][originY].getShouldBeEmpty() && cellsGround[originX][originY].getIsTaken() || (originX-1>=0 && cellsGround[originX-1][originY].getIsTaken()) || (originY-1>=0 && cellsGround[originX][originY-1].getIsTaken()) || (originY-1>=0 && originX-1>=0 && cellsGround[originX-1][originY-1].getIsTaken()) || (originY+1<10 && cellsGround[originX][originY+1].getIsTaken()) || (originX+1<10 && cellsGround[originX+1][originY].getIsTaken()) || (originX+1<10 && originY+1<10 && cellsGround[originX+1][originY+1].getIsTaken()) || (originX+1<10 && originY-1>=0 && cellsGround[originX+1][originY-1].getIsTaken()) || (originY+1<10 && originX-1>=0 && cellsGround[originX-1][originY+1].getIsTaken())){
                    originX = ThreadLocalRandom.current().nextInt(0, numberOfCellsInRow);
                    originY = ThreadLocalRandom.current().nextInt(0, numberOfCellsInRow);
                }
                if(boats[b].getSize()==1){
                    checkSurroundingHorizontal(originX,originY,1, boats[b]);
                    break;
                }
                direction = ThreadLocalRandom.current().nextInt(0, 2);
                if(direction==0){
                    int count = boats[b].getSize();
                    int i = originX,j = originY;
                    while(j<numberOfCellsInRow && !cellsGround[i][j].getIsTaken() && count>0){
                        j++;
                        count--;
                    }
                    if(count>0 && j==numberOfCellsInRow){
                        j=originY-1;
                        while(count>0 && !cellsGround[i][j].getIsTaken()){
                            count--;
                            j--;
                        }
                        if(count==0)
                            originY=j+1;
                    }
                    if(count==0){
                        if(checkSurroundingHorizontal(originX,originY,boats[b].getSize(), boats[b])){
                            canLocate=true;
                        }
                    }
                }else{
                    int count = boats[b].getSize();
                    int i = originX,j = originY;
                    while(i>=0 && !cellsGround[i][j].getIsTaken() && count>0){
                        i--;
                        count--;
                    }
                    if(count>0 && i==-1){
                        i=originX+1;
                        while(count>0 && !cellsGround[i][j].getIsTaken()){
                            count--;
                            i++;
                        }
                        if(count==0)
                            originX=i-1;
                    }
                    if(count==0){
                        if(checkSurroundingVertical(originX,originY,boats[b].getSize(), boats[b])){
                            canLocate=true;
                        }
                    }
                }
            }while(!canLocate);
            if(boats[b].getSize()==1){
                boats[b].setDirection(0);
                boats[b].setStartCell(cellsGround[originX][originY]);
                boats[b].setPosition(cellsGround[originX][originY].getX(),cellsGround[originX][originY].getY());
            }
            else if(direction==0){
                boats[b].setDirection(0);
                boats[b].setStartCell(cellsGround[originX][originY]);
                boats[b].setPosition(cellsGround[originX][originY].getX(),cellsGround[originX][originY].getY());
            }else{
                boats[b].setDirection(1);
                boats[b].setStartCell(cellsGround[originX][originY]);
                boats[b].setRotation(90);
                boats[b].setPosition(cellsGround[originX][originY].getX() + cellsGround[originX][originY].getWidth(),cellsGround[originX][originY].getY());
            }
        }
    }

    /**
     * Зчитуєм клітинку за координатами
     * @param x координата X
     * @param y координата Y
     * @return клітинка
     */
    public Cell findCell(float x, float y){
        for(int i=0;i<numberOfCellsInRow;i++){
            for(int j=0;j<numberOfCellsInRow;j++){
                if(cellsGround[i][j].getX()<=x && x<=cellsGround[i][j].getX()+getCellWidth() && cellsGround[i][j].getY()<=y && y<=cellsGround[i][j].getY()+getCellHeight()){
                    return cellsGround[i][j];
                }
            }
        }
        return null;
    }

    /**
     * Перевіряєм чи можемо розташувати зміщений користувачем корабель
     * @param cellOrigin початкова клітинка
     * @return чи можемо розташувати зміщений користувачем корабель
     */
    private  boolean checkIfCanLocateUserBoat(Cell cellOrigin){
        int x=0,y=0;
        for(int i=0;i<numberOfCellsInRow;i++){
            for(int j=0;j<numberOfCellsInRow;j++){
                if(cellsGround[i][j].getName().equals(cellOrigin.getName())){
                    x=i;
                    y=j;
                    break;
                }
            }
        }
        if(cellsGround[x][y].getIsTaken() || cellsGround[x][y].getShouldBeEmpty()){
            return false;
        }
        if(changedBoat.getDirection()==0){
            for(int i=0;i<changedBoat.getSize();i++){
                if(y+i>=numberOfCellsInRow || cellsGround[x][y+i].getIsTaken() || cellsGround[x][y+i].getShouldBeEmpty()){
                    return false;
                }
            }
            if(!checkSurroundingHorizontal(x,y,changedBoat.getSize(),changedBoat)){
                return false;
            }
        }else if(changedBoat.getDirection()==1){
            for(int i=0;i<changedBoat.getSize();i++){
                if(x-i <0 || cellsGround[x-i][y].getIsTaken() || cellsGround[x-i][y].getShouldBeEmpty()){
                    return false;
                }
            }
            if(!checkSurroundingVertical(x,y,changedBoat.getSize(),changedBoat)){
                return false;
            }
        }
        return true;
    }

    /**
     * Видаляєм колишнє розташування корабля по горизонталі
     * @param originX рядок корабля
     * @param originY стовпець корабля
     */
    private void removeOldlocationHorizontal(int originX, int originY){
        if(originY-1>=0) {
            cellsGround[originX][originY - 1].setShouldBeEmpty(false);
        }
        if (originY - 1 >= 0 && originX - 1 >= 0)
            cellsGround[originX-1][originY - 1].setShouldBeEmpty(false);
        if (originY - 1 >= 0 && originX + 1 < numberOfCellsInRow)
            cellsGround[originX+1][originY - 1].setShouldBeEmpty(false);
        for (int i = 0; i < changedBoat.getSize(); i++) {
            if (originX + 1 < numberOfCellsInRow)
                cellsGround[originX+1][originY + i].setShouldBeEmpty(false);
            if (originX - 1 >= 0)
                cellsGround[originX-1][originY +i].setShouldBeEmpty(false);
        }
        if (originY + changedBoat.getSize() < numberOfCellsInRow && originX - 1 >= 0)
            cellsGround[originX-1][originY + changedBoat.getSize()].setShouldBeEmpty(false);
        if (originY + changedBoat.getSize() < numberOfCellsInRow && originX + 1 < numberOfCellsInRow)
            cellsGround[originX+1][originY + changedBoat.getSize()].setShouldBeEmpty(false);
        if (originY + changedBoat.getSize() < numberOfCellsInRow)
            cellsGround[originX][originY + changedBoat.getSize()].setShouldBeEmpty(false);
        for (int i = 0; i < changedBoat.getSize(); i++) {
            cellsGround[originX][originY+i].setIsTaken(false);
            cellsGround[originX][originY+i].setBoat(null);
        }
    }
    /**
     * Видаляєм колишнє розташування корабля по вертикалі
     * @param originX рядок корабля
     * @param originY стовпець корабля
     */
    private void removeOldlocationVertical(int originX, int originY) {
        if (originX + 1 <numberOfCellsInRow) {
            cellsGround[originX+1][originY].setShouldBeEmpty(false);
        }
        if (originY - 1 >= 0 && originX + 1 <numberOfCellsInRow)
            cellsGround[originX+1][originY - 1].setShouldBeEmpty(false);
        if (originX + 1 <numberOfCellsInRow && originY + 1 < numberOfCellsInRow)
            cellsGround[originX+1][originY +1].setShouldBeEmpty(false);
        for (int i = 0; i < changedBoat.getSize(); i++) {
            if (originY + 1 < numberOfCellsInRow)
                cellsGround[originX-i][originY + 1].setShouldBeEmpty(false);
            if (originY - 1 >= 0)
                cellsGround[originX-i][originY - 1].setShouldBeEmpty(false);
        }
        if (originX - changedBoat.getSize() >= 0 && originY - 1 >= 0)
            cellsGround[originX - changedBoat.getSize()][originY - 1].setShouldBeEmpty(false);
        if (originX - changedBoat.getSize() >= 0 && originY + 1 < numberOfCellsInRow)
            cellsGround[originX - changedBoat.getSize()][originY + 1].setShouldBeEmpty(false);
        if (originX - changedBoat.getSize() >= 0)
            cellsGround[originX - changedBoat.getSize()][originY].setShouldBeEmpty(false);
        for (int i = 0; i < changedBoat.getSize(); i++) {
            cellsGround[originX - i][originY].setIsTaken(false);
            cellsGround[originX-i][originY].setBoat(null);
        }
    }
    /**
     * Видаляєм колишнє розташування корабля
     */
    private void removeOldLocation(){
        Cell previous = changedBoat.getStartCell();
        int x=0,y=0;
        for(int i=0;i<numberOfCellsInRow;i++){
            for(int j=0;j<numberOfCellsInRow;j++){
                if(cellsGround[i][j].equals(previous)) {
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        if(previous!=null){
            if(changedBoat.getDirection()==0)
                removeOldlocationHorizontal(x,y);
            else
                removeOldlocationVertical(x,y);
        }
    }
    /**
     * Добавляєм колишнє розташування корабля по горизонталі
     */
    private void addOldLocation(){
        Cell previous = changedBoat.getStartCell();
        int x=0,y=0;
        for(int i=0;i<numberOfCellsInRow;i++){
            for(int j=0;j<numberOfCellsInRow;j++){
                if(cellsGround[i][j].equals(previous)) {
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        if(previous!=null){
            if(changedBoat.getDirection()==0)
                checkSurroundingHorizontal(x,y, changedBoat.getSize(),changedBoat);
            else
                checkSurroundingVertical(x,y,changedBoat.getSize(),changedBoat);
        }
    }

    /**
     * Обертаємо корабель
     * @return чи вдалось повернути корабель
     */
    public boolean Rotate(){
        Cell cellOrigin = changedBoat.getStartCell();
        removeOldLocation();
        if(changedBoat.getDirection()==0){
            changedBoat.setRotation(90);
            changedBoat.setDirection(1);
            if(checkIfCanLocateUserBoat(cellOrigin)){
                changedBoat.setPosition(cellOrigin.getX()+getCellWidth(),cellOrigin.getY());
            }else{
                changedBoat.setRotation(0);
                changedBoat.setDirection(0);
                changedBoat.setPosition(cellOrigin.getX(),cellOrigin.getY());
                addOldLocation();
                message="Can`t rotate the boat!";
            }
        }else{
            changedBoat.setRotation(0);
            changedBoat.setDirection(0);
            if(checkIfCanLocateUserBoat(cellOrigin)){
                changedBoat.setPosition(cellOrigin.getX(),cellOrigin.getY());
            }else{
                changedBoat.setRotation(90);
                changedBoat.setDirection(1);
                changedBoat.setPosition(cellOrigin.getX()+getCellWidth(),cellOrigin.getY());
                addOldLocation();
                message="Can`t rotate the boat!";
            }
        }
        isBoatChanged=0;
        return true;
    }

    /**
     * Зчитуєм повідомлення
     * @return повідомлення
     */
    public String getMessage(){
        return this.message;
    }

    /**
     * Малюємо поле для гри
     * @param batch
     * @param parentAlpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(isBoatChanged==2 && isCellDragged==false){
            if(changedBoat!=null){
                 Cell cellOrigin = findCell(changedBoat.getX(),changedBoat.getY()-1);
                if(changedBoat.getDirection()==1)
                    cellOrigin = findCell(changedBoat.getX()-getCellWidth(),changedBoat.getY()-1);
                if(changedBoat.getDirection()==1 && changedBoat.getX()-getCellWidth()<getCell(0,0).getX()){
                    changedBoat.setPosition(previousX,previousY);
                    message="Ships should be located on the field!";
                }
                if(changedBoat.getDirection()==1 && changedBoat.getX()-getCellWidth()>getCell(9,9).getX()+getCellWidth()){
                    changedBoat.setPosition(previousX,previousY);
                    message="Ships should be located on the field!";
                }
                if(cellOrigin!=null){
                    int val = Integer.valueOf(cellOrigin.getName());
                    if(changedBoat.getDirection()==0 && val%10+changedBoat.getSize()>10){
                        changedBoat.setPosition(previousX,previousY);
                        message="Ships should be located on the field!";
                    }else if(changedBoat.getDirection()==1 && changedBoat.getSize()*10-val>10){
                        changedBoat.setPosition(previousX,previousY);
                        message="Ships should be located on the field!";
                    }else{
                        removeOldLocation();
                        if(checkIfCanLocateUserBoat(cellOrigin)){
                            changedBoat.setPosition(cellOrigin.getX(),cellOrigin.getY());
                            if(changedBoat.getDirection()==1)
                                changedBoat.setPosition(cellOrigin.getX()+getCellWidth(),cellOrigin.getY());
                            changedBoat.setStartCell(cellOrigin);
                            message=null;
                        }else{
                            changedBoat.setPosition(previousX,previousY);
                            message="Ships can`t be located near each other!";
                            addOldLocation();
                        }
                    }
                }
                else if(changedBoat.getX()<this.getWidth() && changedBoat.getDirection()==1){
                    cellOrigin = findCell(cellsGround[0][0].getX()+getCellWidth()*numberOfCellsInRow, changedBoat.getY()-1);
                    removeOldLocation();
                    if(cellOrigin!=null){
                        if(checkIfCanLocateUserBoat(cellOrigin)){
                            changedBoat.setPosition(cellOrigin.getX() + getCellWidth(),cellOrigin.getY());
                            changedBoat.setStartCell(cellOrigin);
                            message="Ships must be located on the screen!";
                        }
                    }
                    else{
                        addOldLocation();
                        message="Ships must be located on the screen!";
                        changedBoat.setPosition(previousX,previousY);
                    }
                }
                else if(cellOrigin==null){
                    changedBoat.setPosition(previousX,previousY);
                    message="Ships must be located on the field!";
                }
            }
            previousX = 0;
            previousY = 0;
            isBoatChanged=0;
        }
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    /**
     * Зчитуємо stage
     * @return stage
     */
    public Stage getStage(){
        return stage;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }


    /**
     * Вибираємо клітинки для хвилі
     * @return клітинки для хвилі
     */
    public java.util.List<Cell> chooseWaveCells(){
        List<Cell> waveCells = new LinkedList<>();
        int originX1 = ThreadLocalRandom.current().nextInt(0, this.getNumberOfCellsInRow());
        int originY1 = ThreadLocalRandom.current().nextInt(0, this.getNumberOfCellsInRow());
        while(this.getCell(originX1,originY1).isShot()){
            originX1 = ThreadLocalRandom.current().nextInt(0, this.getNumberOfCellsInRow());
            originY1 = ThreadLocalRandom.current().nextInt(0, this.getNumberOfCellsInRow());
        }
        Cell cell1 = this.getCell(originX1,originY1);
        int originX2 = ThreadLocalRandom.current().nextInt(0, this.getNumberOfCellsInRow());
        int originY2 = ThreadLocalRandom.current().nextInt(0, this.getNumberOfCellsInRow());
        while(this.getCell(originX2,originY2).isShot()){
            originX2 = ThreadLocalRandom.current().nextInt(0, this.getNumberOfCellsInRow());
            originY2 = ThreadLocalRandom.current().nextInt(0, this.getNumberOfCellsInRow());
        }
        Cell cell2 = this.getCell(originX2,originY2);
        waveCells.add(cell1);
        waveCells.add(cell2);
        return waveCells;
    }

}