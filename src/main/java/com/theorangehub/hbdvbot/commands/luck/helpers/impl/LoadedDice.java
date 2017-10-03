package com.theorangehub.hbdvbot.commands.luck.helpers.impl;

import com.theorangehub.hbdvbot.commands.luck.helpers.DiceEngine;
import com.theorangehub.hbdvbot.utils.HbdvUtils;
import gnu.trove.list.TIntList;
import gnu.trove.list.linked.TIntLinkedList;
import lombok.Getter;
import lombok.Setter;

public class LoadedDice implements DiceEngine {
    @Getter
    @Setter
    private DiceEngine parent;
    private TIntList predictions = new TIntLinkedList();

    @Override
    public int roll(int sides) {
        if (!predictions.isEmpty()) return HbdvUtils.clamp(predictions.removeAt(0), 1, sides);
        return parent.roll(sides);
    }

    @Override
    public DiceEngine peekable() {
        LoadedDice peek = new LoadedDice();

        peek.setParent(parent.peekable());
        peek.predictions = new TIntLinkedList(predictions);

        return peek;
    }

    public void nextPredictions(int... values) {
        predictions.add(values);
    }
}
