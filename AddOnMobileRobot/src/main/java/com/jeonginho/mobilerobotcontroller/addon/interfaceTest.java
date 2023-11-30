package com.jeonginho.mobilerobotcontroller.addon;
//{"colorblob.png", "hazard.png"},
//        {"predefined.png", "robot.png"}


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class interfaceTest extends JFrame {
    private JButton addSpotButton;
    private JLabel[][] imageLabels;
    private ImageIcon robotImage;
    private ImageIcon colorImage;
    private ImageIcon hazardImage;
    private ImageIcon predefinedImage;
    private ImageIcon defaultImage;
    private JPanel tablePanel;

    public interfaceTest(int row, int col) {
        setTitle("ADD-ON");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 이미지 아이콘 생성 (임시 이미지 사용)
        robotImage = new ImageIcon("robot.png"); // 이미지 파일 경로를 넣어주세요
        hazardImage = new ImageIcon("hazard.png");
        colorImage = new ImageIcon("colorblob.png");
        predefinedImage = new ImageIcon("predefined.png");
        defaultImage = new ImageIcon("default.png");

        // 2차원 배열 생성 (예시로 5x5)
        int rows = row;
        int cols = col;
        imageLabels = new JLabel[rows][cols];

        // GUI 컴포넌트 초기화
        initializeComponents();

        // 2차원 배열에 이미지 레이블 추가
        addImageLabels();

        // 프레임의 크기를 이미지 테이블의 크기에 맞게 조절
        pack();

        // 프레임 표시
        setVisible(true);
    }

    private void initializeComponents() {
        addSpotButton = new JButton("AddSpot");

        // AddSpot 버튼 클릭 이벤트 처리
        addSpotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voiceRecog(); // voiceRecog 함수 실행
            }
        });

        // 상단에 표를 표시할 패널 생성
        tablePanel = new JPanel(new GridLayout(imageLabels.length, imageLabels[0].length));
    }

    private void addImageLabels() {
        for (int i = imageLabels.length - 1; i >= 0; i--) {
            for (int j = 0; j < imageLabels[i].length ; j++) {
                JLabel label = new JLabel(defaultImage); // 이미지 아이콘을 가진 JLabel 생성
                if (j==0 && i == 1)
                    label = new JLabel(robotImage);
                if (j==3 && i == 2)
                    label = new JLabel(colorImage);
                label.setHorizontalAlignment(SwingConstants.CENTER); // 이미지 중앙 정렬
                imageLabels[i][j] = label;
                tablePanel.add(imageLabels[i][j]); // 레이블을 패널에 추가
            }
        }
        // 상단에 표를 표시할 패널을 프레임에 추가
        add(tablePanel, BorderLayout.CENTER);

        // 하단에 버튼을 프레임에 추가
        add(addSpotButton, BorderLayout.SOUTH);
    }

    // voiceRecog 함수 (임시로 출력하는 메시지)
    private void voiceRecog() {
        JOptionPane.showMessageDialog(this, "voiceRecog 함수를 실행합니다.");
    }

    public static void main(String[] args) {
        // GUI 생성
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new interfaceTest(3, 4);
            }
        });
    }
}

