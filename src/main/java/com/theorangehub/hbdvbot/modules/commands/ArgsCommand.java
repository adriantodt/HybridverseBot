package com.theorangehub.hbdvbot.modules.commands;

import br.com.brjdevs.java.utils.strings.StringUtils;
import com.theorangehub.hbdvbot.modules.commands.base.AbstractCommand;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public abstract class ArgsCommand extends AbstractCommand {
    public ArgsCommand() {
        super();
    }

    public ArgsCommand(CommandPermission permission) {
        super(permission);
    }

    protected abstract void call(GuildMessageReceivedEvent event, String content, String[] args);

    @Override
    public void run(GuildMessageReceivedEvent event, String commandName, String content) {
        call(event, content, splitArgs(content));
    }

    protected String[] splitArgs(String content) {
        return StringUtils.efficientSplitArgs(content, 0);
    }
}
