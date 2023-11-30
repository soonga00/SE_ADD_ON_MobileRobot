package com.jeonginho.mobilerobotcontroller.addon;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class initMap extends JFrame {
    private JLabel mapLabel;
    private JTextField mapSize;
    private JLabel startLabel;
    private JTextField startSpot;
    private JLabel spotLabel;
    private JTextField spotInput;
    private JLabel hazardLabel;
    private JTextField hazardSpot;
    private JButton submitBtn;
    private String mapSizeText;
    private String startSpotText;
    private String spotInputText;
    private String hazardSpotText;
    public initMap() {
        setTitle("Map Input");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        // "Map" 부분
        mapLabel = new JLabel("Map");
        add(mapLabel);
        mapSize = new JTextField();
        add(mapSize);

        // "Start" 부분
        startLabel = new JLabel("Start");
        add(startLabel);
        startSpot = new JTextField();
        add(startSpot);

        // "Spot" 부분
        spotLabel = new JLabel("Spot");
        add(spotLabel);
        spotInput = new JTextField();
        add(spotInput);

        // "Hazard" 부분
        hazardLabel = new JLabel("Hazard");
        add(hazardLabel);
        hazardSpot = new JTextField();
        add(hazardSpot);

        // Submit 버튼
        submitBtn = new JButton("Submit");
        add(submitBtn);
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                // interfaceTest 클래스 실행
                SwingUtilities.invokeLater(() -> {
                    new interfaceTest(3, 4);
                });
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setMapSizeText()
    {
        mapSizeText = mapSize.getText();
    }
    public String getMapSizeTextText()
    {
        return mapSizeText;
    }
    public String getStartSpotText()
    {
        return startSpotText;
    }
    public String getSpotInputText()
    {
        return spotInputText;
    }
    public String getHazardSpotText()
    {
        return hazardSpotText;
    }
    public JButton getSubmitBtn() {
        return submitBtn;
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new initMap();
//        });
//    }
}

