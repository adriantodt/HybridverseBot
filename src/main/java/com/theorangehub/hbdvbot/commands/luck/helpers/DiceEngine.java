package com.theorangehub.hbdvbot.commands.luck.helpers;

public interface DiceEngine extends Cloneable {
    int roll(int sides);

    DiceEngine peekable();
}
