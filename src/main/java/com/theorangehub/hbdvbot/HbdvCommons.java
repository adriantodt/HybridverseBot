package com.theorangehub.hbdvbot;

import okhttp3.OkHttpClient;
import us.monoid.web.Resty;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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

    public static final boolean DEV_MODE = !Boolean.parseBoolean("@false@".replace("@", ""));
    public static final Color HBDV_COLOR = new Color(0xA600FF);
    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public static final Random RANDOM = new Random();
    public static final Resty RESTY = new Resty().identifyAsMozilla();

    public static final List<String> SLEEP_QUOTES = Arrays.asList(
        "*vai dormir*",
        "*sonha com dragões*",
        "*guarda as fichas*",
        "*guarda o diário*",
        "*coloca os dados no bolso*",
        "*pega o diário*"
    );

    public static final String VERSION = DEV_MODE ? "DEV" + new SimpleDateFormat("ddMMyyyy").format(new Date()) : "@version@";
}
