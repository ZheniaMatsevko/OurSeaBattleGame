package com.mygdx.game;

public class ComputerGround {
    PlayGround ground;
    public ComputerGround(int numberOfCellsInRow, int numberOfBoats){
        ground = new PlayGround(numberOfCellsInRow,numberOfBoats);
    }
}
