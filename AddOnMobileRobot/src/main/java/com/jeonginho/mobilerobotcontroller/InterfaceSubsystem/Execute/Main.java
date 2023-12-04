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
        input = input.replaceAll("[()]", "");
        String[] parts = input.split("\\s+");

        int[] coordinates = new int[parts.length];
        try {
            for (int i = 0; i < parts.length; i++) {
                coordinates[i] = Integer.parseInt(parts[i]);
            }
            return coordinates;
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }
    private static int[][] convertToCoordinates2DArray(String input) {
        String[] coordinatePairs = input.split("\\)\\(");
        int[][] result = new int[coordinatePairs.length][];

        for (int i = 0; i < coordinatePairs.length; i++) {
            String pair = coordinatePairs[i].replaceAll("[()]", "");
            String[] coordinates = pair.split("\\s+");

            result[i] = new int[coordinates.length];
            try {
                for (int j = 0; j < coordinates.length; j++) {
                    result[i][j] = Integer.parseInt(coordinates[j]);
                }
            } catch (NumberFormatException | NullPointerException e) {
                return null;
            }
        }

        return result;
    }


    public static void main(String[] args) {
        init interfacE = new init();
        JButton submitButton = interfacE.getSubmitBtn();
        if (submitButton != null) {
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
                        showUI userInterface = new showUI();
                        userInterface.start(sim, realRobot, realMap, addOn);

                    });
                }
            });
        }
    }

}