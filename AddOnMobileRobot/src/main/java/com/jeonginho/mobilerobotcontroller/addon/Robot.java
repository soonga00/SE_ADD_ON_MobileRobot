package com.jeonginho.mobilerobotcontroller.addon;

public class Robot {
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    int x;
    int y;
    int[] direction;
    public Robot(int x, int y) {
        this.x = x;
        this.y = y;
        this.direction = DIRECTIONS[0];
    }

    public int getX(){
        return  this.x;
    }
    public int getY(){
        return  this.y;
    }
}
