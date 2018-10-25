package com.github.caluml.morse;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.Arrays;
import java.util.List;

public class Random {

  private static final java.util.Random random = new java.util.Random();

  public static void main(String[] args) throws LineUnavailableException {
    if (args.length != 3) {
      System.err.println("Usage: <dit length ms> <vol (0-127)> <freq (Hz)>");
      System.exit(1);
    }

    Float volume = Float.valueOf(args[1]);
    Double frequency = Double.valueOf(args[2]);
    int ditLength = Integer.parseInt(args[0]);

    Tone tone = new Tone(volume, frequency);

    final AudioFormat af = new AudioFormat(Tone.SAMPLE_RATE, 8, 1, true, true);
    SourceDataLine line = AudioSystem.getSourceDataLine(af);
    line.open(af, Tone.SAMPLE_RATE);
    line.start();

    MorseTutor morseTutor = new MorseTutor(ditLength, tone);

    int count = 0;
    int wrong = 0;
    int right = 0;
    while (true) {
      String text = getText();
      String guess = "xx";
      count++;
      while (!text.equals(guess)) {
        final float percent = 100.0F / (wrong + right) * right;
        System.out.println(percent + "%");
        System.out.println(count + "\t" + wrong + "\t" + right + "\t" + percent);
        playCallsign(tone, line, morseTutor, text);
        guess = System.console().readLine();
        wrong++;
      }
      wrong--;
      right++;
    }
  }

  private static String getText() {
    if (random.nextInt(5) == 1) {
      return generateCallsign();
    } else {
      return getRandomText();
    }
  }

  private static String getRandomText() {
    final List<String> strings = Arrays.asList("cq", "dx", "de",
        "qrz", "qth", "qsb", "qrm", "qsl", "qrp", "qrs", "qsy", "qrx", "qrt",
        "cw", "pse", "agn", "tu", "rst", "599", "559", "5nn", "bristol", "wx", "om", "dr", "cuagn", "tnx", "es",
        "ant", "sri", "ssb", "usb", "lsb", "yl", "xyl", "hw cpy?");
//        final List<String> strings = Arrays.asList("hw cpy?");

    return strings.get(random.nextInt(strings.size()));
  }

  private static void playCallsign(Tone tone, SourceDataLine line, MorseTutor morseTutor, String callsign) {
    for (char c : callsign.toCharArray()) {
      if (' ' == c) {
        morseTutor.play(line, tone.silence(), MorseTutor.dah);
      } else {
        final String morse = Symbols.getSymbol(c).getMorse();
        for (int i = 0; i < morse.length(); i++) {
          final char c1 = morse.charAt(i);
          playChar(tone, line, morseTutor, c1);
        }
        morseTutor.play(line, tone.silence(), MorseTutor.dit);
      }
      line.drain();
    }
  }

  private static void playChar(Tone tone, SourceDataLine line, MorseTutor morseTutor, char c1) {
    if ('-' == c1) {
      morseTutor.play(line, tone.tone(), MorseTutor.dah);
    } else if ('.' == c1) {
      morseTutor.play(line, tone.tone(), MorseTutor.dit);
    }
    morseTutor.play(line, tone.silence(), MorseTutor.gap);
  }

  private static String generateCallsign() {
    final List<String> strings = Arrays.asList("x0x",
        "x0xx", "xx0x", "x00x",
        "x0xxx", "xx0xx", "xxx0x", "x00xx", "xx00x",
        "x00xxx", "xx00xx", "xxx00x");

    String format = strings.get(random.nextInt(strings.size()));
    System.err.println(format);

    final StringBuilder ret = new StringBuilder();

    for (int i = 0; i < format.length(); i++) {
      if (format.charAt(i) == 'x') {
        ret.append((char) (97 + random.nextInt(26)));
      } else if (format.charAt(i) == '0') {
        ret.append((char) (48 + random.nextInt(10)));
      }
    }

    return ret.toString();
  }

}
