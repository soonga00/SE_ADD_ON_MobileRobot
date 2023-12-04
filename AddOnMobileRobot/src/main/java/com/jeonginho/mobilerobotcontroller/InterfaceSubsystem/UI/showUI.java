package com.jeonginho.mobilerobotcontroller.InterfaceSubsystem.UI;


import com.jeonginho.mobilerobotcontroller.AddOnSubsystem.AddOn;
import com.jeonginho.mobilerobotcontroller.EnvironmentSubsystem.RealMap.Map;
import com.jeonginho.mobilerobotcontroller.EnvironmentSubsystem.RealRobot.Robot;
import com.jeonginho.mobilerobotcontroller.EnvironmentSubsystem.Simulator.SIM;
import com.jeonginho.mobilerobotcontroller.InterfaceSubsystem.STT.Parsing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class showUI extends JFrame {
    private static final int LABEL_WIDTH = 100;
    private static final int LABEL_HEIGHT = 100;
    private JLabel[][] imageLabels;
    private JPanel tablePanel;
    private JPanel buttonPanel;
    private Timer timer;
    private boolean isTimerRunning = false;
    private Parsing addSpot;
    private List<Integer> newSpot;

    public void start(SIM sim, Robot robot, Map realMap, AddOn addOn) {
        setTitle("ADD-ON");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        this.addSpot = new Parsing();

        int rows = realMap.getMap().length;
        int cols = realMap.getMap()[0].length;

        imageLabels = new JLabel[rows][cols];

        initializeComponents(sim, realMap, addOn, robot);

        addImageLabels(realMap, addOn, robot);

        pack();

        // 프레임 표시
        setVisible(true);
    }

    private void initializeComponents(SIM sim, Map realMap, AddOn addOn, Robot robot) {
        buttonPanel = new JPanel(new FlowLayout());
        JButton addSpotButton = new JButton("AddSpot");
        JButton startButton = new JButton("Start");

        buttonPanel.add(startButton);
        buttonPanel.add(addSpotButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                run(sim, realMap, addOn, robot); // voiceRecog 함수 실행
            }
        });
        addSpotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopTimer();
                addSpot.addSpot();
                newSpot = addSpot.getSpotPos();
                if (newSpot.get(0) == 0)
                    realMap.updateMap(newSpot.get(1), newSpot.get(2), 'H');
                else if (newSpot.get(0) == 1) {
                    realMap.updateMap(newSpot.get(1), newSpot.get(2), 'C');
                } else if (newSpot.get(0) == -1) {
                    JOptionPane.showMessageDialog(null, "음성인식 실패");

                }
                if (newSpot.get(0) == 0 || newSpot.get(0) == 1) {
                    if (newSpot.get(0) == 0) {
                        String msg = String.format("위험 (%d, %d)", newSpot.get(1), newSpot.get(2));
                        JOptionPane.showMessageDialog(null, msg );
                    }
                    else if (newSpot.get(0) == 1) {
                        String msg = String.format("중요 (%d, %d)", newSpot.get(1), newSpot.get(2));
                        JOptionPane.showMessageDialog(null, msg );
                    }
                    SwingUtilities.invokeLater(() -> {
                        initializeComponents(sim, realMap, addOn, robot);
                        addImageLabels(realMap, addOn, robot);
                        revalidate();
                    });
                }
            }
        });
        tablePanel = new JPanel(new GridLayout(imageLabels.length, imageLabels[0].length));
    }

    private void addImageLabels(Map realMap, AddOn addOn, Robot robot) {

        ImageIcon hazardImage = new ImageIcon("hazard.png");
        ImageIcon colorImage = new ImageIcon("colorblob.png");
        ImageIcon unhazardImage = new ImageIcon("unhazard.png");
        ImageIcon uncolorImage = new ImageIcon("uncolorblob.png");
        ImageIcon predefinedImage = new ImageIcon("predefined.png");
        ImageIcon passedPredefinedImage = new ImageIcon("passedPredefined.png");
        ImageIcon defaultImage = new ImageIcon("default.png");
        ImageIcon pathImage = new ImageIcon("path.png");
        ImageIcon rightImage = new ImageIcon("right.png");
        ImageIcon leftImage = new ImageIcon("left.png");
        ImageIcon upImage = new ImageIcon("up.png");
        ImageIcon downImage = new ImageIcon("down.png");

        int[] robotInfo = robot.getPos();
        int robotPosX = robotInfo[0];
        int robotPosY = robotInfo[1];
        int robotDirection = robotInfo[2];

        char[][] realmap = realMap.getMap();
        char[][] addOnmap = addOn.getMap();
        for (int x = imageLabels.length - 1; x >= 0; x--) {
            for (int y = 0; y < imageLabels[x].length ; y++) {
                JLabel label = new JLabel(defaultImage); // 이미지 아이콘을 가진 JLabel 생성
                label.setBounds(0,0,LABEL_WIDTH,LABEL_HEIGHT);

                if (realmap[x][y] == '.')
                    label = new JLabel(defaultImage);
                if (!addOn.isPathEmpty()) {
                    ArrayList<int[]> curPath = addOn.getPath();
                    for (int k = 0; k < curPath.size(); k++) {
                        if (y == curPath.get(k)[0] && x == curPath.get(k)[1]) {
                            label.setIcon(pathImage);
                            break;
                        }
                    }
                }
                if (robotPosX == y && robotPosY == x) {
                    if (robotDirection == 0)
                        addOverlay(label, upImage);
                    if(robotDirection == 1)
                        addOverlay(label, rightImage);
                    if(robotDirection == 2)
                        addOverlay(label, downImage);
                    if(robotDirection == 3)
                        addOverlay(label, leftImage);
                }
                if (realmap[x][y] == 'P' && addOnmap[x][y] != 'S')
                    addOverlay(label, predefinedImage);
                if (addOnmap[x][y] == 'S')
                    addOverlay(label, passedPredefinedImage);
                if (realmap[x][y] == 'H' && addOnmap[x][y] != 'H')
                    addOverlay(label, unhazardImage);
                if (realmap[x][y] == 'C'&& addOnmap[x][y] != 'C')
                    addOverlay(label, uncolorImage);
                if (addOnmap[x][y] == 'H')
                    addOverlay(label, hazardImage);
                if (addOnmap[x][y] == 'C')
                    addOverlay(label, colorImage);



                label.setHorizontalAlignment(SwingConstants.CENTER);
                imageLabels[x][y] = label;
                tablePanel.add(imageLabels[x][y]);
            }
        }

        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
    }
    private void addOverlay(JLabel label, ImageIcon overlayIcon) {
        JLabel overlayLabel = new JLabel(overlayIcon);
        overlayLabel.setBounds(0, 0, LABEL_WIDTH, LABEL_HEIGHT);
        label.add(overlayLabel, JLayeredPane.PALETTE_LAYER);
    }
    private void startTimer() {
        if (!isTimerRunning) {
            timer.start();
            isTimerRunning = true;
        }
    }
    // 타이머 중지
    private void stopTimer() {
        if (isTimerRunning) {
            timer.stop();
            isTimerRunning = false;
        }
    }
    private void run(SIM sim, Map realMap, AddOn addOn, Robot robot) {
        timer = new Timer(500, new ActionListener() {
            int count = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((!addOn.isVisitedAllPreds()) && count++ < 500) {
                    addOn.orderMovement(sim, robot, realMap);
                    if(addOn.isErrorOccured()){
                        stopTimer();
                        JOptionPane.showMessageDialog(null, "Error: Robot moved twice.");
                        startTimer();
                    }
                    if (addOn.isVisitedAllPreds()){
                        stopTimer();
                        JOptionPane.showMessageDialog(null, "Visited Every Predefined Spot");
                    }

                    SwingUtilities.invokeLater(() -> {
                        initializeComponents(sim, realMap, addOn, robot);
                        addImageLabels(realMap, addOn, robot);
                        revalidate();
                    });
                } else {
                    stopTimer();
                }
            }
        });
        startTimer();
    }

}


