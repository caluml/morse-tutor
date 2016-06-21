package com.github.caluml.morse;

public class Tone {

    public static final int SAMPLE_RATE = 16 * 1024; // ~16KHz
    public static final int SECONDS = 1;

    private byte[] tone = new byte[SECONDS * SAMPLE_RATE];
    private byte[] silence = new byte[SECONDS * SAMPLE_RATE];

    public Tone(float volume, double frequency) {
        // Generate tone
        for (int i = 0; i < tone.length; i++) {
            double period = (double) SAMPLE_RATE / frequency;
            double angle = 2.0 * Math.PI * i / period;
            tone[i] = (byte) (Math.sin(angle) * volume);
        }
    }

    public byte[] tone() {
        return tone;
    }

    public byte[] silence() {
        return silence;
    }
}
