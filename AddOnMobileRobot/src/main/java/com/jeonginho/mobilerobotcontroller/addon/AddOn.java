package com.jeonginho.mobilerobotcontroller.addon;

import java.util.ArrayList;

public class AddOn {
    private char[][] addOnMap;
    private int[][] visitedMap;
    Robot addOnRobot;
    private int predsNum;
    private int visitedPreds = 0;
    private ArrayList<int[]> path = new ArrayList<>();
    private boolean rePath = false;
    private int dfsCnt = 0;
    private int removeCnt = 0;

    public AddOn(char[][] initialMap, int[] start, int[][] spot) {
        int rows = initialMap.length;
        int cols = initialMap[0].length;

        this.addOnMap = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.addOnMap[i][j] = initialMap[i][j];
            }
        }

        this.predsNum = spot.length;
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
    private void showPaths(){
        System.out.println("====================");
        for (int i = this.addOnMap.length - 1; i >= 0 ; i--) {
            for (int j = 0; j < this.addOnMap[i].length; j++) {
                if(this.visitedMap[i][j]==-1)
                    System.out.print(". ");
                else
                    System.out.print(this.visitedMap[i][j] + " ");
            }
            System.out.println();
        }
    }
    private void isAdjSpotVisited(int newX, int newY){
        int[][] adjSpot = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        boolean needChange = false;
        int i = 0;
        int minPastDfsCnt = dfsCnt;
        for(i = 0; i < 4; i++){
            //System.out.println("Current dfsCnt : " + dfsCnt + "path count : " + path.size());
            int targetX = newX + adjSpot[i][0];
            int targetY = newY + adjSpot[i][1];

            if(targetX < 0 | targetY < 0 | targetX > visitedMap[0].length-1 | targetY > visitedMap.length-1) continue;
            if(visitedMap[targetY][targetX] != -1 & visitedMap[targetY][targetX] != dfsCnt-1){
                if(minPastDfsCnt > visitedMap[targetY][targetX]) {
                    minPastDfsCnt = visitedMap[targetY][targetX];
                    needChange = true;
                }
            }
        }
        if(needChange){
            removeCnt = dfsCnt - minPastDfsCnt-1;
            System.out.println("DFS Count : "+dfsCnt+", MinPastDfsCnt : "+minPastDfsCnt+", || removeCnt : "+removeCnt);
        }

    }
    private void dfs(int x, int y, int curDirection) {
        // Direction arrays for moving up, down, left, right
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};

        for (int i = 0; i < 4; i++) {
            if(removeCnt > 0){
                removeCnt--;
                break;
            }
            int newX = x + dx[(i+curDirection)%4];
            int newY = y + dy[(i+curDirection)%4];
            if (visitedPreds == 1)
                return;
            // Check if the new position is within the addOnMap bounds
            if (newX >= 0 && newX < addOnMap[0].length && newY >= 0 && newY < addOnMap.length && addOnMap[newY][newX] != 'H' && visitedMap[newY][newX] == -1) {


                visitedMap[newY][newX] = dfsCnt;
                path.add(new int[]{newX, newY});
                isAdjSpotVisited(newX, newY);

                showPaths();
                if(removeCnt > 0){
                    removeCnt--;
                    break;
                }
                if (addOnMap[newY][newX] == 'P') {
                    visitedPreds++;
                    return;
                }
                dfsCnt++;
                dfs(newX, newY, i);

            }
        }
        visitedMap[path.get(path.size() - 1)[1]][path.get(path.size() - 1)[0]] = -1;
        path.remove(path.size() - 1);
        dfsCnt--;
        if (visitedPreds == 1){
            return;
        }
    }

    public void planPath(SIM sim, Robot robot) {
        this.predsNum = 0;
        this.visitedPreds = 0;
        this.path = new ArrayList<>();
        for(int i = 0; i < addOnMap.length; i++){
            for(int j = 0; j < addOnMap[0].length; j++){
                if(addOnMap[i][j]=='P') predsNum++;
            }
        }

        // Copy the addOnMap to avoid modifying the original addOnMap
        if(predsNum != 0){
            this.visitedMap = new int[this.addOnMap.length][this.addOnMap[0].length];
            for(int i = 0; i< visitedMap.length; i++)
                for(int j = 0; j < visitedMap[0].length; j++)
                    visitedMap[i][j] = -1;
            dfsCnt = 0;
            dfs(addOnRobot.getPos()[0], addOnRobot.getPos()[1], 0);
            showPaths();
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
        if (rePath) {
            System.out.println("Repeat Calculate Path");
            this.planPath(sim, robot);
            rePath = false;
        }
        if(!path.isEmpty()) {
            int nextX = this.path.get(0)[0];
            int nextY = this.path.get(0)[1];

            for (int i = 0; i < calTurns(curDirection, curX, curY, nextX, nextY); i++)
                sim.rotateRobot(robot);
            System.out.println("Current Pos : " + curX + ", " + curY + "  direction : " + curDirection + "   remained P spot : " + visitedPreds + " /" + predsNum);
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
        }else if(predsNum!=0){
            rePath = true;
        }
    }

    public void testMove(SIM sim, Robot robot, Map realMap){
        int cnt = 0;
        while(!this.path.isEmpty()&(predsNum!=0)|(cnt++<100)) {
            System.out.println("\n#"+cnt+":");
            orderMovement(sim, robot, realMap);
            this.printMap();
        }
        System.out.println("===============================\n||    Operation Finished !   ||\n===============================");
    }
    public void printPath(){
        for(int i = 0; i < path.size(); i++){
            System.out.print("["+path.get(i)[0]+", "+path.get(i)[1]+"] ");
        }
        System.out.println();
    }
}