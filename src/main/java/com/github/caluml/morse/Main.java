package com.github.caluml.morse;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: <dit length ms> <vol (0-127)> <freq (Hz)>");
            System.exit(1);
        }

        Float volume = Float.valueOf(args[1]);
        Double frequency = Double.valueOf(args[2]);
        int ditLength = Integer.parseInt(args[0]);

        Tone tone = new Tone(volume, frequency);

        MorseTutor morseTutor = new MorseTutor(ditLength, tone);
        morseTutor.run();
    }
}