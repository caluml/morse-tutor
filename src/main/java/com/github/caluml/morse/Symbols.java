package com.github.caluml.morse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Symbols {

    // No need for strong randomness...
    private static Random random = new Random(System.currentTimeMillis());

    private static final List<Symbol> symbols = new ArrayList<Symbol>();

    static {
        symbols.add(new Symbol('a', ".-"));
        symbols.add(new Symbol('b', "-..."));
        symbols.add(new Symbol('c', "-.-."));
        symbols.add(new Symbol('d', "-.."));
        symbols.add(new Symbol('e', "."));
        symbols.add(new Symbol('f', "..-."));
        symbols.add(new Symbol('g', "--."));
        symbols.add(new Symbol('h', "...."));
        symbols.add(new Symbol('i', ".."));
        symbols.add(new Symbol('j', ".---"));
        symbols.add(new Symbol('k', "-.-"));
        symbols.add(new Symbol('l', ".-.."));
        symbols.add(new Symbol('m', "--"));
        symbols.add(new Symbol('n', "-."));
        symbols.add(new Symbol('o', "---"));
        symbols.add(new Symbol('p', ".--."));
        symbols.add(new Symbol('q', "--.-"));
        symbols.add(new Symbol('r', ".-."));
        symbols.add(new Symbol('s', "..."));
        symbols.add(new Symbol('t', "-"));
        symbols.add(new Symbol('u', "..-"));
        symbols.add(new Symbol('v', "...-"));
        symbols.add(new Symbol('w', ".--"));
        symbols.add(new Symbol('x', "-..-"));
        symbols.add(new Symbol('y', "-.--"));
        symbols.add(new Symbol('z', "--.."));

        symbols.add(new Symbol('0', "-----"));
        symbols.add(new Symbol('1', ".----"));
        symbols.add(new Symbol('2', "..---"));
        symbols.add(new Symbol('3', "...--"));
        symbols.add(new Symbol('4', "....-"));
        symbols.add(new Symbol('5', "....."));
        symbols.add(new Symbol('6', "-...."));
        symbols.add(new Symbol('7', "--..."));
        symbols.add(new Symbol('8', "---.."));
        symbols.add(new Symbol('9', "----."));

        symbols.add(new Symbol('.', ".-.-.-"));
        symbols.add(new Symbol(',', "--..--"));
        symbols.add(new Symbol('/', "-..-."));
        symbols.add(new Symbol('=', "-...-"));
        symbols.add(new Symbol('?', "..--.."));
    }

    /**
     * Picks a random {@link Symbol}. Symbols with higher weightings will be picked more frequently.
     *
     * @return a random {@link Symbol}
     */
    public static Symbol getRandom() {
        int tot = 0;
        for (Symbol s : symbols) {
            tot = tot + s.getWeighting();
        }

        int r = random.nextInt(tot);

        int cur = 0;
        for (Symbol s : symbols) {
            cur = cur + s.getWeighting();
            if (cur > r) {
                return s;
            }
        }

        throw new RuntimeException(String.format("tot %s, cur %s, r %s", tot, cur, r));
    }

    /**
     * Decreases the weighting for a {@link Symbol}
     *
     * Used when a symbol has been correctly chosen
     * Will not decrease the weighting below 1
     *
     * @param s the {@link Symbol}
     */
    public static void lowerWeighting(Symbol s) {
        for (Symbol symbol : symbols) {
            if (s.equals(symbol)) {
                int newWeighting = symbol.getWeighting() - 1;
                if (newWeighting > 0) {
                    symbol.setWeighting(newWeighting);
                }
                return;
            }
        }
    }

    /**
     * Increases the weighting for a {@link Symbol}
     *
     * Used when a symbol has been incorrectly chosen
     *
     * @param s the {@link Symbol}
     */
    public static void increaseWeighting(Symbol s) {
        for (Symbol symbol : symbols) {
            if (s.equals(symbol)) {
                int newWeighting = symbol.getWeighting() + 5;
                symbol.setWeighting(newWeighting);
                return;
            }
        }
    }
}
