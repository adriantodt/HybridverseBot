package com.theorangehub.hbdvbot.commands.luck.helpers.impl;

import com.theorangehub.hbdvbot.commands.luck.helpers.DiceEngine;

import java.util.LinkedHashMap;
import java.util.Map;

public class DiceEngines {

    public static final Map<String, DiceEngine> ENGINES = new LinkedHashMap<>();
    public static final DiceEngine RANDOM_MOD = new RandomMod();
    public static final DiceEngine RANDOM_PURE = new RandomPure();

    static {
        ENGINES.put("rnd", RANDOM_PURE);
        ENGINES.put("rndmod", RANDOM_MOD);
    }
}
