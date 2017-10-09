package com.theorangehub.hbdvbot.commands;

import br.com.brjdevs.java.utils.strings.StringUtils;
import com.theorangehub.hbdvbot.commands.data.HbdvCalendars;
import com.theorangehub.hbdvbot.commands.data.HbdvDate;
import com.theorangehub.hbdvbot.modules.Command;
import com.theorangehub.hbdvbot.modules.commands.ArgsCommand;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.regex.Pattern;

@Command("data")
public class DataCmd extends ArgsCommand {
    private final Pattern SPLIT = Pattern.compile("[-/.]");

    @Override
    protected void call(GuildMessageReceivedEvent event, String content, String[] args) throws Exception {
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
    protected String[] splitArgs(String content) {
        return StringUtils.splitArgs(content, 2);
    }

    @Override
    public MessageEmbed help(GuildMessageReceivedEvent event) {
        return null;
    }
}
