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
    private Sprite[] sprites;
    private Boat[] boats;
    private Boat changedBoat;
    private int isBoatChanged;
    private float previousX;
    private float previousY;
    private boolean isCellDragged;

    public PlayGround(int numberOfCellsInRow, int numberOfBoats){
        isBoatChanged = 0;
        this.numberOfCellsInRow = numberOfCellsInRow;
        setBounds(0,Gdx.graphics.getHeight(),Gdx.graphics.getWidth(),Gdx.graphics.getHeight()-50);
        setTouchable(Touchable.enabled);
        group = new Group();
        stage = new Stage();
        cellsGround = new Cell[numberOfCellsInRow][numberOfCellsInRow];
        float x = 10, y=Gdx.graphics.getHeight()-10;
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
                    x=10;
                    y=cellsGround[i][j-1].getY()-cellsGround[i][j-1].getHeight();
                }
                cellsGround[i][j].setName(String.valueOf(count++));
                group.addActor(cellsGround[i][j]);
            }
        }
        stage.addActor(group);
        boats = new Boat[numberOfBoats];
        sprites = new Sprite[numberOfBoats];
        for(int i=0;i<numberOfBoats;i++){
            if(i==0){
                boats[i] = new Boat(4,Boat.getBoatImage(4));
                sprites[i] = new Sprite(Boat.getBoatImage(4));
            } else if(i>0 && i<3) {
                boats[i] = new Boat(3, Boat.getBoatImage(3));
                sprites[i] = new Sprite(Boat.getBoatImage(3));
            }
            else if(i>2 && i<6) {
                boats[i] = new Boat(2, Boat.getBoatImage(2));
                sprites[i] = new Sprite(Boat.getBoatImage(2));
            }
            else {
                boats[i] = new Boat(1, Boat.getBoatImage(1));
                sprites[i] = new Sprite(Boat.getBoatImage(1));
            }
            stage.addActor(boats[i]);
        }

        /*cells = new Cell[numberOfCellsInRow*numberOfCellsInRow];
        float x = 10;
        float y=Gdx.graphics.getHeight()-10;
        for(int i=0;i<cells.length;i++){
            cells[i] = new Cell();
            if(i==0){
                y-=cells[i].getHeight();
                cells[i].setPosition(x,y);
            }
            else if(i!=0 && i%numberOfCellsInRow!=0){
                x=cells[i-1].getX()+cells[i-1].getWidth();
                cells[i].setPosition(x,y);
            }
            else if(i%numberOfCellsInRow==0){
                x=10;
                y=cells[i-1].getY()-cells[i-1].getHeight();
                cells[i].setPosition(x,y);
            }
            cells[i].setName(String.valueOf(i));
            group.addActor(cells[i]);
        }*/
        /*for(int i=0;i<numberOfCellsInRow;i++){
            if(i==0)
                sprites[i] = new Sprite(new Texture(Gdx.files.internal("boat4.png")));
            else if(i>1 && i<4)
                sprites[i] = new Sprite(new Texture(Gdx.files.internal("boat3.png")));
            else if(i>3 && i<7)
                sprites[i] = new Sprite(new Texture(Gdx.files.internal("boat2.png")));
            else
                sprites[i] = new Sprite(new Texture(Gdx.files.internal("boat.png")));
        }*/
        //sprite = new Sprite(new Texture(Gdx.files.internal("boat4.png")));
        putRandomBoats();
        isCellDragged = false;
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("Here");
                return true;
            }
        });
    }

    public void setCellDragged(boolean cellDragged) {
        isCellDragged = cellDragged;
    }

    public boolean isCellDragged() {
        return isCellDragged;
    }

    public float getPreviousX(){
        return previousX;
    }
    public float getPreviousY(){
        return previousY;
    }

    public void setPreviousX(float previousX) {
        this.previousX = previousX;
    }

    public void setPreviousY(float previousY) {
        this.previousY = previousY;
    }

    public Boat findBoat(Boat boat){
        for(int i=0;i<numberOfCellsInRow;i++){
            if(boats[i]==boat)
                return boats[i];
        }
        return null;
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

    private boolean checkSurroundingHorizontal(int originX, int originY, int boatSize){
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
            System.out.print(cellsGround[originX][originY+i].getName() + " ");
        }
        System.out.println();
        return true;
    }
    private boolean checkSurroundingVertical(int originX, int originY, int boatSize){
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
                    checkSurroundingHorizontal(originX,originY,1);
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
                        if(checkSurroundingHorizontal(originX,originY,boats[b].getSize())){
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
                        if(checkSurroundingVertical(originX,originY,boats[b].getSize())){
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
                /*float loc = 0.5F;
                for(int i=2;i<boats[b].getSize();i++){
                    loc+=0.5;
                }*/
                boats[b].setPosition(cellsGround[originX][originY].getX() + cellsGround[originX][originY].getWidth(),cellsGround[originX][originY].getY());
                //boats[b].setPosition(cellsGround[originX][originY].getX()-cellsGround[originX][originY].getWidth()*loc,cellsGround[originX][originY].getY() + cellsGround[originX][originY].getHeight()*loc);
            }
        }





        /*Random rand = new Random();
        int origin, direction,count,n;
        for (int i=0;i<numberOfCellsInRow;i++){
            origin = ThreadLocalRandom.current().nextInt(0, numberOfCellsInRow*numberOfCellsInRow);
            direction = ThreadLocalRandom.current().nextInt(0, 2);
            count = 3;
            n = origin;
            switch(direction){
                case 0:
                    while((n+1)<100&&cells[n].getY()==cells[n+1].getY() && count>0){
                        n+=1;
                        count--;
                    }
                    while(count>0){
                        origin--;
                        count--;
                    }
                    break;
                case 1:
                    sprite.setRotation(90);
                    while((n+numberOfCellsInRow)<100 && cells[n].getX()==cells[n+numberOfCellsInRow].getX() && count>0){
                        n+=numberOfCellsInRow;
                        count--;
                    }
                    while(count>0){
                        origin-=numberOfCellsInRow;
                        count--;
                    }
                    break;
            }
            System.out.println(cells[origin].getName() + " " + origin);
            boolean canLocate = true;
            if(i>0){
                for(int j=0;j<i;j++){
                    switch (direction){
                        case 0:
                            if(cells[origin].getX()+sprites[i].getWidth()==sprites[j].getX() && cells[origin].getY()==sprites[j].getY()){
                                canLocate=false;
                            }
                            break;
                        case 1:
                            if(cells[origin].getX()==sprites[j].getX() && cells[origin].getY()+sprites[i].getHeight()==sprites[j].getY()){
                                canLocate=false;
                            }
                            break;
                    }
                   if(!canLocate) break;
                }
            }
            if(direction==2 ){
                sprite.setPosition(cells[origin].getX()-sprite.getWidth()/8*3,cells[origin].getY() - sprite.getHeight()/8*3 - cells[origin].getHeight()-5);
            }else{
                sprite.setPosition(cells[origin].getX(),cells[origin].getY());
            }
        }

        /*origin = ThreadLocalRandom.current().nextInt(0, numberOfCellsInRow*numberOfCellsInRow);
        direction = ThreadLocalRandom.current().nextInt(0, 4);
        count = 3;
        n = origin;
        direction = 2;
        switch(direction){
            case 0:
                while((n+1)<100&&cells[n].getY()==cells[n+1].getY() && count>0){
                    n+=1;
                    count--;
                }
                while(count>0){
                    origin--;
                    count--;
                }
                break;
            case 1:
                while((n-1)>=0 && cells[n].getY()==cells[n-1].getY() && count>0){
                    n-=1;
                    count--;
                }
                origin=n;
                break;
            case 2:
                sprite.setRotation(90);
                while((n+numberOfCellsInRow)<100 && cells[n].getX()==cells[n+numberOfCellsInRow].getX() && count>0){
                    n+=numberOfCellsInRow;
                    count--;
                }
                while(count>0){
                    origin-=numberOfCellsInRow;
                    count--;
                }
                break;
            case 3:
                sprite.setRotation(90);
                while((n-numberOfCellsInRow)>=0 && cells[n].getX()==cells[n-numberOfCellsInRow].getX() && count>0){
                    n-=numberOfCellsInRow;
                    count--;
                }
                while(count>0){
                    origin+=numberOfCellsInRow;
                    count--;
                }
                break;
        }
        System.out.println(cells[origin].getName() + " " + origin);
        if(direction==2 ){
            sprite.setPosition(cells[origin].getX()-sprite.getWidth()/8*3,cells[origin].getY() - sprite.getHeight()/8*3 - cells[origin].getHeight()-5);
        }else  if(direction==3 ){
            sprite.setPosition(cells[origin].getX(),cells[origin].getY());
            //sprite.setPosition(cells[origin].getX()-sprite.getWidth()/8*3,cells[origin].getY() + sprite.getHeight()/8*3 + cells[origin].getHeight()+5);
        }else {
            sprite.setPosition(cells[origin].getX(),cells[origin].getY());
        }

        /*
        cells[origin].changeColor();
        int direction = ThreadLocalRandom.current().nextInt(0, 4);
        int count = 3;
        int n = origin;
        switch(direction){
            case 0:
                while(cells[n].getY()==cells[n+1].getY() && count>0){
                    n+=1;
                    cells[n].changeColor();
                    count--;
                }
                while(count>0){
                    origin--;
                    cells[origin].changeColor();
                    count--;
                }
                break;
            case 1:
                while(n>=0 && cells[n].getY()==cells[n-1].getY() && count>0){
                    n-=1;
                    cells[n].changeColor();
                    count--;
                }
                while(count>0){
                    origin++;
                    cells[origin].changeColor();
                    count--;
                }
                break;
            case 2:
                while(n>=0 && cells[n].getX()==cells[n+7].getX() && count>0){
                    n+=7;
                    cells[n].changeColor();
                    count--;
                }
                while(count>0){
                    origin-=7;
                    cells[origin].changeColor();
                    count--;
                }
                break;
            case 3:
                while(n>=0 && cells[n].getX()==cells[n-7].getX() && count>0){
                    n-=7;
                    cells[n].changeColor();
                    count--;
                }
                while(count>0){
                    origin+=7;
                    cells[origin].changeColor();
                    count--;
                }
                break;
        }*/
    }
    public Cell findCell(float x, float y){
        for(int i=0;i<numberOfCellsInRow;i++){
            for(int j=0;j<numberOfCellsInRow;j++){
                if(cellsGround[i][j].getX()<=x && x<=cellsGround[i][j].getX()+getCellWidth() && cellsGround[i][j].getY()<=y && y<=cellsGround[i][j].getY()+getCellHeight()){
                    //System.out.println("cell x:" + cellsGround[i][j].getX() + " y:" + cellsGround[i][j].getY() + " size:" + getCellWidth());
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
                if(cellsGround[x][y+i].getIsTaken() || cellsGround[x][y+i].getShouldBeEmpty()){
                    System.out.println("checkIfCanLocateUserBoat3" + " x:" + x + " y:" + (y+i));
                    return false;
                }
            }
            if(!checkSurroundingHorizontal(x,y,changedBoat.getSize())){
                System.out.println("checkIfCanLocateUserBoat4");
                return false;
            }
        }else if(changedBoat.getDirection()==1){
            for(int i=0;i<changedBoat.getSize();i++){
                if(cellsGround[x-i][y].getIsTaken() || cellsGround[x-i][y].getShouldBeEmpty()){
                    System.out.println("checkIfCanLocateUserBoat5" + "x:" + (x-i) + " y:" + y);
                    return false;
                }
            }
            if(!checkSurroundingVertical(x,y,changedBoat.getSize())){
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
                checkSurroundingHorizontal(x,y, changedBoat.getSize());
            else
                checkSurroundingVertical(x,y,changedBoat.getSize());
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
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(isBoatChanged==2 && isCellDragged==false){
            System.out.println("changed");
            if(changedBoat!=null){
                //System.out.println("--------------------------"+changedBoat.getX() + " " + changedBoat.getY());
                Cell cellOrigin = findCell(changedBoat.getX(),changedBoat.getY()-1);
                if(changedBoat.getDirection()==1)
                    cellOrigin = findCell(changedBoat.getX()-getCellWidth(),changedBoat.getY()-1);
                if(cellOrigin!=null){
                    System.out.println("-----------cellOrigin name----------------"+((Cell)cellOrigin).getName());
                    int val = Integer.valueOf(cellOrigin.getName());
                    if(changedBoat.getDirection()==0 && val%10+changedBoat.getSize()>10){
                        //System.out.println("first");
                        changedBoat.setPosition(previousX,previousY);
                    }else if(changedBoat.getDirection()==1 && changedBoat.getSize()*10-val>10){
                        //System.out.println("second");
                        changedBoat.setPosition(previousX,previousY);
                    }else{
                        //System.out.println("third");
                        removeOldLocation();
                        if(checkIfCanLocateUserBoat(cellOrigin)){
                            //System.out.println("3333333333333333333333333333");
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
                    //System.out.println("fourth");
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
    public Group getGroup(){
        return group;
    }


}
      /*Image cell1 = new Image(new Texture(Gdx.files.internal("square.png")));
        Image cell2 = new Image(new Texture(Gdx.files.internal("square.png")));
        Image cell3 = new Image(new Texture(Gdx.files.internal("square.png")));
        Image cell4 = new Image(new Texture(Gdx.files.internal("square.png")));
        Image cell5 = new Image(new Texture(Gdx.files.internal("square.png")));
        Image cell6 = new Image(new Texture(Gdx.files.internal("square.png")));
        Image cell7 = new Image(new Texture(Gdx.files.internal("square.png")));
        Image cell8 = new Image(new Texture(Gdx.files.internal("square.png")));
        Image cell9 = new Image(new Texture(Gdx.files.internal("square.png")));
        Image cell10 = new Image(new Texture(Gdx.files.internal("square.png")));
        Image cell11 = new Image(new Texture(Gdx.files.internal("square.png")));
        Image cell12 = new Image(new Texture(Gdx.files.internal("square.png")));
        Image cell13 = new Image(new Texture(Gdx.files.internal("square.png")));
        Image cell14 = new Image(new Texture(Gdx.files.internal("square.png")));
        Image cell15 = new Image(new Texture(Gdx.files.internal("square.png")));
        Image cell16 = new Image(new Texture(Gdx.files.internal("square.png")));*/
      /*cell1.setPosition(10,Gdx.graphics.getHeight()-cell1.getHeight()-10);
        cell2.setPosition(cell1.getX()+cell1.getWidth(),cell1.getY());
        cell3.setPosition(cell2.getX()+cell2.getWidth(),cell1.getY());
        cell4.setPosition(cell3.getX()+cell3.getWidth(),cell1.getY());
        cell5.setPosition(10,cell1.getY()-cell1.getHeight());
        cell6.setPosition(cell5.getX()+cell5.getWidth(),cell5.getY());
        cell7.setPosition(cell6.getX()+cell6.getWidth(),cell5.getY());
        cell8.setPosition(cell7.getX()+cell7.getWidth(),cell5.getY());

        /*group.addActor(cell1);
        group.addActor(cell2);
        group.addActor(cell3);
        group.addActor(cell4);
        group.addActor(cell5);
        group.addActor(cell6);
        group.addActor(cell7);
        group.addActor(cell8);*/