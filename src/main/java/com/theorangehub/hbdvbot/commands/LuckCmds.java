package com.theorangehub.hbdvbot.commands;

import com.theorangehub.hbdvbot.commands.luck.DiceEngine;
import com.theorangehub.hbdvbot.commands.luck.impl.DiceEngines;
import com.theorangehub.hbdvbot.modules.CommandRegistry;
import com.theorangehub.hbdvbot.modules.Event;
import com.theorangehub.hbdvbot.modules.Module;
import com.theorangehub.hbdvbot.modules.commands.ArgsCommand;
import com.theorangehub.hbdvbot.modules.commands.CommandPermission;
import com.theorangehub.hbdvbot.utils.commands.EmoteReference;
import gnu.trove.list.TIntList;
import gnu.trove.list.linked.TIntLinkedList;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Module
public class LuckCmds {
    private static final Pattern DICE = Pattern.compile("\\d+d\\d+");
    public static DiceEngine engine = DiceEngines.RANDOM_MOD;

    @Event
    @SuppressWarnings("Duplicates")
    public static void peek(CommandRegistry registry) {
        registry.register("peek", new ArgsCommand(CommandPermission.OWNER) {

            @Override
            protected void call(GuildMessageReceivedEvent event, String content, String[] args) {
                DiceEngine engine = LuckCmds.engine.peekable();

                TIntList resultados = new TIntLinkedList();
                boolean here = false;
                for (String arg : args) {
                    arg = arg.toLowerCase();

                    if (arg.equals("-here")) {
                        here = true;
                    }

                    if (arg.startsWith("d")) {
                        try {
                            resultados.add(engine.roll(Math.max(Integer.parseInt(arg.substring(1)), 2)));
                        } catch (Exception ignored) {}
                    }

                    if (DICE.matcher(arg).matches()) {
                        try {
                            int d = arg.indexOf('d');

                            int amount = Integer.parseInt(arg.substring(0, d));
                            int sides = Math.max(Integer.parseInt(arg.substring(d + 1)), 2);

                            for (int i = 0; i < amount; i++) {
                                resultados.add(engine.roll(sides));
                            }
                        } catch (Exception ignored) {}
                    }
                }

                if (resultados.isEmpty()) {
                    resultados.add(engine.roll(20));
                }

                (here ? event.getChannel() : event.getAuthor().openPrivateChannel().complete()).sendMessage(
                    EmoteReference.DICE + "**Resultados Futuros**: " + IntStream.of(resultados.toArray())
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(", "))
                ).queue();

                if (!here) {
                    event.getMessage().addReaction("✅").queue();
                }
            }

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return null;
            }
        });
    }

    @Event
    @SuppressWarnings("Duplicates")
    public static void roll(CommandRegistry registry) {
        registry.register("dados", new ArgsCommand() {
            @Override
            protected void call(GuildMessageReceivedEvent event, String content, String[] args) {
                DiceEngine engine = LuckCmds.engine;

                TIntList resultados = new TIntLinkedList();
                for (String arg : args) {
                    arg = arg.toLowerCase();

                    if (arg.startsWith("d")) {
                        try {
                            resultados.add(engine.roll(Math.max(Integer.parseInt(arg.substring(1)), 2)));
                        } catch (Exception ignored) {}
                    }

                    if (DICE.matcher(arg).matches()) {
                        try {
                            int d = arg.indexOf('d');

                            int amount = Integer.parseInt(arg.substring(0, d));
                            int sides = Math.max(Integer.parseInt(arg.substring(d + 1)), 2);

                            for (int i = 0; i < amount; i++) {
                                resultados.add(engine.roll(sides));
                            }
                        } catch (Exception ignored) {}
                    }
                }

                if (resultados.isEmpty()) {
                    resultados.add(engine.roll(20));
                }

                event.getChannel().sendMessage(
                    EmoteReference.DICE + "**Resultados**: " + IntStream.of(resultados.toArray())
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(", "))
                ).queue();
            }

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return null;
            }
        });
    }
}
