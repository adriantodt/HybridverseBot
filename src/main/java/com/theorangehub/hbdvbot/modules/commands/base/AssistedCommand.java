package com.theorangehub.hbdvbot.modules.commands.base;

import com.theorangehub.hbdvbot.utils.CommandUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * "Assisted" version of the {@link Command} interface, providing some "common ground" for all Commands based on it.
 */
public interface AssistedCommand extends Command {
    default EmbedBuilder baseEmbed(GuildMessageReceivedEvent event, String name) {
        return CommandUtils.baseEmbed(event, name);
    }

    default EmbedBuilder baseEmbed(GuildMessageReceivedEvent event, String name, String image) {
        return CommandUtils.baseEmbed(event, name, image);
    }

    default EmbedBuilder helpEmbed(GuildMessageReceivedEvent event, String name) {
        return baseEmbed(event, name)
            .setThumbnail("https://cdn.pixabay.com/photo/2012/04/14/16/26/question-34499_960_720.png")
            .addField("Permissão Requerida", permission().toString(), true);
    }

    default void onHelp(GuildMessageReceivedEvent event) {
        CommandUtils.onHelp(this, event);
    }
}
