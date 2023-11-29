package com.jeonginho.mobilerobotcontroller.addon;

public class Map {
    private int xSize;
    private int ySize;
    private final char[][] map;
    /*
    Spot Type
        0 : none
        1 : Hazard
        2 : Color Blob (중요 지점)
        3 : Predefined Spot (탐색 지점)

     */
    public Map(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.map = new char[xSize][ySize];

        for(int i = xSize - 1; i >= 0; i--)
            for(int j = ySize - 1; j >= 0; j--)
                map[i][j] = '□';
    }

    public void UpdateMap(int x, int y, char spotType){
        this.map[x][y] = spotType;
    }
    public int getSpotType(int x, int y){
        return this.map[x][y];
    }

    public char[][] getMap(){
        return this.map;
    }

    public void printMap(){
        System.out.println();
        for(int y = ySize - 1; y >= 0; y--){
            for(int x = 0; x < xSize; x++){
                System.out.print(map[x][y] + " ");
            }
            System.out.println();
        }
        System.out.println("=========== map is printed ===========");
    }
}
