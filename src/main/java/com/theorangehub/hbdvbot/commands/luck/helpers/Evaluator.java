package com.theorangehub.hbdvbot.commands.luck.helpers;

import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.map.hash.TObjectDoubleHashMap;

public class Evaluator {
    private final String str;
    private final TObjectDoubleMap<String> consts;
    private int pos = -1, ch;

    public Evaluator(String str, TObjectDoubleMap<String> consts) {
        this.str = str;
        this.consts = consts;
    }

    public Evaluator(String str) {
        this(str, new TObjectDoubleHashMap<>());
    }

    private void nextChar() {
        ch = (++pos < str.length()) ? str.charAt(pos) : -1;
    }

    private boolean eat(int charToEat) {
        while (ch == ' ') nextChar();

        if (ch == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }

    public double parse() {
        nextChar();
        double x = parseExpr();
        if (pos < str.length()) throw new RuntimeException("Unexpected: " + ((char) ch));
        return x;
    }

    private double parseExpr() {
        double x = parseTerm();
        for(;;) {
            if (eat('+')) x += parseTerm();
            else if (eat('-')) x -= parseTerm();
            else return x;
        }
    }

    private double parseTerm() {
        double x = parseFactor();
        for(;;) {
            if (eat('*')) x *= parseFactor();
            else if (eat('/')) x /= parseFactor();
            else return x;
        }
    }

    private double parseFactor() {
        if (eat('+')) return parseFactor();
        if (eat('-')) return -parseFactor();

        double x;
        int startPos = this.pos;
        if (eat('(')) {
            x = parseExpr();
            eat(')');
        } else if ((ch >= '0' && ch <= '9') || ch == '.') {
            while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
            x = Double.parseDouble(str.substring(startPos, this.pos));
        } else if (ch >= 'a' && ch <= 'z') {
            while ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')) nextChar();
            String key = str.substring(startPos, this.pos);
            if (!consts.containsKey(key)) throw new RuntimeException("Unknown constant: " + key);
            x = consts.get(key);
        } else {
            throw new RuntimeException("Unexpected: " + ((char) ch));
        }

        if (eat('^')) x = Math.pow(x, parseFactor());

        return x;
    }
}
