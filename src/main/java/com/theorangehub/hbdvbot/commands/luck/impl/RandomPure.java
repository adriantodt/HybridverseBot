package com.theorangehub.hbdvbot.commands.luck.impl;

import com.theorangehub.hbdvbot.commands.luck.DiceEngine;
import com.theorangehub.hbdvbot.commands.luck.impl.base.PureDiceEngine;

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
