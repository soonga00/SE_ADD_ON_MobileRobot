package com.jeonginho.mobilerobotcontroller.addon;

import java.util.ArrayList;

public class AddOn {
    private char[][] addOnMap;
    private char[][] visitedMap;
    Robot addOnRobot;
    private int spotNum;
    private int spotsVisited = 0;
    private ArrayList<int[]> path = new ArrayList<>();
    private boolean rePath = false;

    public AddOn(char[][] initialMap, int[] start, int[][] spot) {
        int rows = initialMap.length;
        int cols = initialMap[0].length;

        this.addOnMap = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.addOnMap[i][j] = initialMap[i][j];
            }
        }

        this.spotNum = spot.length;
        this.addOnRobot = new Robot(start[0], start[1]);
    }
    public void printMap() {
        for (int i = this.addOnMap.length - 1; i >= 0 ; i--) {
            for (int j = 0; j < this.addOnMap[i].length; j++) {
                if(addOnRobot.getPos()[0]==j&&addOnRobot.getPos()[1]==i)
                    System.out.print("R" + " ");
                else
                    System.out.print(this.addOnMap[i][j] + " ");
            }
            System.out.println();
        }
    }
    public void printTest(int x, int y) {
        for (int i = this.addOnMap.length - 1; i >= 0 ; i--) {
            for (int j = 0; j < this.addOnMap[i].length; j++) {
                if(x==j&&y==i)
                    System.out.print("R" + " ");
                else
                    System.out.print(this.addOnMap[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("============================");
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
                //this.printTest(newX, newY);
                dfs(newX, newY);
                if (spotsVisited == spotNum)
                    break;
            }
        }
        if (spotsVisited == spotNum){
            return;
        }
        if (addOnMap[path.get(path.size() - 1)[1]][path.get(path.size() - 1)[0]] == 'P')
                spotsVisited--;
        visitedMap[path.get(path.size() - 1)[1]][path.get(path.size() - 1)[0]] = '.';
        path.remove(path.size() - 1);
    }

    public void planPath(SIM sim, Robot robot) {
        this.spotNum = 0;
        this.spotsVisited = 0;
        this.path = new ArrayList<>();
        for(int i = 0; i < addOnMap.length; i++){
            for(int j = 0; j < addOnMap[0].length; j++){
                if(addOnMap[i][j]=='P') spotNum++;
            }
        }
        // Copy the addOnMap to avoid modifying the original addOnMap
        this.visitedMap = new char[this.addOnMap.length][this.addOnMap[0].length];
        dfs(addOnRobot.getPos()[0], addOnRobot.getPos()[1]);
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
        if(!path.isEmpty()) {
            int curX = addOnRobot.getPos()[0];
            int curY = addOnRobot.getPos()[1];
            int curDirection = addOnRobot.getPos()[2];
            if (rePath) {
                System.out.println("Repeat Calculate Path");
                this.planPath(sim, robot);
                rePath = false;
            }
            printPath();
            int nextX = this.path.get(0)[0];
            int nextY = this.path.get(0)[1];

            for (int i = 0; i < calTurns(curDirection, curX, curY, nextX, nextY); i++)
                sim.rotateRobot(robot);
            System.out.println("Current Pos : " + curX + ", " + curY + "  direction : " + curDirection + "   remained P spot : " + spotsVisited + " /" + spotNum);
            System.out.println("NExt Movement : " + nextX + ", " + nextY);

            for (int i = 0; i < 4; i++) {
                if (sim.isColorblob(robot, realMap)[i]) {
                    this.addOnMap[curY + Robot.DIRECTIONS[i][1]][curX + Robot.DIRECTIONS[i][0]] = 'C';
                }
            }
            if (sim.isHazard(robot, realMap)) {
                rePath = true;
                System.out.println("\b HAZARD!!!!!!");
                this.addOnMap[nextY][nextX] = 'H';
            } else {
                sim.moveRobot(robot, realMap);
                if (sim.getPos(robot)[0] != nextX || sim.getPos(robot)[1] != nextY) {
                    rePath = true;
                } else
                    this.path.remove(0);
            }
            if (addOnMap[sim.getPos(robot)[1]][sim.getPos(robot)[0]] == 'P') {
                System.out.println("//==//== Already Passed Predefined Spot. Delete that spot");
                addOnMap[sim.getPos(robot)[1]][sim.getPos(robot)[0]] = 'S';
            }
            addOnRobot.setPos(sim.getPos(robot)[0], sim.getPos(robot)[1], sim.getPos(robot)[2]);
        }
    }

    public void testMove(SIM sim, Robot robot, Map realMap){
        int cnt = 0;
        while(!this.path.isEmpty()&(spotNum!=0)&(cnt++<500)) {
            System.out.println("#"+cnt+":");
            orderMovement(sim, robot, realMap);
            this.printMap();
        }
    }
    public void printPath(){
        for(int i = 0; i < path.size(); i++){
            System.out.print("["+path.get(i)[0]+", "+path.get(i)[1]+"] ");
        }
        System.out.println();
    }
}