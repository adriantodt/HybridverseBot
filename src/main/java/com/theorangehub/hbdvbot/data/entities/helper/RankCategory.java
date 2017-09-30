package com.theorangehub.hbdvbot.data.entities.helper;

import com.theorangehub.hbdvbot.utils.HbdvUtils;

public enum RankCategory {
    INEXPERIENTE("Inexperiente"),
    INICIANTE("Iniciante"),
    AMADOR("Amador"),
    INTERMEDIARIO("Intermediário"),
    EXPERIENTE("Experiente"),
    AVANÇADO("Avançado"),
    PROFISSIONAL("Profissional"),
    EXPERT("Expert"),
    PRODIGIO("Prodígio"),
    MESTRE("Mestre"),
    MESTRE_PRODIGO("Mestre Pródigo"),
    GRAO_MESTRE("Grão Mestre"),
    ESPECIALISTA("Especialista"),
    GRAO_ESPECIALISTA("Grão Especialista"),
    MÁXIMO("Máximo");

    private final String nome;

    RankCategory(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }

    public String getNome() {
        return nome;
    }

    public static String display(int lvl) {
        RankCategory[] values = values();
        int length = values.length;
        return values[HbdvUtils.clamp(lvl, 0, length - 1)] + " (" + lvl + "/" + (length - 1) + ")";
    }

}
