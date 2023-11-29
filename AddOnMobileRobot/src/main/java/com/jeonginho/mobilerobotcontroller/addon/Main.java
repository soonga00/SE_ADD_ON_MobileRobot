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
    public static int[] input(String type) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(type + " : ");

        // Read the input line
        String inputLine = scanner.nextLine();
        inputLine = inputLine.replaceAll("[()]", "");

        // Split the input into an array of strings based on commas
        String[] values = inputLine.split(",");

        // Create a one-dimensional array
        int[] array = Arrays.stream(values)
                .mapToInt(Integer::parseInt)
                .toArray();

        return array;
    }
    public static int[][] input2D(String type) {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter a two-dimensional array in the specified format
        System.out.print(type + " : ");

        // Read the input line
        String inputLine = scanner.nextLine();

        // Remove unnecessary characters from the input
        inputLine = inputLine.replaceAll("[()]", "");

        // Split the input line into an array of strings based on spaces
        String[] pairs = inputLine.split("\\s+");

        // Create a list to store pairs
        ArrayList<int[]> pairList = new ArrayList<>();

        // Parse each pair and add it to the list
        for (String pair : pairs) {
            String[] values = pair.split(",");
            if (values.length == 2) {
                try {
                    int x = Integer.parseInt(values[0]);
                    int y = Integer.parseInt(values[1]);
                    pairList.add(new int[]{x, y});
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter valid integer pairs.");
                    return null;
                }
            } else {
                System.out.println("Invalid input. Please enter pairs in the format (x, y).");
                return null;
            }
        }

        // Convert the list to a two-dimensional array
        int[][] twoDArray = new int[pairList.size()][2];
        for (int i = 0; i < pairList.size(); i++) {
            twoDArray[i] = pairList.get(i);
        }

        return twoDArray;
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
        sizeOfMap = new int[] {3,4};
        start = new int[] {0,0};
        predefined = new int[][] {{3,2}, {1,4}};
        hazard = new int[][] {{1,2}, {2,1}, {2,0}, {3,4}, {1, 0}};

        print("Map", sizeOfMap);
        print("Start", start);
        print2D("Spot", predefined);
        print2D("Hazard", hazard);


        AddOn addOn = new AddOn(sizeOfMap, start, predefined, hazard);
        addOn.printMap();

        SIM sim = new SIM();
        Robot realRobot = new Robot(start[0], start[1]);
        Map realMap = new Map(sizeOfMap[0], sizeOfMap[1]);

        for(int i = 0; i < predefined.length; i++)
            realMap.updateMap(predefined[i][0],predefined[i][1], 'P');
        for(int i = 0; i < hazard.length; i++)
            realMap.updateMap(hazard[i][0],hazard[i][1], 'H');
        realMap.printMap();
//        path = addOn.planPath(sim, realRobot);
//        print2D("PATH", path);

    }
}
