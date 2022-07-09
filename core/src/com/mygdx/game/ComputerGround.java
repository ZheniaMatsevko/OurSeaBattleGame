package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ComputerGround extends Actor {
    private PlayGround ground;
    private PlayGround userGround;
    private boolean isStrike;
    private List<Cell> damagedCells;
    private int damagedDirection;
    private int bonusScore = 20;
    private SeaBattleGame game;


    public List<Cell> getDamagedCells() {
        return damagedCells;
    }
    public void setSeaBattleGame(SeaBattleGame game) {
        this.game = game;
    }

    public int getBonusScore(){
        return bonusScore;
    }
    public void setBonusScore(int score) {
        this.bonusScore = score;
    }

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

    public void shoot(Label messageLabel){
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
                    messageLabel.setText("      Computer shot at " + userGround.getCellName(cell) + " and damaged the ship!");
                }else{
                    messageLabel.setText("      Computer shot at " + userGround.getCellName(cell) + " and killed the ship!");
                    game.setYourShipsKilled(game.getYourShipsKilled()+1);
                }
                bonusScore--;
                isStrike = true;
            }else{
                cell.changeColor(Color.GRAY);
                cell.setShot(true);
                isStrike = false;
                messageLabel.setText("      Computer shot at " + userGround.getCellName(cell) + " and missed.");
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
                        messageLabel.setText("      Computer shot at " + userGround.getCellName(cell) + " and damaged the ship!");
                    }else{
                        damagedCells.clear();
                        messageLabel.setText("      Computer shot at " + userGround.getCellName(cell) + " and killed the ship!");
                        game.setYourShipsKilled(game.getYourShipsKilled()+1);
                    }
                    damagedDirection=direction;
                    bonusScore--;
                    isStrike = true;
                }else{
                    cell.changeColor(Color.GRAY);
                    cell.setShot(true);
                    isStrike = false;
                    messageLabel.setText("      Computer shot at " + userGround.getCellName(cell) + " and missed.");
                }
            }else{
                if(damagedDirection<2){
                    if(damagedDirection==0 && userGround.getJ(damagedCells.get(damagedCells.size()-1))+1<userGround.getNumberOfCellsInRow() && !userGround.getCell(userGround.getI(damagedCells.get(damagedCells.size()-1)),userGround.getJ(damagedCells.get(damagedCells.size()-1))+1).isShot()){
                        cell = userGround.getCell(userGround.getI(damagedCells.get(damagedCells.size()-1)),userGround.getJ(damagedCells.get(damagedCells.size()-1))+1);
                        compKill(cell,messageLabel);
                    }else if(damagedDirection==0){
                       cell = userGround.getCell(userGround.getI(damagedCells.get(0)),userGround.getJ(damagedCells.get(0))-1);
                        compKill(cell,messageLabel);
                        damagedDirection=1;
                    }else if(damagedDirection==1 && userGround.getJ(damagedCells.get(damagedCells.size()-1))-1>=0 && !userGround.getCell(userGround.getI(damagedCells.get(damagedCells.size()-1)),userGround.getJ(damagedCells.get(damagedCells.size()-1))-1).isShot()){
                        cell = userGround.getCell(userGround.getI(damagedCells.get(damagedCells.size()-1)),userGround.getJ(damagedCells.get(damagedCells.size()-1))-1);
                        compKill(cell,messageLabel);
                    }else if(damagedDirection==1){
                        cell = userGround.getCell(userGround.getI(damagedCells.get(0)),userGround.getJ(damagedCells.get(0))+1);
                        compKill(cell,messageLabel);
                        damagedDirection=0;
                    }
                }else{
                    if(damagedDirection==2 && userGround.getI(damagedCells.get(damagedCells.size()-1))+1<userGround.getNumberOfCellsInRow() && !userGround.getCell(userGround.getI(damagedCells.get(damagedCells.size()-1))+1,userGround.getJ(damagedCells.get(damagedCells.size()-1))).isShot()){
                        cell = userGround.getCell(userGround.getI(damagedCells.get(damagedCells.size()-1))+1,userGround.getJ(damagedCells.get(damagedCells.size()-1)));
                        compKill(cell,messageLabel);
                    }else if(damagedDirection==2){
                        cell = userGround.getCell(userGround.getI(damagedCells.get(0))-1,userGround.getJ(damagedCells.get(0)));
                        compKill(cell,messageLabel);
                        damagedDirection=3;
                    }else if(damagedDirection==3 && userGround.getI(damagedCells.get(damagedCells.size()-1))-1 >=0 &&!userGround.getCell(userGround.getI(damagedCells.get(damagedCells.size()-1))-1,userGround.getJ(damagedCells.get(damagedCells.size()-1))).isShot()){
                        cell = userGround.getCell(userGround.getI(damagedCells.get(damagedCells.size()-1))-1,userGround.getJ(damagedCells.get(damagedCells.size()-1)));
                        compKill(cell,messageLabel);
                    }else if(damagedDirection==3){
                        cell = userGround.getCell(userGround.getI(damagedCells.get(0))+1,userGround.getJ(damagedCells.get(0)));
                        compKill(cell,messageLabel);
                        damagedDirection=2;
                    }
                }
            }

        }
    }
    private void compKill(Cell cell,Label messageLabel){
        if(!cell.isShot()) {
            if (userGround.checkShotCell(cell)) {
                if (!userGround.killCell(cell)) {
                    damagedCells.add(cell);
                    messageLabel.setText("      Computer shot at " + userGround.getCellName(cell) + " and damaged the ship!");
                } else {
                    damagedCells.clear();
                    messageLabel.setText("      Computer shot at " + userGround.getCellName(cell) + " and killed the ship!");
                    game.setYourShipsKilled(game.getYourShipsKilled() + 1);
                }
                bonusScore--;
                isStrike = true;
            } else {
                cell.changeColor(Color.GRAY);
                cell.setShot(true);
                isStrike = false;
                messageLabel.setText("      Computer shot at " + userGround.getCellName(cell) + " and missed.");
            }
        }
        else damagedCells.clear();
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
