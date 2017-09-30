package com.theorangehub.hbdvbot.commands;

import br.com.brjdevs.java.utils.strings.StringUtils;
import com.theorangehub.hbdvbot.commands.atrib.Atributo;
import com.theorangehub.hbdvbot.commands.dice.DiceEngine;
import com.theorangehub.hbdvbot.commands.dice.impl.DiceEngines;
import com.theorangehub.hbdvbot.data.entities.helper.Const;
import com.theorangehub.hbdvbot.data.entities.Dados;
import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import com.theorangehub.hbdvbot.modules.CommandRegistry;
import com.theorangehub.hbdvbot.modules.Event;
import com.theorangehub.hbdvbot.modules.Module;
import com.theorangehub.hbdvbot.modules.commands.CommandPermission;
import com.theorangehub.hbdvbot.modules.commands.SimpleCommand;
import com.theorangehub.hbdvbot.utils.DiscordUtils;
import com.theorangehub.hbdvbot.utils.HbdvUtils;
import com.theorangehub.hbdvbot.utils.commands.EmoteReference;
import gnu.trove.list.TIntList;
import gnu.trove.list.linked.TIntLinkedList;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Module
public class LuckCmds {
    public static DiceEngine engine = DiceEngines.RANDOM_MOD;

    @Event
    @SuppressWarnings("Duplicates")
    public static void peek(CommandRegistry registry) {
        registry.register("peek", new SimpleCommand(CommandPermission.OWNER) {
            @Override
            protected void call(GuildMessageReceivedEvent event, String content, String[] args) {
                int sides = 20;
                DiceEngine engine = LuckCmds.engine.peekable();

                TIntList resultados = new TIntLinkedList();
                boolean here = false;
                for (String arg : args) {
                    arg = arg.toLowerCase();

                    if (arg.equalsIgnoreCase("-here")) {
                        here = true;
                    }

                    if (arg.startsWith("d")) {
                        try {
                            sides = Math.max(Integer.parseInt(arg.substring(1)), 2);
                        } catch (Exception ignored) {}
                    } else if (arg.equals("rolar")) {
                        resultados.add(engine.roll(sides));
                    }
                }

                if (resultados.isEmpty()) {
                    resultados.add(engine.roll(sides));
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
        registry.register("dados", new SimpleCommand() {
            @Override
            protected void call(GuildMessageReceivedEvent event, String content, String[] args) {
                int sides = 20;
                DiceEngine engine = LuckCmds.engine;

                TIntList resultados = new TIntLinkedList();
                for (String arg : args) {
                    arg = arg.toLowerCase();

                    if (arg.startsWith("d")) {
                        try {
                            sides = Math.max(Integer.parseInt(arg.substring(1)), 2);
                        } catch (Exception ignored) {}
                    } else if (arg.equals("rolar")) {
                        resultados.add(engine.roll(sides));
                    }
                }

                if (resultados.isEmpty()) {
                    resultados.add(engine.roll(sides));
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

    @Event
    public static void tentar(CommandRegistry registry) {
        //tentar <FICHA> <AÇÃO> [MODIFICADORES...]
        /*
        !tentar David criarmagia
		!tentar Zac receberdano --resistência 8 --dado 12
		!tentar Tatsu sorte set 0
		*/
        registry.register("tentar", new SimpleCommand() {
            private boolean equalsIgnoreCaseAny(String v, String... v2) {
                for (String v1 : v2) if (v.equalsIgnoreCase(v1)) return true;
                return false;
            }

            private void tentar(GuildMessageReceivedEvent event, Ficha ficha, String action, String[] extraArgs) {
                Dados dados = HbdvData.db().getDadosPorId(action);

                if (dados == null) {
                    event.getChannel().sendMessage(
                        EmoteReference.ERROR + "Nenhuma Ação com o Nome/ID de \"" + action + "\""
                    ).queue();

                    return;
                }

                if (extraArgs.length != 0) {
                    for (Entry<String, String> entries : HbdvUtils.parse(extraArgs).entrySet()) {
                        String k = entries.getKey(), v = entries.getValue();

                        if (k == null || v == null) continue;
                        k = k.startsWith("-") ? k.substring(1) : k;

                        //if (equalsIgnoreCaseAny(k, "d", "-d", "dado", "-dado", "dadoinicial", "-dadoinicial", "dadobase", "-dadobase")) {
                        //    try {
                        //        dados.setDadoBase(Integer.parseInt(v));
                        //    } catch (NumberFormatException ignored) {}
                        //}

                        Atributo atributo = null;

                        try {
                            atributo = Atributo.valueOf(k);
                        } catch (IllegalArgumentException ignored) {}

                        if (atributo != null) {
                            try {
                                atributo.set(ficha, Double.parseDouble(v));
                            } catch (NumberFormatException ignored) {}
                        } else {
                            try {
                                dados.getConsts().put(k, new Const().setValue(Double.valueOf(v)).setType("const"));
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }

                TIntList resultados = dados.calculate(ficha);

                event.getChannel().sendMessage(
                    EmoteReference.DICE + "**Resultados**: " + IntStream.of(resultados.toArray())
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(", "))
                ).queue();
            }

            @Override
            protected void call(GuildMessageReceivedEvent event, String content, String[] args) {
                if (args.length < 2) {
                    onHelp(event);
                    return;
                }

                String selector = args[0], action = args[1];
                String[] extraArgs = args.length == 2 ? new String[0] : Arrays.copyOfRange(args, 2, args.length);

                Ficha ficha = HbdvData.db().getFichaPorId(selector);

                if (ficha != null) {
                    tentar(event, ficha, action, extraArgs);
                    return;
                }

                List<Ficha> fichas = HbdvData.db().getFichasPorNome(selector);

                if (fichas.isEmpty()) {
                    event.getChannel().sendMessage(
                        EmoteReference.ERROR + "Nenhum Personagem com o Nome/ID de \"" + selector + "\""
                    ).queue();

                    return;
                }

                if (fichas.size() == 1) {
                    tentar(event, fichas.get(0), action, extraArgs);
                    return;
                }

                DiscordUtils.selectList(event, fichas,
                    Ficha::displayToString,
                    s -> baseEmbed(event, "Selecione a Ficha:").setDescription(s).build(),
                    f -> tentar(event, f, action, extraArgs)
                );
            }

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return null;
            }

            @Override
            protected String[] splitArgs(String content) {
                return StringUtils.efficientSplitArgs(content, 0);
            }
        });
    }
}