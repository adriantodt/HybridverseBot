package com.theorangehub.hbdvbot.modules.commands;

import com.theorangehub.hbdvbot.modules.commands.base.AbstractCommand;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public abstract class NoArgsCommand extends AbstractCommand {
    public NoArgsCommand() {
        super();
    }

    public NoArgsCommand(CommandPermission permission) {
        super(permission);
    }

    protected abstract void call(GuildMessageReceivedEvent event, String content);

    @Override
    public void run(GuildMessageReceivedEvent event, String commandName, String content) {
        call(event, content);
    }
}