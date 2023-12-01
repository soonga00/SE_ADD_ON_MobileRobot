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
    public void printMap() {
        for (int i = this.map.length - 1; i >= 0 ; i--) {
            for (int j = 0; j < this.map[i].length; j++) {
                System.out.print(this.map[i][j] + " ");
            }
            System.out.println();
        }
    }
}