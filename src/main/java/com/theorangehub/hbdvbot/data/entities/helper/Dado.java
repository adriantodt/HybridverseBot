package com.theorangehub.hbdvbot.data.entities.helper;

import com.theorangehub.hbdvbot.commands.tentar.Evaluator;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class Dado {
    private String code;
    private Map<String, Const> consts = new LinkedHashMap<>();
    private Integer min, max;

    public int calculate(Ficha ficha) {
        int x = (int) new Evaluator(String.valueOf(code), DadoHelper.calculateConsts(consts, ficha)).parse();
        if (min != null) x = Math.max(x, min);
        if (max != null) x = Math.min(x, max);
        return x;
    }
}
