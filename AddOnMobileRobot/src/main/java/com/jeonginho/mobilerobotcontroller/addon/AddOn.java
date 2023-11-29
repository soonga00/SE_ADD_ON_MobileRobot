package com.jeonginho.mobilerobotcontroller.addon;

import java.util.ArrayList;
import java.util.Arrays;

public class AddOn {
    private char[][] AddOnMap;
    private char[][] copyMap;
    private char[][] visitedMap;
    Robot mobileRobot;
    private int spotNum;
    private int hazardNum;
    private int spotsVisited = 0;
    ArrayList<int[]> path = new ArrayList<>();
    public void printMap() {
        for (int i = this.AddOnMap.length - 1; i >= 0 ; i--) {
            for (int j = 0; j < this.AddOnMap[i].length; j++) {
                System.out.print(this.AddOnMap[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void initializeMapToNull() {
        for (char[] chars : AddOn.this.AddOnMap) {
            Arrays.fill(chars, '.');
        }
    }
    public AddOn(int[] size, int[] start, int[][] spot, int[][] hazard) {
        this.AddOnMap = new char[size[1]+1][size[0]+1];
        initializeMapToNull();
        this.mobileRobot = new Robot(start[0], start[1]);
        this.spotNum = spot.length;
        this.hazardNum = hazard.length;
        AddOn.this.AddOnMap[start[1]][start[0]] = 'R';
        for (int i = 0; i < spotNum; i++)
        {
            int x = spot[i][0];
            int y = spot[i][1];
            AddOn.this.AddOnMap[y][x] = 'S';
        }

        for (int i = 0; i < hazardNum; i++)
        {
            int x = hazard[i][0];
            int y = hazard[i][1];
            AddOn.this.AddOnMap[y][x] = 'H';
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
            // Check if the new position is within the AddOnMap bounds
            if (newX >= 0 && newX < copyMap[0].length && newY >= 0 && newY < copyMap.length && copyMap[newY][newX] != 'H' && visitedMap[newY][newX] != 'V') {
                // Mark the new position as visited
                if (copyMap[newY][newX] == 'S') {
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
        if (copyMap[path.get(path.size() - 1)[1]][path.get(path.size() - 1)[0]] == 'S')
                spotsVisited--;
        visitedMap[path.get(path.size() - 1)[1]][path.get(path.size() - 1)[0]] = '.';
        path.remove(path.size() - 1);
    }

    public int[][] planPath() {
        // Copy the AddOnMap to avoid modifying the original AddOnMap
        this.copyMap = Arrays.stream(this.AddOnMap)
                .map(char[]::clone)
                .toArray(char[][]::new);
        this.visitedMap = new char[this.AddOnMap.length][this.AddOnMap[0].length];
        dfs(mobileRobot.getPos()[0], mobileRobot.getPos()[1]);
        int[][] result = new int[path.size()][2];
        for (int i = 0; i < path.size(); i++) {
            result[i] = path.get(i);
        }
        return result;
    }
}