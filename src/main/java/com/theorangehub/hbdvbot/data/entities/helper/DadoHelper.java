package com.theorangehub.hbdvbot.data.entities.helper;

import com.theorangehub.hbdvbot.data.entities.Ficha;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.map.hash.TObjectDoubleHashMap;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DadoHelper {
    public static TObjectDoubleMap<String> calculateConsts(Map<String,Const> consts, Ficha ficha) {
        TObjectDoubleMap<String> result = new TObjectDoubleHashMap<>();

        for (Entry<String, Const> entry : consts.entrySet()) {
            String key = entry.getKey();
            Const value = entry.getValue();

            //            if (value.getMin() == null && value.getMax() == null) {
            //                value.setMin(min);
            //                value.setMax(max);
            //            }

            Map<String, Const> mergedConsts = new LinkedHashMap<>(consts);
            mergedConsts.putAll(value.getConsts());
            value.setConsts(mergedConsts);

            result.put(key, value.calculate(ficha));
        }

        return result;
    }
}
