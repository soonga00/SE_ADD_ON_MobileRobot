package com.jeonginho.mobilerobotcontroller.EnvironmentSubsystem.RealRobot;

import com.jeonginho.mobilerobotcontroller.EnvironmentSubsystem.RealMap.Map;

import java.util.Random;

public class Robot {
    public static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private int x;
    private int y;
    private int direction;

    public Robot(int initialX, int initialY) {
        this.x = initialX;
        this.y = initialY;
        this.direction = 0;
    }

    public void moveForward(Map realMap) {
        Random random = new Random();
        double probability = random.nextDouble();
        int distance = (probability <= 0.1) ? 2 : 1;
        if(distance==2){
            System.out.println("=============================error occured!==========");
        }
        this.x += DIRECTIONS[direction][0] * distance;
        this.y += DIRECTIONS[direction][1] * distance;

        if(this.y < 0) this.y = 0;
        if(this.y > realMap.getMap().length-1) this.y = realMap.getMap().length-1;
        if(this.x < 0) this.x = 0;
        if(this.x > realMap.getMap()[0].length-1) this.x = realMap.getMap()[0].length-1;
        if(distance == 2 & realMap.getSpotType(x,y)=='H'){
            System.out.println("I'm at ["+x+", "+y+"]. I moved twice, but there's hazard. Cancle move.");
            this.x += DIRECTIONS[(direction+2)%4][0];
            this.y += DIRECTIONS[(direction+2)%4][1];
        }

    }

    public void rotate() {
        direction = (direction + 1) % 4;
        System.out.println("Robot direction"+direction);

    }
    public void setPos(int x, int y, int direction){
        this.x = x;
        this.y = y;
        this.direction = direction;
    }
    public int[] getPos(){
        return new int[]{this.x, this.y, this.direction};
    }
}