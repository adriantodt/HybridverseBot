package com.theorangehub.hbdvbot.commands.luck.helpers.impl.base;

import com.theorangehub.hbdvbot.commands.luck.helpers.DiceEngine;

import java.io.*;
import java.util.Random;

public abstract class PureDiceEngine implements DiceEngine {
    protected final Random random;

    public PureDiceEngine(Random random) {
        this.random = random;
    }

    @Override
    public int roll(int sides) {
        return random.nextInt(sides) + 1;
    }

    protected Random cloneRandom(Random random) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            out.writeObject(random);

            return (Random) new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray())).readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
