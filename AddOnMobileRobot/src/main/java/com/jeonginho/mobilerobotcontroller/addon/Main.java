package com.jeonginho.mobilerobotcontroller.addon;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void printMap(char[][] map) {
        for (int i = map.length - 1; i >= 0 ; i--) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static void print(String type, int[] array) {
        System.out.print(type + "data : ");
        System.out.println("[" + array[0] + ", " + array[1] + "] ");

    }

    public static void print2D(String type, int[][] twoDArray) {
        System.out.print(type + "data : [ ");
        for (int[] row : twoDArray) {
            System.out.print("[" + row[0] + ", " + row[1] + "] ");
        }
        System.out.println("]");
    }

    public static void main(String[] args) {
        int[]   sizeOfMap;
        int[]   start;
        int[][] predefined;
        int[][] hazard;
        int[][] path;

//        map = input("Map");
//        start = input("Start");
//        predefined = input2D("Spot");
//        hazard = input2D("Hazard");
        sizeOfMap = new int[] {5,7};
        start = new int[] {0,0};
        predefined = new int[][] {{4,5}, {3,7}, {4,0}};
        hazard = new int[][] {{1,2}, {2,1}, {2,0}, {3,4}, {1, 0}};

        print("Map", sizeOfMap);
        print("Start", start);
        print2D("Spot", predefined);
        print2D("Hazard", hazard);



        SIM sim = new SIM();
        Robot realRobot = new Robot(start[0], start[1]);
        Map realMap = new Map(sizeOfMap[0], sizeOfMap[1], predefined, hazard);

        AddOn addOn = new AddOn(realMap.getMap(), start, predefined);
        addOn.printMap();

        realMap.updateMap(5,2,'H');

        realMap.updateMap(0,4,'C');

        realMap.updateMap(2,6,'C');
        realMap.updateMap(4,3,'C');
        addOn.planPath(sim, realRobot);
        addOn.testMove(sim,realRobot,realMap);

        System.out.println("===============================\n||    Operation Finished !   ||\n===============================");
    }
}
