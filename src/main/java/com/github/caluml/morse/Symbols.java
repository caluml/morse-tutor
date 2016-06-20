package com.github.caluml.morse;

import java.util.Random;

public class Symbols {

    // No need for strong randomness..
    private static Random random = new Random(System.currentTimeMillis());

    public static String get(char c) {
        switch (c) {
            case 'a':
                return ".-";
            case 'b':
                return "-...";
            case 'c':
                return "-.-.";
            case 'd':
                return "-..";
            case 'e':
                return ".";
            case 'f':
                return "..-.";
            case 'g':
                return "--.";
            case 'h':
                return "....";
            case 'i':
                return "..";
            case 'j':
                return ".---";
            case 'k':
                return "-.-";
            case 'l':
                return ".-..";
            case 'm':
                return "--";
            case 'n':
                return "-.";
            case 'o':
                return "---";
            case 'p':
                return ".--.";
            case 'q':
                return "--.-";
            case 'r':
                return ".-.";
            case 's':
                return "...";
            case 't':
                return "-";
            case 'u':
                return "..-";
            case 'v':
                return "...-";
            case 'w':
                return ".--";
            case 'x':
                return "-..-";
            case 'y':
                return "-.--";
            case 'z':
                return "--..";
            case '1':
                return ".----";
            case '2':
                return "..---";
            case '3':
                return "...--";
            case '4':
                return "....-";
            case '5':
                return ".....";
            case '6':
                return "-....";
            case '7':
                return "--...";
            case '8':
                return "---..";
            case '9':
                return "----.";
            case '0':
                return "-----";
            case '.':
                return ".-.-.-";
            case ',':
                return "--..--";
            case '?':
                return "..--..";
            case '/':
                return "-..-.";
            case '=':
                return "-...-";
            default:
                throw new RuntimeException("Unknown symbol " + c);
        }
    }

    public static char getRandom() {
        int r = random.nextInt(40);

        if (r < 26) {
            // Letter
            return (char) (r + 97);
        } else if (r < 36) {
            // Number
            return (char) (r + 22);
        } else {
            // punctuation
            switch (r) {
                case 36:
                    return '.';
                case 37:
                    return ',';
                case 38:
                    return '?';
                case 39:
                    return '/';
                case 40:
                    return '=';
            }
            throw new RuntimeException("r = " + r);
        }
    }
}
