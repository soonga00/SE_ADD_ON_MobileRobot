package com.jeonginho.mobilerobotcontroller.addon;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class interfaceTest extends JFrame {
    private static final int LABEL_WIDTH = 100; // 레이블 너비
    private static final int LABEL_HEIGHT = 100; // 레이블 높이
    private JLabel[][] imageLabels;
    private ImageIcon colorImage;
    private ImageIcon hazardImage;
    private ImageIcon uncolorImage;
    private ImageIcon unhazardImage;
    private ImageIcon predefinedImage;
    private ImageIcon defaultImage;
    private ImageIcon pathImage;
    private ImageIcon rightImage;
    private ImageIcon leftImage;
    private ImageIcon upImage;
    private ImageIcon downImage;
    private JPanel tablePanel;
    private JPanel buttonPanel;
    private Timer timer;
    private boolean isTimerRunning = false;
    private voiceTest addSpot;
    private List<Integer> newSpot;

    public void start(SIM sim, Robot robot, Map realMap, AddOn addOn) {
        setTitle("ADD-ON");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        hazardImage = new ImageIcon("hazard.png");
        colorImage = new ImageIcon("colorblob.png");
        unhazardImage = new ImageIcon("unhazard.png");
        uncolorImage = new ImageIcon("uncolorblob.png");
        predefinedImage = new ImageIcon("predefined.png");
        defaultImage = new ImageIcon("default.png");
        pathImage = new ImageIcon("path.png");
        rightImage = new ImageIcon("right.png");
        leftImage = new ImageIcon("left.png");
        upImage = new ImageIcon("up.png");
        downImage = new ImageIcon("down.png");

        this.addSpot = new voiceTest();

        int rows = realMap.getMap().length;
        int cols = realMap.getMap()[0].length;

        imageLabels = new JLabel[rows][cols];

        // GUI 컴포넌트 초기화
        initializeComponents(sim, realMap, addOn, robot);

        // 2차원 배열에 이미지 레이블 추가
        addImageLabels(realMap, addOn, robot);

        // 프레임의 크기를 이미지 테이블의 크기에 맞게 조절
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
        // AddSpot 버튼 클릭 이벤트 처리
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
        // 상단에 표를 표시할 패널 생성
        tablePanel = new JPanel(new GridLayout(imageLabels.length, imageLabels[0].length));
    }

    private void addImageLabels(Map realMap, AddOn addOn, Robot robot) {
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
                if (addOn.path != null) {
                    for (int k = 0; k < addOn.path.size(); k++) {
                        if (y == addOn.path.get(k)[0] && x == addOn.path.get(k)[1]) {
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
                if (realmap[x][y] == 'P')
                    addOverlay(label, predefinedImage);
                if (realmap[x][y] == 'H' && addOnmap[x][y] != 'H')
                    addOverlay(label, unhazardImage);
                if (realmap[x][y] == 'C'&& addOnmap[x][y] != 'C')
                    addOverlay(label, uncolorImage);
                if (addOnmap[x][y] == 'H')
                    addOverlay(label, hazardImage);
                if (addOnmap[x][y] == 'C')
                    addOverlay(label, colorImage);



                label.setHorizontalAlignment(SwingConstants.CENTER); // 이미지 중앙 정렬
                imageLabels[x][y] = label;
                tablePanel.add(imageLabels[x][y]); // 레이블을 패널에 추가
            }
        }

        // 상단에 표를 표시할 패널을 프레임에 추가
        add(tablePanel, BorderLayout.CENTER);

        // 하단에 버튼을 프레임에 추가
        add(buttonPanel, BorderLayout.SOUTH);
    }
    private void addOverlay(JLabel label, ImageIcon overlayIcon) {
        JLabel overlayLabel = new JLabel(overlayIcon);
        overlayLabel.setBounds(0, 0, LABEL_WIDTH, LABEL_HEIGHT);
        label.add(overlayLabel, JLayeredPane.PALETTE_LAYER);
    }
    // 타이머 시작
    private void startTimer() {
        if (!isTimerRunning) { // 타이머가 이미 실행 중이 아니라면 시작
            timer.start();
            isTimerRunning = true; // 타이머 동작 중임을 표시
        }
    }
    // 타이머 중지
    private void stopTimer() {
        if (isTimerRunning) { // 타이머가 동작 중이라면 중지
            timer.stop();
            isTimerRunning = false; // 타이머 동작 중이 아님을 표시
        }
    }
    private void run(SIM sim, Map realMap, AddOn addOn, Robot robot) {
        addOn.planPath();
        timer = new Timer(500, new ActionListener() {
            int count = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((addOn.predsNum != 0) && count++ < 500) {
                    addOn.orderMovement(sim, robot, realMap);
                    if (addOn.path.isEmpty()) {
                        addOn.planPath();
                        if (addOn.predsNum ==0)
                            stopTimer();
                    }
                    SwingUtilities.invokeLater(() -> {
                        initializeComponents(sim, realMap, addOn, robot);
                        addImageLabels(realMap, addOn, robot);
                        revalidate();
                    });
                } else {
                    stopTimer(); // 타이머 중지
                }
            }
        });
        startTimer(); // 타이머 시작
    }
}


