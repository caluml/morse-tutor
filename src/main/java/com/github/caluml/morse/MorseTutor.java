package com.github.caluml.morse;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class MorseTutor implements KeyListener {

    // https://en.wikipedia.org/wiki/Morse_code#Timing
    private final float dit;       // dit length in milliseconds
    private final float dah;       // dah length in milliseconds
    private final float gap;       // gap length in milliseconds - usually the same as a dit

    private final Tone tone;

    private long start;

    /**
     * The randomly chosen symbol
     */
    private Symbol random;

    private SourceDataLine line;

    private Map<Character, Integer> wrong = new HashMap<Character, Integer>();
    private Map<Character, ArrayList<Integer>> right = new HashMap<Character, ArrayList<Integer>>();

    private int keyPresses = 1;

    public MorseTutor(int dit, Tone tone) {
        this.dit = dit;
        this.dah = dit * 3.0f;
        this.gap = dit;
        this.tone = tone;
    }

    public void run() throws Exception {
        startGUI();

        final AudioFormat af = new AudioFormat(Tone.SAMPLE_RATE, 8, 1, true, true);
        line = AudioSystem.getSourceDataLine(af);
        line.open(af, Tone.SAMPLE_RATE);
        line.start();

        long totalStart = System.currentTimeMillis();
        random = Symbols.getRandom();
        play(random);
        start = System.currentTimeMillis();

        synchronized (this) {
            wait();
        }

        line.close();
        System.out.println("Exiting thread");

        Set<Map.Entry<Character, Integer>> entries = wrong.entrySet();
        for (Map.Entry<Character, Integer> m : entries) {
            System.out.println(m.getKey() + " wrong " + m.getValue() + " times");
        }

        int totalChars = 0;
        for (Map.Entry<Character, ArrayList<Integer>> charTimings : right.entrySet()) {
            int sum = 0;
            for (int i : charTimings.getValue()) {
                sum = sum + i;
                totalChars++;
            }
            System.out.println("Average for " + charTimings.getKey() + ": " +
                    ((float) sum / charTimings.getValue().size()) + " ms (out of " + charTimings.getValue().size() + ")");
        }

        long end = System.currentTimeMillis();
        float minutes = (end - totalStart) / 60000f;
        System.out.println("Start:             " + new Date(totalStart));
        System.out.println("End:               " + new Date(end));
        System.out.println("Elapsed:           " + minutes + " minutes");
        int numRight = getNumRight(right);
        int numWrong = getNumWrong(wrong);
        if (numRight + numWrong > 0) {
            System.out.println("Right:             " + numRight);
            System.out.println("Wrong:             " + numWrong);
            System.out.println("% correct:         " + (100f / (numRight + numWrong)) * numRight);
            System.out.println("Total chars/min:   " + totalChars / minutes);
            System.out.println("Correct chars/min: " + numRight / minutes);
        }

        System.exit(0);
    }

    private void startGUI() {
        JFrame frame = new JFrame("Morse");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(500, 500);

        JTextField typingArea = new JTextField(20);
        typingArea.addKeyListener(this);
        frame.getContentPane().add(typingArea, BorderLayout.PAGE_START);

        frame.pack();
        frame.setVisible(true);
    }

    private void play(Symbol symbol) {
        String morse = symbol.getMorse();
        for (int i = 0; i < morse.length(); i++) {
            if ('-' == morse.charAt(i)) {
                play(line, tone.tone(), dah);
            } else if ('.' == morse.charAt(i)) {
                play(line, tone.tone(), dit);
            }
            play(line, tone.silence(), gap);
        }
        line.drain();
    }

    private void play(SourceDataLine line, byte[] audio, float ms) {
        ms = Math.min(ms, Tone.SECONDS * 1000);
        float length = Tone.SAMPLE_RATE * ms / 1000;
        line.write(audio, 0, (int) length);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        if (keyEvent.getKeyChar() == ' ') {
            // Repeat
            play(random);
        } else if ((int) keyEvent.getKeyChar() == 27) {
            // Escape
            synchronized (this) {
                notifyAll();
            }
        } else if (keyEvent.getKeyChar() == random.getSymbol()) {
            recordCorrect(random, (System.currentTimeMillis() - start));

            random = Symbols.getRandom();
            play(random);
            start = System.currentTimeMillis();
        } else {
            recordIncorrect(random);
            play(random);
        }

        if (keyPresses % 10 == 0) {
            System.out.println("Correct: " + getPercentage() + " %");
        }
        keyPresses++;
    }

    private float getPercentage() {
        int numRight = getNumRight(right);
        int numWrong = getNumWrong(wrong);

        if (numRight + numWrong == 0) return 0.0f;
        return (100f / (numRight + numWrong)) * numRight;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        // Intentionally blank
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        // Intentionally blank
    }

    private void recordCorrect(Symbol symbol, long ms) {
        Symbols.lowerWeighting(symbol);
        ArrayList<Integer> list = right.get(symbol.getSymbol());
        if (list == null) list = new ArrayList<Integer>();
        list.add((int) ms);
        right.put(symbol.getSymbol(), list);
    }

    private void recordIncorrect(Symbol symbol) {
        Symbols.increaseWeighting(symbol);
        Integer i = wrong.get(symbol.getSymbol());
        if (i == null) i = 1;
        wrong.put(symbol.getSymbol(), i + 1);
    }

    private int getNumRight(Map<Character, ArrayList<Integer>> rightMap) {
        int ret = 0;
        for (Map.Entry<Character, ArrayList<Integer>> entry : rightMap.entrySet()) {
            ret = ret + entry.getValue().size();
        }
        return ret;
    }

    private int getNumWrong(Map<Character, Integer> wrongMap) {
        int ret = 0;
        for (Map.Entry<Character, Integer> entry : wrongMap.entrySet()) {
            ret = ret + entry.getValue();
        }
        return ret;
    }

}
