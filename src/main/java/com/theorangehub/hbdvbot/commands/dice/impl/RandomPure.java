package com.theorangehub.hbdvbot.commands.dice.impl;

import com.theorangehub.hbdvbot.commands.dice.DiceEngine;
import com.theorangehub.hbdvbot.commands.dice.impl.base.PureDiceEngine;

import java.util.Random;

public class RandomPure extends PureDiceEngine {
    public RandomPure() {
        super(new Random());
    }

    private RandomPure(Random random) {
        super(random);
    }

    @Override
    public DiceEngine peekable() {
        return new RandomPure(cloneRandom(random));
    }
}
