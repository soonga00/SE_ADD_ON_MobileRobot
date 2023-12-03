package com.jeonginho.mobilerobotcontroller.InterfaceSubsystem.STT;

import javax.swing.*;
import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ByteArrayInputStream;

public class RecordAudio {
    private final AudioFormat format;
    private TargetDataLine line;

    public RecordAudio(AudioFormat format) {
        this.format = format;
    }

    public void startRecording() {
        try {
            line = AudioSystem.getTargetDataLine(format);
            line.open(format);
            line.start();

            JOptionPane.showMessageDialog(null, "Start Recording...\n녹음을 시작합니다.");

            // 녹음할 시간 (초)
            int recordingTime = 5;

            // 오디오 스트림을 읽어서 바이트 배열로 저장
            byte[] buffer = new byte[line.getBufferSize() / 5];
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            long startTime = System.currentTimeMillis();

            while ((System.currentTimeMillis() - startTime) < recordingTime * 1000) {
                int bytesRead = line.read(buffer, 0, buffer.length);
                out.write(buffer, 0, bytesRead);
            }

            // 녹음 중지
            JOptionPane.showMessageDialog(null, "Recording is stopped...\n녹음이 끝났습니다.");

            // 자원 해제
            out.close();
            line.stop();
            line.close();

            // 파일로 저장
            saveToFile(out.toByteArray());

            JOptionPane.showMessageDialog(null, "Record file is Saved.\n녹음 파일을 저장했습니다.\nPlease wait...\n잠시만 기다려 주세요...");

        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile(byte[] audioData) {
        try {

            File outputFile = new File("records/recorded.wav");
            AudioFormat audioFormat = line.getFormat();
            AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(audioData), audioFormat, audioData.length);
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outputFile);

            // 자원 해제
            audioInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
