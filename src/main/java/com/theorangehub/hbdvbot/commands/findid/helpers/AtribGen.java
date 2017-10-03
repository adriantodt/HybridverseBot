package com.theorangehub.hbdvbot.commands.findid.helpers;

import lombok.Data;

import java.util.Random;

@Data
public class AtribGen {
    private final String nome;

    public boolean checkBad(int v) {
        return v < 5;
    }

    public boolean checkGood(int v) {
        return v > 7;
    }

    public int gen(Random r) {
        return r.nextInt(10) + 1;
    }
}
