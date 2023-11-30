package com.jeonginho.mobilerobotcontroller.addon;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
public class Main {
    public static int[] input(String type, String inputLine) {
        inputLine = inputLine.replaceAll("[()]", "");

        // Split the input into an array of strings based on commas
        String[] values = inputLine.split(",");

        // Create a one-dimensional array
        int[] array = Arrays.stream(values)
                .mapToInt(Integer::parseInt)
                .toArray();

        return array;
    }
    public static int[][] input2D(String type, String inputLine) {
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

        initMap interfacE = new initMap();

        JButton submitButton = interfacE.getSubmitBtn();

        if (submitButton != null) {
            // 가져온 버튼이 null이 아닌 경우, ActionListener 등록 또는 기타 동작 수행
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    interfacE.setMapSizeText();
                    String mapSizeText = interfacE.getMapSizeTextText();
                    System.out.println("Map Size: " + mapSizeText);
                }
            });
        } else {
            System.out.println("버튼이 null입니다. 버튼 초기화 코드를 확인해주세요.");
        }
//        initMap interfacE = new initMap();

//        System.out.println(interfacE.getMapSizeText());
//        sizeOfMap = input("Map", interfacE.getMapSizeText());
//        start = input("Start", interfacE.getStartSpotText());
//        predefined = input2D("Spot", interfacE.getSpotInputText());
//        hazard = input2D("Hazard", interfacE.getHazardSpotText());

//        sizeOfMap = new int[] {3,4};
//        start = new int[] {0,0};
//        predefined = new int[][] {{3,2}, {1,4}};
//        hazard = new int[][] {{1,2}, {2,1}, {2,0}, {3,4}, {1, 0}};

//        print("Map", sizeOfMap);
//        print("Start", start);
//        print2D("Spot", predefined);
//        print2D("Hazard", hazard);



//        SIM sim = new SIM();
//        Robot realRobot = new Robot(start[0], start[1]);
//        Map realMap = new Map(sizeOfMap[0], sizeOfMap[1], predefined, hazard);
//        printMap(realMap.getMap());
//        AddOn addOn = new AddOn(realMap.getMap(), start, predefined);
////        path = addOn.planPath(sim, realRobot);
////        print2D("PATH", path);
//        addOn.planPath();
//
//        addOn.testMove(sim,realRobot,realMap);
    }
}
