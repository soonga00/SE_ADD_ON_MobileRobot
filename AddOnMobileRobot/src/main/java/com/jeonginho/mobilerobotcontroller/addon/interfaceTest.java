package com.jeonginho.mobilerobotcontroller.addon;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class interfaceTest extends JFrame {
    private JButton addSpotButton;
    private JButton startButton;
    private JLabel[][] imageLabels;
    private ImageIcon robotImage;
    private ImageIcon colorImage;
    private ImageIcon hazardImage;
    private ImageIcon uncolorImage;
    private ImageIcon unhazardImage;
    private ImageIcon predefinedImage;
    private ImageIcon defaultImage;
    private JPanel tablePanel;
    private JPanel buttonPanel;
    private Timer timer;
    private boolean isTimerRunning = false;
    private voiceTest addSpot;
    private List<Integer> newSpot;
    public void start(SIM sim, Robot robot, Map realMap, AddOn addOn) {
        setTitle("ADD-ON");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 이미지 아이콘 생성 (임시 이미지 사용)
        robotImage = new ImageIcon("robot.png"); // 이미지 파일 경로를 넣어주세요
        hazardImage = new ImageIcon("hazard.png");
        colorImage = new ImageIcon("colorblob.png");
        unhazardImage = new ImageIcon("unhazard.png");
        uncolorImage = new ImageIcon("uncolorblob.png");
        predefinedImage = new ImageIcon("predefined.png");
        defaultImage = new ImageIcon("default.png");

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
//    public interfaceTest(SIM sim, Robot robot, Map map, AddOn addOn) {
//        setTitle("ADD-ON");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        // 이미지 아이콘 생성 (임시 이미지 사용)
//        robotImage = new ImageIcon("robot.png"); // 이미지 파일 경로를 넣어주세요
//        hazardImage = new ImageIcon("hazard.png");
//        colorImage = new ImageIcon("colorblob.png");
//        predefinedImage = new ImageIcon("predefined.png");
//        defaultImage = new ImageIcon("default.png");
//
//        this.sim = sim;
//        this.robot = robot;
//        this.map = map;
//        this.addOn = addOn;
//        this.realMap = this.map.getMap();
//        this.addSpot = new voiceTest();
//
//        int rows = realMap.length;
//        int cols = realMap[0].length;
//
//        imageLabels = new JLabel[rows][cols];
//
//        // GUI 컴포넌트 초기화
//        initializeComponents();
//
//        // 2차원 배열에 이미지 레이블 추가
//        addImageLabels();
//
//        // 프레임의 크기를 이미지 테이블의 크기에 맞게 조절
//        pack();
//
//        // 프레임 표시
//        setVisible(true);
//    }

    private void initializeComponents(SIM sim, Map realMap, AddOn addOn, Robot robot) {
        buttonPanel = new JPanel(new FlowLayout());
        addSpotButton = new JButton("AddSpot");
        startButton = new JButton("Start");

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
                if (realmap[x][y] == '.')
                    label = new JLabel(defaultImage);
                if (realmap[x][y] == 'P')
                    label = new JLabel(predefinedImage);
                if (realmap[x][y] == 'H')
                    label = new JLabel(unhazardImage);
                if (realmap[x][y] == 'C')
                    label = new JLabel(uncolorImage);
                if (addOnmap[x][y] == 'H')
                    label = new JLabel(hazardImage);
                if (addOnmap[x][y] == 'C')
                    label = new JLabel(colorImage);
                if (robotPosX == y && robotPosY == x)
                    label = new JLabel(robotImage);
                if(addOn.path != null)
                    for(int k = 0; k < addOn.path.size(); k++) {
                        if (y == addOn.path.get(k)[0] && x == addOn.path.get(k)[1])
                            label = new JLabel(unhazardImage);
                    }
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
    // 이미지를 회전하는 메서드
//    private static Image rotateImage(Image image, int angle) {
//        BufferedImage rotatedBufferedImage = new BufferedImage(
//                image.getWidth(null),
//                image.getHeight(null),
//                BufferedImage.TYPE_INT_ARGB);
//
//        // 회전을 위한 AffineTransform 객체 생성
//        AffineTransform transform = new AffineTransform();
//        transform.rotate(Math.toRadians(angle), image.getWidth(null) / 2.0, image.getHeight(null) / 2.0);
//
//        // 이미지 회전
//        Graphics2D g2d = rotatedBufferedImage.createGraphics();
//        g2d.setTransform(transform);
//        g2d.drawImage(image, 0, 0, null);
//        g2d.dispose();
//
//        return rotatedBufferedImage;
//    }
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
        addOn.planPath(sim, robot);
        timer = new Timer(1000, new ActionListener() {
            int count = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((addOn.predsNum != 0) && count++ < 500) {
                    addOn.orderMovement(sim, robot, realMap);
                    if (addOn.path.isEmpty()) {
                        addOn.planPath(sim, robot);
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


