package com.theorangehub.hbdvbot.commands.ficha;

import br.com.brjdevs.java.utils.strings.StringUtils;
import com.theorangehub.hbdvbot.data.HbdvData;
import com.theorangehub.hbdvbot.data.entities.Ficha;
import com.theorangehub.hbdvbot.modules.commands.CommandPermission;
import com.theorangehub.hbdvbot.modules.commands.SimpleCommand;
import com.theorangehub.hbdvbot.utils.commands.EmoteReference;
import com.theorangehub.hbdvbot.utils.helpers.IntegerSelector;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract class FichaBaseCommand extends SimpleCommand {
    public FichaBaseCommand() {
        super();
    }

    public FichaBaseCommand(CommandPermission permission) {
        super(permission);
    }

    protected abstract void handle(GuildMessageReceivedEvent event, Ficha ficha, String[] args) throws Exception;

    @Override
    protected void call(GuildMessageReceivedEvent event, String content) throws Exception {
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

        if (fichas.size() == 1) {
            handle(event, fichas.get(0), params);
            return;
        }

        EmbedBuilder embed = baseEmbed(event, "Selecione a Ficha:");

        int i = 0;
        for (Ficha ficha : fichas) {
            i++;
            embed.addField(i + ". " + ficha.getNome(), ficha.displaySeleção(), false);
        }

        Message message = event.getChannel().sendMessage(embed.build()).complete();

        Future<Integer> value = new IntegerSelector(event)
            .min(1)
            .max(fichas.size())
            .initialTimeout(20, TimeUnit.SECONDS)
            .build();

        Integer v = value.get() - 1;

        message.delete().queue();

        handle(event, fichas.get(v), params);
    }
}
