package com.jeonginho.mobilerobotcontroller.InterfaceSubsystem.UI;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class init extends JFrame {
    private JTextField mapSize;
    private JTextField startSpot;
    private JTextField spotInput;
    private JTextField colorInput;
    private JTextField hazardSpot;
    private JButton submitBtn;
    private String mapSizeText;
    private String startSpotText;
    private String spotInputText;
    private String colorInputText;
    private String hazardSpotText;
    public init() {
        setTitle("Map Input");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        // "Map" 부분
        JLabel mapLabel = new JLabel("Map");
        add(mapLabel);
        mapSize = new JTextField();
        add(mapSize);

        // "Start" 부분
        JLabel startLabel = new JLabel("Start");
        add(startLabel);
        startSpot = new JTextField();
        add(startSpot);

        // "Spot" 부분
        JLabel spotLabel = new JLabel("Spot");
        add(spotLabel);
        spotInput = new JTextField();
        add(spotInput);

        // "Colorblob"
        JLabel colorLabel = new JLabel("Color");
        add(colorLabel);
        colorInput = new JTextField();
        add(colorInput);

        // "Hazard" 부분
        JLabel hazardLabel = new JLabel("Hazard");
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
    public String getMapSizeText()
    {
        return mapSizeText;
    }
    public void setStartSpotText() { startSpotText = startSpot.getText(); }
    public String getStartSpotText()
    {
        return startSpotText;
    }
    public void setSpotInputText() { spotInputText = spotInput.getText(); }
    public String getSpotInputText()
    {
        return spotInputText;
    }
    public void setColorInputText() { colorInputText = colorInput.getText(); }
    public String getColorInputText()
    {
        return colorInputText;
    }
    public void setHazardSpotText() { hazardSpotText = hazardSpot.getText(); }
    public String getHazardSpotText()
    {
        return hazardSpotText;
    }
    public JButton getSubmitBtn() {
        return submitBtn;
    }

}

