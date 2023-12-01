package com.jeonginho.mobilerobotcontroller.addon;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

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
    private static int[] convertToCoordinatesArray(String input) {
        input = input.replaceAll("[\\(\\)]", ""); // 괄호 제거
        String[] parts = input.split("\\s+"); // 공백을 기준으로 분리

        int[] coordinates = new int[parts.length];
        try {
            for (int i = 0; i < parts.length; i++) {
                coordinates[i] = Integer.parseInt(parts[i]);
            }
            return coordinates;
        } catch (NumberFormatException | NullPointerException e) {
            // 숫자로 변환할 수 없거나 null인 경우, 유효하지 않은 입력으로 간주
            return null;
        }
    }
    private static int[][] convertToCoordinates2DArray(String input) {
        String[] coordinatePairs = input.split("\\)\\("); // 괄호 사이의 쌍을 분리
        int[][] result = new int[coordinatePairs.length][];

        for (int i = 0; i < coordinatePairs.length; i++) {
            String pair = coordinatePairs[i].replaceAll("[()]", ""); // 괄호 제거
            String[] coordinates = pair.split("\\s+"); // 공백을 기준으로 좌표 분리

            result[i] = new int[coordinates.length];
            try {
                for (int j = 0; j < coordinates.length; j++) {
                    result[i][j] = Integer.parseInt(coordinates[j]);
                }
            } catch (NumberFormatException | NullPointerException e) {
                // 숫자로 변환할 수 없거나 null인 경우, 유효하지 않은 입력으로 간주하고 해당 배열을 null로 설정
                return null;
            }
        }

        return result;
    }


    public static void main(String[] args) {
        initMap interfacE = new initMap();
        JButton submitButton = interfacE.getSubmitBtn();
        if (submitButton != null) {
            // 가져온 버튼이 null이 아닌 경우, ActionListener 등록 또는 기타 동작 수행
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    interfacE.setMapSizeText();
                    interfacE.setStartSpotText();
                    interfacE.setSpotInputText();
                    interfacE.setColorInputText();
                    interfacE.setHazardSpotText();
                    String mapSizeText = interfacE.getMapSizeText();
                    String startSpotText = interfacE.getStartSpotText();
                    String spotInputText = interfacE.getSpotInputText();
                    String colorInputText = interfacE.getColorInputText();
                    String hazardInputText = interfacE.getHazardSpotText();

                    mapSizeText = "(4 5)";
                    startSpotText = "(1 2)";
                    spotInputText = "((4 2)(1 5))";
                    colorInputText = "((0 2)(2 2)(4 4))";
                    hazardInputText = "((1 0)(3 2))";

                    int[] sizeOfMap;
                    int[] start;
                    int[][] predefined;
                    int[][] color;
                    int[][] hazard;

                    sizeOfMap = convertToCoordinatesArray(mapSizeText);
                    start = convertToCoordinatesArray(startSpotText);
                    predefined = convertToCoordinates2DArray(spotInputText);
                    color = convertToCoordinates2DArray(colorInputText);
                    hazard = convertToCoordinates2DArray(hazardInputText);

                    SIM sim = new SIM();
//                    Robot realRobot = new Robot(start[0], start[1]);
                    Map realMap = new Map(sizeOfMap[0], sizeOfMap[1], predefined, hazard, color);
                    AddOn addOn = new AddOn(realMap.getInitialMap(), start, predefined);

                    SwingUtilities.invokeLater(() -> {
//                        interfaceTest userInterface = new interfaceTest(sim, addOn.addOnRobot, realMap, addOn);
                        interfaceTest userInterface = new interfaceTest();
                        userInterface.start(sim, addOn.addOnRobot, realMap, addOn);

                    });
                }
            });
        }
    }

}
