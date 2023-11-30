package com.jeonginho.mobilerobotcontroller.addon;

import java.util.ArrayList;

public class AddOn {
    private char[][] addOnMap;
    private char[][] visitedMap;
    Robot addOnRobot;
    private final int spotNum;
    private int spotsVisited = 0;
    private ArrayList<int[]> path = new ArrayList<>();
    private boolean rePath = false;

    public AddOn(char[][] initialMap, int[] start, int[][] spot) {
        this.addOnMap = initialMap;
        this.spotNum = spot.length;
        this.addOnRobot = new Robot(start[0], start[1]);
    }
    public void printMap() {
        for (int i = this.addOnMap.length - 1; i >= 0 ; i--) {
            for (int j = 0; j < this.addOnMap[i].length; j++) {
                if(addOnRobot.getPos()[0]==j && addOnRobot.getPos()[1]==i)
                    System.out.print("R" + " ");
                else
                    System.out.print(this.addOnMap[i][j] + " ");
            }
            System.out.println();
        }
    }
    public void dfs(int x, int y) {
        // Direction arrays for moving up, down, left, right
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};

        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];

            if (spotsVisited == spotNum)
                return;
            // Check if the new position is within the addOnMap bounds
            if (newX >= 0 && newX < addOnMap[0].length && newY >= 0 && newY < addOnMap.length && addOnMap[newY][newX] != 'H' && visitedMap[newY][newX] != 'V') {
                // Mark the new position as visited
                if (addOnMap[newY][newX] == 'P') {
                    spotsVisited++;
                    if (spotsVisited == spotNum) {
                        path.add(new int[]{newX, newY});
                        return;
                    }
                }
                visitedMap[newY][newX] = 'V';
                path.add(new int[]{newX, newY});
                dfs(newX, newY);
                if (spotsVisited == spotNum)
                    break;
            }
        }
        if (spotsVisited == spotNum)
            return;
        if (addOnMap[path.get(path.size() - 1)[1]][path.get(path.size() - 1)[0]] == 'P')
            spotsVisited--;
        visitedMap[path.get(path.size() - 1)[1]][path.get(path.size() - 1)[0]] = '.';
        path.remove(path.size() - 1);
    }

    public static void print2D(String type, int[][] twoDArray) {
        System.out.print(type + "data : [ ");
        for (int[] row : twoDArray) {
            System.out.print("[" + row[0] + ", " + row[1] + "] ");
        }
        System.out.println("]");
    }

    public void planPath() {
        // Copy the addOnMap to avoid modifying the original addOnMap
        this.visitedMap = new char[this.addOnMap.length][this.addOnMap[0].length];
        dfs(this.addOnRobot.getPos()[1], this.addOnRobot.getPos()[0]);
        int[][] result = new int[path.size()][2];
        for (int i = 0; i < path.size(); i++) {
            result[i] = path.get(i);
        }
    }

    private int calTurns(int direction, int curX, int curY, int nextX, int nextY){
        int targetDirection;
        if (nextX > curX) {
            targetDirection = 1; // Right
        } else if (nextX < curX) {
            targetDirection = 3; // Left
        } else if (nextY > curY) {
            targetDirection = 0; // Up
        } else {
            targetDirection = 2; // Down
        }
        if(direction > targetDirection) targetDirection += 4;
        return targetDirection - direction;
    }
    public void orderMovement(SIM sim, Robot robot, Map realMap) {
        int curX = addOnRobot.getPos()[0];
        int curY = addOnRobot.getPos()[1];
        int curDirection = addOnRobot.getPos()[2];
        if (rePath){
            this.planPath();
            rePath = false;
        }
        int nextX = this.path.get(0)[0];
        int nextY = this.path.get(0)[1];

        for(int i = 0; i < calTurns(curDirection, curX, curY, nextX, nextY); i++)
            sim.rotateRobot(robot);
        if(sim.isHazard(robot, realMap)){
            rePath = true;
        }
        else{
            sim.moveRobot(robot, realMap);
            this.path.remove(0);
            if(sim.getPos(robot)[0]!=nextX || sim.getPos(robot)[1]!=nextY){
                rePath = true;
            }
        }


        addOnRobot.setPos(sim.getPos(robot)[0], sim.getPos(robot)[1], sim.getPos(robot)[2]);
    }

    public void testMove(SIM sim, Robot robot, Map realMap){
        int i = 1;
        while(!this.path.isEmpty()){
            orderMovement(sim,robot,realMap);
            int[][] result = new int[path.size()][2];
            for (int j = 0; j < path.size(); j++) {
                result[j] = path.get(j);
            }
            i++;
        }
    }
}