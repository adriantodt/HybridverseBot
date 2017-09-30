package com.theorangehub.hbdvbot.commands.dice;

public interface DiceEngine extends Cloneable {
    int roll(int sides);

    DiceEngine peekable();
}
