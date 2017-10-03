package com.theorangehub.hbdvbot.commands;

import br.com.brjdevs.java.utils.strings.StringUtils;
import com.theorangehub.dml.DML;
import com.theorangehub.dml.DMLBuilder;
import com.theorangehub.hbdvbot.commands.calendars.HbdvCalendars;
import com.theorangehub.hbdvbot.commands.calendars.HbdvDate;
import com.theorangehub.hbdvbot.modules.CommandRegistry;
import com.theorangehub.hbdvbot.modules.Event;
import com.theorangehub.hbdvbot.modules.Module;
import com.theorangehub.hbdvbot.modules.commands.NoArgsCommand;
import com.theorangehub.hbdvbot.modules.commands.SimpleCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.regex.Pattern;

@Module
public class UtilsCmds {

    @Event
    public static void datahora(CommandRegistry cr) {

        cr.register("data", new SimpleCommand() {
            private final Pattern SPLIT = Pattern.compile("[-/.]");

            @Override
            protected void call(GuildMessageReceivedEvent event, String content, String[] args) {
                try {
                    String[] partes = SPLIT.split(args[0], 3);

                    int dia = Integer.parseInt(partes[0]);
                    int mes = Integer.parseInt(partes[1]);
                    int ano = Integer.parseInt(partes[2]);

                    HbdvDate date = HbdvCalendars.REGISTRY.get(args[1].toLowerCase()).of(ano, mes, dia);

                    event.getChannel().sendMessage(
                        baseEmbed(event, "Data/Hora do RPG")
                            .setDescription(String.format(
                                "**Apex**: %s\n**AP**: %s\n**Equivalente**: %s",
                                date.format(HbdvCalendars.APEX),
                                date.format(HbdvCalendars.AFTER_PLAGUE),
                                date.format(HbdvCalendars.REAL_LIFE)
                            ))
                            .build()
                    ).queue();

                } catch (Exception e) {
                    onHelp(event);
                }
            }

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return null;
            }

            @Override
            protected String[] splitArgs(String content) {
                return StringUtils.splitArgs(content, 2);
            }
        });
    }

    @Event
    public static void test(CommandRegistry cr) {

        cr.register("test", new NoArgsCommand() {

            @Override
            public MessageEmbed help(GuildMessageReceivedEvent event) {
                return null;
            }

            @Override
            protected void call(GuildMessageReceivedEvent event, String content) {
                DMLBuilder builder = new DMLBuilder() {
                    @Override
                    protected EmbedBuilder newEmbedBuilder() {
                        return super.newEmbedBuilder()
                            .setColor(event.getMember().getColor())
                            .setFooter("Requerido por " + event.getMember().getEffectiveName(), event.getAuthor().getEffectiveAvatarUrl());
                    }
                };

                DML.parse(builder, content);
                event.getChannel().sendMessage(builder.build()).queue();
            }
        });
    }
}
