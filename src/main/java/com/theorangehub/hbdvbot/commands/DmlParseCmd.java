package com.theorangehub.hbdvbot.commands;

import com.theorangehub.dml.DML;
import com.theorangehub.dml.DMLBuilder;
import com.theorangehub.hbdvbot.modules.Command;
import com.theorangehub.hbdvbot.modules.commands.SimpleCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

@Command("dmlparse")
public class DmlParseCmd extends SimpleCommand {
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

    @Override
    public MessageEmbed help(GuildMessageReceivedEvent event) {
        return null;
    }
}
