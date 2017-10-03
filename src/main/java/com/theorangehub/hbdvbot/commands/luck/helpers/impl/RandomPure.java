package com.theorangehub.hbdvbot.commands.luck.helpers.impl;

import com.theorangehub.hbdvbot.commands.luck.helpers.DiceEngine;
import com.theorangehub.hbdvbot.commands.luck.helpers.impl.base.PureDiceEngine;

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
