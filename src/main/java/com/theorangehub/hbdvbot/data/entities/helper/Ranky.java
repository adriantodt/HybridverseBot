package com.theorangehub.hbdvbot.data.entities.helper;

import com.theorangehub.hbdvbot.utils.HbdvUtils;

import static com.theorangehub.hbdvbot.data.entities.helper.RankCategory.*;

public enum Ranky {
    INEXPERIENTE(RankCategory.INEXPERIENTE, 0),

    INICIANTE_10(INICIANTE, 10), INICIANTE_9(INICIANTE, 9), INICIANTE_8(INICIANTE, 8), INICIANTE_7(INICIANTE, 7),
    INICIANTE_6(INICIANTE, 6), INICIANTE_5(INICIANTE, 5), INICIANTE_4(INICIANTE, 4), INICIANTE_3(INICIANTE, 3),
    INICIANTE_2(INICIANTE, 2), INICIANTE_1(INICIANTE, 1),

    AMADOR_9(AMADOR, 9), AMADOR_8(AMADOR, 8), AMADOR_7(AMADOR, 7), AMADOR_6(AMADOR, 6), AMADOR_5(AMADOR, 5),
    AMADOR_4(AMADOR, 4), AMADOR_3(AMADOR, 3), AMADOR_2(AMADOR, 2), AMADOR_1(AMADOR, 1),

    INTERMEDIARIO_8(INTERMEDIARIO, 8), INTERMEDIARIO_7(INTERMEDIARIO, 7), INTERMEDIARIO_6(INTERMEDIARIO, 6),
    INTERMEDIARIO_5(INTERMEDIARIO, 5), INTERMEDIARIO_4(INTERMEDIARIO, 4), INTERMEDIARIO_3(INTERMEDIARIO, 3),
    INTERMEDIARIO_2(INTERMEDIARIO, 2), INTERMEDIARIO_1(INTERMEDIARIO, 1),

    AVANÇADO_7(AVANÇADO, 7), AVANÇADO_6(AVANÇADO, 6), AVANÇADO_5(AVANÇADO, 5), AVANÇADO_4(AVANÇADO, 4),
    AVANÇADO_3(AVANÇADO, 3), AVANÇADO_2(AVANÇADO, 2), AVANÇADO_1(AVANÇADO, 1),

    EXPERT_6(EXPERT, 6), EXPERT_5(EXPERT, 5), EXPERT_4(EXPERT, 4), EXPERT_3(EXPERT, 3), EXPERT_2(EXPERT, 2),
    EXPERT_1(EXPERT, 1),

    MESTRE_5(MESTRE, 5), MESTRE_4(MESTRE, 4), MESTRE_3(MESTRE, 3), MESTRE_2(MESTRE, 2), MESTRE_1(MESTRE, 1),

    GRAO_MESTRE_4(GRAO_MESTRE, 4), GRAO_MESTRE_3(GRAO_MESTRE, 3), GRAO_MESTRE_2(GRAO_MESTRE, 2),
    GRAO_MESTRE_1(GRAO_MESTRE, 1),

    ESPECIALISTA_3(ESPECIALISTA, 3), ESPECIALISTA_2(ESPECIALISTA, 2), ESPECIALISTA_1(ESPECIALISTA, 1),

    GRAO_ESPECIALISTA_2(GRAO_ESPECIALISTA, 2), GRAO_ESPECIALISTA_1(GRAO_ESPECIALISTA, 1),

    O_MAGO(RankCategory.MÁXIMO, 0);

    public static String display(int lvl) {
        Ranky[] values = values();
        int length = values.length;
        return values[HbdvUtils.clamp(lvl, 0, length - 1)] + " (" + lvl + "/" + (length - 1) + ")";
    }

    private final RankCategory category;
    private final int subCategory;
    private final String toString;

    Ranky(RankCategory category, int subCategory) {
        this.category = category;
        this.subCategory = subCategory;
        //Cached
        this.toString = category.toString() + (subCategory == 0 ? "" : " " + HbdvUtils.toRoman(subCategory));
    }

    public String toString() {
        return toString;
    }

    public RankCategory getCategory() {
        return category;
    }

    public int getSubCategory() {
        return subCategory;
    }
}
