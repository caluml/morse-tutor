package com.github.caluml.morse;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MorseTutor implements KeyListener {

    private static final int DIT = 50;
    private static final int DAH = DIT * 3;
    private static final int GAP = 50;

    private long start;
    private Character random;
    private SourceDataLine line;

    private java.lang.Object exit = new Object();

    private Map<Character, Integer> wrong = new HashMap<Character, Integer>();
    private Map<Character, ArrayList<Integer>> right = new HashMap<Character, ArrayList<Integer>>();

    public void run() {
        try {
            startGUI();

            final AudioFormat af = new AudioFormat(Note.SAMPLE_RATE, 8, 1, true, true);
            line = AudioSystem.getSourceDataLine(af);
            line.open(af, Note.SAMPLE_RATE);
            line.start();


            random = Symbols.getRandom();
            play(random);
            start = System.currentTimeMillis();

            synchronized (exit) {
                exit.wait();
            }

            line.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        System.out.println("Exiting thread");

        Set<Map.Entry<Character, Integer>> entries = wrong.entrySet();
        for (Map.Entry<Character, Integer> m : entries) {
            System.out.println(m.getKey() + " wrong " + m.getValue() + " times");
        }

        for (Map.Entry<Character, ArrayList<Integer>> charTimings : right.entrySet()) {
            int sum = 0;
            for (int i : charTimings.getValue()) {
                sum = sum + i;
            }
            System.out.println("Average " + charTimings.getKey() + " = " +
                    ((float) sum / charTimings.getValue().size()) + " ms (" + charTimings.getValue().size() + ")");
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

    private void play(char c) {
        String morse = Symbols.get(c);
        for (int i = 0; i < morse.length(); i++) {
            if ('-' == morse.charAt(i)) {
                play(line, Note.A5, DAH);
            } else if ('.' == morse.charAt(i)) {
                play(line, Note.A5, DIT);
            }
            play(line, Note.REST, GAP);
        }
        line.drain();
    }

    private void play(SourceDataLine line, Note note, int ms) {
        ms = Math.min(ms, Note.SECONDS * 1000);
        int length = Note.SAMPLE_RATE * ms / 1000;
        line.write(note.data(), 0, length);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        if (keyEvent.getKeyChar() == ' ') {
            // Repeat
            play(random);
        } else if ((int) keyEvent.getKeyChar() == 27) {
            synchronized (exit) {
                exit.notifyAll();
            }
        } else if (keyEvent.getKeyChar() == random) {
            recordCorrect(random, (System.currentTimeMillis() - start));

            random = Symbols.getRandom();
            play(random);
            start = System.currentTimeMillis();
        } else {
            recordIncorrect(random);
            play(random);
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        // Intentionally blank
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        // Intentionally blank
    }

    private void recordCorrect(Character c, long ms) {
        System.out.println(c + ": " + ms + " ms");
        ArrayList<Integer> list = right.get(c);
        if (list == null) list = new ArrayList<Integer>();
        list.add((int) ms);
        right.put(c, list);
    }

    private void recordIncorrect(Character c) {
        Integer i = wrong.get(c);
        if (i == null) i = 1;
        wrong.put(c, i + 1);
    }
}
