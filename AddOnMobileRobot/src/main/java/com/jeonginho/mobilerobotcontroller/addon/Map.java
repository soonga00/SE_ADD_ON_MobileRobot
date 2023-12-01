package com.jeonginho.mobilerobotcontroller.addon;

public class Map {
    private final int xSize;
    private final int ySize;
    private char[][] map;

    public Map(int xSize, int ySize, int[][] preds, int[][] hazard, int[][] colorBolob) {
        this.xSize = xSize + 1;
        this.ySize = ySize + 1;
        this.map = new char[this.ySize][this.xSize];

        for(int i = xSize; i >= 0; i--)
            for(int j = ySize; j >= 0; j--)
                this.updateMap(i,j,'.');
        for(int i = 0; i < preds.length; i++)
            this.updateMap(preds[i][0],preds[i][1], 'P');
        for(int i = 0; i < hazard.length; i++)
            this.updateMap(hazard[i][0],hazard[i][1], 'H');
        for(int i = 0; i < colorBolob.length; i++)
            this.updateMap(colorBolob[i][0],colorBolob[i][1], 'C');
    }

    public void updateMap(int x, int y, char spotType){
        this.map[y][x] = spotType;
    }
    public char getSpotType(int x, int y){
        return this.map[y][x];
    }

    public char[][] getMap(){
        return this.map;
    }

}
