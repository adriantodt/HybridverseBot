package com.theorangehub.hbdvbot.core.listeners.command;

import br.com.brjdevs.java.utils.extensions.Async;
import com.theorangehub.hbdvbot.core.CommandProcessorAndRegistry;
import com.theorangehub.hbdvbot.core.listeners.OptimizedListener;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

@Slf4j
public class CommandListener extends OptimizedListener<GuildMessageReceivedEvent> {
    public static final CommandProcessorAndRegistry PROCESSOR = new CommandProcessorAndRegistry();

    public CommandListener() {
        super(GuildMessageReceivedEvent.class);
    }

    @Override
    public void event(GuildMessageReceivedEvent event) {
        // @formatter:off
		if (
			event.getAuthor().isBot()
			||
			!event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_WRITE)
			&&
			!event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)
		) return;
		// @formatter:on

        Async.thread(
            String.format(
                "Cmd:%s#%s:%s",
                event.getAuthor().getName(),
                event.getAuthor().getDiscriminator(),
                event.getMessage().getRawContent()
            ),
            () -> PROCESSOR.run(event)
        );
    }
}
