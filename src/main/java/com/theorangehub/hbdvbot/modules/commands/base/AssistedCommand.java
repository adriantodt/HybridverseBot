package com.theorangehub.hbdvbot.modules.commands.base;

import com.theorangehub.hbdvbot.HbdvCommons;
import com.theorangehub.hbdvbot.commands.info.HelpBuilder;
import com.theorangehub.hbdvbot.utils.CommandUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * "Assisted" version of the {@link ICommand} interface, providing some "common ground" for all Commands based on it.
 */
public interface AssistedCommand extends ICommand {
    default EmbedBuilder baseEmbed(GuildMessageReceivedEvent event, String name) {
        return CommandUtils.baseEmbed(event, name);
    }

    default EmbedBuilder baseEmbed(GuildMessageReceivedEvent event, String name, String image) {
        return CommandUtils.baseEmbed(event, name, image);
    }

    default HelpBuilder helpBuilder(GuildMessageReceivedEvent event, String name) {
        return new HelpBuilder(event, name, permission()).cor(HbdvCommons.HBDV_COLOR);
    }

    default EmbedBuilder helpEmbed(GuildMessageReceivedEvent event, String name) {
        return CommandUtils.helpEmbed(event, name, permission());
    }

    default void onHelp(GuildMessageReceivedEvent event) {
        CommandUtils.onHelp(this, event);
    }
}
