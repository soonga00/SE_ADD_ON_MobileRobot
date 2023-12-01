package com.jeonginho.mobilerobotcontroller.addon;

import javax.swing.*;
import java.util.ArrayList;

public class AddOn {
    private char[][] addOnMap;
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

    public void planPath(SIM sim, Robot robot) {
        this.predsNum = 0;
        this.visitedPreds = 0;
        this.dfsCnt = 0;
        this.path = new ArrayList<>();
        for(int i = 0; i < addOnMap.length; i++){
            for(int j = 0; j < addOnMap[0].length; j++){
                if(addOnMap[i][j]=='P') predsNum++;
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
            this.planPath(sim, robot);
            rePath = false;
        }
        if(!path.isEmpty()) {
            System.out.print("path = ");
            printPath();
            System.out.println();
            int nextX = this.path.get(0)[0];
            int nextY = this.path.get(0)[1];

            for (int i = 0; i < 4; i++) {
                if (sim.isColorblob(robot, realMap)[i]) {
                    this.addOnMap[curY + Robot.DIRECTIONS[i][1]][curX + Robot.DIRECTIONS[i][0]] = 'C';
                }
            }
            for (int i = 0; i < calTurns(curDirection, curX, curY, nextX, nextY); i++)
                sim.rotateRobot(robot);
            System.out.println("Current Pos : " + curX + ", " + curY + "  direction : " + sim.getPos(robot)[2] + "   remained P spot : " + visitedPreds + " /" + predsNum);
            System.out.println("Next Movement : " + nextX + ", " + nextY);

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

    public void removeCircle(){
        int pathIdx = 0;
        int[][] direction = {{1,0}, {0,1}, {-1,0}, {0,-1}};

        System.out.println("====================================");
        printPath();
        showPaths();
        while(true)
        {
            if (path.size() < 4) {
                showPaths();
                return;
            }
            int nowX = path.get(pathIdx)[0];
            int nowY = path.get(pathIdx)[1];
            int nowPath = visitedMap[nowY][nowX];
            int maxPathNum = 0;
            for(int i=0; i<4; i++)
            {
                int targetX = nowX + direction[i][0];
                int targetY = nowY + direction[i][1];
                if(targetX < 0 | targetY < 0 | targetX > visitedMap[0].length-1 | targetY > visitedMap.length-1) {
                    //System.out.println("nowPath : " + nowPath+" target Path : over table path idx : " + pathIdx);
                    continue;
                }
                int targetPath = visitedMap[targetY][targetX];
                //System.out.println("nowPath : " + nowPath+" target Path : "+ targetPath+" path idx : " + pathIdx);

                if(targetPath > nowPath + 1 & targetPath > maxPathNum)
                    maxPathNum = targetPath;
            }
            if (maxPathNum != 0)
            {
                int removeNum = maxPathNum - nowPath - 1;
                for(int i = 0; i <removeNum; i++) {
                    System.out.println("pass is reomoved path size is : "+ path.size());
                    path.remove(pathIdx + 1);
                }
            }
            if (path.size() - 1 == pathIdx)
                break;
            pathIdx++;
        }
        printPath();
        showPaths();
    }
    public char[][] getMap(){
        return this.addOnMap;
    }

    public void testMove(SIM sim, Robot robot, Map realMap){
        int cnt = 0;
        while((predsNum!=0)&(cnt++<500)) {
            System.out.println("#"+cnt+" : \n");
            orderMovement(sim, robot, realMap);
            if (path.isEmpty()) {
                System.out.println("predefined spot is founded");
                planPath(sim, robot);
                if (predsNum ==0)
                    break;
            }

        }
        this.printMap();

        System.out.println("\n\n——————————————————\n\n총 돈 횟수 : "+ cnt);
    }
    public void printPath(){
        for(int i = 0; i < path.size(); i++){
            System.out.print("["+path.get(i)[0]+", "+path.get(i)[1]+"] ");
        }
        System.out.println();
    }
    public void showPaths(){
        System.out.println("====================");
        int tempCnt = 0;
        for (int i = this.addOnMap.length - 1; i >= 0 ; i--) {
            System.out.print(i+"| ");
            for (int j = 0; j < this.addOnMap[i].length; j++) {
                tempCnt = 0;
                for(int k = 0; k < path.size(); k++)
                    if(i == path.get(k)[1] && j == path.get(k)[0]){
                        System.out.print(String.format("%2d",k)+ " ");
                        tempCnt=1;
                    }
                if(tempCnt == 0)
                    System.out.print(" . ");
            }
            System.out.println();
        }
        System.out.print("_______________________\n |");
        for(int i = 0; i < this.addOnMap[0].length; i++)
            System.out.print(String.format("%3d",i));
        System.out.println("      : "+visitedPreds);
        printPath();
    }
}

// --------------------------------------

//package com.jeonginho.mobilerobotcontroller.addon;
//
//import java.util.ArrayList;
//
//public class AddOn {
//    private char[][] addOnMap;
//    private char[][] visitedMap;
//    Robot addOnRobot;
//    public int predsNum;
//    private int visitedPreds = 0;
//    public ArrayList<int[]> path = new ArrayList<>();
//    private boolean rePath = false;
//    public AddOn(char[][] initialMap, int[] start, int[][] spot) {
//        int rows = initialMap.length;
//        int cols = initialMap[0].length;
//
//        this.addOnMap = new char[rows][cols];
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                this.addOnMap[i][j] = initialMap[i][j];
//            }
//        }
//
//        this.predsNum = spot.length;
//        this.addOnRobot = new Robot(start[0], start[1]);
//    }
//    public void printMap() {
//        for (int i = this.addOnMap.length - 1; i >= 0 ; i--) {
//            for (int j = 0; j < this.addOnMap[i].length; j++) {
//                if(addOnRobot.getPos()[0]==j&&addOnRobot.getPos()[1]==i)
//                    System.out.print("R" + " ");
//                else
//                    System.out.print(this.addOnMap[i][j] + " ");
//            }
//            System.out.println();
//        }
//    }
//    public void dfs(int x, int y) {
//        // Direction arrays for moving up, down, left, right
//        int[] dx = {1, 0, -1, 0};
//        int[] dy = {0, 1, 0, -1};
//
//        for (int i = 0; i < 4; i++) {
//            int newX = x + dx[i];
//            int newY = y + dy[i];
//            if (visitedPreds == predsNum)
//                return;
//            // Check if the new position is within the addOnMap bounds
//            if (newX >= 0 && newX < addOnMap[0].length && newY >= 0 && newY < addOnMap.length && addOnMap[newY][newX] != 'H' && visitedMap[newY][newX] != 'V') {
//                // Mark the new position as visited
//                if (addOnMap[newY][newX] == 'P') {
//                    visitedPreds++;
//                    if (visitedPreds == predsNum) {
//                        path.add(new int[]{newX, newY});
//                        return;
//                    }
//                }
//                visitedMap[newY][newX] = 'V';
//                path.add(new int[]{newX, newY});
//                //this.printTest(newX, newY);
//                dfs(newX, newY);
//                if (visitedPreds == predsNum)
//                    break;
//            }
//        }
//        if (visitedPreds == predsNum){
//            return;
//        }
//        if (addOnMap[path.get(path.size() - 1)[1]][path.get(path.size() - 1)[0]] == 'P')
//                visitedPreds--;
//        visitedMap[path.get(path.size() - 1)[1]][path.get(path.size() - 1)[0]] = '.';
//        path.remove(path.size() - 1);
//    }
//
//    public void planPath(SIM sim, Robot robot) {
//        this.predsNum = 0;
//        this.visitedPreds = 0;
//        this.path = new ArrayList<>();
//        for(int i = 0; i < addOnMap.length; i++){
//            for(int j = 0; j < addOnMap[0].length; j++){
//                if(addOnMap[i][j]=='P') predsNum++;
//            }
//        }
//        // Copy the addOnMap to avoid modifying the original addOnMap
//        this.visitedMap = new char[this.addOnMap.length][this.addOnMap[0].length];
//        dfs(addOnRobot.getPos()[0], addOnRobot.getPos()[1]);
//    }
//
//    private int calTurns(int direction, int curX, int curY, int nextX, int nextY){
//        int targetDirection;
//        if (nextX > curX) {
//            targetDirection = 1; // Right
//        } else if (nextX < curX) {
//            targetDirection = 3; // Left
//        } else if (nextY > curY) {
//            targetDirection = 0; // Up
//        } else {
//            targetDirection = 2; // Down
//        }
//        if(direction > targetDirection) targetDirection += 4;
//        return targetDirection - direction;
//    }
//    public void orderMovement(SIM sim, Robot robot, Map realMap) {
//            int curX = addOnRobot.getPos()[0];
//            int curY = addOnRobot.getPos()[1];
//            int curDirection = addOnRobot.getPos()[2];
//            if (rePath) {
//                System.out.println("Repeat Calculate Path");
//                this.planPath(sim, robot);
//                rePath = false;
//            }
//        if(!path.isEmpty()) {
//            printPath();
//            int nextX = this.path.get(0)[0];
//            int nextY = this.path.get(0)[1];
//
//            for (int i = 0; i < calTurns(curDirection, curX, curY, nextX, nextY); i++)
//                sim.rotateRobot(robot);
////            System.out.println("Current Pos : " + curX + ", " + curY + "  direction : " + curDirection + "   remained P spot : " + visitedPreds + " /" + predsNum);
////            System.out.println("NExt Movement : " + nextX + ", " + nextY);
//
//            for (int i = 0; i < 4; i++) {
//                if (sim.isColorblob(robot, realMap)[i]) {
//                    this.addOnMap[curY + Robot.DIRECTIONS[i][1]][curX + Robot.DIRECTIONS[i][0]] = 'C';
//                }
//            }
//            if (sim.isHazard(robot, realMap)) {
//                rePath = true;
//                System.out.println("\b HAZARD!!!!!!");
//                this.addOnMap[nextY][nextX] = 'H';
//            } else {
//                sim.moveRobot(robot, realMap);
//                if (sim.getPos(robot)[0] != nextX || sim.getPos(robot)[1] != nextY) {
//                    rePath = true;
//                } else
//                    this.path.remove(0);
//            }
//            if (addOnMap[sim.getPos(robot)[1]][sim.getPos(robot)[0]] == 'P') {
//                System.out.println("//==//== Already Passed Predefined Spot. Delete that spot");
//                addOnMap[sim.getPos(robot)[1]][sim.getPos(robot)[0]] = 'S';
//            }
//            addOnRobot.setPos(sim.getPos(robot)[0], sim.getPos(robot)[1], sim.getPos(robot)[2]);
//        }
//    }
//
//    public void testMove(SIM sim, Robot robot, Map realMap){
//        int cnt = 0;
//        while(!this.path.isEmpty()&(predsNum!=0)&(cnt++<500)) {
//            System.out.println("#"+cnt+":");
//            orderMovement(sim, robot, realMap);
//            try {
//                Thread.sleep(500); //1초 대기
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//            this.printMap();
//        }
//    public void printPath(){
//        for(int i = 0; i < path.size(); i++){
//            System.out.print("["+path.get(i)[0]+", "+path.get(i)[1]+"] ");
//        }
//        System.out.println();
//    }
//
//    public char[][] getMap(){
//        return this.addOnMap;
//    }
//}