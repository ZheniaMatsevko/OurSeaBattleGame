package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ComputerGround extends Actor {
    private PlayGround ground;
    private PlayGround userGround;
    private boolean isStrike;
    private List<Cell> damagedCells;
    private int damagedDirection;
    public ComputerGround(int numberOfCellsInRow, int numberOfBoats, int x, int y, PlayGround userGround){
        ground = new PlayGround(numberOfCellsInRow,numberOfBoats,x,y);
        ground.setBoatsVisible(false);
        isStrike = false;
        this.userGround = userGround;
        damagedCells = new LinkedList<>();
    }
    public PlayGround getGround(){
        return ground;
    }

    public void setStrike(boolean strike) {
        isStrike = strike;
    }

    public boolean isStrike() {
        return isStrike;
    }

    public void shoot(){
        int originX, originY;
        if(damagedCells.isEmpty()){
            originX = ThreadLocalRandom.current().nextInt(0, userGround.getNumberOfCellsInRow());
            originY = ThreadLocalRandom.current().nextInt(0, userGround.getNumberOfCellsInRow());
            while(userGround.getCell(originX,originY).isShot()){
                originX = ThreadLocalRandom.current().nextInt(0, userGround.getNumberOfCellsInRow());
                originY = ThreadLocalRandom.current().nextInt(0, userGround.getNumberOfCellsInRow());
            }
            Cell cell = userGround.getCell(originX,originY);
            if(userGround.checkShotCell(cell)){
                if(!userGround.killCell(cell)){
                    damagedCells.add(cell);
                }
                isStrike = true;
            }else{
                cell.changeColor();
                cell.setShot(true);
                isStrike = false;
            }
        }else{
            boolean shouldRepeat = true;
            int direction = 0;
            Cell cell = null;
            if(damagedCells.size()==1){
                while(shouldRepeat){
                    direction = ThreadLocalRandom.current().nextInt(0, 4);
                    switch (direction){
                        case 0:
                            if((userGround.getJ(damagedCells.get(0))+1)<userGround.getNumberOfCellsInRow() &&!(userGround.getCell(userGround.getI(damagedCells.get(0)),userGround.getJ(damagedCells.get(0))+1)).isShot()) {
                                shouldRepeat = false;
                                cell = userGround.getCell(userGround.getI(damagedCells.get(0)),userGround.getJ(damagedCells.get(0))+1);
                            }
                            break;
                        case 1:
                            if((userGround.getJ(damagedCells.get(0))-1)>=0 &&!(userGround.getCell(userGround.getI(damagedCells.get(0)),userGround.getJ(damagedCells.get(0))-1)).isShot()) {
                                shouldRepeat = false;
                                cell = userGround.getCell(userGround.getI(damagedCells.get(0)),userGround.getJ(damagedCells.get(0))-1);
                            }
                            break;
                        case 2:
                            if((userGround.getI(damagedCells.get(0))+1)<userGround.getNumberOfCellsInRow() &&!(userGround.getCell(userGround.getI(damagedCells.get(0))+1,userGround.getJ(damagedCells.get(0)))).isShot()) {
                                shouldRepeat = false;
                                cell = userGround.getCell(userGround.getI(damagedCells.get(0))+1,userGround.getJ(damagedCells.get(0)));
                            }
                            break;
                        case 3:
                            if((userGround.getI(damagedCells.get(0))-1)>=0 &&!(userGround.getCell(userGround.getI(damagedCells.get(0))-1,userGround.getJ(damagedCells.get(0)))).isShot()) {
                                shouldRepeat = false;
                                cell = userGround.getCell(userGround.getI(damagedCells.get(0))-1,userGround.getJ(damagedCells.get(0)));
                            }
                            break;
                    }
                }
                if(userGround.checkShotCell(cell)){
                    if(!userGround.killCell(cell)){
                        damagedCells.add(cell);
                    }else{
                        damagedCells.clear();
                    }
                    if(direction==0 || direction==1)
                        damagedDirection=0;
                    else
                        damagedDirection=1;
                    isStrike = true;
                }else{
                    cell.changeColor();
                    cell.setShot(true);
                    isStrike = false;
                }
            }else{
                if(damagedDirection==0){
                    if(userGround.getJ(damagedCells.get(damagedCells.size()-1))+1<userGround.getNumberOfCellsInRow() && !userGround.getCell(userGround.getI(damagedCells.get(damagedCells.size()-1)),userGround.getJ(damagedCells.get(damagedCells.size()-1))+1).isShot()){
                        cell = userGround.getCell(userGround.getI(damagedCells.get(damagedCells.size()-1)),userGround.getJ(damagedCells.get(damagedCells.size()-1))+1);
                        if(userGround.checkShotCell(cell)){
                            if(!userGround.killCell(cell)){
                                damagedCells.add(cell);
                            }else{
                                damagedCells.clear();
                            }
                            isStrike = true;
                        }else{
                            cell.changeColor();
                            cell.setShot(true);
                            isStrike = false;
                        }
                    }else if(!userGround.getCell(userGround.getI(damagedCells.get(damagedCells.size()-1)),userGround.getJ(damagedCells.get(damagedCells.size()-1))-1).isShot()){
                        cell = userGround.getCell(userGround.getI(damagedCells.get(damagedCells.size()-1)),userGround.getJ(damagedCells.get(damagedCells.size()-1))-1);
                        if(userGround.checkShotCell(cell)){
                            if(!userGround.killCell(cell)){
                                damagedCells.add(cell);
                            }else{
                                damagedCells.clear();
                            }
                            isStrike = true;
                        }else{
                            cell.changeColor();
                            cell.setShot(true);
                            isStrike = false;
                        }
                    }else{
                        cell = userGround.getCell(userGround.getI(damagedCells.get(0)),userGround.getJ(damagedCells.get(0))-1);
                        if(userGround.checkShotCell(cell)){
                            if(!userGround.killCell(cell)){
                                damagedCells.add(cell);
                            }else{
                                damagedCells.clear();
                            }
                            isStrike = true;
                        }else{
                            cell.changeColor();
                            cell.setShot(true);
                            isStrike = false;
                        }
                    }
                }else{
                    if(userGround.getI(damagedCells.get(damagedCells.size()-1))+1<userGround.getNumberOfCellsInRow() && !userGround.getCell(userGround.getI(damagedCells.get(damagedCells.size()-1))+1,userGround.getJ(damagedCells.get(damagedCells.size()-1))).isShot()){
                        cell = userGround.getCell(userGround.getI(damagedCells.get(damagedCells.size()-1))+1,userGround.getJ(damagedCells.get(damagedCells.size()-1)));
                        if(userGround.checkShotCell(cell)){
                            if(!userGround.killCell(cell)){
                                damagedCells.add(cell);
                            }else{
                                damagedCells.clear();
                            }
                            isStrike = true;
                        }else{
                            cell.changeColor();
                            cell.setShot(true);
                            isStrike = false;
                        }
                    }else if(!userGround.getCell(userGround.getI(damagedCells.get(damagedCells.size()-1))-1,userGround.getJ(damagedCells.get(damagedCells.size()-1))).isShot()){
                        cell = userGround.getCell(userGround.getI(damagedCells.get(damagedCells.size()-1))-1,userGround.getJ(damagedCells.get(damagedCells.size()-1)));
                        if(userGround.checkShotCell(cell)){
                            if(!userGround.killCell(cell)){
                                damagedCells.add(cell);
                            }else{
                                damagedCells.clear();
                            }
                            isStrike = true;
                        }else{
                            cell.changeColor();
                            cell.setShot(true);
                            isStrike = false;
                        }
                    }else{
                        cell = userGround.getCell(userGround.getI(damagedCells.get(0))-1,userGround.getJ(damagedCells.get(0)));
                        if(userGround.checkShotCell(cell)){
                            if(!userGround.killCell(cell)){
                                damagedCells.add(cell);
                            }else{
                                damagedCells.clear();
                            }
                            isStrike = true;
                        }else{
                            cell.changeColor();
                            cell.setShot(true);
                            isStrike = false;
                        }
                    }
                }
            }

        }
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        ground.getStage().act(Gdx.graphics.getDeltaTime());
        ground.getStage().draw();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
