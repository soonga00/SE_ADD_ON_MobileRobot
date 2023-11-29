package com.jeonginho.mobilerobotcontroller.addon;

import java.util.Random;

public class Robot {
    private int x;
    private int y;
    public static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private int direction;
    private char symbol;

    public Robot(int initialX, int initialY) {
        this.x = initialX;
        this.y = initialY;
        this.direction = 0;
    }

    public void moveForward(Map realMap) {
        Random random = new Random();
        double probability = random.nextDouble();
        int distance = (probability <= 0.1) ? 2 : 1;
        this.x += DIRECTIONS[direction][0] * distance;
        this.y += DIRECTIONS[direction][1] * distance;

        if(this.y < 0) this.y = 0;
        if(this.y > realMap.getMap().length) this.y = realMap.getMap().length;
        if(this.x < 0) this.x = 0;
        if(this.x > realMap.getMap()[0].length) this.x = realMap.getMap()[0].length;
    }

    public void rotate() {
        direction = (direction + 1) % 4;

    }
    public void setPos(int x, int y, int direction){
        this.x = x;
        this.y = y;
        this.direction = direction;
    }
    public int[] getPos(){
        return new int[]{this.x, this.y, this.direction};
    }
    public void printWholeMap(Map realMap){
        char[][] map = realMap.getMap();
        for(int y = map[0].length-1; y >= 0 ; y--){
            for(int x = 0; x < map.length; x++){
                if(x == this.x && y == this.y)
                    System.out.print("R ");
                else
                    System.out.print(map[x][y]+" ");
            }
            System.out.println();
        }
    }
}