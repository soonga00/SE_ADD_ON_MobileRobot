package com.jeonginho.mobilerobotcontroller.InterfaceSubsystem.Execute;

import com.jeonginho.mobilerobotcontroller.AddOnSubsystem.AddOn;
import com.jeonginho.mobilerobotcontroller.EnvironmentSubsystem.RealMap.Map;
import com.jeonginho.mobilerobotcontroller.EnvironmentSubsystem.RealRobot.Robot;
import com.jeonginho.mobilerobotcontroller.EnvironmentSubsystem.Simulator.SIM;
import com.jeonginho.mobilerobotcontroller.InterfaceSubsystem.UI.init;
import com.jeonginho.mobilerobotcontroller.InterfaceSubsystem.UI.showUI;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class Main {
    private static int[] convertToCoordinatesArray(String input) {
        input = input.replaceAll("[()]", ""); // 괄호 제거
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
        init interfacE = new init();
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
                    assert start != null;
                    Robot realRobot = new Robot(start[0], start[1]);
                    assert sizeOfMap != null;
                    Map realMap = new Map(sizeOfMap[0], sizeOfMap[1], predefined, hazard, color);
                    AddOn addOn = new AddOn(realMap.getInitialMap(), start, predefined);

                    SwingUtilities.invokeLater(() -> {
//                        interfaceTest userInterface = new interfaceTest(sim, addOn.addOnRobot, realMap, addOn);
                        showUI userInterface = new showUI();
                        userInterface.start(sim, realRobot, realMap, addOn);

                    });
                }
            });
        }
    }

}