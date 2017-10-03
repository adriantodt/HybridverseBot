package com.theorangehub.hbdvbot.modules.commands.base;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Interface used for handling commands within the bot.
 */
public interface ICommand extends SecuredCommandRunnable {

    /**
     * Embed to be used on help command
     *
     * @param event the event that triggered the help
     * @return a Nullable {@link MessageEmbed}
     */
    MessageEmbed help(GuildMessageReceivedEvent event);

}
