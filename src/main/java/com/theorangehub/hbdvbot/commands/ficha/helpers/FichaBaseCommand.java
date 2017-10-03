package com.theorangehub.hbdvbot.commands.ficha.helpers;

import br.com.brjdevs.java.utils.strings.StringUtils;
import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import com.theorangehub.hbdvbot.modules.commands.CommandPermission;
import com.theorangehub.hbdvbot.modules.commands.NoArgsCommand;
import com.theorangehub.hbdvbot.utils.DiscordUtils;
import com.theorangehub.hbdvbot.utils.commands.EmoteReference;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public abstract class FichaBaseCommand extends NoArgsCommand {
    public FichaBaseCommand() {
        super();
    }

    public FichaBaseCommand(CommandPermission permission) {
        super(permission);
    }

    protected abstract void handle(GuildMessageReceivedEvent event, Ficha ficha, String[] args);

    @Override
    protected void call(GuildMessageReceivedEvent event, String content) {
        String[] args = StringUtils.efficientSplitArgs(content, 0);
        if (args.length < 1) {
            onHelp(event);
            return;
        }

        String selector = args[0];

        String[] params = Arrays.copyOfRange(args, 1, args.length);

        List<Ficha> fichas = HbdvData.db().getFichasPorNome(selector);
        if (fichas.isEmpty()) {
            event.getChannel().sendMessage(
                EmoteReference.ERROR + "Nenhum Personagem com o Nome de \"" + selector + "\""
            ).queue();

            return;
        }

        if (fichas.size() > 1) {
            DiscordUtils.selectList(event, fichas,
                Ficha::displayToString,
                s -> baseEmbed(event, "Selecione a Ficha:").setDescription(s).build(),
                f -> handle(event, f, params)
            );
        }

        handle(event, fichas.get(0), params);
    }
}
