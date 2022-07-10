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

    public void setBombedCells(LinkedList<Cell> bombedCells) {
        this.bombedCells = bombedCells;
    }
    public LinkedList<Cell> getBombedCells(){
        return this.bombedCells;
    }
    public Cell getMainBombedCell(){
        return bombedCells.getFirst();
    }

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

    public Cell getMainRadaredCell(){
        return radaredCells[0];
    }
    public Cell[] getRadaredCells() {
        return radaredCells;
    }
    public void setScore(int score){
        this.score = score;
    }
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
    public boolean getIsBoatsVisible(){
        return isBoatsVisible;
    }
    public void setBoatsVisible(boolean boatsVisible) {
        isBoatsVisible = boatsVisible;
        for(int i=0;i<numberOfCellsInRow;i++){
            boats[i].setVisible(boatsVisible);
        }
    }
    public boolean checkShotCell(Cell cell){
        if(!cell.getIsTaken())
            return false;
        return true;
    }

    public Cell getPaintedCell() {
        return paintedCell;
    }

    public void setPaintedCell(Cell paintedCell) {
        this.paintedCell = paintedCell;
    }
    public String getCellName(Cell cell){
        String name="";
        String letters = "ABCDEFGHIJ";
        name+=letters.charAt(getJ(cell)) + String.valueOf(getI(cell)+1);
        return name;
    }

    public boolean killCell(Cell cell){
        cell.setShot(true);
        System.out.println("Shot: "+cell.getName());
        if(cell.getBoat().strike()){
            score++;
            System.out.println("Killed");
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
            System.out.println("Damaged");
            Image cross = new Image(new Texture(Gdx.files.internal("x.png")));
            cross.setPosition(cell.getX(),cell.getY());
            stage.addActor(cross);
            return false;
        }

    }

    public int getScore() {
        return score;
    }

    public void setCellDragged(boolean cellDragged) {
        isCellDragged = cellDragged;
    }

    public boolean isCellDragged() {
        return isCellDragged;
    }

    public void setPreviousX(float previousX) {
        this.previousX = previousX;
    }

    public void setPreviousY(float previousY) {
        this.previousY = previousY;
    }

    public int isBoatChanged() {
        return isBoatChanged;
    }
    public void setIsBoatChanged(int val){
        this.isBoatChanged = val;
    }
    public Boat getChangedBoat(){
        return this.changedBoat;
    }
    public void setChangedBoat(Boat changedBoat) {
        if(previousX==0 && previousY==0){
            previousX = changedBoat.getX();
            previousY = changedBoat.getY();
        }
        this.changedBoat = changedBoat;
    }
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
    public float getCellWidth(){
        return cellsGround[0][0].getWidth();
    }
    public float getCellHeight(){
        return cellsGround[0][0].getHeight();
    }

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
    public int getI(Cell cell){
        for(int i=0;i<numberOfCellsInRow;i++){
            for(int j=0;j<numberOfCellsInRow;j++){
                if(cellsGround[i][j].equals(cell))
                    return i;
            }
        }
        return -1;
    }
    public int getJ(Cell cell){
        for(int i=0;i<numberOfCellsInRow;i++){
            for(int j=0;j<numberOfCellsInRow;j++){
                if(cellsGround[i][j].equals(cell))
                    return j;
            }
        }
        return -1;
    }
    public Cell getCell(int i, int j){
        return cellsGround[i][j];
    }
    public int getNumberOfCellsInRow(){
        return numberOfCellsInRow;
    }
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

    public String getMessage(){
        return this.message;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(isBoatChanged==2 && isCellDragged==false){
            if(changedBoat!=null){
                 Cell cellOrigin = findCell(changedBoat.getX(),changedBoat.getY()-1);
                if(changedBoat.getDirection()==1)
                    cellOrigin = findCell(changedBoat.getX()-getCellWidth(),changedBoat.getY()-1);
                if(changedBoat.getDirection()==1 && changedBoat.getX()-getCellWidth()>=getCell(0,0).getX()){
                    System.out.println("Boat: " + (changedBoat.getX()-getCellWidth())  + " cell: " + getCell(0,0).getX());
                }else if(changedBoat.getDirection()==1){
                    System.out.println("NOO");
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
    public Stage getStage(){
        return stage;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }


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