package com.theorangehub.hbdvbot.modules.commands.base;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public interface ICommandRunnable {
    /**
     * Invokes the command to be executed.
     *
     * @param event       the event that triggered the command
     * @param commandName the command name that was used
     * @param content     the arguments of the command
     */
    void run(GuildMessageReceivedEvent event, String commandName, String content) throws Exception;
}
