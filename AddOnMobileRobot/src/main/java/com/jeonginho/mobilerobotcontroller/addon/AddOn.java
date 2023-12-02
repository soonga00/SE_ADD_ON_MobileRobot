package com.jeonginho.mobilerobotcontroller.addon;

import java.util.ArrayList;

public class AddOn {
    private final char[][] addOnMap;
    private int[][] visitedMap;
    Robot addOnRobot;
    public int predsNum;
    private int visitedPreds = 0;
    public ArrayList<int[]> path;
    private int dfsCnt;
    private boolean rePath = false;
    public AddOn(char[][] initialMap, int[] start, int[][] spot) {
        int rows = initialMap.length;
        int cols = initialMap[0].length;

        this.addOnMap = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(initialMap[i], 0, this.addOnMap[i], 0, cols);
        }

        this.predsNum = spot.length;
        this.addOnRobot = new Robot(start[0], start[1]);
    }
    public void dfs(int x, int y) {
        // Direction arrays for moving up, down, left, right
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};

        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            if (visitedPreds == 1)
                return;
            // Check if the new position is within the addOnMap bounds
            if (newX >= 0 && newX < addOnMap[0].length && newY >= 0 && newY < addOnMap.length && addOnMap[newY][newX] != 'H' && visitedMap[newY][newX] == -1) {
                // Mark the new position as visited
                if (addOnMap[newY][newX] == 'P') {
                    visitedPreds++;
                    visitedMap[newY][newX] = dfsCnt++;
                    path.add(new int[]{newX, newY});
                    return;
                }
                visitedMap[newY][newX] = dfsCnt++;
                path.add(new int[]{newX, newY});
                //this.printTest(newX, newY);
                dfs(newX, newY);
                if (visitedPreds == 1)
                    break;
            }
        }
        if (visitedPreds == 1){
            return;
        }
        if (addOnMap[path.get(path.size() - 1)[1]][path.get(path.size() - 1)[0]] == 'P')
            visitedPreds--;
        visitedMap[path.get(path.size() - 1)[1]][path.get(path.size() - 1)[0]] = -1;
        path.remove(path.size() - 1);
        dfsCnt--;
    }

    public void planPath() {
        this.predsNum = 0;
        this.visitedPreds = 0;
        this.dfsCnt = 0;
        this.path = new ArrayList<>();
        for (char[] chars : addOnMap) {
            for (int j = 0; j < addOnMap[0].length; j++) {
                if (chars[j] == 'P') predsNum++;
            }
        }
        System.out.println("preds Num : "+ predsNum);
        if (predsNum == 0)
            return;
        // Copy the addOnMap to avoid modifying the original addOnMap
        this.visitedMap = new int[this.addOnMap.length][this.addOnMap[0].length];
        for(int i = 0; i < addOnMap.length; i++){
            for(int j = 0; j < addOnMap[0].length; j++){
                visitedMap[i][j] = -1;
            }
        }
        dfs(addOnRobot.getPos()[0], addOnRobot.getPos()[1]);
        removeCircle();
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
            this.planPath();
            rePath = false;
        }
        if(!path.isEmpty()) {
            System.out.print("path = ");
            System.out.println();
            int nextX = this.path.get(0)[0];
            int nextY = this.path.get(0)[1];
            int remainedTurns = calTurns(curDirection, curX, curY, nextX, nextY);

            for (int i = 0; i < 4; i++) {
                if (sim.isColorblob(robot, realMap)[i]) {
                    this.addOnMap[curY + Robot.DIRECTIONS[i][1]][curX + Robot.DIRECTIONS[i][0]] = 'C';
                }
            }
            if(remainedTurns>0) {
                sim.rotateRobot(robot);
            }else {

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
                    for (int i = 0; i < 4; i++) {
                        if (sim.isColorblob(robot, realMap)[i]) {
                            this.addOnMap[nextY + Robot.DIRECTIONS[i][1]][nextX + Robot.DIRECTIONS[i][0]] = 'C';
                        }
                    }
                }
                if (addOnMap[sim.getPos(robot)[1]][sim.getPos(robot)[0]] == 'P') {
                    System.out.println("//==//== Already Passed Predefined Spot. Delete that spot");
                    addOnMap[sim.getPos(robot)[1]][sim.getPos(robot)[0]] = 'S';
                }
                addOnRobot.setPos(sim.getPos(robot)[0], sim.getPos(robot)[1], sim.getPos(robot)[2]);
            }
        }

    }

    public void removeCircle(){
        int pathIdx = 0;
        int[][] direction = {{1,0}, {0,1}, {-1,0}, {0,-1}};

        while (path.size() >= 4) {
            int nowX = path.get(pathIdx)[0];
            int nowY = path.get(pathIdx)[1];
            int nowPath = visitedMap[nowY][nowX];
            int maxPathNum = 0;
            for (int i = 0; i < 4; i++) {
                int targetX = nowX + direction[i][0];
                int targetY = nowY + direction[i][1];
                if (targetX < 0 | targetY < 0 | targetX > visitedMap[0].length - 1 | targetY > visitedMap.length - 1) {
                    continue;
                }
                int targetPath = visitedMap[targetY][targetX];

                if (targetPath > nowPath + 1 & targetPath > maxPathNum)
                    maxPathNum = targetPath;
            }
            if (maxPathNum != 0) {
                int removeNum = maxPathNum - nowPath - 1;
                for (int i = 0; i < removeNum; i++) {
                    path.remove(pathIdx + 1);
                }
            }
            if (path.size() - 1 == pathIdx)
                break;
            pathIdx++;
        }
    }
    public char[][] getMap(){
        return this.addOnMap;
    }
}
