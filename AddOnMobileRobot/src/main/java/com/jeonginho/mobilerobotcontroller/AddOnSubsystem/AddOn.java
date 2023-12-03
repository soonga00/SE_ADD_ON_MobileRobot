package com.jeonginho.mobilerobotcontroller.AddOnSubsystem;

import com.jeonginho.mobilerobotcontroller.EnvironmentSubsystem.RealMap.Map;
import com.jeonginho.mobilerobotcontroller.EnvironmentSubsystem.RealRobot.Robot;
import com.jeonginho.mobilerobotcontroller.EnvironmentSubsystem.Simulator.SIM;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddOn {
    private final char[][] addOnMap;
    private Robot addOnRobot;
    private int predsNum;
    private ArrayList<int[]> path;
    private int dfsCnt;
    private boolean rePath = true;
    private boolean posError = false;
    private Path newPath;
    public AddOn(char[][] initialMap, int[] start, int[][] spot) {
        int rows = initialMap.length;
        int cols = initialMap[0].length;
        newPath = new Path();

        this.addOnMap = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(initialMap[i], 0, this.addOnMap[i], 0, cols);
        }

        this.predsNum = spot.length;
        this.addOnRobot = new Robot(start[0], start[1]);
    }

    public ArrayList<int[]> getPath(){
        return this.path;
    }
    public void orderMovement(SIM sim, Robot realRobot, Map realMap){

        System.out.println("AddOn Part STart!");
        MoveRobot moveRobot = new MoveRobot();
        System.out.println("////////Repath : "+rePath);
        if (rePath) {
            System.out.println("Repeat Calculate Path");
            this.path = newPath.planPath(addOnMap, addOnRobot);
            predsNum = newPath.getPredsNum();
            rePath = false;
        }
        boolean[] repathAndError = moveRobot.orderMovement(sim, addOnMap, addOnRobot, realRobot, realMap, path);
        this.rePath = repathAndError[0];
        this.posError = repathAndError[1];

    }


    public char[][] getMap(){
        return this.addOnMap;
    }
    public boolean isPathEmpty(){
        if(path == null)
            return true;
        return path.size() < 2;
    }
    public boolean isErrorOccured(){
        return posError;
    }
    public boolean isVisitedAllPreds(){
        return predsNum == 0;
    }
}
