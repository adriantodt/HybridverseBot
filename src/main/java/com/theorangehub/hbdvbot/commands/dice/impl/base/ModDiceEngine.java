package com.theorangehub.hbdvbot.commands.dice.impl.base;

import java.util.Random;

public abstract class ModDiceEngine extends PureDiceEngine {
    protected final Random shadow;

    public ModDiceEngine(Random random, Random shadow) {
        super(random);
        this.shadow = shadow;
    }

    @Override
    public int roll(int sides) {
        return Math.min(random.nextInt(sides) + (sides / 5 == 0 ? 0 : shadow.nextInt(sides / 5)), sides - 1) + 1;
    }

}
