package com.theorangehub.hbdvbot.commands;

import br.com.brjdevs.java.utils.extensions.CollectionUtils;
import com.theorangehub.hbdvbot.HbdvBot;
import com.theorangehub.hbdvbot.commands.info.AsyncInfoMonitor;
import com.theorangehub.hbdvbot.commands.info.CommandStatsManager;
import com.theorangehub.hbdvbot.core.CommandProcessorAndRegistry;
import com.theorangehub.hbdvbot.modules.CommandRegistry;
import com.theorangehub.hbdvbot.modules.Event;
import com.theorangehub.hbdvbot.modules.Module;
import com.theorangehub.hbdvbot.modules.commands.SimpleCommand;
import com.theorangehub.hbdvbot.modules.commands.base.Command;
import com.theorangehub.hbdvbot.modules.events.PostLoadEvent;
import com.theorangehub.hbdvbot.utils.CommandUtils;
import com.theorangehub.hbdvbot.utils.commands.EmoteReference;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.theorangehub.hbdvbot.HbdvCommons.RANDOM;
import static java.lang.String.format;

@Module
public class InfoCmds {
    public static final List<String> HELP_JOKES = Arrays.asList(
        "Parabéns, você descobriu como usar o comando de ajuda!",
        "Te ajuda a se ajudar."
    );

