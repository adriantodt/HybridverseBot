package com.theorangehub.hbdvbot.data.entities.helper;

import com.theorangehub.hbdvbot.commands.LuckCmds;
import com.theorangehub.hbdvbot.commands.atrib.Atributo;
import com.theorangehub.hbdvbot.commands.tentar.Evaluator;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class Const {
    private Map<String, Const> consts = new LinkedHashMap<>();
    private Double min, max;
    private String type;
    private Object value;

    public double calculate(Ficha ficha) {
        double x;

        switch (type) {
            case "const": {
                x = ((Number) value).doubleValue();
                break;
            }
            case "field": {
                x = Atributo.valueOf(String.valueOf(value)).get(ficha).doubleValue();
                break;
            }
            case "expr": {
                x = new Evaluator(String.valueOf(value), DadoHelper.calculateConsts(consts, ficha)).parse();
                break;
            }
            case "random": {
                x = LuckCmds.engine.roll(((Number) value).intValue());
                break;
            }
            default: {
                throw new IllegalArgumentException("type: " + type);
            }
        }

        if (min != null) x = Math.max(x, min);
        if (max != null) x = Math.min(x, max);
        return x;
    }
}
