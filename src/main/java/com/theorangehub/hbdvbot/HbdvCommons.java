package com.theorangehub.hbdvbot;

import okhttp3.OkHttpClient;
import us.monoid.web.Resty;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HbdvCommons {
    public static final List<String> BOOT_QUOTES = Arrays.asList(
        "Procurando adversários...",
        "Abrindo o Portal Mágico...",
        "Lendo Fichas...",
        "Criando Multiversos...",
        "Acordando Dragões..."
    );

    public static final Random RANDOM = new Random();
    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public static final Resty RESTY = new Resty().identifyAsMozilla();
    public static final List<String> SLEEP_QUOTES = Arrays.asList(
        "*vai dormir*",
        "*sonha com dragões*",
        "*guarda as fichas*",
        "*guarda o diário*",
        "*coloca os dados no bolso*",
        "*pega o diário*"
    );
}
