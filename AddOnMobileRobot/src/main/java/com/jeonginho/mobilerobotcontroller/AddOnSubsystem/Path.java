package com.jeonginho.mobilerobotcontroller.AddOnSubsystem;

import com.jeonginho.mobilerobotcontroller.EnvironmentSubsystem.RealRobot.Robot;

import java.util.ArrayList;

public class Path {
    private boolean pathFound;
    private char[][] addOnMap;
    private int[][] visitedMap;
    private int dfsCnt;
    private ArrayList<int[]> path;
    private int predsNum;

    protected Path() {
        pathFound = false;
    }

    protected ArrayList<int[]> planPath(char[][] addOnMap, Robot addOnRobot) {
        System.out.println("PlanPath STart!");
        predsNum = 0;
        this.addOnMap = addOnMap;
        this.dfsCnt = 0;
        this.path = new ArrayList<>();
        for (char[] chars : addOnMap) {
            for (int j = 0; j < addOnMap[0].length; j++) {
                if (chars[j] == 'P') predsNum++;
            }
        }
        System.out.println("preds Num : "+ predsNum);
        if (predsNum > 0) {
            // Copy the addOnMap to avoid modifying the original addOnMap
            this.visitedMap = new int[this.addOnMap.length][this.addOnMap[0].length];
            for (int i = 0; i < addOnMap.length; i++) {
                for (int j = 0; j < addOnMap[0].length; j++) {
                    visitedMap[i][j] = -1;
                }
            }
            int initX = addOnRobot.getPos()[0];
            int initY = addOnRobot.getPos()[1];
            //System.out.println("@@ Initial Pos : ["+initX+", "+initY+"]");
            path.add(new int[]{initX, initY});
            visitedMap[initY][initX] = dfsCnt++;
            pathFound=false;
            dfs(initX, initY);
            removeCircle();
        }
        return this.path;
    }

    private void dfs(int x, int y) {
        // Direction arrays for moving up, down, left, right
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};

        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            if (pathFound)
                return;
            // Check if the new position is within the addOnMap bounds
            if (newX >= 0 && newX < addOnMap[0].length && newY >= 0 && newY < addOnMap.length && addOnMap[newY][newX] != 'H' && visitedMap[newY][newX] == -1) {
                // Mark the new position as visited
                if (addOnMap[newY][newX] == 'P') {
                    pathFound = true;
                    visitedMap[newY][newX] = dfsCnt++;
                    path.add(new int[]{newX, newY});
                    return;
                }
                visitedMap[newY][newX] = dfsCnt++;
                path.add(new int[]{newX, newY});
                //this.printTest(newX, newY);
                dfs(newX, newY);
                if (pathFound)
                    break;
            }
        }
        if (pathFound){
            return;
        }
        visitedMap[path.get(path.size() - 1)[1]][path.get(path.size() - 1)[0]] = -1;
        path.remove(path.size() - 1);
        dfsCnt--;
    }
    private void removeCircle(){
        int pathIdx = 0;
        int[][] direction = {{1,0}, {0,1}, {-1,0}, {0,-1}};

        while (path.size() >= 4) {
            int nowX = path.get(pathIdx)[0];
            int nowY = path.get(pathIdx)[1];
            int nowPath = visitedMap[nowY][nowX];
            int maxPathNum = 0;
            int maxPathDistance = 0;
            int maxDirection = 0;
            for(int i = 0; i < 4; i++){
                for(int n = 0; n < path.size()-pathIdx-3; n++){
                    int targetX = nowX + direction[i][0]*(n+1);
                    int targetY = nowY + direction[i][1]*(n+1);
                    if (targetX < 0 | targetY < 0 | targetX > visitedMap[0].length - 1 | targetY > visitedMap.length - 1) {
                        continue;
                    }
                    int targetPath = visitedMap[targetY][targetX];
                    int targetType = addOnMap[targetY][targetX];
                    if(targetType == 'H')
                        break;
                    if (targetPath > nowPath + n + 1 & targetPath > maxPathNum) {
                        maxPathNum = targetPath;
                        maxPathDistance = n+1;
                        maxDirection = i;
                    }
                }
            }
            if (maxPathNum != 0) {
                int removeNum = maxPathNum - nowPath - 1;
                for (int i = 0; i < removeNum; i++) {
                    path.remove(pathIdx +1);
                }
            }
            for(int i = 0; i < addOnMap.length; i++){
                for(int j = 0; j < addOnMap[0].length; j++){
                    visitedMap[i][j] = -1;
                }
            }
            for(int i = 0; i < maxPathDistance; i++){
                path.add(pathIdx+i+1,new int[]{nowX+direction[maxDirection][0]*(i+1),nowY+direction[maxDirection][1]*(i+1)});
            }
            for(int i = 0; i < path.size()-1; i++){
                if(path.get(i)[1]==path.get(i+1)[1]&&path.get(i)[0]==path.get(i+1)[0]){
                    path.remove(i+1);
                }
            }
            for(int i = 0; i < path.size(); i++){
                visitedMap[path.get(i)[1]][path.get(i)[0]] = i;
            }

            if (path.size() - 1 == pathIdx)
                break;
            pathIdx++;
        }
    }
    protected int getPredsNum(){
        return predsNum;
    }
}
