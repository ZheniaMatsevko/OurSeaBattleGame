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


    public PlayGround(int numberOfCellsInRow, int numberOfBoats, int xC, int yC){
        paintedCell = null;
        isBoatsVisible=true;
        isBoatChanged = 0;
        score = 0;
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
    public void returnChangedBoat(){
        if(changedBoat.getDirection()==0)
            changedBoat.setPosition(changedBoat.getStartCell().getX(),changedBoat.getStartCell().getY());
        else
            changedBoat.setPosition(changedBoat.getStartCell().getX()+getCellWidth(),changedBoat.getStartCell().getY());
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
        LinkedList<Cell> emptyCells = new LinkedList<>();
        if(originY-1>=0 && cellsGround[originX][originY-1].getIsTaken()){
            System.out.println("checkSurroundingHorizontal1");
            return false;
        }else if(originY-1>=0){
            emptyCells.add(cellsGround[originX][originY-1]);
        }
        if(originY-1>=0 && originX-1>=0 && cellsGround[originX-1][originY-1].getIsTaken()) {
            System.out.println("checkSurroundingHorizontal2");
            return false;
        }
        else if(originY-1>=0 && originX-1>=0)
            emptyCells.add(cellsGround[originX-1][originY-1]);
        if(originY-1>=0 && originX+1<numberOfCellsInRow && cellsGround[originX+1][originY-1].getIsTaken()) {
            System.out.println("checkSurroundingHorizontal3");
            return false;
        }
        else if(originY-1>=0 && originX+1<numberOfCellsInRow)
            emptyCells.add(cellsGround[originX+1][originY-1]);
        for(int i=0;i<boatSize;i++){
            if(originX+1<numberOfCellsInRow && cellsGround[originX+1][originY+i].getIsTaken()) {
                System.out.println("checkSurroundingHorizontal4" + " x:" + (originX+1) + " y:" + (originY+i));
                return false;
            }
            else if(originX+1<numberOfCellsInRow)
                emptyCells.add(cellsGround[originX+1][originY+i]);
            if(originX-1>=0 && cellsGround[originX-1][originY+i].getIsTaken()) {
                System.out.println("checkSurroundingHorizontal5"+ " x:" + (originX-1) + " y:" + (originY+i));
                return false;
            }
            else if(originX-1>=0)
                emptyCells.add(cellsGround[originX-1][originY+i]);
        }
        if(originY+boatSize<numberOfCellsInRow && originX-1>=0 && cellsGround[originX-1][originY+boatSize].getIsTaken()) {
            System.out.println("checkSurroundingHorizontal6");
            return false;
        }
        else if(originY+boatSize<numberOfCellsInRow && originX-1>=0)
            emptyCells.add(cellsGround[originX-1][originY+boatSize]);
        if(originY+boatSize<numberOfCellsInRow && originX+1<numberOfCellsInRow && cellsGround[originX+1][originY+boatSize].getIsTaken()) {
            System.out.println("checkSurroundingHorizontal7");
            return false;
        }
        else if(originY+boatSize<numberOfCellsInRow && originX+1<numberOfCellsInRow)
            emptyCells.add(cellsGround[originX+1][originY+boatSize]);
        if(originY+boatSize<numberOfCellsInRow && cellsGround[originX][originY+boatSize].getIsTaken()) {
            System.out.println("checkSurroundingHorizontal8");
            return false;
        }
        else if(originY+boatSize<numberOfCellsInRow)
            emptyCells.add(cellsGround[originX][originY+boatSize]);
        System.out.print("checkSurroundingHorizontal: setEmpty: ");
        for(Cell cell: emptyCells){
            cell.setShouldBeEmpty(true);
            System.out.print(cell.getName() + " ");
        }
        System.out.print("  setTaken: ");
        for(int i=0;i<boatSize;i++){
            cellsGround[originX][originY+i].setIsTaken(true);
            cellsGround[originX][originY+i].setBoat(boat);
            System.out.print(cellsGround[originX][originY+i].getName() + " ");
        }
        System.out.println();
        return true;
    }
    private boolean checkSurroundingVertical(int originX, int originY, int boatSize, Boat boat){
        LinkedList<Cell> emptyCells = new LinkedList<>();
        if(originX+1<numberOfCellsInRow && cellsGround[originX+1][originY].getIsTaken()){
            return false;
        }else if(originX+1<numberOfCellsInRow){
            emptyCells.add(cellsGround[originX+1][originY]);
        }
        if(originY-1>=0 && originX+1<numberOfCellsInRow && cellsGround[originX+1][originY-1].getIsTaken())
            return false;
        else if(originY-1>=0 && originX+1<numberOfCellsInRow)
            emptyCells.add(cellsGround[originX+1][originY-1]);
        if(originX+1<numberOfCellsInRow && originY+1<numberOfCellsInRow && cellsGround[originX+1][originY+1].getIsTaken())
            return false;
        else if(originX+1<numberOfCellsInRow && originY+1<numberOfCellsInRow)
            emptyCells.add(cellsGround[originX+1][originY+1]);
        for(int i=0;i<boatSize;i++){
            if(originY+1<numberOfCellsInRow && cellsGround[originX-i][originY+1].getIsTaken())
                return false;
            else if(originY+1<numberOfCellsInRow)
                emptyCells.add(cellsGround[originX-i][originY+1]);
            if(originY-1>=0 && cellsGround[originX-i][originY-1].getIsTaken())
                return false;
            else if(originY-1>=0)
                emptyCells.add(cellsGround[originX-i][originY-1]);
        }
        if(originX-boatSize>=0 && originY-1>=0 && cellsGround[originX-boatSize][originY-1].getIsTaken())
            return false;
        else if(originX-boatSize>=0 && originY-1>=0)
            emptyCells.add(cellsGround[originX-boatSize][originY-1]);
        if(originX-boatSize>=0 && originY+1<numberOfCellsInRow && cellsGround[originX-boatSize][originY+1].getIsTaken())
            return false;
        else if(originX-boatSize>=0 && originY+1<numberOfCellsInRow)
            emptyCells.add(cellsGround[originX-boatSize][originY+1]);
        if(originX-boatSize>=0 && cellsGround[originX-boatSize][originY].getIsTaken())
            return false;
        else if(originX-boatSize>=0)
            emptyCells.add(cellsGround[originX-boatSize][originY]);
        System.out.print("checkSurroundingVertical: ");
        System.out.print("  setEmpty: ");
        for(Cell cell: emptyCells){
            cell.setShouldBeEmpty(true);
            System.out.print(cell.getName() + " ");
        }
        System.out.print("  setTaken: ");
        for(int i=0;i<boatSize;i++){
            cellsGround[originX-i][originY].setIsTaken(true);
            cellsGround[originX-i][originY].setBoat(boat);
            System.out.print(cellsGround[originX-i][originY].getName() + " ");
        }
        System.out.println();
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
            System.out.println("b=" + b + " size: " + boats[b].getSize() + " dir: " + direction);
            if(boats[b].getSize()==1){
                System.out.println(originX + " " + originY);
                boats[b].setDirection(0);
                boats[b].setStartCell(cellsGround[originX][originY]);
                boats[b].setPosition(cellsGround[originX][originY].getX(),cellsGround[originX][originY].getY());
            }
            else if(direction==0){
                boats[b].setDirection(0);
                boats[b].setStartCell(cellsGround[originX][originY]);
                System.out.println(originX + " " + originY);
                boats[b].setPosition(cellsGround[originX][originY].getX(),cellsGround[originX][originY].getY());
            }else{
                boats[b].setDirection(1);
                boats[b].setStartCell(cellsGround[originX][originY]);
                System.out.println(originX + " " + originY);
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
    public Cell getCellByName(String name){
        for(int i=0;i<numberOfCellsInRow;i++){
            for(int j=0;j<numberOfCellsInRow;j++){
                if(cellsGround[i][j].getName().equals(name))
                    return cellsGround[i][j];
                }
            }
        return null;
    }
    private  boolean checkIfCanLocateUserBoat(Cell cellOrigin){
        System.out.println("checkIfCanLocateUserBoat");
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
        System.out.println(cellOrigin.getName() + " x:" + x + " y:" + y + cellOrigin.getShouldBeEmpty() + " " + cellOrigin.getIsTaken());
        if(cellsGround[x][y].getIsTaken() || cellsGround[x][y].getShouldBeEmpty()){
            System.out.println("checkIfCanLocateUserBoat1");
            return false;
        }
        if(changedBoat.getDirection()==0){
            for(int i=0;i<changedBoat.getSize();i++){
                if(y+i>=numberOfCellsInRow || cellsGround[x][y+i].getIsTaken() || cellsGround[x][y+i].getShouldBeEmpty()){
                    System.out.println("checkIfCanLocateUserBoat3" + " x:" + x + " y:" + (y+i));
                    return false;
                }
            }
            if(!checkSurroundingHorizontal(x,y,changedBoat.getSize(),changedBoat)){
                System.out.println("checkIfCanLocateUserBoat4");
                return false;
            }
        }else if(changedBoat.getDirection()==1){
            for(int i=0;i<changedBoat.getSize();i++){
                if(x-i <0 || cellsGround[x-i][y].getIsTaken() || cellsGround[x-i][y].getShouldBeEmpty()){
                    System.out.println("checkIfCanLocateUserBoat5" + "x:" + (x-i) + " y:" + y);
                    return false;
                }
            }
            if(!checkSurroundingVertical(x,y,changedBoat.getSize(),changedBoat)){
                System.out.println("checkIfCanLocateUserBoat6");
                return false;
            }
        }
        return true;
    }
    private void removeOldlocationHorizontal(int originX, int originY){
        LinkedList<Cell> emptyCells = new LinkedList<>();
        if(originY-1>=0) {
            emptyCells.add(cellsGround[originX][originY - 1]);
            //cellsGround[originX][originY - 1].setShouldBeEmpty(false);
        }
        if (originY - 1 >= 0 && originX - 1 >= 0)
            emptyCells.add(cellsGround[originX-1][originY - 1]);
            //cellsGround[originX - 1][originY - 1].setShouldBeEmpty(false);
        if (originY - 1 >= 0 && originX + 1 < numberOfCellsInRow)
            emptyCells.add(cellsGround[originX+1][originY - 1]);
            //cellsGround[originX + 1][originY - 1].setShouldBeEmpty(false);
        for (int i = 0; i < changedBoat.getSize(); i++) {
            if (originX + 1 < numberOfCellsInRow)
                emptyCells.add(cellsGround[originX+1][originY + i]);
                //cellsGround[originX + 1][originY + i].setShouldBeEmpty(false);
            if (originX - 1 >= 0)
                emptyCells.add(cellsGround[originX-1][originY +i]);
                //cellsGround[originX - 1][originY + i].setShouldBeEmpty(false);
        }
        if (originY + changedBoat.getSize() < numberOfCellsInRow && originX - 1 >= 0)
            emptyCells.add(cellsGround[originX-1][originY + changedBoat.getSize()]);
            //cellsGround[originX - 1][originY + changedBoat.getSize()].setShouldBeEmpty(false);
        if (originY + changedBoat.getSize() < numberOfCellsInRow && originX + 1 < numberOfCellsInRow)
            emptyCells.add(cellsGround[originX+1][originY + changedBoat.getSize()]);
            //cellsGround[originX + 1][originY + changedBoat.getSize()].setShouldBeEmpty(false);
        if (originY + changedBoat.getSize() < numberOfCellsInRow)
            emptyCells.add(cellsGround[originX][originY + changedBoat.getSize()]);
            //cellsGround[originX][originY + changedBoat.getSize()].setShouldBeEmpty(false);
        System.out.print("removeOldlocationHorizontal: ");
        System.out.print("  setTakenFALSE: ");
        for (int i = 0; i < changedBoat.getSize(); i++) {
            cellsGround[originX][originY+i].setIsTaken(false);
            cellsGround[originX][originY+i].setBoat(null);
            System.out.print(cellsGround[originX][originY+i].getName() + " ");
        }
        System.out.print("  setEmptyFALSE: ");
        for(Cell cell: emptyCells){
            cell.setShouldBeEmpty(false);
            System.out.print(cell.getName() + " ");
        }
        System.out.println();
    }
    private void removeOldlocationVertical(int originX, int originY) {
        LinkedList<Cell> emptyCells = new LinkedList<>();
        if (originX + 1 <numberOfCellsInRow) {
            emptyCells.add(cellsGround[originX+1][originY]);
            //cellsGround[originX - 1][originY].setShouldBeEmpty(false);
        }
        if (originY - 1 >= 0 && originX + 1 <numberOfCellsInRow)
            emptyCells.add(cellsGround[originX+1][originY - 1]);
            //cellsGround[originX - 1][originY - 1].setShouldBeEmpty(false);
        if (originX + 1 <numberOfCellsInRow && originY + 1 < numberOfCellsInRow)
            emptyCells.add(cellsGround[originX+1][originY +1]);
            //cellsGround[originX - 1][originY + 1].setShouldBeEmpty(false);
        for (int i = 0; i < changedBoat.getSize(); i++) {
            if (originY + 1 < numberOfCellsInRow)
                emptyCells.add(cellsGround[originX-i][originY + 1]);
               // cellsGround[originX - i][originY + 1].setShouldBeEmpty(false);
            if (originY - 1 >= 0)
                emptyCells.add(cellsGround[originX-i][originY - 1]);
                //cellsGround[originX - i][originY - 1].setShouldBeEmpty(false);
        }
        if (originX - changedBoat.getSize() >= 0 && originY - 1 >= 0)
            emptyCells.add(cellsGround[originX - changedBoat.getSize()][originY - 1]);
            //cellsGround[originX - changedBoat.getSize()][originY - 1].setShouldBeEmpty(false);
        if (originX - changedBoat.getSize() >= 0 && originY + 1 < numberOfCellsInRow)
            emptyCells.add(cellsGround[originX - changedBoat.getSize()][originY + 1]);
            //cellsGround[originX - changedBoat.getSize()][originY + 1].setShouldBeEmpty(false);
        if (originX - changedBoat.getSize() >= 0)
            emptyCells.add(cellsGround[originX - changedBoat.getSize()][originY]);
            //cellsGround[originX - changedBoat.getSize()][originY].setShouldBeEmpty(false);
        System.out.print("removeOldlocationVertical: ");
        System.out.print("  setTakenFALSE: ");
        for (int i = 0; i < changedBoat.getSize(); i++) {
            cellsGround[originX - i][originY].setIsTaken(false);
            cellsGround[originX-i][originY].setBoat(null);
            System.out.print(cellsGround[originX-i][originY].getName() + " ");
        }
        System.out.print("  setEmptyFALSE: ");
        for(Cell cell: emptyCells){
            cell.setShouldBeEmpty(false);
            System.out.print(cell.getName() + " ");
        }
        System.out.println();
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
        System.out.println("==========Old Cell==============="+previous.getName() + " x:" + x + "y: " + y);
        if(previous!=null){
            if(changedBoat.getDirection()==0)
                removeOldlocationHorizontal(x,y);
            else
                removeOldlocationVertical(x,y);
        }else{
            System.out.println("Old cell not found");
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
            System.out.println("Add old Location");
            if(changedBoat.getDirection()==0)
                checkSurroundingHorizontal(x,y, changedBoat.getSize(),changedBoat);
            else
                checkSurroundingVertical(x,y,changedBoat.getSize(),changedBoat);
        }else{
            System.out.println("Old cell not found");
        }
    }

    public void checkBoatsLocation(){
        for(int i=0;i<numberOfCellsInRow;i++){
            if(boats[i].getDirection()==0){
                if(boats[i].getX()!=boats[i].getStartCell().getX() || boats[i].getY()!=boats[i].getStartCell().getY()){
                    boats[i].setPosition(boats[i].getStartCell().getX(),boats[i].getStartCell().getY());
                }
            }else{
                if(boats[i].getX()!=boats[i].getStartCell().getX()+getCellWidth() || boats[i].getY()!=boats[i].getStartCell().getY()){
                    boats[i].setPosition(boats[i].getStartCell().getX()+getCellWidth(),boats[i].getStartCell().getY());
                }
            }
        }
    }
    public boolean Rotate(){
        Cell cellOrigin = changedBoat.getStartCell();
        removeOldLocation();
        System.out.println("Start cell for rotation: " + cellOrigin.getName());
        if(changedBoat.getDirection()==0){
            changedBoat.setRotation(90);
            changedBoat.setDirection(1);
            if(checkIfCanLocateUserBoat(cellOrigin)){
                System.out.println("Here");
                changedBoat.setPosition(cellOrigin.getX()+getCellWidth(),cellOrigin.getY());
            }else{
                System.out.println("No");
                changedBoat.setRotation(0);
                changedBoat.setDirection(0);
                changedBoat.setPosition(cellOrigin.getX(),cellOrigin.getY());
                addOldLocation();
            }
        }else{
            changedBoat.setRotation(0);
            changedBoat.setDirection(0);
            if(checkIfCanLocateUserBoat(cellOrigin)){
                System.out.println("Here");
                changedBoat.setPosition(cellOrigin.getX(),cellOrigin.getY());
            }else{
                System.out.println("No");
                changedBoat.setRotation(90);
                changedBoat.setDirection(1);
                changedBoat.setPosition(cellOrigin.getX()+getCellWidth(),cellOrigin.getY());
                addOldLocation();
            }
        }
        isBoatChanged=0;
        return true;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(isBoatChanged==2 && isCellDragged==false){
            System.out.println("changed");
            if(changedBoat!=null){
                 Cell cellOrigin = findCell(changedBoat.getX(),changedBoat.getY()-1);
                if(changedBoat.getDirection()==1)
                    cellOrigin = findCell(changedBoat.getX()-getCellWidth(),changedBoat.getY()-1);
                if(cellOrigin!=null){
                    System.out.println("-----------cellOrigin name----------------"+((Cell)cellOrigin).getName());
                    int val = Integer.valueOf(cellOrigin.getName());
                    if(changedBoat.getDirection()==0 && val%10+changedBoat.getSize()>10){
                        changedBoat.setPosition(previousX,previousY);
                    }else if(changedBoat.getDirection()==1 && changedBoat.getSize()*10-val>10){
                        changedBoat.setPosition(previousX,previousY);
                    }else{
                        removeOldLocation();
                        if(checkIfCanLocateUserBoat(cellOrigin)){
                            changedBoat.setPosition(cellOrigin.getX(),cellOrigin.getY());
                            if(changedBoat.getDirection()==1)
                                changedBoat.setPosition(cellOrigin.getX()+getCellWidth(),cellOrigin.getY());
                            changedBoat.setStartCell(cellOrigin);
                        }else{
                            changedBoat.setPosition(previousX,previousY);
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
                        }
                    }
                    else{
                        addOldLocation();
                        changedBoat.setPosition(previousX,previousY);
                    }
                }
                else if(cellOrigin==null){
                    changedBoat.setPosition(previousX,previousY);
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

}