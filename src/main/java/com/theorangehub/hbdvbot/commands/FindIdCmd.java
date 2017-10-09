package com.theorangehub.hbdvbot.commands;

import br.com.brjdevs.java.utils.threads.builder.ThreadBuilder;
import com.theorangehub.hbdvbot.commands.findid.AtribGen;
import com.theorangehub.hbdvbot.commands.findid.FindIdTask;
import com.theorangehub.hbdvbot.commands.findid.FindIdTaskResult;
import com.theorangehub.hbdvbot.modules.Command;
import com.theorangehub.hbdvbot.modules.commands.ArgsCommand;
import com.theorangehub.hbdvbot.utils.commands.EmoteReference;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

@Command("findid")
public class FindIdCmd extends ArgsCommand {
    private static Map<String, AtribGen> atributos = new LinkedHashMap<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(5, new ThreadBuilder().setName("IDFinder-Service-%d"));

    static {
        atributos.put("força", new AtribGen("Força"));
        atributos.put("resistência", new AtribGen("Resistência"));
        atributos.put("magia", new AtribGen("Magia"));
        atributos.put("máximoDeMana", new AtribGen("Máximo de Mana") {
            @Override
            public boolean checkBad(int v) {
                return v < 150;
            }

            @Override
            public int gen(Random r) {
                return (r.nextInt(50) + 1) * 10;
            }

            @Override
            public boolean checkGood(int v) {
                return v > 350;
            }

        });
        atributos.put("destreza", new AtribGen("Destreza"));
        atributos.put("técnicas", new AtribGen("Técnicas"));
        atributos.put("carisma", new AtribGen("Carisma"));
        atributos.put("inteligência", new AtribGen("Inteligência"));
        atributos.put("sorte", new AtribGen("Sorte"));
        atributos.put("pontosDeVida", new AtribGen("Pontos de Vida") {
            @Override
            public int gen(Random r) {
                return r.nextInt(40) + 11;
            }

            @Override
            public boolean checkGood(int v) {
                return v > 35;
            }

            @Override
            public boolean checkBad(int v) {
                return v < 20;
            }
        });
        atributos.put("dinheiro", new AtribGen("Magnuns") {
            @Override
            public int gen(Random r) {
                return (r.nextInt(2000) + 1) * 10;
            }

            @Override
            public boolean checkGood(int v) {
                return v > 11000;
            }

            @Override
            public boolean checkBad(int v) {
                return v < 5000;
            }
        });
    }

    @Override
    protected void call(GuildMessageReceivedEvent event, String content, String[] args) {
        Set<String> atribsPositivos = new LinkedHashSet<>();
        Set<String> atribsNegativos = new LinkedHashSet<>();
        Set<String> allAtribs = atributos.keySet();
        TObjectIntMap<String> atribsExatos = new TObjectIntHashMap<>();

        for (String arg : args) {
            if (arg.startsWith("-")) {
                String arg1 = arg.substring(1);

                if (allAtribs.contains(arg1)) {
                    atribsNegativos.add(arg1);
                    continue;
                }
            }

            if (arg.startsWith("+")) {
                String arg1 = arg.substring(1);

                if (allAtribs.contains(arg1)) {
                    atribsPositivos.add(arg1);
                    continue;
                }
            }

            int equals = arg.indexOf('=');
            if (equals != -1) {
                String k = arg.substring(0, equals);
                String v = arg.substring(equals + 1);

                if (allAtribs.contains(k)) {
                    try {
                        atribsExatos.put(k, Integer.valueOf(v));
                    } catch (NumberFormatException ignored) {
                        ignored.printStackTrace();
                    }
                    continue;
                }
            }

            if (allAtribs.contains(arg)) {
                atribsPositivos.add(arg);
                continue;
            }
        }

        atribsNegativos.removeAll(atribsPositivos);

        Future<FindIdTaskResult> task = pool.submit(
            FindIdTask.builder()
                .atributos(atributos)
                .atribsExatos(atribsExatos)
                .atribsNegativos(atribsNegativos)
                .atribsPositivos(atribsPositivos)
                .build()
        );

        FindIdTaskResult result;

        long millis = -System.currentTimeMillis();
        try {
            result = task.get(15, TimeUnit.SECONDS);
            millis += System.currentTimeMillis();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            task.cancel(true);
            event.getChannel().sendMessage(EmoteReference.ERROR + "Nenhuma ficha com tais valores encontrada a tempo. (Tente de novo)").queue();
            return;
        }

        EmbedBuilder embed = baseEmbed(event, "Ficha Encontrada!")
            .setFooter(
                "Tentativa #" + result.attempt() + " / Encontrado em " + millis + "ms / Requerido por " + event.getMember().getEffectiveName()
                , event.getAuthor().getEffectiveAvatarUrl()
            );

        StringJoiner j = new StringJoiner("\n");

        j.add("**ID**: " + result.id() + "\n");

        for (Entry<String, AtribGen> entry : atributos.entrySet()) {
            String k = entry.getKey();
            AtribGen v = entry.getValue();

            j.add("**" + v.getNome() + "**: " + result.atributos().get(k));
        }

        event.getChannel().sendMessage(embed.setDescription(j.toString()).build()).queue();
    }

    @Override
    public MessageEmbed help(GuildMessageReceivedEvent event) {
        return null;
    }
}
