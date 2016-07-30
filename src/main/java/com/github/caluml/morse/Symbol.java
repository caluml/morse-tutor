package com.github.caluml.morse;

/**
 * Class to represent a Morse code symbol.
 */
public class Symbol {

    /**
     * The symbol, e.g. a, 7, or ?
     */
    private final char symbol;

    /**
     * The morse for the character, e.g. -.-
     */
    private final String morse;

    /**
     * The weighting of the character. A higher number will come up more often.
     */
    private int weighting = 4;

    /**
     * Creates a new {@link Symbol} with a default weighting
     * @param symbol the symbol
     * @param morse the morse
     */
    public Symbol(char symbol, String morse) {
        this.symbol = symbol;
        this.morse = morse;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getMorse() {
        return morse;
    }

    public int getWeighting() {
        return weighting;
    }

    public void setWeighting(int weighting) {
        this.weighting = weighting;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Symbol symbol1 = (Symbol) o;

        if (symbol != symbol1.symbol) return false;
        if (weighting != symbol1.weighting) return false;
        return morse != null ? morse.equals(symbol1.morse) : symbol1.morse == null;

    }

    @Override
    public int hashCode() {
        int result = (int) symbol;
        result = 31 * result + (morse != null ? morse.hashCode() : 0);
        result = 31 * result + weighting;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Symbol{");
        sb.append("symbol=").append(symbol);
        sb.append(", morse='").append(morse).append('\'');
        sb.append(", weighting=").append(weighting);
        sb.append('}');
        return sb.toString();
    }
}
