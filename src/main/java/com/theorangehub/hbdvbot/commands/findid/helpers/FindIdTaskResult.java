package com.theorangehub.hbdvbot.commands.findid.helpers;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class FindIdTaskResult {
    private final TObjectIntMap<String> atributos = new TObjectIntHashMap<>();
    private long id, attempt;
}
