package com.jeonginho.mobilerobotcontroller.addon;

public class Map {
    private final int xSize;
    private final int ySize;
    private final char[][] map;
    /*
    Spot Type
        0 : none
        1 : Hazard
        2 : Color Blob (중요 지점)
        3 : Predefined Spot (탐색 지점)

     */
    public Map(int xSize, int ySize) {
        this.xSize = xSize + 1;
        this.ySize = ySize + 1;
        this.map = new char[this.ySize][this.xSize];

        for(int i = xSize; i >= 0; i--)
            for(int j = ySize; j >= 0; j--)
                map[j][i] = '□';
    }

    public void updateMap(int x, int y, char spotType){
        this.map[y][x] = spotType;
    }
    public int getSpotType(int x, int y){
        return this.map[y][x];
    }

    public char[][] getMap(){
        return this.map;
    }

    public void printMap(){
        System.out.println();
        for(int y = ySize - 1; y >= 0; y--){
            for(int x = 0; x < xSize; x++){
                System.out.print(map[y][x] + " ");
            }
            System.out.println();
        }
        System.out.println("=========== map is printed ===========");
    }
}
