package com.jeonginho.mobilerobotcontroller.InterfaceSubsystem.STT;

import javax.sound.sampled.AudioFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Parsing {
    private List<Integer> posSpot;
    private static String[] inputStr(String input) {
        return input.split("[\\s.]");
    }
    public List<Integer> getSpotPos() { return posSpot; }
    public void addSpot() {
        Map<String, Integer> numdic = new HashMap<>();

        numdic.put("일", 1);
        numdic.put("이", 2);
        numdic.put("삼", 3);
        numdic.put("사", 4);
        numdic.put("오", 5);
        numdic.put("육", 6);
        numdic.put("칠", 7);
        numdic.put("팔", 8);
        numdic.put("구", 9);
        numdic.put("십", 10);
        numdic.put("십일", 11);
        numdic.put("십이", 12);
        numdic.put("십삼", 13);
        numdic.put("십사", 14);
        numdic.put("십오", 15);
        numdic.put("십육", 16);
        numdic.put("십칠", 17);
        numdic.put("십팔", 18);
        numdic.put("십구", 19);
        numdic.put("이십", 20);
        numdic.put("이십일", 21);
        numdic.put("이십이", 22);
        numdic.put("이십삼", 23);
        numdic.put("이십사", 24);
        numdic.put("이십오", 25);
        numdic.put("이십육", 26);
        numdic.put("이십칠", 27);
        numdic.put("이십팔", 28);
        numdic.put("이십구", 29);
        numdic.put("삼십", 30);


        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000, 16, 1, 2, 16000, false);
        RecordAudio record = new RecordAudio(format);
        record.startRecording();

        SpeechToText voiceRec = new SpeechToText();
        String recognized = voiceRec.getRecognizedText();
        String[] arr = inputStr(recognized);
        posSpot = new ArrayList<Integer>();
        for (int i = 0; i < arr.length; i++) {
            if (i == 0) {
                if (arr[i].equals("위험") || arr[i].equals("위엄")) {
                    posSpot.add(0);
                } else if (arr[i].equals("중요") || arr[i].equals("중용") || arr[i].equals("중료") || arr[i].equals("중려")) {
                    posSpot.add(1);
                } else
                    posSpot.add(-1);

            }
            else {
                for (Map.Entry<String, Integer> txt : numdic.entrySet())
                {
                    String key = txt.getKey();
                    Integer value = txt.getValue();
                    if (arr[i].equals(key)) {
                        posSpot.add(value);
                    }
                }
            }
        }
    }
}
