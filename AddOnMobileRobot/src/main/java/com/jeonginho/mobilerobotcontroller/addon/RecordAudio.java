package com.jeonginho.mobilerobotcontroller.addon;

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

            JOptionPane.showMessageDialog(null, "녹음을 시작합니다.");

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
            JOptionPane.showMessageDialog(null, "녹음을 중지합니다.");

            // 자원 해제
            out.close();
            line.stop();
            line.close();

            // 파일로 저장
            saveToFile(out.toByteArray());

            JOptionPane.showMessageDialog(null, "녹음 파일이 저장되었습니다.");

        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile(byte[] audioData) {
        try {
            // 파일로 저장
            File outputFile = new File("/Users/kkdh15/Desktop/dh/UOS/SE/records/recorded.wav");
            AudioFormat audioFormat = line.getFormat();
            AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(audioData), audioFormat, audioData.length);
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outputFile);

            // 자원 해제
            audioInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        // 녹음할 오디오 포맷 설정
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000, 16, 1, 2, 16000, false);

        RecordAudio recorder = new RecordAudio(format);
        recorder.startRecording();
    }
}
