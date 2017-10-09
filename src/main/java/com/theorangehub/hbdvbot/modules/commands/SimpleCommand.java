package com.theorangehub.hbdvbot.modules.commands;

import com.theorangehub.hbdvbot.modules.commands.base.AbstractCommand;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public abstract class SimpleCommand extends AbstractCommand {
    public SimpleCommand() {
        super();
    }

    public SimpleCommand(CommandPermission permission) {
        super(permission);
    }

    protected abstract void call(GuildMessageReceivedEvent event, String content) throws Exception;

    @Override
    public void run(GuildMessageReceivedEvent event, String commandName, String content) throws Exception {
        call(event, content);
    }
}