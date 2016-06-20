package com.github.caluml.morse;

public class Main {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: <dit length ms> <vol (0-127)> <freq (Hz)>");
            System.exit(1);
        }

        Tone tone = new Tone(Float.valueOf(args[1]), Double.valueOf(args[2]));

        MorseTutor morseTutor = new MorseTutor(Integer.parseInt(args[0]), tone);

        morseTutor.run();
    }
}