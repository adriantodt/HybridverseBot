package com.theorangehub.hbdvbot.commands.dice.impl;

import com.theorangehub.hbdvbot.commands.dice.DiceEngine;
import com.theorangehub.hbdvbot.commands.dice.impl.base.ModDiceEngine;

import java.util.Random;

public class RandomMod extends ModDiceEngine {
    public RandomMod() {
        super(new Random(), new Random());
    }

    private RandomMod(Random random, Random shadow) {
        super(random, shadow);
    }

    @Override
    public DiceEngine peekable() {
        return new RandomMod(cloneRandom(random), cloneRandom(shadow));
    }
}
