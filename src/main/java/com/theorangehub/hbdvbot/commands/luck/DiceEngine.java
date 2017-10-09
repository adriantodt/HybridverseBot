package com.theorangehub.hbdvbot.commands.luck;

public interface DiceEngine extends Cloneable {
    int roll(int sides);

    DiceEngine peekable();
}
