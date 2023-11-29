package com.jeonginho.mobilerobotcontroller.addon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class AddOn {
    char[][] map;
    Robot mobileRobot;
    int spotNum;
    int hazardNum;
    ArrayList<int[]> path;
    public void printMap() {
        for (int i = this.map.length - 1; i >= 0 ; i--) {
            for (int j = 0; j < this.map[i].length; j++) {
                System.out.print(this.map[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void initializeMapToNull() {
        for (char[] chars : map) {
            Arrays.fill(chars, '.');
        }
    }
    public AddOn(int[] size, int[] start, int[][] spot, int[][] hazard) {
        this.map = new char[size[1]+1][size[0]+1];
        initializeMapToNull();
        this.mobileRobot = new Robot(start[0], start[1]);
        this.spotNum = spot.length;
        this.hazardNum = hazard.length;
        map[start[1]][start[0]] = 'R';
        for (int i = 0; i < spotNum; i++)
        {
            int x = spot[i][0];
            int y = spot[i][1];
            map[y][x] = 'S';
        }

        for (int i = 0; i < hazardNum; i++)
        {
            int x = hazard[i][0];
            int y = hazard[i][1];
            map[y][x] = 'H';
        }
    }

    private void dfs(char[][] copyMap, int x, int y, int spotsVisited) {
        // Direction arrays for moving up, down, left, right
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            System.out.println(newX +", "+newY);
            // Check if the new position is within the map bounds
            if (newX >= 0 && newX < copyMap[0].length && newY >= 0 && newY < copyMap.length) {
                // Check if the new position is not a hazard and not visited
                if (copyMap[newY][newX] != 'H' && copyMap[newY][newX] != 'V') {
                    // Mark the new position as visited
                    copyMap[newY][newX] = 'V';
                    path.add(new int[]{newX, newY});
                    if (copyMap[newY][newX] == 'S') {
                        spotsVisited++;
                        if (spotsVisited == spotNum)
                            return ;
                    }
                    System.out.println(newX +", "+newY);
                    dfs(copyMap, newX, newY, spotsVisited);
                }
            }
        }
    }

    public int[][] planPath() {
        // Copy the map to avoid modifying the original map
        char[][] copyMap = Arrays.stream(this.map)
                .map(char[]::clone)
                .toArray(char[][]::new);
        System.out.println("before dfs");
        dfs(copyMap, mobileRobot.x, mobileRobot.y, 0);
        System.out.println("after dfs");
        int[][] result = new int[path.size()][2];
        for (int i = 0; i < path.size(); i++) {
            result[i] = path.get(i);
        }
        return result;
    }

//    public char planPath(Robot mobileRobot, AddOn addOn)
//    {
//
//    }
}