    @Event
    public static void about(CommandRegistry registry) {
        registry.register("sobre", new SimpleCommand() {
            @Override
            protected void call(GuildMessageReceivedEvent event, String content, String[] args) {
                List<Guild> guilds = HbdvBot.getInstance().getGuilds();
                int guildCount = guilds.size();
                int usersCount = HbdvBot.getInstance().getUsers().size();
                long onlineCount = guilds.stream()
                    .flatMap(guild -> guild.getMembers().stream())
                    .filter(user -> !user.getOnlineStatus().equals(OnlineStatus.OFFLINE))
                    .map(member -> member.getUser().getId())
                    .distinct()
                    .count();
                int tcCount = HbdvBot.getInstance().getTextChannels().size();
                int vcCount = HbdvBot.getInstance().getVoiceChannels().size();
                long millis = ManagementFactory.getRuntimeMXBean().getUptime();
                long seconds = millis / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;

                event.getChannel().sendMessage(baseEmbed(event, "Sobre o Hbdv")
                    .setDescription("Bot para Consulta de Informações do #HBDVRPG")
                    .addField(
                        "Informação:",
                        "**Versão do Bot**: " + HbdvBot.getVersion() + "\n" +
                            "**Uptime**: " +
                            format("%d:%02d:%02d:%02d", days, hours % 24, minutes % 60, seconds % 60) + "\n" +
                            "**Threads**: " + Thread.activeCount() + "\n" +
                            "**Servidores**: " + guildCount + "\n" +
                            "**Usuários (Online/Total)**: " + onlineCount + "/" + usersCount + "\n" +
                            "**Canais de Texto/Voz**: " + tcCount + "/" + vcCount + "\n",
                        false
                    )
                    .setFooter(
                        "Comandos durante a Sessão: " + CommandProcessorAndRegistry.getCommandCount(),
                        event.getJDA().getSelfUser().getAvatarUrl()
                    )
                    .build()
                ).queue();
            }

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return helpBuilder(event, "Sobre")
                    .descrição("Informações e Status sobre o Bot.")
                    .build();
            }
        });
    }

    @Event
    public static void help(CommandRegistry cr) {

        cr.register("help", new SimpleCommand() {
            @Override
            protected void call(GuildMessageReceivedEvent event, String content, String[] args) {
                if (!content.isEmpty()) {

                    Command command = HbdvBot.getRegistry().commands().get(content);

                    if (command != null) {
                        CommandUtils.onHelp(command, event);
                    } else {
                        event.getChannel().sendMessage(
                            EmoteReference.ERROR + "Não existe nenhum comando com esse nome!"
                        ).queue();
                    }
                    return;
                }

                event.getChannel().sendMessage(
                    baseEmbed(event, "Ajuda do HbdvBot")
                        .setDescription(
                            HbdvBot.getRegistry().commands().keySet().stream().collect(Collectors.joining("` `", "`", "`"))
                        ).build()
                ).queue();
            }

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return helpBuilder(event, "Ajuda")
                    .descrição("**" + CollectionUtils.random(HELP_JOKES, RANDOM) + "**")
                    .uso("help", "Lista os comandos do Bot.")
                    .uso("help <comando>", "Retorna a ajuda de um comando específico.")
                    .build();
            }
        });
    }

    @Event
    public static void onPostLoad(PostLoadEvent e) {
        AsyncInfoMonitor.start();
    }

    @Event
    public static void ping(CommandRegistry cr) {
        cr.register("ping", new SimpleCommand() {
            @Override
            protected void call(GuildMessageReceivedEvent event, String content, String[] args) {
                long start = System.currentTimeMillis();
                event.getChannel().sendTyping().queue(v -> {
                    long ping = System.currentTimeMillis() - start;
                    event.getChannel().sendMessage(format(
                        "%s**Ping**: `%dms/%dms` (Discord API/Websocket)",
                        EmoteReference.MEGA, ping, event.getJDA().getPing()
                    )).queue();
                });
            }

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return helpBuilder(event, "Ping")
                    .descrição("**Brinca de Ping-Pong com o Discord e descobre o delay**.")
                    .build();
            }
        });
    }

    @Event
    public static void stats(CommandRegistry cr) {
        cr.register("stats", new SimpleCommand() {
            @Override
            protected void call(GuildMessageReceivedEvent event, String content, String[] args) {
                if (args.length > 0 && args[0].equals("cmds")) {
                    if (args.length > 1) {
                        String what = args[1];
                        if (what.equals("total")) {
                            event.getChannel().sendMessage(CommandStatsManager
                                .fillEmbed(CommandStatsManager.TOTAL_CMDS, baseEmbed(event, "Command Stats | Total"))
                                .build()).queue();
                            return;
                        }

                        if (what.equals("daily")) {
                            event.getChannel().sendMessage(CommandStatsManager
                                .fillEmbed(CommandStatsManager.DAY_CMDS, baseEmbed(event, "Command Stats | Daily"))
                                .build()).queue();
                            return;
                        }

                        if (what.equals("hourly")) {
                            event.getChannel().sendMessage(CommandStatsManager
                                .fillEmbed(CommandStatsManager.HOUR_CMDS, baseEmbed(event, "Command Stats | Hourly"))
                                .build()).queue();
                            return;
                        }

                        if (what.equals("now")) {
                            event.getChannel().sendMessage(CommandStatsManager
                                .fillEmbed(CommandStatsManager.MINUTE_CMDS, baseEmbed(event, "Command Stats | Now"))
                                .build()).queue();
                            return;
                        }
                    }

                    //Default
                    event.getChannel().sendMessage(baseEmbed(event, "Command Stats")
                        .addField("Agora", CommandStatsManager.resume(CommandStatsManager.MINUTE_CMDS), false)
                        .addField("Nessa Hora", CommandStatsManager.resume(CommandStatsManager.HOUR_CMDS), false)
                        .addField("Hoje", CommandStatsManager.resume(CommandStatsManager.DAY_CMDS), false)
                        .addField("Total", CommandStatsManager.resume(CommandStatsManager.TOTAL_CMDS), false)
                        .build()
                    ).queue();

                    return;
                }

                event.getChannel().sendMessage(
                    baseEmbed(event, "Estatísticas da Sessão:")
                        .addField(
                            "Uso de Recursos:",
                            "**Threads**: " + AsyncInfoMonitor.getThreadCount() + "\n" +
                                "**RAM**: " + (AsyncInfoMonitor.getTotalMemory() - AsyncInfoMonitor.getFreeMemory()) + "MB/" + AsyncInfoMonitor
                                .getMaxMemory() + "MB\n" +
                                "**Memória Alocada**: " + AsyncInfoMonitor.getTotalMemory() + "MB (" + AsyncInfoMonitor.getFreeMemory() + "MB restante)\n" +
                                "**Uso de CPU**: " + AsyncInfoMonitor.getCpuUsage() + "%\n"
                            , false
                        )
                        .addField(
                            "Servidor:",
                            "**RAM** (Total/Livre/Usada): " + String.format(
                                "%.2fGB/%.2fGB/%.2fGB", AsyncInfoMonitor.getVpsMaxMemory(), AsyncInfoMonitor.getVpsFreeMemory(),
                                AsyncInfoMonitor.getVpsUsedMemory()
                            ) + "\n" +
                                "**Núcleos de CPU**: " + AsyncInfoMonitor.getAvailableProcessors() + " Núcleos\n" +
                                "**Uso de CPU**:" + AsyncInfoMonitor.getVpsCPUUsage() + "%"
                            , false
                        )
                        .build()
                ).queue();
            }

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return helpBuilder(event, "Estatísticas")
                    .descrição("Mostra as estatisticas do HbdvBot")
                    .uso("stats", "Lista as estatísticas da sessão.")
                    .uso("stats cmds", "Lista o resumo sobre o uso dos comandos.")
                    .uso("stats cmds <now/hourly/dialy/total>", "Informações detalhadas sobre o uso dos comandos.")
                    .build();
            }
        });
    }
}