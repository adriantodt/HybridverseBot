package com.theorangehub.hbdvbot.commands.info;

import br.com.brjdevs.java.utils.threads.builder.ThreadBuilder;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CommandStatsManager {
    public static final Map<String, AtomicInteger>
        TOTAL_CMDS = new ConcurrentHashMap<>(),
        DAY_CMDS = new ConcurrentHashMap<>(),
        HOUR_CMDS = new ConcurrentHashMap<>(),
        MINUTE_CMDS = new ConcurrentHashMap<>();
    private static final char ACTIVE_BLOCK = '\u2588';
    private static final char EMPTY_BLOCK = '\u200b';
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor(
        new ThreadBuilder().setName("CommandStatsManager Executor")
    );

    public static String bar(int percent, int total) {
        int activeBlocks = (int) ((float) percent / 100f * total);
        StringBuilder builder = new StringBuilder().append('`').append(EMPTY_BLOCK);
        for (int i = 0; i < total; i++) builder.append(activeBlocks > i ? ACTIVE_BLOCK : ' ');
        return builder.append(EMPTY_BLOCK).append('`').toString();
    }

    public static EmbedBuilder fillEmbed(Map<String, AtomicInteger> commands, EmbedBuilder builder) {
        int total = commands.values().stream().mapToInt(AtomicInteger::get).sum();

        if (total == 0) {
            builder.addField("Nothing Here.", "Just dust.", false);
            return builder;
        }

        commands.entrySet().stream()
            .filter(entry -> entry.getValue().get() > 0)
            .sorted(Comparator.comparingInt(entry -> total - entry.getValue().get()))
            .limit(12)
            .forEachOrdered(entry -> {
                int percent = entry.getValue().get() * 100 / total;
                builder.addField(
                    entry.getKey(), String.format("%s %d%% (%d)", bar(percent, 15), percent, entry.getValue().get()),
                    true
                );
            });

        return builder;
    }

    public static void log(String cmd) {
        if (cmd.isEmpty()) return;

        TOTAL_CMDS.computeIfAbsent(cmd, k -> new AtomicInteger(0)).incrementAndGet();
        DAY_CMDS.computeIfAbsent(cmd, k -> new AtomicInteger(0)).incrementAndGet();
        HOUR_CMDS.computeIfAbsent(cmd, k -> new AtomicInteger(0)).incrementAndGet();
        MINUTE_CMDS.computeIfAbsent(cmd, k -> new AtomicInteger(0)).incrementAndGet();

        EXECUTOR.schedule(() -> MINUTE_CMDS.get(cmd).decrementAndGet(), 1, TimeUnit.MINUTES);
        EXECUTOR.schedule(() -> HOUR_CMDS.get(cmd).decrementAndGet(), 1, TimeUnit.HOURS);
        EXECUTOR.schedule(() -> DAY_CMDS.get(cmd).decrementAndGet(), 1, TimeUnit.DAYS);
    }

    public static String resume(Map<String, AtomicInteger> commands) {
        int total = commands.values().stream().mapToInt(AtomicInteger::get).sum();

        return (total == 0) ? ("No Commands issued.") : ("Count: " + total + "\n" + commands.entrySet().stream()
            .filter(entry -> entry.getValue().get() > 0)
            .sorted(Comparator.comparingInt(entry -> total - entry.getValue().get()))
            .limit(5)
            .map(entry -> {
                int percent = Math.round((float) entry.getValue().get() * 100 / total);
                return String.format(
                    "%s %d%% **%s** (%d)", bar(percent, 15), percent, entry.getKey(), entry.getValue().get());
            })
            .collect(Collectors.joining("\n")));
    }
}
