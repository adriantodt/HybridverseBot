package com.theorangehub.hbdvbot.commands;

import com.theorangehub.dml.DML;
import com.theorangehub.dml.DMLBuilder;
import com.theorangehub.hbdvbot.modules.CommandRegistry;
import com.theorangehub.hbdvbot.modules.Event;
import com.theorangehub.hbdvbot.modules.Module;
import com.theorangehub.hbdvbot.modules.commands.NoArgsCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

@Module
public class UtilsCmds {


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
