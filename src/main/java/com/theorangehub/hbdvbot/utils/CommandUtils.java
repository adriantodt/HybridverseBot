package com.theorangehub.hbdvbot.utils;

import com.theorangehub.hbdvbot.modules.commands.CommandPermission;
import com.theorangehub.hbdvbot.modules.commands.base.ICommand;
import com.theorangehub.hbdvbot.utils.commands.EmoteReference;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class CommandUtils {
    public static EmbedBuilder baseEmbed(GuildMessageReceivedEvent event, String name) {
        return baseEmbed(event, name, event.getJDA().getSelfUser().getEffectiveAvatarUrl());
    }

    public static EmbedBuilder baseEmbed(GuildMessageReceivedEvent event, String name, String image) {
        return new EmbedBuilder()
            .setAuthor(name, null, image)
            .setColor(event.getMember().getColor())
            .setFooter("Requerido por " + event.getMember().getEffectiveName(), event.getAuthor().getEffectiveAvatarUrl());
    }

    public static EmbedBuilder helpEmbed(GuildMessageReceivedEvent event, String name, CommandPermission permission) {
        return baseEmbed(event, name)
            .setThumbnail("https://cdn.pixabay.com/photo/2012/04/14/16/26/question-34499_960_720.png")
            .addField("Permissão Requerida", permission.toString(), true);
    }

    public static void doTimes(int times, Runnable runnable) {
        for (int i = 0; i < times; i++) runnable.run();
    }

    public static void onHelp(ICommand command, GuildMessageReceivedEvent event) {
        if (!command.permission().test(event.getMember())) {
            event.getChannel().sendMessage(EmoteReference.STOP + "Você não tem permissão para ver a ajuda desse comando.")
                .queue(message -> message.delete().queueAfter(30, TimeUnit.SECONDS));
            return;
        }

        MessageEmbed helpEmbed = command.help(event);

        if (helpEmbed == null) {
            event.getChannel().sendMessage(EmoteReference.ERROR + "O comando não tem ajuda ao usuário.")
                .queue(message -> message.delete().queueAfter(30, TimeUnit.SECONDS));
            return;
        }

        event.getChannel().sendMessage(helpEmbed).queue();
    }
}
