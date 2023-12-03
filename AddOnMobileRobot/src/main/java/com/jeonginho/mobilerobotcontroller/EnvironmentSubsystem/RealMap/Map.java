package com.jeonginho.mobilerobotcontroller.EnvironmentSubsystem.RealMap;

public class Map {
    private final int xSize;
    private final int ySize;
    private final char[][] map;

    public Map(int xSize, int ySize, int[][] preds, int[][] hazard, int[][] colorBolob) {
        this.xSize = xSize + 1;
        this.ySize = ySize + 1;
        this.map = new char[this.ySize][this.xSize];

        for(int i = xSize; i >= 0; i--)
            for(int j = ySize; j >= 0; j--)
                this.updateMap(i,j,'.');
        for (int[] pred : preds) this.updateMap(pred[0], pred[1], 'P');
        for (int[] ints : hazard) this.updateMap(ints[0], ints[1], 'H');
        for (int[] ints : colorBolob) this.updateMap(ints[0], ints[1], 'C');
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
    public char[][] getInitialMap() {
        char[][] mapForAddOn = new char[ySize][xSize];
        for(int i = xSize-1; i >= 0; i--)
            for(int j = ySize-1; j >= 0; j--)
                if(this.map[j][i] != 'P')
                    mapForAddOn[j][i] = '.';
                else
                    mapForAddOn[j][i] = 'P';
        return mapForAddOn;
    }
}